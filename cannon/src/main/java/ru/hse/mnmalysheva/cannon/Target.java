package ru.hse.mnmalysheva.cannon;


import javafx.geometry.Point2D;

public class Target {
    private final Point2D location;
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
