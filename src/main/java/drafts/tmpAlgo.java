package drafts;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class tmpAlgo implements api.DirectedWeightedGraphAlgorithms {
    private DWGraph graph;
    private String filename;

    public tmpAlgo(DWGraph g) {
        graph = g;
    }

    public tmpAlgo(String filename) {
        this.filename = filename;
        boolean flag = load(filename);
        if (!flag) this.graph = null;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        Iterator<NodeData> node_iter = g.nodeIter();
        Iterator<EdgeData> edge_iter = g.edgeIter();
        HashMap<Integer, NodeData> nodes = new HashMap<>();
        HashMap<Integer, HashMap<Integer, EdgeData>> edges = new HashMap<>();
        while (node_iter.hasNext()) {
            NodeData n = node_iter.next();
            nodes.put(n.getKey(), new Node(n.getKey(), new GeoLoc(n.getLocation().x(), n.getLocation().y(), n.getLocation().z())));
            edges.put(n.getKey(), new HashMap<>());
        }
        while (edge_iter.hasNext()) {
            EdgeData e = edge_iter.next();
            edges.get(e.getSrc()).put(e.getDest(), new Edge(e.getSrc(), e.getWeight(), e.getDest()));
            Node n = (Node) nodes.get(e.getDest());
            n.addNeighbor(e.getSrc());
        }
        graph = new DWGraph(nodes, edges);
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return null;
    }

    @Override
    public boolean isConnected() {
        int start_id = -1;
        for (int i = 0; i < graph.getNodes().values().size(); i++) {
            if (i == 0) {
                start_id = graph.getNodes().get(i).getKey();
            }
            Node curr_node = (Node) graph.getNodes().get(i);
            curr_node.setC(Node.Color.WHITE);
        }
        return DFSIter(this.graph, start_id) && DFSIter(transposeEdges(), start_id);
    }

    private boolean DFS(DWGraph graph, int start_id) {
        if (start_id == -1) return false;
        Node start_node = (Node) graph.getNode(start_id);
        DFSVisit(start_node, graph);
        for (NodeData n : graph.getNodes().values()) {
            Node curr_node = (Node) n;
            if (curr_node.getC().equals(Node.Color.WHITE)) {
                return false;
            }
        }
        return true;
    }

    private void DFSVisit(Node n, DWGraph graph) {
        n.setC(Node.Color.GRAY);
        for (Integer dest : graph.getEdges().get(n.getKey()).keySet()) {
            Node dest_node = (Node) graph.getNode(dest);
            if (dest_node.getC().equals(Node.Color.WHITE)) {
                DFSVisit(dest_node, graph);
            }
        }
        n.setC(Node.Color.BLACK);
    }

    private boolean DFSIter(DWGraph graph, int start_id) {
        if (start_id == -1) return false;
        Node start_node = (Node) graph.getNode(start_id);
        Stack<NodeData> stack = new Stack<>();
        stack.push(start_node);
        DFSVisitIter(stack, graph);
        for (NodeData n : graph.getNodes().values()) {
            Node curr_node = (Node) n;
            if (curr_node.getC().equals(Node.Color.WHITE)) {
                return false;
            }
        }
        return true;
    }

    private void DFSVisitIter(Stack<NodeData> stack, DWGraph graph) {
        while (!stack.isEmpty()) {
            Node n = (Node) stack.pop();
            n.setC(Node.Color.GRAY);
            for (Integer dest : graph.getEdges().get(n.getKey()).keySet()) {
                Node dest_node = (Node) graph.getNode(dest);
                if (dest_node.getC().equals(Node.Color.WHITE)) {
                    stack.push(dest_node);
                }
            }
            n.setC(Node.Color.BLACK);
        }
    }

    public DWGraph transposeEdges() {
        HashMap<Integer, NodeData> nodes = new HashMap<>();
        HashMap<Integer, HashMap<Integer, EdgeData>> edges = new HashMap<>();
        for (NodeData n : graph.getNodes().values()) {
            Node new_node = new Node(n.getKey(), new GeoLoc(n.getLocation().x(), n.getLocation().y(), n.getLocation().z()));
            new_node.setC(Node.Color.WHITE);
            nodes.put(new_node.getKey(), new_node);
            edges.put(new_node.getKey(), new HashMap<>());
        }
        for (Integer src : graph.getEdges().keySet()) {
            for (Integer dest : graph.getEdges().get(src).keySet()) {
                Edge curr_edge = (Edge) graph.getEdges().get(src).get(dest);
                Edge new_edge = new Edge(curr_edge.getDest(), curr_edge.getWeight(), curr_edge.getSrc());
                edges.get(new_edge.getSrc()).put(new_edge.getDest(), new_edge);
                Node n = (Node) nodes.get(new_edge.getDest());
                n.addNeighbor(new_edge.getSrc());
            }
        }
        return new DWGraph(nodes, edges);
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(file))));
            Edge[] json_edges = gson.fromJson(jsonObject.get("Edges").toString(), Edge[].class);
            JSONArray json_nodes = jsonObject.getJSONArray("Nodes");
            HashMap<Integer, NodeData> nodes = new HashMap<>();
            HashMap<Integer, HashMap<Integer, EdgeData>> edges = new HashMap<>();
            for (int i = 0; i < json_nodes.length(); i++) {
                Node node = Node.deserializeNode(json_nodes.getJSONObject(i).toString());
                nodes.put(node.getKey(), node);
                edges.put(node.getKey(), new HashMap<>());
            }
            for (Edge e : json_edges) {
                Node n = (Node) nodes.get(e.getSrc());
                n.addNeighbor(e.getDest());
                edges.get(e.getSrc()).put(e.getDest(), e);
            }
            this.graph = new DWGraph(nodes, edges);
            return true; //Successfully loaded
        } catch (IOException e) {
            e.printStackTrace();
            return false; //loading unsuccessful
        }
    }
}
