import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
class DWGraphAlgoTest {

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
        System.out.println();
    }
}