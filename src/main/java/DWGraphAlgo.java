import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import org.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
                unvisited.addLast(currNode.getKey());
            }
        }
        //initialization complete
        NodeData currNode = src;
        double currVal = 0;
        while (!unvisited.isEmpty()) { //while there are still unvisited nodes
            Iterator<EdgeData> itrNeigh = this.graph.edgeIter(currNode.getKey());
            while (itrNeigh.hasNext()) { // for each unvisited neighbour of the current vertex (from here....
                EdgeData neighEdge = itrNeigh.next();
                if (!visited.contains(neighEdge.getDest())) { // ...to here)
                    double weightToCompare = neighEdge.getWeight() + currVal;
                    if (map.get(neighEdge.getDest())[0] > weightToCompare) {
                        map.put(neighEdge.getDest(), new double[]{weightToCompare, neighEdge.getSrc()});
                    }
                }
            }
            visited.addLast(currNode.getKey());
            unvisited.removeFirstOccurrence(currNode.getKey());

            currNode = this.graph.getNode(smallestNeigh(currNode, visited)); //unvisited vertex with smallest known distance from
            // current vertex
//            currVal = smallestNeighbourKeyAndWeight[1];
            currVal = map.get(currNode.getKey())[0];
        }
        return map;
    }

    private int smallestNeigh(NodeData src, LinkedList<Integer> visited) {
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
        return minKey;
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        double dist = this.DijkstrasAlgo(this.graph.getNode(src)).get(dest)[0];
        if (dist == Double.MAX_VALUE) return -1;
        return dist;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        HashMap<Integer, double[]> map = this.DijkstrasAlgo(this.graph.getNode(src));
        if (map.get(dest)[0] == Double.MAX_VALUE) return null; //if no path exists returns null

        LinkedList<NodeData> ret = new LinkedList<>();
        int currNodeKey = dest;
        while (currNodeKey != src) {
            ret.addFirst(this.graph.getNode(currNodeKey));
            currNodeKey = (int) map.get(currNodeKey)[1];
        }
        ret.addFirst(this.graph.getNode(currNodeKey));
        return ret;
    }

    @Override
    public NodeData center() {
        int[] maxKeys = new int[graph.nodeSize()];
        double[] maxValues = new double[graph.nodeSize()];

        Iterator<NodeData> itr = graph.nodeIter();
        int ind = 0;
        while(itr.hasNext()) {
            NodeData currNode = itr.next();
            maxKeys[ind] = currNode.getKey(); //add the key of the node to the array of keys
            HashMap<Integer, double[]> map = this.DijkstrasAlgo(currNode);
            double currMaxVal = 0;
            for (Map.Entry<Integer, double[]> entry : map.entrySet()) {
                if (currMaxVal < entry.getValue()[0]) {
                    currMaxVal = entry.getValue()[0];
                }
            }
            maxValues[ind] = currMaxVal;
            ind++;
        }

        double minValueInArr = Double.MAX_VALUE;
        int minValIndex = 0;
        for (int i = 0; i < maxValues.length; i++) {
            if (maxValues[i] < minValueInArr) {
                minValueInArr = maxValues[i];
                minValIndex = i;
            }
        }
        return this.graph.getNode(maxKeys[minValIndex]);
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
