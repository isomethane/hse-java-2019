package ru.hse.mnmalysheva.cannon;

public enum ProjectileType {
    SMALL(600, 3, 3),
    MEDIUM(400, 6,  20),
    LARGE(200, 10, 40);

    private final double speed;
    private final double radius;
    private final double explosionRadius;

    ProjectileType(double speed, double radius, double explosionRadius) {
        this.speed = speed;
        this.radius = radius;
        this.explosionRadius = explosionRadius;
    }

    public double getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }
}
