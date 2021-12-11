import GUI.GraphUI;
import Graph.DWGraphAlgo;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import javafx.application.Application;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraph ans = (new DWGraphAlgo(json_file)).getGraph();
        return ans;
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = new DWGraphAlgo(json_file);
        return ans;
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        GraphUI.algo = alg;
        GUI.GraphUI.algo_file = json_file;
        Application.launch(GraphUI.class, json_file);
    }

    public static void main(String[] args) {
        String file_path = args[0];
        runGUI(file_path);
    }
}