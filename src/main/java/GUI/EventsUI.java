package GUI;

import Graph.GeoLoc;
import Graph.Node;
import api.NodeData;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EventsUI {

    public static Stage getInputNode() {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Add Node");

        Label id = new Label("Insert ID:");
        id.setWrapText(true);
        Label loc_x = new Label("Insert X coordinate:");
        loc_x.setWrapText(true);
        Label loc_y = new Label("Insert Y coordinate:");
        loc_y.setWrapText(true);
        Label loc_z = new Label("Insert Z coordinate:");
        loc_z.setWrapText(true);
        Label l = new Label();
        l.setWrapText(true);

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
                    int inp1 = Integer.parseInt(text1.getText());
                    double inp2 = Double.parseDouble(text2.getText());
                    double inp3 = Double.parseDouble(text2.getText());
                    double inp4 = Double.parseDouble(text2.getText());
                    GeoLoc gl = new GeoLoc(inp2, inp3, inp4);
                    Node n = new Node(inp1, gl);
                    GraphUI.algo.getGraph().addNode(n);
                    l.setText(String.format("Node %d was added", n.getKey()));
                    text1.setText("");
                    text2.setText("");
                    text3.setText("");
                    text4.setText("");
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
        stage.setResizable(false);
        stage.setTitle("Add Edge");

        Label src = new Label("Insert source node:");
        src.setWrapText(true);
        Label dest = new Label("Insert destination node:");
        dest.setWrapText(true);
        Label w = new Label("Insert edge weight:");
        w.setWrapText(true);
        Label l = new Label();
        l.setWrapText(true);

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    text2.getText();
                    text3.getText();
                    int inp1 = Integer.parseInt(text1.getText());
                    int inp2 = Integer.parseInt(text2.getText());
                    double inp3 = Double.parseDouble(text3.getText());
                    if (GraphUI.algo.getGraph().getNode(inp1) != null && GraphUI.algo.getGraph().getNode(inp2) != null) {
                        GraphUI.algo.getGraph().connect(inp1, inp2, inp3);

                    }
                    l.setText(String.format("Edge (%d,%d) was added", inp1, inp2));
                    text1.setText("");
                    text2.setText("");
                    text3.setText("");
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
        stage.setResizable(false);
        stage.setTitle("Remove Node");

        Label id = new Label("Insert Node ID:");
        id.setWrapText(true);
        Label l = new Label();
        l.setWrapText(true);

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    int inp1 = Integer.parseInt(text1.getText());
                    if (GraphUI.algo.getGraph().getNode(inp1) != null) {
                        GraphUI.algo.getGraph().removeNode(inp1);
                        l.setText(String.format("Node %d was removed", inp1));
                    } else {
                        l.setText("The node entered does not exist!");
                    }
                    text1.setText("");
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
        stage.setResizable(false);
        stage.setTitle("Remove Edge");

        Label src = new Label("Insert Edge source:");
        src.setWrapText(true);
        Label dest = new Label("Insert Edge destination:");
        dest.setWrapText(true);

        Label l = new Label();
        l.setWrapText(true);

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    text2.getText();
                    int inp1 = Integer.parseInt(text1.getText());
                    int inp2 = Integer.parseInt(text2.getText());
                    if (GraphUI.algo.getGraph().getEdge(inp1, inp2) != null) {
                        GraphUI.algo.getGraph().removeEdge(inp1, inp2);
                        l.setText(String.format("Edge (%d,%d) was removed", inp1, inp2));
                    } else {
                        l.setText("The edge entered does not exist!");
                    }
                    text1.setText("");
                    text2.setText("");
                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(src, text1, dest, text2, button, l);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);

        return stage;
    }

    public static Stage shortestPathDist(Label algo_res) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Shortest Path Distance");

        Label header = new Label("Computes the length of the shortest path between the source to destination nodes.");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        header.setWrapText(true);

        Label src = new Label("Insert source node:");
        src.setWrapText(true);
        Label dest = new Label("Insert destination node:");
        dest.setWrapText(true);

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    text2.getText();
                    int inp1 = Integer.parseInt(text1.getText());
                    int inp2 = Integer.parseInt(text2.getText());
                    if (GraphUI.algo.getGraph().getNode(inp1) != null && GraphUI.algo.getGraph().getNode(inp2) != null) {
                        double ans = GraphUI.algo.shortestPathDist(inp1, inp2);
                        algo_res.setText(String.format("The shortest path distance is %f", ans));
                    } else {
                        algo_res.setText("No path was found!");
                    }
                    text1.setText("");
                    text2.setText("");
                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(header, src, text1, dest, text2, button);

        Scene scene = new Scene(layout, 250, 200);
        stage.setScene(scene);

        return stage;
    }

    public static Stage shortestPath(Label algo_res) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Shortest Path");

        Label header = new Label("Computes the shortest path between the source to destination nodes.");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        header.setWrapText(true);

        Label src = new Label("Insert source node:");
        src.setWrapText(true);
        Label dest = new Label("Insert destination node:");
        dest.setWrapText(true);

        Button button = new Button("Submit");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        button.setOnAction((ActionEvent event) ->
                {
                    text1.getText();
                    text2.getText();
                    int inp1 = Integer.parseInt(text1.getText());
                    int inp2 = Integer.parseInt(text2.getText());
                    if (GraphUI.algo.getGraph().getNode(inp1) != null && GraphUI.algo.getGraph().getNode(inp2) != null) {
                        List<NodeData> ans = GraphUI.algo.shortestPath(inp1, inp2);
                        algo_res.setText(String.format("The shortest path is %s", stringPath(ans)));
                    } else {
                        algo_res.setText("No path was found!");
                    }
                    text1.setText("");
                    text2.setText("");
                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(header, src, text1, dest, text2, button);

        Scene scene = new Scene(layout, 250, 200);
        stage.setScene(scene);

        return stage;
    }

    public static Stage tsp(Label algo_res) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Travelling Salesman Problem");
        Label header = new Label("Computes a list of consecutive nodes which go over all the nodes in cities.");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        header.setWrapText(true);
        Label msg = new Label("Insert node to add to cities:");
        msg.setWrapText(true);
        Label l = new Label();
        l.setWrapText(true);
        List<NodeData> lst = new ArrayList<>();
        TextField text = new TextField();
        Button button1 = new Button("Add");

        button1.setOnAction(keyEvent -> {
            text.getText();
            int inp = Integer.parseInt(text.getText());
            if (GraphUI.algo.getGraph().getNode(inp) != null) {
                lst.add(GraphUI.algo.getGraph().getNode(inp));
                l.setText(String.format("Node %d was added to cities", inp));
            } else {
                l.setText("The node entered does not exist!");
            }
            text.setText("");
        });

        Button button = new Button("Submit");
        button.setOnAction((ActionEvent event) ->
                {
                    List<NodeData> ans = GraphUI.algo.tsp(lst);
                    algo_res.setText(String.format("The TSP path is %s", stringPath(ans)));
                }
        );

        VBox layout = new VBox(5);
        layout.getChildren().addAll(header, msg, text, button1, l, button);

        Scene scene = new Scene(layout, 250, 200);
        stage.setScene(scene);

        return stage;
    }

    private static StringBuilder stringPath(List<NodeData> ans) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ans.size(); i++) {
            if (i == 0) sb.append(ans.get(i).getKey());
            else sb.append(String.format(" -> %d", ans.get(i).getKey()));
        }
        return sb;
    }

    public static Alert confirmCloseEvent() {
        Alert close_confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Button b_close = (Button) close_confirmation.getDialogPane().lookupButton(ButtonType.OK);
        b_close.setText("Yes");
        close_confirmation.setHeaderText("Exit Program");
        close_confirmation.initModality(Modality.APPLICATION_MODAL);
        return close_confirmation;
    }
}
