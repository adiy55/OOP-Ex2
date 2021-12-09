import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void getKey() {
        Node n1 = new Node(0, new GeoLoc(0.1, 0.3, 2.5));
        assertEquals(0, n1.getKey());
        Node n2 = new Node(42, new GeoLoc(0.1, 0.3, 2.5));
        assertEquals(42, n2.getKey());
    }

    @Test
    void getLocation() {
        Node n1 = new Node(21, new GeoLoc(35.1, 32.2, 0));
        assertEquals(35.1, n1.getLocation().x());
        assertEquals(32.2, n1.getLocation().y());
        assertEquals(0.0, n1.getLocation().z());
    }

    @Test
    void deserializeNode() {
        String str = "{\"pos\":\"35.1,32.2,0.0\",\"id\":21}";
        Node n1 = Node.deserializeNode(str);
        assertEquals(21, n1.getKey());
        assertEquals(35.1, n1.getLocation().x());
        assertEquals(32.2, n1.getLocation().y());
        assertEquals(0.0, n1.getLocation().z());
    }

    @Test
    void getWeight() {
        Node n1 = new Node(21, new GeoLoc(35.1, 32.2, 0));
        assertEquals(0, n1.getWeight());
    }

    @Test
    void getInfo() {
        Node n1 = new Node(21, new GeoLoc(35.1, 32.2, 0));
        n1.setInfo("blah blah");
        assertTrue("blah blah".equals(n1.getInfo()));
    }

    @Test
    void getTag() {
        Node n1 = new Node(21, new GeoLoc(35.1, 32.2, 0));
        n1.setTag(42);
        assertEquals(42, n1.getTag());
    }

    @Test
    void getNeighbors() {
        Node n1 = new Node(21, new GeoLoc(35.1, 32.2, 0));
        n1.addNeighbor(0);
        n1.addNeighbor(3);
        n1.addNeighbor(6);
        HashSet<Integer> set = n1.getNeighbors();
        //TODO: finish iterating over set
    }

    @Test
    void getC() {
        Node n1 = new Node(21, new GeoLoc(35.1, 32.2, 0));
        assertEquals(Node.Color.WHITE, n1.getC());
    }

    @Test
    void testToString() {
        Node n1 = new Node(21, new GeoLoc(35.1, 32.2, 0));
        assertEquals("Node{id=21, location=GeoLoc{x=35.1, y=32.2, z=0.0}}", n1.toString());
    }
}