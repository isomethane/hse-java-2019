package ru.hse.mnmalysheva.cannon;

import javafx.geometry.Point2D;

/** This class represents simple target in Cannon game. **/
public class Target {
    /** Target location in pixels **/
    private final Point2D location;
    /** Target radius in pixels **/
    private final double radius;

    public Target(Point2D location, double radius) {
        this.location = location;
        this.radius = radius;
    }

    public Point2D getLocation() {
        return location;
    }

    public double getRadius() {
        return radius;
    }
}
