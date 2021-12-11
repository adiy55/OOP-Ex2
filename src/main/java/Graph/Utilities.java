package Graph;

import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Utilities {
    /**
     * deep copy of all nodes. Returned map of copied nodes
     *
     * @param original
     * @return
     */
    public static HashMap<Integer, NodeData> copyNodes(HashMap<Integer, NodeData> original) {
        HashMap<Integer, NodeData> copy = new HashMap<>();
        for (Map.Entry<Integer, NodeData> entry : original.entrySet()) {
            copy.put(entry.getKey(), new Node((Node) entry.getValue()));
        }
        return copy;
    }

    /**
     * deep copy of all edges. Returned map of copied edges
     *
     * @param original
     * @return
     */
    public static HashMap<Integer, HashMap<Integer, EdgeData>> copyEdges
    (HashMap<Integer, HashMap<Integer, EdgeData>> original) {
        HashMap<Integer, HashMap<Integer, EdgeData>> copy = new HashMap<>();
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : original.entrySet()) {
            HashMap<Integer, EdgeData> subCopy = copyInsideEdges(entry.getValue());
            copy.put(entry.getKey(), subCopy);
        }
        return copy;
    }

    /**
     * helper function to the copyEdges function. Copies the inner map of the edges.
     *
     * @param original
     * @return
     */
    private static HashMap<Integer, EdgeData> copyInsideEdges(HashMap<Integer, EdgeData> original) {
        HashMap<Integer, EdgeData> copy = new HashMap<>();
        for (Map.Entry<Integer, EdgeData> entry : original.entrySet()) {
            copy.put(entry.getKey(), new Edge((Edge) entry.getValue()));
        }
        return copy;
    }

    /**
     * deep copy of hashset of neighbours (integers: keys of nodes)
     *
     * @param old
     * @return copy hashset<Integer>
     */
    public static HashSet<Integer> copyNeighbours(HashSet<Integer> old) {
        HashSet<Integer> ret = new HashSet<>(old);
        for (Integer num : old) {
            ret.add(num);
        }
        return ret;
    }
}