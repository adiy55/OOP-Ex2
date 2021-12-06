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
import java.util.List;
import java.util.Stack;

public class DWGraphAlgo implements api.DirectedWeightedGraphAlgorithms {
    private DWGraph graph;
    private String filename;

    public DWGraphAlgo(String filename) {
        this.filename = filename;
        boolean flag = load(filename);
        if (!flag) this.graph = null;
    }

//    public DWGraphAlgo(String file) {
//        filename = file;
//    }

    @Override
    public void init(DirectedWeightedGraph g) { // todo: can we assume that this is a DWGraph ??????
        graph = (DWGraph) g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
//        return new DWGraph(this.graph);
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

//    private boolean DFS() {
//        HashMap<Integer, NodeData> nodes = graph.getNodes();
//        HashMap<Integer, HashMap<Integer, EdgeData>> edges = graph.getEdges();
//        Stack<Node> stack = new Stack<>();
//        for (NodeData n: graph.getEdges().) {
//            Node curr_node = (Node) n;
//            curr_node.setC(Node.Color.WHITE);
//        }
//        for (NodeData n: nodes.values()) {
//            Node curr_node = (Node) n;
//            if(curr_node.getC().equals(Node.Color.WHITE)){
//
//            }
//        }
//
//    }
//
//    private void DFSVisit(Stack<Node> s){
//        Node n = s.pop();
//        n.setC(Node.Color.GRAY);
//        for (EdgeData e : graph.getEdges().get(n.getKey()).values()) {
//
//
//        }
//
//    }

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
        System.out.println(new Gson().toJson(this.graph));
        return true;
    }

    @Override
    public boolean load(String file) {
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(file))));
//            String[] json_edges = gson.fromJson(String.valueOf(jsonObject.get("Edges")), String[].class);

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

    public static void main(String[] args) {
        DWGraphAlgo dw = new DWGraphAlgo("data/G1.json");
        dw.load(dw.filename);
        System.out.println(dw.graph.getEdges());

    }
}
