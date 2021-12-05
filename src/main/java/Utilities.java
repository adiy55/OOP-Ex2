import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Map;

public class Utilities {
    public static HashMap<Integer, NodeData> copyNodes(HashMap<Integer, NodeData> original) {
        HashMap<Integer, NodeData> copy = new HashMap<>();
        for (Map.Entry<Integer, NodeData> entry : original.entrySet()) {
            copy.put(entry.getKey(), new Node((Node) entry.getValue()));
        }
        return copy;
    }

    public static HashMap<Integer, HashMap<Integer, EdgeData>> copyEdges
            (HashMap<Integer, HashMap<Integer, EdgeData>> original) {
        HashMap<Integer, HashMap<Integer, EdgeData>> copy = new HashMap<>();
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : original.entrySet()) {
            HashMap<Integer, EdgeData> subCopy = copyInsideEdges(entry.getValue());
            copy.put(entry.getKey(), subCopy);
        }
        return copy;
    }

    private static HashMap<Integer, EdgeData> copyInsideEdges(HashMap<Integer, EdgeData> original) {
        HashMap<Integer, EdgeData> copy = new HashMap<>();
        for (Map.Entry<Integer, EdgeData> entry : original.entrySet()) {
            copy.put(entry.getKey(), new Edge((Edge) entry.getValue()));
        }
        return copy;
    }
}