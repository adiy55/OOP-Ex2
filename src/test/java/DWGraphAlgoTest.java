import org.junit.jupiter.api.Test;

import java.util.HashMap;

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
    }

    @Test
    void shortestPath() {
    }

    @Test
    void center() {
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