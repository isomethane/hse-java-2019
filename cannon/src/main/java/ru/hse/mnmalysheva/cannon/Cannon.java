package ru.hse.mnmalysheva.cannon;

import javafx.geometry.Point2D;

public class Cannon {
    public static double WHEEL_RADIUS = 10;
    public static double BARREL_LENGTH = 30;
    private static double HALF_PI = Math.PI / 2;
    private static double SPEED = 100;
    private static double ROTATION_SPEED = 0.5;
    private static double COOLDOWN = 0.15;

    private final Landscape landscape;
    private Point2D location;
    private Direction direction;
    private boolean isMoving;

    private RotateDirection rotateDirection = RotateDirection.NO;
    private double angle = 0;

    private double timeSinceLastFire;
    private boolean isFiring;
    private ProjectileType projectileType = ProjectileType.SMALL;

    public Cannon(Landscape landscape, double locationX, Direction direction) {
        this.landscape = landscape;
        this.location = landscape.getPoint(locationX);
        this.direction = direction;
    }

    public void update(double deltaTime) {
        if (isMoving) {
            location = landscape.move(
                    location.getX(),
                    deltaTime * SPEED * direction.toDouble()
            );
        }
        angle += deltaTime * ROTATION_SPEED * rotateDirection.toDouble();
        angle = angle < -HALF_PI ? -HALF_PI : angle > HALF_PI ? HALF_PI : angle;

        timeSinceLastFire += deltaTime;
    }

    public Projectile fire() {
        if (isFiring && timeSinceLastFire > COOLDOWN) {
            timeSinceLastFire = 0;
            return new Projectile(
                    getBarrelEnd(),
                    getAngle(),
                    projectileType.getSpeed(),
                    projectileType.getRadius(),
                    projectileType.getExplosionRadius()
            );
        }
        return null;
    }

    public Point2D getLocation() {
        return new Point2D(location.getX(), location.getY() + WHEEL_RADIUS);
    }

    public Point2D getBarrelEnd() {
        var location = getLocation();
        var angle = getAngle();
        return new Point2D(
                location.getX() + BARREL_LENGTH * Math.cos(angle),
                location.getY() + BARREL_LENGTH * Math.sin(angle)
        );
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getAngle() {
        return direction == Direction.RIGHT ? angle : Math.PI - angle;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setRotateDirection(RotateDirection rotateDirection) {
        this.rotateDirection = rotateDirection;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public enum Direction {
        LEFT(-1),
        RIGHT(1);

        private double value;

        Direction(double value) {
            this.value = value;
        }

        public double toDouble() {
            return value;
        }
    }

    public enum RotateDirection {
        DOWN(-1),
        NO(0),
        UP(1);

        private double value;

        RotateDirection(double value) {
            this.value = value;
        }

        public double toDouble() {
            return value;
        }
    }
}
