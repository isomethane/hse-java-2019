package ru.hse.mnmalysheva.cannon;

/** This class represents some types of {@link Projectile} characteristics. **/
public enum ProjectileType {
    SMALL(600, 3, 3),
    MEDIUM(400, 6,  20),
    LARGE(200, 10, 40);

    /** Speed in pixels/second. **/
    private final double speed;
    /** Projectile radius in pixels **/
    private final double radius;
    /** Explosion radius in pixels **/
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
