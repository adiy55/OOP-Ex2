import com.google.gson.*;

import java.lang.reflect.Type;

public class Edge implements api.EdgeData {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public Edge(int src, double weight, int dest) {
        this.src = src;
        this.weight = weight;
        this.dest = dest;
    }

    public static Edge deserializeEdge(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializer<Edge> deserializer = (json1, typeOfT, context) -> {
            JsonObject jsonObject = json1.getAsJsonObject();
            return new Edge(
                    jsonObject.get("src").getAsInt(),
                    jsonObject.get("w").getAsDouble(),
                    jsonObject.get("dest").getAsInt()
            );
        };
        gsonBuilder.registerTypeAdapter(Edge.class, deserializer);
        Gson customGson = gsonBuilder.create();
        return customGson.fromJson(json, Edge.class);
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
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

    @Override
    public String toString() {
        return "Edge{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + weight +
                '}';
    }
}
