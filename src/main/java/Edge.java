import com.google.gson.*;

import java.lang.reflect.Type;
import java.security.spec.ECGenParameterSpec;

public class Edge implements api.EdgeData {
    private int src;
    private int dest;
    private double w;
    private String info;
    private int tag;

    public Edge(int src, double w, int dest) {
        this.src = src;
        this.w = w;
        this.dest = dest;
    }

    public Edge(Edge e) {
        this.src = e.src;
        this.dest = e.dest;
        this.w = e.w;
        this.info = e.info;
        this.tag = e.tag;
    }

    public boolean equals (Object o) {
        if (! (o instanceof Edge)) return false;
        Edge e = (Edge) o;
        if (e.src != this.src) return false;
        if (e.dest != this.dest) return false;
        if (e.w != this.w) return false;
        if (!e.info.equals(this.info)) return false;
        if (e.tag != this.tag) return false;
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
        return this.w;
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
                ", weight=" + w +
                '}';
    }
}
