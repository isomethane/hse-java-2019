package ru.hse.mnmalysheva.cannon;

import javafx.geometry.Point2D;

class Explosion {
    private static final double LIFE_TIME = 1;
    private static final double FADE_AWAY_TIME = 0.5;

    final Point2D location;

    private final double maximumRadius;
    private double time;

    Explosion(Point2D location, double maximumRadius) {
        this.location = location;
        this.maximumRadius = maximumRadius;
    }

    void update(double deltaTime) {
        time += deltaTime;
    }

    boolean isDead() {
        return time > LIFE_TIME + FADE_AWAY_TIME;
    }

    double getCurrentRadius() {
        if (time >= LIFE_TIME) {
            return maximumRadius;
        }
        return maximumRadius * time / LIFE_TIME;
    }

    double getCurrentOpacity() {
        if (time <= LIFE_TIME) {
            return 1;
        }
        return 1 - (time - LIFE_TIME) / FADE_AWAY_TIME;
    }
}