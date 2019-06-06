package ru.hse.mnmalysheva.cannon;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** This class represents Cannon game logic. **/
public class Game {
    public static double WIDTH = 1280;
    public static double HEIGHT = 720;
    private final Landscape landscape;
    private final List<Cannon> cannons = new ArrayList<>();
    private final Set<Projectile> projectiles = new HashSet<>();
    private final List<Projectile> explodedProjectiles = new ArrayList<>();
    private Target target;
    private GameState gameState = GameState.IN_PROGRESS;

    public Game(Landscape landscape) {
        this.landscape = landscape;
    }

    /** Updates game state with respect to time since last update. **/
    public void update(double deltaTime) {
        for (var c : cannons) {
            c.update(deltaTime);
            var projectile = c.fire();
            if (projectile != null) {
                projectiles.add(projectile);
            }
        }

        var toDelete = new ArrayList<Projectile>();
        for (var p : projectiles) {
            p.update(deltaTime);

            var location = p.getLocation();
            if (location.getX() < 0 || location.getX() >= WIDTH) {
                toDelete.add(p);
            } else if (landscape.isUnderLandscape(location)) {
                toDelete.add(p);
                explodedProjectiles.add(p);
                if (location.distance(target.getLocation()) < p.getExplosionRadius() + target.getRadius()) {
                    gameState = GameState.WIN;
                }
            } else if (location.distance(target.getLocation()) < target.getRadius()) {
                toDelete.add(p);
                explodedProjectiles.add(p);
                gameState = GameState.WIN;
            }
        }
        for (var p : toDelete) {
            projectiles.remove(p);
        }
    }

    public Cannon addCannon(double locationX, @NotNull Cannon.Direction direction) {
        var cannon = new Cannon(landscape, locationX, direction);
        cannons.add(cannon);
        return cannon;
    }

    public @NotNull Set<Projectile> getProjectiles() {
        return projectiles;
    }

    public @NotNull List<Projectile> getExplodedProjectiles() {
        return explodedProjectiles;
    }

    public void setTarget(@NotNull Target target) {
        this.target = target;
    }

    public GameState getGameState() {
        return gameState;
    }

    public enum GameState {
        IN_PROGRESS, WIN
    }
}
