import com.google.gson.*;

import java.lang.reflect.Type;
import java.security.spec.ECGenParameterSpec;

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

    public Edge(Edge e) {
        this.src = e.src;
        this.dest = e.dest;
        this.weight = e.weight;
        this.info = e.info;
        this.tag = e.tag;
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
