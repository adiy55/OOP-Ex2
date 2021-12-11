package GUI;

import Graph.Node;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class TimerUI extends AnimationTimer {
    private final DirectedWeightedGraphAlgorithms algo;
    private final Pane root;
    private final ScaleUI scaleUI;

    public TimerUI(DirectedWeightedGraphAlgorithms algo, Pane root) {
        this.algo = algo;
        this.root = root;
        scaleUI = new ScaleUI(algo);
    }

    @Override
    public void handle(long l) {
        addNodes();
    }

    private void addNodes() {
        Iterator<NodeData> node_iter = algo.getGraph().nodeIter();
        ArrayList<Text> labels = new ArrayList<>();
        while (node_iter.hasNext()) {
            Node n = (Node) node_iter.next();
            Point2D point = scaleUI.getAdjustedPoint(n);
            Circle circle = new Circle();
            circle.setCenterX(point.getX());
            circle.setCenterY(point.getY());
            circle.setRadius(8);
            Text text = new Text(Integer.toString(n.getKey()));
            text.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
            text.setLayoutX(circle.getCenterX());
            text.setLayoutY(circle.getCenterY());
            text.setFill(Color.RED);
            labels.add(text);
            start();
            this.root.getChildren().addAll(circle);
            addEdges(point, n.getKey());
        }
        this.root.getChildren().addAll(labels);
        stop();
    }

    private void addEdges(Point2D point, int key) {
        Iterator<EdgeData> edge_iter = algo.getGraph().edgeIter(key);
        while (edge_iter.hasNext()) {
            EdgeData e = edge_iter.next();
            boolean mark = false;
            Point2D dest_point = scaleUI.getAdjustedPoint(algo.getGraph().getNode(e.getDest()));
            Line l = new Line();
            l.setStartX(point.getX());
            l.setStartY(point.getY());
            l.setEndX(dest_point.getX());
            l.setEndY(dest_point.getY());
            l.setStrokeWidth(2);
            start();
            this.root.getChildren().add(l);
            addArrow(point, dest_point, mark);
        }
        stop();
    }

    private void addArrow(Point2D point, Point2D dest_point, boolean mark) {
        double dist = point.distance(dest_point);
        double x_left = dest_point.getX() + ((15 / dist) * (((point.getX() - dest_point.getX()) * Math.cos(50) + ((point.getY() - dest_point.getY()) * (Math.sin(50))))));
        double x_right = dest_point.getX() + ((15 / dist) * (((point.getX() - dest_point.getX()) * Math.cos(50) - ((point.getY() - dest_point.getY()) * (Math.sin(50))))));
        double y_left = dest_point.getY() + ((15 / dist) * (((point.getY() - dest_point.getY()) * Math.cos(50) - ((point.getX() - dest_point.getX()) * (Math.sin(50))))));
        double y_right = dest_point.getY() + ((15 / dist) * (((point.getY() - dest_point.getY()) * Math.cos(50) + ((point.getX() - dest_point.getX()) * (Math.sin(50))))));
        Line left = new Line();
        left.setStartX(dest_point.getX());
        left.setStartY(dest_point.getY());
        left.setEndX(x_left);
        left.setEndY(y_left);
        left.setStrokeWidth(2);
        Line right = new Line();
        right.setStartX(dest_point.getX());
        right.setStartY(dest_point.getY());
        right.setEndX(x_right);
        right.setEndY(y_right);
        right.setStrokeWidth(2);
        if(mark){
            left.setStroke(Color.GOLD);
            right.setStroke(Color.GOLD);
        }
        start();
        this.root.getChildren().addAll(left, right);
        stop();
    }
}
