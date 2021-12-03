import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;

public class DWGraph implements api.DirectedWeightedGraph {
    private final HashMap<Integer, NodeData> nodes;
    private final HashMap<Integer, HashMap<Integer, EdgeData>> edges; // key = src, value: key = dest, value = edge
    private int numEdges;
    private int modeCount; // most recent mode count
    private int nodeIterMC; // mode count when node iterator method was called
    private int edgeIterMC;
    private int specEdgeIterMC;

    public DWGraph(HashMap<Integer, NodeData> nodes, HashMap<Integer, HashMap<Integer, EdgeData>> edges) {
        this.nodes = nodes;
        numEdges = 0;
        this.edges = edges;
        for (HashMap<Integer, EdgeData> map : this.edges.values()) {
            numEdges += map.size();
        }
        this.nodeIterMC = -1; //init to invalid value, so as not to throw exception when there is no need to
        this.edgeIterMC = -1;
        this.specEdgeIterMC = -1;
    }

    @Override
    public NodeData getNode(int key) {
        try {
            return nodes.get(key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public EdgeData getEdge(int src, int dest) { // todo: throw exceptions
        return edges.get(src).get(dest);
    }

    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        Edge e = new Edge(src, w, dest);
        if (edges.get(src) != null) {
            edges.get(src).put(dest, e);
        } else {
            HashMap<Integer, EdgeData> temp = new HashMap<>();
            temp.put(dest, e);
            edges.put(src, temp);
        } // overrides an existing edge (if there is one)

        //TODO: add to relevant neighbours
        this.numEdges++;
        this.modeCount++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        if (this.modeCount != this.nodeIterMC && this.nodeIterMC != -1) {
            throw new RuntimeException("The graph was changed!");
        }
        nodeIterMC = modeCount;
        return nodes.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        if (this.modeCount != this.edgeIterMC && this.edgeIterMC != -1) {
            throw new RuntimeException("The graph was changed!");
        }
        this.specEdgeIterMC = this.modeCount;
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        if (this.modeCount != this.specEdgeIterMC && this.specEdgeIterMC != -1) {
            throw new RuntimeException("The graph was changed!");
        }
        this.edgeIterMC = this.modeCount;
        return edges.get(node_id).values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {
        edges.remove(key);
        Node n = (Node) nodes.get(key);
        for (Integer src : n.getNeighbors()) { // neighbor = node id which has an edge to the key
            edges.get(src).remove(key);
        }
        this.modeCount++;
        return nodes.remove(key);
    }

    /**
     * Assuming edge exists, removes it
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (edges.get(src).size() == 1) { //if there is only one edge going from this src, then it must be dest.
            edges.remove(src);
        }
        edges.get(src).remove(dest); //otherwise, remove the specific hashmap of dest from the hashmap of src
        this.modeCount++;
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
