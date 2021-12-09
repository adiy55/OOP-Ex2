import Graph.Edge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    private static final double EPS = 0.001 * 0.001;

    @Test
    void getSrc() {
        Edge e1 = new Edge(0, 0.2, 2);
        assertEquals(0, e1.getSrc());
    }

    @Test
    void getDest() {
        Edge e1 = new Edge(0, 0.2, 2);
        assertEquals(2, e1.getDest());
    }

    @Test
    void getWeight() {
        Edge e1 = new Edge(0, 0.2, 2);
        assertEquals(0.2, e1.getWeight(), EPS);
    }

    @Test
    void getInfo() {
        Edge e1 = new Edge(0, 0.2, 2);
        e1.setInfo("blah blah");
        assertEquals("blah blah", e1.getInfo());
    }

    @Test
    void getTag() {
        Edge e1 = new Edge(0, 0.2, 2);
        e1.setTag(42);
        assertEquals(42, e1.getTag());
    }

    @Test
    void testToString() {
        Edge e1 = new Edge(0, 0.2, 2);
        assertEquals("Graph.Edge{src=0, dest=2, weight=0.2}", e1.toString());
    }
}