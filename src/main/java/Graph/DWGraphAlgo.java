package Graph;

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
        load(filename);
    }

    public DWGraphAlgo(DWGraphAlgo g) {
        this.filename = "";
        this.init(g.graph);
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        Iterator<NodeData> node_iter = g.nodeIter(); //iterators
        Iterator<EdgeData> edge_iter = g.edgeIter();
        HashMap<Integer, NodeData> nodes = new HashMap<>(); //new maps
        HashMap<Integer, HashMap<Integer, EdgeData>> edges = new HashMap<>();
        while (node_iter.hasNext()) { //filling maps
            NodeData n = node_iter.next();
            nodes.put(n.getKey(), new Node(n.getKey(), new GeoLoc(n.getLocation().x(), n.getLocation().y(), n.getLocation().z())));
            edges.put(n.getKey(), new HashMap<>());
        }
        while (edge_iter.hasNext()) { //filling maps
            EdgeData e = edge_iter.next();
            edges.get(e.getSrc()).put(e.getDest(), new Edge(e.getSrc(), e.getWeight(), e.getDest()));
            Node n = (Node) nodes.get(e.getDest());
            n.addNeighbor(e.getSrc());
        }
        graph = new DWGraph(nodes, edges); //assigning
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
    public boolean isConnected() { // iterative DFS
        Map.Entry<Integer, NodeData> temp = graph.getNodes().entrySet().iterator().next(); //a single entry to save its key
        Integer firstKey = temp.getKey();
        for (Map.Entry<Integer, NodeData> entry : graph.getNodes().entrySet()) { //for each entry in the hashmap
            if (entry.getKey() != firstKey) { //if the entry's key was not the one saved
                Node curr_node = (Node) entry.getValue();
                curr_node.setC(Node.Color.WHITE);
            }
        }
        return DFS(this.graph, firstKey) && DFS(transposeEdges(), firstKey);
    }

    private boolean DFS(DWGraph graph, int start_id) {
        if (start_id == -1) return false;
        Node start_node = (Node) graph.getNode(start_id);
        Stack<NodeData> stack = new Stack<>();
        stack.push(start_node);
        DFSVisit(stack, graph);
        for (NodeData n : graph.getNodes().values()) {
            Node curr_node = (Node) n;
            if (curr_node.getC().equals(Node.Color.WHITE)) {
                return false;
            }
        }
        return true;
    }

    private void DFSVisit(Stack<NodeData> stack, DWGraph graph) {
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

    public DWGraph transposeEdges() { //turn all edges in the opposite directions
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

    /**
     * Class to be used as comparator of the priority Queue. We pass the result map as an attribute to the constructor.
     * The priority is assigned in ascending order of weights (whichever are discovered up to this point).
     */
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
     * @return Resulting hashmap
     */
    public HashMap<Integer, double[]> DijkstrasAlgo(NodeData src) {
        HashMap<Integer, double[]> map = new HashMap<>();
        //ret[0] == distances
        //ret[1] == previous Graph.Node (key of node) visited (to calculate path)
        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> unvisited = new PriorityQueue<>(new PriorityQueueComparator(map));
        map.put(src.getKey(), new double[]{0, 0.5}); //0.5 is some invalid key of Graph.Node (should be integer)
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
                        unvisited.remove(neighDest); //remove and add the node with corrected priority to the priority Queue
                        unvisited.add(neighDest);
                    }
                }
            }
            visited.add(currNode.getKey());
            if (!unvisited.isEmpty()) {
                currNode = this.graph.getNode(unvisited.remove()); //unvisited vertex with smallest known distance from
                // current vertex
                currVal = map.get(currNode.getKey())[0];
            }
        }
        return map;
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
        while (currNodeKey != src) { // iterate over previous nodes from the dest node, until the src node is found
            ret.addFirst(this.graph.getNode(currNodeKey));
            currNodeKey = (int) map.get(currNodeKey)[1];
        }
        ret.addFirst(this.graph.getNode(currNodeKey)); //add last node
        return ret;
    }

    @Override
    public NodeData center() {
        int minMaxKey = Integer.MAX_VALUE;
        double minMaxValue = Double.MAX_VALUE;

        Iterator<NodeData> itr = graph.nodeIter();
        while (itr.hasNext()) { //for each node
            NodeData currNode = itr.next();
            HashMap<Integer, double[]> map = this.DijkstrasAlgo(currNode);
            double currMaxVal = 0;
            for (Map.Entry<Integer, double[]> entry : map.entrySet()) { //for each entry in map
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

    /**
     * Implemented using a greedy algorithm. For each unvisited city, check which path is shortest (in terms of weight)
     * to each other unvisited node. Choose the closest, append its path to the returned list.
     *
     * @param cities
     * @return list of nodes
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        LinkedList<NodeData> ret = new LinkedList<>(); //list to be returned
        NodeData currNode = cities.get(0); //current node we are working on
        ret.add(currNode);
        HashSet<NodeData> visitedCities = new HashSet<>();
        while (!cities.isEmpty()) { //while there are still unvisited cities
            visitedCities.add(currNode);
            double minDistance = Double.MAX_VALUE;
            NodeData nextNode = currNode;
            cities.remove(currNode);
            List<NodeData> path = new LinkedList<>();

            for (NodeData node : cities) { //For each unvisited node out of the cities, calculate the one which is closest, save its path
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
            for (NodeData node : path) { //The closest node's path (out of all cities) is appended to the list which is to be returned
                if (node != path.get(0)) {
                    ret.addLast(node);
                    visitedCities.add(node);
                    cities.remove(node); //if exists
                }
            }
        }
        return ret;
    }

    /**
     * Creating json (String) objects and arrays out of the graph. appended to one another, returned
     *
     * @param file - the file name (may include a relative path).
     * @return boolean value
     */
    @Override
    public boolean save(String file) {
        JSONArray nodes = new JSONArray();
        JSONArray edges = new JSONArray();

        Iterator<NodeData> itrN = this.graph.nodeIter();
        while (itrN.hasNext()) {
            NodeData currNode = itrN.next();
            JSONObject obj = new JSONObject();
            obj.put("pos", currNode.getLocation().x() + "," + currNode.getLocation().y() + "," + currNode.getLocation().z());
            obj.put("id", currNode.getKey());
            nodes.put(obj);
        }

        Iterator<EdgeData> itrE = this.graph.edgeIter();
        while (itrE.hasNext()) {
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
            for (int i = 0; i < json_nodes.length(); i++) { //for each node
                Node node = Node.deserializeNode(json_nodes.getJSONObject(i).toString());
                nodes.put(node.getKey(), node);
                edges.put(node.getKey(), new HashMap<>());
            }
            for (Edge e : json_edges) { //for each edge
                Node n = (Node) nodes.get(e.getSrc());
                n.addNeighbor(e.getDest());
                edges.get(e.getSrc()).put(e.getDest(), e);
            }
            this.graph = new DWGraph(nodes, edges);
            return true; //Successfully loaded
        } catch (IOException e) {
            e.printStackTrace();
            this.graph = null;
            return false; // loading unsuccessful
        }
    }
}
