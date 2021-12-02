import api.GeoLocation;

import java.util.HashMap;

public class Node implements api.NodeData {
    private int id;
    private GeoLocation location;
    private double weight;
    private HashMap<Integer, Edge> edgesOut;
    private String info;
    private int tag;

    public Node (int id, GeoLocation loc, double weight) {
        this.id = id;
        this.location = loc;
        this.weight = weight;
        this.edgesOut = new HashMap<Integer, Edge>();
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

    public HashMap<Integer, Edge> getEdgesOut() {
        return edgesOut;
    }

    public void setEdgesOut(HashMap<Integer, Edge> edgesOut) {
        this.edgesOut = edgesOut;
    }
}
