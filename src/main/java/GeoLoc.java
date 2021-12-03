public class GeoLoc implements api.GeoLocation {
    private double x;
    private double y;
    private double z;

    public GeoLoc(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
}
