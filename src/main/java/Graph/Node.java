package Graph;

import api.GeoLocation;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashSet;

public class Node implements api.NodeData {
    private int id;
    private GeoLocation location;
    private double weight;
    private HashSet<Integer> neighbors; // node id (the src) with an edge that dest == this node
    private String info;
    private int tag;
    private Color c;

    public enum Color {
        WHITE,
        GRAY,
        BLACK
    }

    /**
     * Basic Constructor
     * @param id
     * @param loc
     */
    public Node(int id, GeoLocation loc) {
        this.id = id;
        this.location = loc;
        this.weight = 0;
        this.neighbors = new HashSet<>();
        this.c = Color.WHITE;
    }

    /**
     * Copy Constructor
     * @param old
     */
    public Node(Node old) {
        this.id = old.id;
        this.location = new GeoLoc((GeoLoc) old.location);
        this.weight = old.weight;
        this.neighbors = Utilities.copyNeighbours(old.neighbors);
        this.info = old.info;
        this.tag = old.tag;
        this.c = Color.WHITE;
    }

    /**
     * Receive json string, convert to node
     * @param json
     * @return Node
     */
    public static Node deserializeNode(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializer<Node> deserializer = new JsonDeserializer<>() {
            @Override
            public Node deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                return new Node(
                        jsonObject.get("id").getAsInt(),
                        new GeoLoc(jsonObject.get("pos").getAsString())
                );
            }
        };
        gsonBuilder.registerTypeAdapter(Node.class, deserializer);
        Gson customGson = gsonBuilder.create();
        return customGson.fromJson(json, Node.class);
    }

    @Override
    public int getKey() {
        return this.id;
    }

    @Override
    public GeoLocation getLocation() {
        if (this.location != null) return this.location;
        return null;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public HashSet<Integer> getNeighbors() {
        return this.neighbors;
    }

    public void addNeighbor(int neighbor_id) {
        neighbors.add(neighbor_id);
    }

    public void removeNeighbor(int neighbor_id) {
        neighbors.remove(neighbor_id);
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Graph.Node{" +
                "id=" + id +
                ", location=" + location +
                '}';
    }
}

