import Graph.GeoLoc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoLocTest {
    private static final double EPS = 0.001*0.001;
    @Test
    void x() {
        GeoLoc g1 = new GeoLoc(0.1, 0.3, 2.5);
        assertEquals(0.1, g1.x(), EPS);
        GeoLoc g2 = new GeoLoc(314.647, 3573.245642, 65.356526);
        assertEquals(314.647, g2.x(), EPS);
    }

    @Test
    void y() {
        GeoLoc g1 = new GeoLoc(0.1, 0.3, 2.5);
        assertEquals(0.3, g1.y(), EPS);
        GeoLoc g2 = new GeoLoc(314.647, 3573.245642, 65.356526);
        assertEquals(3573.245642, g2.y(), EPS);
    }

    @Test
    void z() {
        GeoLoc g1 = new GeoLoc(0.1, 0.3, 2.5);
        assertEquals(2.5, g1.z(), EPS);
        GeoLoc g2 = new GeoLoc(314.647, 3573.245642, 65.356526);
        assertEquals(65.356526, g2.z(), EPS);
    }

    @Test
    void distance() {
        GeoLoc g1 = new GeoLoc(0.1, 0.3, 2.5);
        GeoLoc g2 = new GeoLoc(314.647, 3573.245642, 65.356526);
        assertEquals(3587.315336, g1.distance(g2), EPS);
        assertEquals(3587.315336, g2.distance(g1), EPS);
        GeoLoc g3 = new GeoLoc(0, 0, 0);
        GeoLoc g4 = new GeoLoc(0, 0, 0);
        assertEquals(0, g1.distance(g1));
        assertEquals(0, g2.distance(g2));
        assertEquals(0, g3.distance(g4));
        assertEquals(0, g3.distance(g3));
    }

    @Test
    void testToString() {
        GeoLoc g1 = new GeoLoc(0.1, 0.3, 2.5);
        assertEquals("Graph.GeoLoc{x=0.1, y=0.3, z=2.5}", g1.toString());
    }
}