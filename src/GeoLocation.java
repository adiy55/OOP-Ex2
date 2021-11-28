public class GeoLocation implements api.GeoLocation {
    private double x;
    private double y;
    private double z;

    public GeoLocation(){

    }

    @Override
    public double x() {
        return 0;
    }

    @Override
    public double y() {
        return 0;
    }

    @Override
    public double z() {
        return 0;
    }

    @Override
    public double distance(api.GeoLocation g) {
        return 0;
    }
}
