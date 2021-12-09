import api.DirectedWeightedGraphAlgorithms;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// todo: fix margins, update graph after adding a node
public class GraphUI extends Application {
    final static int width = 600;
    final static int height = 600;
    static DirectedWeightedGraphAlgorithms algo;
    static DirectedWeightedGraphAlgorithms original_algo;
    private Pane pane;
    private VBox vbox;

    @Override
    public void start(Stage stage) {
        initGUI(stage);
    }

    private void initGUI(Stage stage) {
        stage.setResizable(true);
        stage.setTitle("Directed Weighted Graph UI");
        pane = new Pane();
        MenuBar menu_bar = new MenuBar();
        vbox = new VBox();
//        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 10, 0, 10));

        Label algo_res = new Label();

        Menu menu_file = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((ActionEvent event) -> Platform.exit());
        MenuItem save = new MenuItem("Save");
        MenuItem load = new MenuItem("Load");
        menu_file.getItems().addAll(save, load, exit);

        Menu menu_edit = new Menu("Edit");
        Menu add = new Menu("Add");
        MenuItem add_node = new MenuItem("Add Node");
        EventHandler<ActionEvent> node_event = actionEvent -> EventsUI.getInputNode().show();
        add_node.setOnAction(node_event);
        MenuItem add_edge = new MenuItem("Add Edge");
        EventHandler<ActionEvent> edge_event = actionEvent -> EventsUI.getInputEdge().show();
        add_edge.setOnAction(edge_event);
        Menu delete = new Menu("Delete");
        MenuItem del_node = new MenuItem("Delete Node");
        EventHandler<ActionEvent> dnode_event = actionEvent -> EventsUI.deleteNode().show();
        del_node.setOnAction(dnode_event);
        MenuItem del_edge = new MenuItem("Delete Edge");
        EventHandler<ActionEvent> dedge_event = actionEvent -> EventsUI.deleteEdge().show();
        del_edge.setOnAction(dedge_event);
        delete.getItems().addAll(del_node, del_edge);
        add.getItems().addAll(add_node, add_edge);
        menu_edit.getItems().addAll(add, delete);

        Button b = new Button("Algorithm");
        String[] choices = {"isConnected", "shortestPathDist", "shortestPath", "center", "tsp"};
        ChoiceDialog<Object> dialog = new ChoiceDialog<>(new Separator(), choices);
        dialog.setTitle("Run Algorithm");
        EventHandler<ActionEvent> event = actionEvent -> {

            if (dialog.getSelectedItem().equals(choices[0])) {
                String ans = algo.isConnected() ? "The graph is strongly connected" : "The graph is not strongly connected";
                algo_res.setText(ans);
            } // todo: finish cases
            dialog.show();
        };
        b.setOnAction(event);

        AnimationTimer timerUI = new TimerUI(algo, pane);
        timerUI.start();

        menu_bar.getMenus().addAll(menu_file, menu_edit);
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(menu_bar, b, new Label("Algorithm Output:"), algo_res);
        vbox.getChildren().addAll(toolbar, pane);
        Scene scene = new Scene(vbox, height+30, width+60);
        stage.setScene(scene);
        stage.show();

    }


}
