package GUI;

import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import javafx.geometry.Point2D;

import java.util.Iterator;

public class ScaleUI {
    private DirectedWeightedGraphAlgorithms algo;
    private double minX, minY, maxX, maxY;

    public ScaleUI(DirectedWeightedGraphAlgorithms algo) {
        this.algo = algo;
        minX = minY = Double.MAX_VALUE;
        maxX = maxY = Double.MIN_VALUE;
        calcRange();
    }

    private void calcRange() {
        if (algo.getGraph().nodeSize() == 1) {
            return;
        }
        Iterator<NodeData> node_iter = algo.getGraph().nodeIter();
        while (node_iter.hasNext()) {
            NodeData n = node_iter.next();
            double curr_x = n.getLocation().x();
            double curr_y = n.getLocation().y();
            if (curr_x > maxX) maxX = curr_x;
            if (curr_x < minX) minX = curr_x;
            if (curr_y > maxY) maxY = curr_y;
            if (curr_y < minY) minY = curr_y;
        }
    }

    public Point2D getAdjustedPoint(NodeData node) {
        double x_range = maxX - minX;
        double y_range = maxY - minY;
        double x_dist = node.getLocation().x() - minX;
        double y_dist = node.getLocation().y() - minY;
        double x_adjusted = (x_dist / x_range) * GraphUI.width;
        double y_adjusted = (y_dist / y_range) * GraphUI.height;
        return new Point2D(x_adjusted, y_adjusted);
    }

}
