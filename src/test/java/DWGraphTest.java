import api.EdgeData;
import api.NodeData;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DWGraphTest {
    private static final double EPS = 0.001*0.001;
    @Test
    void getNode() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/Test1.json").getGraph());
        assertEquals(35.19589389346247, test.getNode(0).getLocation().x(), EPS);
    }

    @Test
    void getEdge() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/Test1.json").getGraph());
        assertEquals(1, test.getEdge(0, 1).getWeight(), EPS);
        assertEquals(6, test.getEdge(0, 2).getWeight(), EPS);
    }

    @Test
    void addNode() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/Test1.json").getGraph());
        test.addNode(new Node(5, new GeoLoc(1,54,0)));
        assertEquals(1, test.getNode(5).getLocation().x(), EPS);
        assertEquals(6, test.nodeSize());

        test.addNode(new Node(5, new GeoLoc(1,54,0))); //adding another identical node. Verifying the size didn't change
        assertEquals(6, test.nodeSize());
    }

    @Test
    void connect() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        test.connect(0, 10, 5.65);
        assertEquals(5.65, test.getEdge(0, 10).getWeight(), EPS);
    }

    @Test
    void nodeIter() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        Iterator<NodeData> itr = test.nodeIter();
        int i = 0;
        while (itr.hasNext()) {
            i++;
            itr.next();
        }
        assertEquals(17, i);
    }

    @Test
    void edgeIter() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        Iterator<EdgeData> itr = test.edgeIter();
        int i = 0;
        while (itr.hasNext()) {
            i++;
            itr.next();
        }
        assertEquals(36, i);
    }

    @Test
    void testEdgeIter() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        Iterator<EdgeData> itr = test.edgeIter(2);
        int i = 0;
        while (itr.hasNext()) {
            i++;
            itr.next();
        }
        assertEquals(3, i);
    }

    @Test
    void removeNode() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        test.removeNode(0);
        assertEquals(16, test.nodeSize());
        assertEquals(32, test.edgeSize());
    }

    @Test
    void removeEdge() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        test.removeEdge(0, 1);
        assertEquals(17, test.nodeSize());
        assertEquals(35, test.edgeSize());
        assertEquals(1.8635670623870366, test.getEdge(1, 0).getWeight(), EPS); //didn't remove the edge in the opposite direction
    }

    @Test
    void nodeSize() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        assertEquals(17, test.nodeSize());

        test = (DWGraph)(new DWGraphAlgo("data/Test1.json").getGraph());
        assertEquals(5, test.nodeSize());
    }

    @Test
    void edgeSize() {
        DWGraph test = (DWGraph)(new DWGraphAlgo("data/G1.json").getGraph());
        assertEquals(36, test.edgeSize());

        test = (DWGraph)(new DWGraphAlgo("data/Test1.json").getGraph());
        assertEquals(8, test.edgeSize());
    }
}