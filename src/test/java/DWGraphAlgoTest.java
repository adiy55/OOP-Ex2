import api.DirectedWeightedGraph;
import api.NodeData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class DWGraphAlgoTest {
    private static final double EPS = 0.001 * 0.001;
    @Test
    void init() {
    }

    @Test
    void copy() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/G1.json");
        DWGraph testGraph = (DWGraph) test1.getGraph();
        DWGraph test2 = (DWGraph) test1.copy();
        assertNotEquals(testGraph, test2);
        assertNotEquals(testGraph.getNode(0), test2.getNode(0));
        assertEquals(testGraph.getNode(0).getKey(), test2.getNode(0).getKey());
        assertEquals(testGraph.getEdge(6, 5).getWeight(), test2.getEdge(6,5).getWeight());
    }

    @Test
    void isConnected() {//TODO
    }

    @Test
    void shortestPathDist() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/Test1.json");
        assertEquals(2.0, test1.shortestPathDist(0, 3), EPS);
        assertEquals(5.0, test1.shortestPathDist(2, 4), EPS);
        assertEquals(6.0, test1.shortestPathDist(1, 4), EPS);
        assertEquals(7.0, test1.shortestPathDist(0, 4), EPS);

        test1 = new DWGraphAlgo("data/Test2.json");
        //graph picture: https://prnt.sc/22b1x3v
        assertEquals(3, test1.shortestPathDist(3, 2));
        assertEquals(9, test1.shortestPathDist(4, 0));

        test1 = new DWGraphAlgo("data/G1.json");
        assertEquals(7.436, test1.shortestPathDist(0, 8), 0.001);
        System.out.println(test1.shortestPathDist(0, 6));

        test1 = new DWGraphAlgo("data/1000Nodes.json");
        System.out.println(test1.shortestPathDist(0, 2));
    }

    @Test
    void shortestPath() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/Test1.json");
        List<NodeData> lst = test1.shortestPath(0, 3);
        assertEquals(0, lst.get(0).getKey());
        assertEquals(1, lst.get(1).getKey());
        assertEquals(3, lst.get(2).getKey());
        assertEquals(3, lst.size());

        lst = test1.shortestPath(4, 0);
        assertEquals(null, lst);

        lst = test1.shortestPath(0, 4);
        assertEquals(0, lst.get(0).getKey());
        assertEquals(1, lst.get(1).getKey());
        assertEquals(3, lst.get(2).getKey());
        assertEquals(4, lst.get(3).getKey());
        assertEquals(4, lst.size());


        test1 = new DWGraphAlgo("data/G1.json");
        lst = test1.shortestPath(0, 8);
        assertEquals(0, lst.get(0).getKey());
        assertEquals(1, lst.get(1).getKey());
        assertEquals(2, lst.get(2).getKey());
        assertEquals(6, lst.get(3).getKey());
        assertEquals(7, lst.get(4).getKey());
        assertEquals(8, lst.get(5).getKey());
        assertEquals(6, lst.size());

        test1 = new DWGraphAlgo("data/Test2.json");
        //graph picture: https://prnt.sc/22b1x3v
        lst = test1.shortestPath(4, 0);
        assertEquals(4, lst.get(0).getKey());
        assertEquals(3, lst.get(1).getKey());
        assertEquals(1, lst.get(2).getKey());
        assertEquals(0, lst.get(3).getKey());
        assertEquals(4, lst.size());

        test1 = new DWGraphAlgo("data/G1.json");
        lst = test1.shortestPath(2, 5);
        assertEquals(2, lst.get(0).getKey());
        assertEquals(6, lst.get(1).getKey());
        assertEquals(5, lst.get(2).getKey());
        assertEquals(3, lst.size());
    }

    @Test
    void center() { //TODO: finish and debug
        DWGraphAlgo test = new DWGraphAlgo("data/Test1.json");
        assertEquals(0, test.center().getKey());

        test = new DWGraphAlgo("data/Test2.json");
        assertEquals(3, test.center().getKey());

        test = new DWGraphAlgo("data/1000Nodes.json");
        System.out.println(test.center().getKey());
    }

    @Test
    void tsp() {
        DWGraphAlgo test = new DWGraphAlgo("data/Test2.json");
        List<NodeData> lstTest = new LinkedList<NodeData>();
        lstTest.add(test.getGraph().getNode(4));
        lstTest.add(test.getGraph().getNode(2));
        System.out.println(test.tsp(lstTest));

        test = new DWGraphAlgo("data/G1.json");
        lstTest = new LinkedList<NodeData>();
        lstTest.add(test.getGraph().getNode(2));
        lstTest.add(test.getGraph().getNode(5));
        lstTest.add(test.getGraph().getNode(9));
        List<NodeData> lst = test.tsp(lstTest);
        for (int i = 0; i < lst.size(); i++) {
            System.out.println(lst.get(i).getKey());
        }
    }

    @Test
    void save() { //TODO
        DWGraphAlgo test = new DWGraphAlgo("data/G1.json");
        test.save("data/test1G1.json");
        DWGraphAlgo test1 = new DWGraphAlgo("data/test1G1.json");

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