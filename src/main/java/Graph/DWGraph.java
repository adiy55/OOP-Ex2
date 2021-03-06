package Graph;

import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class DWGraph implements api.DirectedWeightedGraph {
    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges; // key = src, value: key = dest, value = edge
    private int numEdges;
    private int modeCount;

    public DWGraph(HashMap<Integer, NodeData> nodes, HashMap<Integer, HashMap<Integer, EdgeData>> edges) {
        this.nodes = nodes;
        this.edges = edges;
        numEdges = modeCount = 0;
        for (HashMap<Integer, EdgeData> map : this.edges.values()) {
            numEdges += map.size();
        }
    }

    public DWGraph(DWGraph g) {
        this.nodes = Utilities.copyNodes(g.nodes);
        this.edges = Utilities.copyEdges(g.edges);
        this.numEdges = g.numEdges;
        this.modeCount = g.modeCount;
    }

    @Override
    public NodeData getNode(int key) {
        return nodes.containsKey(key) ? nodes.get(key) : null;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return (edges.containsKey(src) && edges.get(src).containsKey(dest)) ? edges.get(src).get(dest) : null;
    }

    /*
    If the nodes HashMap contains a node with the same key, the new node will overwrite the old node.
    */
    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), n);
        if (!edges.containsKey(n.getKey())) {
            edges.put(n.getKey(), new HashMap<>());
        }
    }

    /*
    If the edges HashMap contains an edge with the same src or dest, overwrites the existing edge.
    */
    @Override
    public void connect(int src, int dest, double w) {
        if (!nodes.containsKey(src) || !nodes.containsKey(dest) || w <= 0 || src == dest) {
            throw new IllegalArgumentException("Invalid edge!");
        }
        Edge e = new Edge(src, w, dest);
        if (edges.containsKey(src)) {
            edges.get(src).put(dest, e);
        } else {
            HashMap<Integer, EdgeData> temp = new HashMap<>();
            temp.put(dest, e);
            edges.put(src, temp);
        }
        Node n = (Node) nodes.get(dest); // add src node to dest node neighbors
        n.addNeighbor(src);
        this.numEdges++;
        this.modeCount++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return new Iterator<>() {
            private ArrayList<NodeData> data = new ArrayList<>(nodes.values());
            private int iter_mc = modeCount;
            private int index = 0;

            @Override
            public boolean hasNext() {
                if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
                return index < data.size();
            }

            @Override
            public NodeData next() {
                if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
                return data.get(index++);
            }

            @Override
            public void remove() {
                if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
                removeNode(data.get(index).getKey());
                iter_mc++;
            }

            @Override
            public void forEachRemaining(Consumer<? super NodeData> action) {
                if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
                data.subList(index + 1, data.size() - 1).forEach(action);
            }
        };
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return new edgesIterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new edgesIterator(node_id);
    }

    private class edgesIterator implements Iterator<EdgeData> {
        private ArrayList<EdgeData> data;
        private int iter_mc;
        private int index;

        public edgesIterator() {
            data = new ArrayList<>();
            iter_mc = modeCount;
            index = 0;
            for (Integer key : edges.keySet()) {
                data.addAll(edges.get(key).values());
            }
        }

        public edgesIterator(int node_id) {
            data = new ArrayList<>();
            data.addAll(edges.get(node_id).values());
            iter_mc = modeCount;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
            return index < data.size();
        }

        @Override
        public EdgeData next() {
            if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
            return data.get(index++);
        }

        @Override
        public void remove() {
            if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
            removeEdge(data.get(index).getSrc(), data.get(index).getDest());
            iter_mc++;
        }

        @Override
        public void forEachRemaining(Consumer<? super EdgeData> action) {
            if (iter_mc != modeCount) throw new RuntimeException("Invalid iterator!");
            data.subList(index + 1, data.size() - 1).forEach(action);
        }
    }

    /*
    Each node has a HashSet containing the ID's of its neighbors -> O(k) runtime (k = number of neighbors)
     */
    @Override
    public NodeData removeNode(int key) {
        for (Integer dest : edges.get(key).keySet()) { // dest nodes contain src as a neighbor
            Node n = (Node) nodes.get(dest);
            n.removeNeighbor(key);
        }
        numEdges -= edges.get(key).size();
        edges.remove(key); // remove all edges that src == key
        if (nodes.containsKey(key)) { // remove edges that key == dest
            Node n = (Node) nodes.get(key);
            for (Integer src : n.getNeighbors()) { // neighbor = src node of an edge with key as the dest
                edges.get(src).remove(key);
                numEdges--;
            }
            modeCount++;
            return nodes.remove(key);
        }
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (edges.get(src).containsKey(dest)) { // dest node contains src as a neighbor
            Node n = (Node) nodes.get(dest);
            n.removeNeighbor(src);
            EdgeData e = edges.get(src).remove(dest);
            modeCount++;
            numEdges--;
            return e; // otherwise, remove the specific hashmap of dest from the hashmap of src
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return this.numEdges;
    }

    @Override
    public int getMC() {
        return this.modeCount;
    }

    @Override
    public String toString() {
        return "Graph.DWGraph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                ", numEdges=" + numEdges +
                ", modeCount=" + modeCount +
                '}';
    }


    public HashMap<Integer, NodeData> getNodes() {
        return nodes;
    }

    public HashMap<Integer, HashMap<Integer, EdgeData>> getEdges() {
        return edges;
    }
}
