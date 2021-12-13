import Graph.DWGraph;
import Graph.DWGraphAlgo;
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
    void init() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/G1.json");
        DWGraphAlgo test2 = new DWGraphAlgo(test1);
        assertEquals(test1.getGraph().nodeSize(), test2.getGraph().nodeSize());
        assertEquals(test1.getGraph().edgeSize(), test2.getGraph().edgeSize());
        assertEquals(test1.getGraph().getEdge(0, 1).getWeight(), test2.getGraph().getEdge(0, 1).getWeight());

        test1 = new DWGraphAlgo("data/emptyGraph.json");
        test2 = new DWGraphAlgo(test1);
        assertEquals(0, test2.getGraph().nodeSize());
        assertEquals(0, test2.getGraph().edgeSize());
    }

    @Test
    void isConnected() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/G1.json");
        assertTrue(test1.isConnected());
        test1.getGraph().removeNode(8);
        test1.getGraph().removeNode(14);
        assertFalse(test1.isConnected());

        test1 = new DWGraphAlgo("data/G2.json");
        assertTrue(test1.isConnected());

        test1 = new DWGraphAlgo("data/G3.json");
        assertTrue(test1.isConnected());

        test1 = new DWGraphAlgo("data/emptyGraph.json");
        assertFalse(test1.isConnected());
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
        assertEquals(4.827, test1.shortestPathDist(0, 6), 0.001);

        test1 = new DWGraphAlgo("data/1000Nodes.json");
        assertEquals(714.397, test1.shortestPathDist(0, 6), 0.001);

        test1 = new DWGraphAlgo("data/emptyGraph.json");
        assertEquals(-1, test1.shortestPathDist(0, 6), 0.001);
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
        assertNull(lst);

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

        test1 = new DWGraphAlgo("data/emptyGraph.json");
        assertEquals(null, test1.shortestPath(0, 6));
    }

    @Test
    void center() {
        DWGraphAlgo test = new DWGraphAlgo("data/Test1.json");
        assertEquals(0, test.center().getKey());

        test = new DWGraphAlgo("data/Test2.json");
        assertEquals(3, test.center().getKey());

        test = new DWGraphAlgo("data/1000Nodes.json");
        assertEquals(362, test.center().getKey());

        test = new DWGraphAlgo("data/emptyGraph.json");
        assertEquals(null, test.center());
    }

    @Test
    void tsp() {
        DWGraphAlgo test = new DWGraphAlgo("data/Test2.json");
        List<NodeData> lstTest = new LinkedList<>();
        lstTest.add(test.getGraph().getNode(4));
        lstTest.add(test.getGraph().getNode(2));
        test = new DWGraphAlgo("data/G1.json");
        lstTest = new LinkedList<>();
        lstTest.add(test.getGraph().getNode(2));
        lstTest.add(test.getGraph().getNode(5));
        lstTest.add(test.getGraph().getNode(9));
        List<NodeData> lst = test.tsp(lstTest);
        assertEquals(2, lst.get(0).getKey());
        assertEquals(6, lst.get(1).getKey());
        assertEquals(5, lst.get(2).getKey());
        assertEquals(6, lst.get(3).getKey());
        assertEquals(7, lst.get(4).getKey());
        assertEquals(8, lst.get(5).getKey());
        assertEquals(9, lst.get(6).getKey());

        DWGraphAlgo test1 = new DWGraphAlgo("data/emptyGraph.json");
        assertEquals(null, test1.tsp(lstTest));
    }

    @Test
    void save() {
        DWGraphAlgo test1 = new DWGraphAlgo("data/Test1.json");
        test1.save("data/testOutput.json");
        DWGraphAlgo test2 = new DWGraphAlgo("data/testOutput.json");
        assertEquals(test1.getGraph().getNode(0).getWeight(), test2.getGraph().getNode(0).getWeight());
        assertEquals(test1.getGraph().getNode(2).getWeight(), test2.getGraph().getNode(2).getWeight());

        test1 = new DWGraphAlgo("data/G1.json");
        test1.save("data/testOutput.json");
        test2 = new DWGraphAlgo("data/testOutput.json");
        assertEquals(test1.getGraph().getEdge(0, 1).getWeight(), test2.getGraph().getEdge(0, 1).getWeight());
        assertEquals(test1.getGraph().getNode(1).getLocation().x(), test2.getGraph().getNode(1).getLocation().x());

        test1 = new DWGraphAlgo("data/emptyGraph.json");
        assertEquals(0, test1.getGraph().nodeSize());
        assertEquals(0, test1.getGraph().edgeSize());
    }

/**
 * Tests run on big graphs:
 */
//    @Test
//    void bigTest1() {
//        DWGraphAlgo test = new DWGraphAlgo("data/100000.json");
//        List<NodeData> lstTest = new LinkedList<>();
//        lstTest.add(test.getGraph().getNode(33));
//        lstTest.add(test.getGraph().getNode(675));
//        lstTest.add(test.getGraph().getNode(123));
//        lstTest.add(test.getGraph().getNode(7));
//        lstTest.add(test.getGraph().getNode(654));
//        lstTest.add(test.getGraph().getNode(677));
//        List<NodeData> lst = test.tsp(lstTest);
//        for (int i = 0; i < lst.size(); i++) {
//            System.out.println(lst.get(i).getKey());
//        }
//        System.out.println();
//    }
//    @Test
//    void bigTest2() {
//        DWGraphAlgo test = new DWGraphAlgo("data/100000.json");
//        System.out.println(test.center());
//    }
//
//    @Test
//    void bigTest3() {
//        DWGraphAlgo test = new DWGraphAlgo("data/100000.json");
//        System.out.println(test.shortestPath(31, 826));
//    }
}