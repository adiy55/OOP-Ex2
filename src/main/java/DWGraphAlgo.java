import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import org.json.*;

import java.io.FileWriter;
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



    private class PriorityQueueComparator implements Comparator<Integer> {
        private HashMap<Integer, double[]> map;

        public PriorityQueueComparator(HashMap<Integer, double[]> map) {
            this.map = map;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            if (map.get(o1)[0] == map.get(o2)[0]) return 0;
            else if (map.get(o1)[0] < map.get(o2)[0]) return -1;
            else return 1;
        }
    }


    /**
     * Idea for implementation of algorithm taken from:
     * https://www.youtube.com/watch?v=pVfj6mxhdMw
     * and from
     * https://www.youtube.com/watch?v=XB4MIexjvY0
     * and from
     * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     * <p>
     * Function to calculate the shortest distances between the src an all other nodes. returns a hashmap containing
     * keys of nodes, their minimal distance from src node, and the previous node they visited on their way from
     * the src node
     *
     * @param src
     * @return
     */
    private HashMap<Integer, double[]> DijkstrasAlgo(NodeData src) {
        HashMap<Integer, double[]> map = new HashMap<>();
        //ret[0] == distances
        //ret[1] == previous Node (key of node) visited (to calculate path)
        HashSet<Integer> visited = new HashSet<>();
        HashSet<Integer> unvisited = new HashSet<>();
        //ArrayList<Integer> unvisited = new ArrayList<>();
        map.put(src.getKey(), new double[]{0, 0.5}); //0.5 is some invalid key of Node (should be integer)
        unvisited.add(src.getKey());
        Iterator<NodeData> itr = this.graph.nodeIter();
        while (itr.hasNext()) { //initialization of distances to "infinity" except the src node,
            // which was already initialized at 0
            NodeData currNode = itr.next();
            if (currNode.getKey() != src.getKey()) {
                map.put(currNode.getKey(), new double[]{Double.MAX_VALUE, 0.5});
                unvisited.add(currNode.getKey());
            }
        }
        //initialization complete
        NodeData currNode = src;
        double currVal = 0;
        while (!unvisited.isEmpty()) { //while there are still unvisited nodes
            Iterator<EdgeData> itrNeigh = this.graph.edgeIter(currNode.getKey());
            while (itrNeigh.hasNext()) { // for each unvisited neighbour of the current vertex (from here....
                EdgeData neighEdge = itrNeigh.next();
                if (!visited.contains(neighEdge.getSrc())) { // ...to here)
                    double weightToCompare = neighEdge.getWeight() + currVal;
                    int neighDest = neighEdge.getDest();
                    if (map.get(neighDest)[0] > weightToCompare) {
                        map.put(neighDest, new double[]{weightToCompare, neighEdge.getSrc()});
                    }
                }
            }
            visited.add(currNode.getKey());
            unvisited.remove(currNode.getKey());

            currNode = this.graph.getNode(smallestNeigh(unvisited, visited, map)); //unvisited vertex with smallest known distance from
            // current vertex
            currVal = map.get(currNode.getKey())[0];
        }
        return map;
    }

    /**
     * Function to return the key of the unvisited node with the smallest value.
     *
     * @param unvisited hashset of unvisited nodes
     * @param visited   hashset of visited nodes
     * @param map       map of values
     * @return double
     */
    private int smallestNeigh(HashSet<Integer> unvisited, HashSet<Integer> visited, HashMap<Integer, double[]> map) {
        double minVal = Double.MAX_VALUE;
        int minKey = 0;
//        Iterator<NodeData> itrNeigh = this.graph.nodeIter();
        for (Integer i : unvisited) {
            NodeData currNode = this.graph.getNode(i);
            if (!visited.contains(currNode.getKey())) {
                if (minVal >= map.get(currNode.getKey())[0]) {
                    minVal = map.get(currNode.getKey())[0];
                    minKey = currNode.getKey();
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
        int minMaxKey = Integer.MAX_VALUE;
        double minMaxValue = Double.MAX_VALUE;

        Iterator<NodeData> itr = graph.nodeIter();
        while (itr.hasNext()) {
            NodeData currNode = itr.next();
            HashMap<Integer, double[]> map = this.DijkstrasAlgo(currNode);
            double currMaxVal = 0;
            for (Map.Entry<Integer, double[]> entry : map.entrySet()) {
                if (currMaxVal < entry.getValue()[0]) {
                    currMaxVal = entry.getValue()[0];
                }
            }
            if (minMaxValue > currMaxVal) {
                minMaxKey = currNode.getKey();
                minMaxValue = currMaxVal;
            }
        }
        return this.graph.getNode(minMaxKey);
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        LinkedList<NodeData> ret = new LinkedList<>(); //list to be returned
        NodeData currNode = cities.get(0); //current node we are working on
        ret.add(currNode);
        HashSet<NodeData> visitedCities = new HashSet<>();
        while (!cities.isEmpty()) {
            visitedCities.add(currNode);
            double minDistance = Double.MAX_VALUE;
            NodeData nextNode = currNode;
            cities.remove(currNode);
            List<NodeData> path = new LinkedList<>();

            for (NodeData node : cities) {
                if (!visitedCities.contains(node)) {
                    double currDistance = this.shortestPathDist(currNode.getKey(), node.getKey());
                    if (currDistance < minDistance) {
                        minDistance = currDistance;
                        nextNode = node;
                        path = this.shortestPath(currNode.getKey(), node.getKey());
                    }
                }
            }
            currNode = nextNode;
            for (NodeData node : path) {
                if (node != path.get(0)) {
                    ret.addLast(node);
                    visitedCities.add(node);
                    cities.remove(node); //if exists
                }
            }
        }
        return ret;
    }

    @Override
    public boolean save(String file) {
        JSONArray nodes = new JSONArray();
        JSONArray edges = new JSONArray();

        Iterator<NodeData> itrN = this.graph.nodeIter();
        while(itrN.hasNext()) {
            NodeData currNode = itrN.next();
            JSONObject obj = new JSONObject();
            obj.put("pos", currNode.getLocation().x()+","+ currNode.getLocation().y()+","+currNode.getLocation().z());
            obj.put("id", currNode.getKey());
            nodes.put(obj);
        }

        Iterator<EdgeData> itrE = this.graph.edgeIter();
        while(itrE.hasNext()) {
            EdgeData currEdge = itrE.next();
            JSONObject obj = new JSONObject();
            obj.put("src", currEdge.getSrc()).put("w", currEdge.getWeight()).put("dest", currEdge.getDest());
            edges.put(obj);
        }

        JSONObject graph = new JSONObject().put("Edges", edges).put("Nodes", nodes);

        try {
            FileWriter fw = new FileWriter(file);
            fw.write(graph + "");
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
