import api.EdgeData;
import api.NodeData;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
