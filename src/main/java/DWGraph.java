import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;

public class DWGraph implements api.DirectedWeightedGraph {
    private HashMap<Integer, Node> nodes;
    private int numEdges;
//    private HashMap<Integer, HashMap<Integer, Edge>> edges; // key = src, value: key = dest, value = edge

    public DWGraph(HashMap<Integer, Node> nodes) {
        this.nodes = nodes;
        numEdges = 0;
//        for (Node node : this.nodes) {
//            numEdges += node.getEdgesOut().size();
//        }
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
        return nodes.get(src).getEdgesOut().get(dest);
    }

    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), (Node) n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        Edge e = new Edge(src, dest, w);
        nodes.get(src).getEdgesOut().put(dest, e); // overrides an existing edge (if there is one)
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return null;
    }

    @Override
    public NodeData removeNode(int key) {
//        nodes.get(key).getEdgesOut()
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {

        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }
}
