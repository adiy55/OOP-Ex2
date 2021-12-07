import api.NodeData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class DWGraphAlgoTest {
    private static final double EPS = 0.001 * 0.001;
    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
    }

    @Test
    void isConnected() {
    }

    @Test
    void shortestPathDist() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/Test1.json");
        assertEquals(2.0, test1.shortestPathDist(0, 3), EPS);
        assertEquals(5.0, test1.shortestPathDist(2, 4), EPS);
        assertEquals(6.0, test1.shortestPathDist(1, 4), EPS);
        assertEquals(7.0, test1.shortestPathDist(0, 4), EPS);
    }

    @Test
    void shortestPath() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/Test1.json");
        List<NodeData> lst = test1.shortestPath(0,3);
        assertEquals(0, lst.get(0).getKey());
        assertEquals(1, lst.get(1).getKey());
        assertEquals(3, lst.get(2).getKey());

        lst = test1.shortestPath(4,0);
        assertEquals(null, lst);

        lst = test1.shortestPath(0,4);
        assertEquals(0, lst.get(0).getKey());
        assertEquals(1, lst.get(1).getKey());
        assertEquals(3, lst.get(2).getKey());
        assertEquals(4, lst.get(3).getKey());
    }

    @Test
    void center() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/Test1.json");
        assertEquals(0, test1.center().getKey());
    }

    @Test
    void tsp() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }

    @Test
    void DijkstrasAlgo() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/Test1.json");
        HashMap<Integer, double[]> temp = test1.DijkstrasAlgo(test1.getGraph().getNode(0));
        assertArrayEquals(temp.get(0), new double[] {0.0, 0.5}, EPS);
        assertArrayEquals(temp.get(1), new double[] {1.0, 0.0}, EPS);
        assertArrayEquals(temp.get(2), new double[] {3.0, 1.0}, EPS);
        assertArrayEquals(temp.get(3), new double[] {2.0, 1.0}, EPS);
        assertArrayEquals(temp.get(4), new double[] {7.0, 3.0}, EPS);
    }
}