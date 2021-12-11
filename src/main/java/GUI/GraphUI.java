package GUI;

import Graph.DWGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

public class GraphUI extends Application {
    public static int width = 1000;
    public static int height = 500;
    public static DirectedWeightedGraphAlgorithms algo;
    public static String algo_file;
    public static FileChooser chooser = initFileChooser();

    // todo: split into smaller functions, add edge weights, fix scaling when adding a node
    @Override
    public void start(Stage stage) {
        initGUI(stage);
    }

    private void initGUI(Stage stage) {
        stage.setMaximized(true);
        stage.setTitle("Directed Weighted Graph UI");

        Pane pane = new Pane();
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 10, 0, 10));
        MenuBar menu_bar = new MenuBar();
        ToolBar toolbar = new ToolBar();
        Label algo_res = new Label();
        algo_res.setWrapText(true);

        Menu menu_file = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(windowEvent -> {
            Alert close_event = EventsUI.confirmCloseEvent();
            Optional<ButtonType> response = close_event.showAndWait();
            if (response.isPresent() && ButtonType.OK.equals(response.get())) {
                Platform.exit();
            }
        });

        MenuItem save_as = new MenuItem("Save As");
        save_as.setOnAction(actionEvent -> {
            try {
                File selected_dir = chooser.showSaveDialog(stage);
                if (algo.save(selected_dir.getAbsolutePath())) {
                    chooser.setInitialDirectory(selected_dir.getParentFile());
                    stop();
                    start(stage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                algo.load(algo_file);
            }
        });

        MenuItem load = new MenuItem("Load");
        load.setOnAction(actionEvent -> {
            try {
                File selected_dir = chooser.showOpenDialog(stage);
                if (algo.load(selected_dir.getAbsolutePath())) {
                    chooser.setInitialDirectory(selected_dir.getParentFile());
                    algo_file = selected_dir.getAbsolutePath();
                    stop();
                    start(stage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                algo.load(algo_file);
            }
        });
        MenuItem reset = new MenuItem("Reset");
        reset.setOnAction(actionEvent -> {
            algo.load(algo_file);
            try {
                stop();
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction(actionEvent -> {
            algo.init(new DWGraph(new HashMap<>(), new HashMap<>()));
            try {
                stop();
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        menu_file.getItems().addAll(reset, clear, save_as, load, exit);
        Menu menu_edit = new Menu("Edit");
        Menu add = new Menu("Add");
        MenuItem add_node = new MenuItem("Add Node");
        EventHandler<ActionEvent> node_event = actionEvent -> {
            EventsUI.getInputNode().showAndWait();
            try {
                stop();
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        add_node.setOnAction(node_event);

        MenuItem add_edge = new MenuItem("Add Edge");
        EventHandler<ActionEvent> edge_event = actionEvent -> {
            EventsUI.getInputEdge().showAndWait();
            try {
                stop();
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        add_edge.setOnAction(edge_event);

        Menu delete = new Menu("Delete");
        MenuItem del_node = new MenuItem("Delete Node");
        EventHandler<ActionEvent> dnode_event = actionEvent -> {
            EventsUI.deleteNode().showAndWait();
            try {
                stop();
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        del_node.setOnAction(dnode_event);

        MenuItem del_edge = new MenuItem("Delete Edge");
        EventHandler<ActionEvent> del_edge_event = actionEvent -> {
            EventsUI.deleteEdge().showAndWait();
            try {
                stop();
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        del_edge.setOnAction(del_edge_event);
        delete.getItems().addAll(del_node, del_edge);
        add.getItems().addAll(add_node, add_edge);
        menu_edit.getItems().addAll(add, delete);

        Button b = new Button("Algorithm");
        String[] choices = {"isConnected", "shortestPathDist", "shortestPath", "center", "tsp"};
        ChoiceDialog<Object> dialog = new ChoiceDialog<>(new Separator(), choices);
        dialog.setTitle("Run Algorithm");
        dialog.setHeaderText("Select an algorithm");
        b.setOnAction(event -> {
            dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(dialog.selectedItemProperty().isNull());
            dialog.showAndWait().ifPresent(choice -> {
                if (dialog.getSelectedItem().equals(choices[0])) {
                    String ans = algo.isConnected() ? "The graph is strongly connected" : "The graph is not strongly connected";
                    System.out.println(ans);
                    algo_res.setText(ans);
                } else if (dialog.getSelectedItem().equals(choices[1])) {
                    EventsUI.shortestPathDist(algo_res).showAndWait();
                } else if (dialog.getSelectedItem().equals(choices[2])) {
                    EventsUI.shortestPath(algo_res).showAndWait();
                } else if (dialog.getSelectedItem().equals(choices[3])) {
                    NodeData ans = algo.center();
                    algo_res.setText(String.format("The center is node %d", ans.getKey()));
                } else if (dialog.getSelectedItem().equals(choices[4])) {
                    EventsUI.tsp(algo_res).showAndWait();
                }
            });
            dialog.setSelectedItem(new Separator());
        });

        AnimationTimer timerUI = new TimerUI(algo, pane);
        timerUI.start();

        menu_bar.getMenus().addAll(menu_file, menu_edit);
        Label alg_out = new Label("Algorithm Output:");
        alg_out.setUnderline(true);
        alg_out.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        alg_out.setWrapText(true);
        toolbar.getItems().addAll(menu_bar, b, alg_out, algo_res);
        vbox.getChildren().addAll(toolbar, pane);
        Scene scene = new Scene(vbox, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    private static FileChooser initFileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("File Explorer");
        File default_dir = new File("c:/");
        chooser.setInitialDirectory(default_dir);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        return chooser;
    }

}
