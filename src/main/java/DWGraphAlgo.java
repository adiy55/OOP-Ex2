import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import org.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        return new DWGraph(this.graph);
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

//    private Object[] floydWarshall() {
//        double[][] distances = new double[this.graph.nodeSize()][this.graph.nodeSize()];
//        for (int i = 0; i < distances.length; i++) {
//            for (int j = 0; j < distances.length; j++) {
//                distances[i][j] = Integer.MAX_VALUE;
//            }
//        }
//        int[][] nexts = new int[this.graph.nodeSize()][this.graph.nodeSize()];
//        for (int i = 0; i < distances.length; i++) {
//            for (int j = 0; j < distances.length; j++) {
//                nexts[i][j] = -1;
//            }
//        }
//
//        Iterator<EdgeData> itrEdges = this.graph.edgeIter();
//        while (itrEdges.hasNext()) {
//            EdgeData currEdge = itrEdges.next();
//            Node tempSrcNode = (Node)(this.graph.getNode(currEdge.getSrc()));
//            Node tempDestNode = (Node)(this.graph.getNode(currEdge.getDest()));
//            distances[tempSrcNode.getOurID()][tempDestNode.getOurID()] = currEdge.getWeight();
//            nexts[tempSrcNode.getOurID()][tempDestNode.getOurID()] = tempDestNode.getOurID();
//        }
//
//        Iterator<NodeData> itrNodes = this.graph.nodeIter();
//        while (itrEdges.hasNext()) {
//            Node currNode = (Node) itrNodes.next();
//            distances[currNode.getOurID()][currNode.getOurID()] = 0;
//            nexts[currNode.getOurID()][currNode.getOurID()] = currNode.getOurID();
//        }
//
//        for (int k = 0; k < this.graph.nodeSize(); k++) { // standard Floyd-Warshall implementation
//            for (int i = 0; i < this.graph.nodeSize(); i++) {
//                for (int j = 0; j < this.graph.nodeSize(); j++) {
//                    if (distances[i][j] > distances[i][k] + distances[k][j]) {
//                        distances[i][j] = distances[i][k] + distances[k][j];
//                        nexts[i][j] = nexts[i][k];
//                    }
//                }
//            }
//        }
//        Object[] arr = new Object[2];
//        arr[0] = distances;
//        arr[1] = nexts;
//        return arr;
//    }
//
//    private void path(int[][] nexts, Node src, Node dest) {
//        LinkedList<NodeData> currPath = new LinkedList<>();
//        if (nexts[src.getOurID()][dest.getOurID()] == -1) {
//            return;
//        }
//        currPath.addLast(src);
//        while (src.getOurID() != dest.getOurID()) {
//            dest = nexts[][];
//        }
//    }

    public HashMap<Integer, double[]> DijkstrasAlgo(NodeData src) {
        HashMap<Integer, double[]> map = new HashMap<>();
        //ret[0] == distances
        //ret[1] == previous Node (key of node) visited (to calculate path)
        LinkedList<Integer> visited = new LinkedList<>();
        LinkedList<Integer> unvisited = new LinkedList<>();
        map.put(src.getKey(), new double[]{0, 0.5}); //0.5 is some invalid key of Node (should be integer)
        unvisited.addLast(src.getKey());
        Iterator<NodeData> itr = this.graph.nodeIter();
        while (itr.hasNext()) { //initialization of distances to "infinity" except the src node,
            // which was already initialized at 0
            NodeData currNode = itr.next();
            if (currNode.getKey() != src.getKey()) {
                map.put(currNode.getKey(), new double[]{Double.MAX_VALUE, 0.5});
            }
            unvisited.addLast(currNode.getKey());
        }
        //initialization complete

        while (!unvisited.isEmpty()) { //while there are still unvisited nodes
            double[] tempArr = smallestNeigh(src, visited);
            NodeData currNode = this.graph.getNode((int) tempArr[0]); //visit unvisited vertex with smallest known distance from
            // current vertex
            Iterator<EdgeData> itrNeigh = this.graph.edgeIter(currNode.getKey());
            while (itrNeigh.hasNext()) { // for each unvisited neighbour of the current vertex (from here....
                EdgeData neighEdge = itrNeigh.next();
                if (!visited.contains(neighEdge.getDest())) { // ...to here)
                    double weightToCompare = neighEdge.getWeight() + tempArr[1];
                    if (map.get(neighEdge.getDest())[0] > weightToCompare) {
                        map.put(neighEdge.getDest(), new double[]{weightToCompare, neighEdge.getDest()});
                    }
                }
            }
            visited.addLast(currNode.getKey());
            unvisited.removeFirstOccurrence(currNode.getKey());
        }
        return map;
    }

    private double[] smallestNeigh(NodeData src, LinkedList<Integer> visited) {
        double minVal = Double.MAX_VALUE;
        int minKey = 0;
        Iterator<EdgeData> itrNeigh = this.graph.edgeIter(src.getKey());
        while (itrNeigh.hasNext()) {
            EdgeData currEdge = itrNeigh.next();
            if (!visited.contains(currEdge.getDest())) {
                if (minVal > currEdge.getWeight()) {
                    minVal = currEdge.getWeight();
                    minKey = currEdge.getDest();
                }
            }
        }
        return new double[]{minKey, minVal};
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
        System.out.println(new Gson().toJson(this.graph));
        return true;
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
