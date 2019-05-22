package ru.hse.mnmalysheva.cannon;

import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/** This class represents landscape in Cannon game. **/
public class Landscape {
    private static double EPSILON = 1e-7;
    private double[] xPoints;
    private double[] yPoints;
    private double minimumX;
    private double maximumX;

    /**
     * Constructs a new landscape.
     * @param shape points representing landscape.
     */
    public Landscape(@NotNull List<Point2D> shape) {
        xPoints = new double[shape.size()];
        yPoints = new double[shape.size()];
        for (int i = 0; i < shape.size(); i++) {
            var point = shape.get(i);
            xPoints[i] = point.getX();
            yPoints[i] = point.getY();
        }
        minimumX = xPoints[0];
        maximumX = xPoints[xPoints.length - 1];
    }

    /** Returns point on the landscape with specified x coordinate. **/
    public @NotNull Point2D getPoint(double x) {
        return new Point2D(x, getY(x));
    }

    /**
     * Returns result of delta shift from specified start x coordinate.
     * @param x start coordinate in pixels.
     * @param delta shift in pixels.
     * @return result location.
     */
    public @NotNull Point2D move(double x, double delta) {
        if (x + delta < minimumX) {
            return getPoint(minimumX + EPSILON);
        }
        if (x + delta > maximumX) {
            return getPoint(maximumX - EPSILON);
        }
        return getPoint(x + delta);
    }

    /** Checks if specified point lies under landscape. **/
    public boolean isUnderLandscape(@NotNull Point2D point) {
        return getY(point.getX()) > point.getY();
    }

    private double getY(double x) {
        int segmentIndex = getSegmentIndex(x);
        double x1 = xPoints[segmentIndex];
        double x2 = xPoints[segmentIndex + 1];
        double y1 = yPoints[segmentIndex];
        double y2 = yPoints[segmentIndex + 1];
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }

    private int getSegmentIndex(double x) {
        if (x < minimumX || x >= maximumX) {
            throw new IllegalArgumentException("point is out of landscape");
        }
        int index = Arrays.binarySearch(xPoints, x);
        return index >= 0 ? index : -index - 2;
    }
}
