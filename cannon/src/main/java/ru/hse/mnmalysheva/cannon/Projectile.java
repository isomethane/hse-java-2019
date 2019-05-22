package ru.hse.mnmalysheva.cannon;

import javafx.geometry.Point2D;

public class Projectile {
    private static final double GRAVITY_ACCELERATION = 350;
    private final double initialX;
    private final double initialY;
    private final double initialSpeedX;
    private final double initialSpeedY;
    private final double radius;
    private final double explosionRadius;

    private Point2D location;
    private double time;

    public Projectile(Point2D location, double angle, double speed, double radius, double explosionRadius) {
        initialX = location.getX();
        initialY = location.getY();
        initialSpeedX = speed * Math.cos(angle);
        initialSpeedY = speed * Math.sin(angle);
        this.radius = radius;
        this.explosionRadius = explosionRadius;
    }

    public void update(double deltaTime) {
        time += deltaTime;
        double x = initialX + initialSpeedX * time;
        double y = initialY + initialSpeedY * time - GRAVITY_ACCELERATION * time * time / 2.0;
        location = new Point2D(x, y);
    }

    public Point2D getLocation() {
        return location;
    }

    public double getRadius() {
        return radius;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }
}
