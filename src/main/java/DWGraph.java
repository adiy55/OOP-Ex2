import api.EdgeData;
import api.NodeData;
import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph implements api.DirectedWeightedGraph {
    private final HashMap<Integer, NodeData> nodes;
    private final HashMap<Integer, HashMap<Integer, EdgeData>> edges; // key = src, value: key = dest, value = edge
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
        this.nodeIterMC = g.nodeIterMC;
        this.edgeIterMC = g.edgeIterMC;
        this.specEdgeIterMC = g.specEdgeIterMC;
    }

    @Override
    public NodeData getNode(int key) {
        return nodes.containsKey(key) ? nodes.get(key) : null;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return (edges.containsKey(src) && edges.get(src).containsKey(dest)) ? edges.get(src).get(dest) : null;
    }

    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), n);
    }

    @Override
    public void connect(int src, int dest, double w) { // overwrites an existing edge (if there is one)
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
        return new nodeIterator();
    }

    private class nodeIterator implements Iterator<NodeData> {
        private final ArrayList<NodeData> data;
        private final int iter_mc;
        private int index;

        public nodeIterator() {
            data = new ArrayList<>();
            data.addAll(nodes.values());
            iter_mc = modeCount;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            if (iter_mc != modeCount) {
                throw new RuntimeException("Invalid iterator!");
            }
            return index < data.size();
        }

        @Override
        public NodeData next() {
            if (iter_mc != modeCount) {
                throw new RuntimeException("Invalid iterator!");
            }
            return data.get(index++);
        }
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return new edgesIterator();
    }

    private class edgesIterator implements Iterator<EdgeData> {
        private final ArrayList<EdgeData> data;
        private final int iter_mc;
        private int index;

        public edgesIterator() {
            data = new ArrayList<>();
            iter_mc = modeCount;
            index = 0;
            for (Integer key : edges.keySet()) {
                data.addAll(edges.get(key).values());
            }
        }

        @Override
        public boolean hasNext() {
            if (iter_mc != modeCount) {
                throw new RuntimeException("Invalid iterator!");
            }
            return index < data.size();
        }

        @Override
        public EdgeData next() {
            if (iter_mc != modeCount) {
                throw new RuntimeException("Invalid iterator!");
            }
            return data.get(index++);
        }
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new edgeIter(node_id);
    }

    private class edgeIter implements Iterator<EdgeData> {
        private final ArrayList<EdgeData> data;
        private final int iter_mc;
        private int index;

        public edgeIter(int node_id) {
            data = new ArrayList<>();
            data.addAll(edges.get(node_id).values());
            iter_mc = modeCount;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            if (iter_mc != modeCount) {
                throw new RuntimeException("Invalid iterator!");
            }
            return index < data.size();
        }

        @Override
        public EdgeData next() {
            if (iter_mc != modeCount) {
                throw new RuntimeException("Invalid iterator!");
            }
            return data.get(index++);
        }
    }

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
            if (edges.get(src).size() == 1) { // if there is only one edge going from this src, then it must be dest.
                edges.remove(src);
            }
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
        return "DWGraph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                ", numEdges=" + numEdges +
                ", modeCount=" + modeCount +
                '}';
    }
}
