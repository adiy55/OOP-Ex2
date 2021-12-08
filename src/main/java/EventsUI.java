import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


// animation timer
// todo: action to insert new path
//          action to show algorithms (i.e. mark shortest path)
public class EventsUI {
//    public static DirectedWeightedGraphAlgorithms algo;

//    public static ArrayList<Line> initGraphUI() {
//        DirectedWeightedGraph graph = algo.getGraph();
//        ArrayList<Line> lines = new ArrayList<>();
//        Iterator<EdgeData> edge_iter = graph.edgeIter();
//        while (edge_iter.hasNext()) {
//            EdgeData e = edge_iter.next();
//            NodeData src = graph.getNode(e.getSrc());
//            NodeData dest = graph.getNode(e.getDest());
//            lines.add(new Line(src.getLocation().x() * 10000000, src.getLocation().y() * 10000000, dest.getLocation().x(), dest.getLocation().y()));
//        }
//        return lines;
//    }

    public static Stage getInputNode() {

        Stage stage = new Stage();
        stage.setTitle("Add Node");

        Label id = new Label("Insert ID:");
        Label loc_x = new Label("Insert X coordinate:");
        Label loc_y = new Label("Insert Y coordinate:");
        Label loc_z = new Label("Insert Z coordinate:");
        Label l = new Label();

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        TextField text4 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    text2.getText();
                    text3.getText();
                    text4.getText();
                    GeoLoc gl = new GeoLoc(Double.parseDouble(text2.getText()), Double.parseDouble(text3.getText()), Double.parseDouble(text4.getText()));
                    Node n = new Node(Integer.parseInt(text1.getText()), gl);
                    GraphUI.algo.getGraph().addNode(n);
                    l.setText(String.format("Node %d was added", n.getKey()));

                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(id, text1, loc_x, text2, loc_y, text3, loc_z, text4, button, l);

        Scene scene = new Scene(layout, 300, 280);
        stage.setScene(scene);

        return stage;
    }

    public static Stage getInputEdge() {

        Stage stage = new Stage();
        stage.setTitle("Add Edge");

        Label src = new Label("Insert source node:");
        Label dest = new Label("Insert destination node:");
        Label w = new Label("Insert edge weight:");
        Label l = new Label();

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    text2.getText();
                    text3.getText();
                    GraphUI.algo.getGraph().connect(Integer.parseInt(text1.getText()), Integer.parseInt(text2.getText()), Double.parseDouble(text3.getText()));
                    l.setText(String.format("Edge (%s,%s) was added", text1.getText(), text2.getText()));
                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(src, text1, dest, text2, w, text3, button, l);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);

        return stage;
    }

    public static Stage deleteNode() {

        Stage stage = new Stage();
        stage.setTitle("Remove Node");

        Label id = new Label("Insert Node ID:");
        Label l = new Label();

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    GraphUI.algo.getGraph().removeNode(Integer.parseInt(text1.getText()));
                    l.setText(String.format("Node %s was removed", text1.getText()));
                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(id, text1, button, l);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);

        return stage;
    }

    public static Stage deleteEdge() {

        Stage stage = new Stage();
        stage.setTitle("Remove Edge");

        Label src = new Label("Insert Edge src:");
        Label dest = new Label("Insert Edge dest:");

        Label l = new Label();

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    text2.getText();
                    GraphUI.algo.getGraph().removeEdge(Integer.parseInt(text1.getText()), Integer.parseInt(text2.getText()));
                    l.setText(String.format("Edge (%s,%s) was removed", text1.getText(), text2.getText()));
                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(src, text1, dest, text2, button, l);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);

        return stage;
    }
}
