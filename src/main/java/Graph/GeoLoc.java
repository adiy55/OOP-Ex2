package Graph;

public class GeoLoc implements api.GeoLocation {
    private double x;
    private double y;
    private double z;

    public GeoLoc(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GeoLoc(GeoLoc g) {
        this.x = g.x;
        this.y = g.y;
        this.z = g.z;
    }

    public GeoLoc(String loc) {
        String[] str = loc.split(",");
        x = Double.parseDouble(str[0]);
        y = Double.parseDouble(str[1]);
        z = Double.parseDouble(str[2]);
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(api.GeoLocation g) {
        double xSq = Math.pow((this.x - g.x()), 2);
        double ySq = Math.pow((this.y - g.y()), 2);
        double zSq = Math.pow((this.z - g.z()), 2);
        return Math.sqrt(xSq + ySq + zSq);
    }

    @Override
    public String toString() {
        return "Graph.GeoLoc{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
