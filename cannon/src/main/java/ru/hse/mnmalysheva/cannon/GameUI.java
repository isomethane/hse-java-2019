package ru.hse.mnmalysheva.cannon;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/** This class represents Cannon game UI. **/
public class GameUI extends Application {
    private GraphicsContext graphicsContext;
    private double[] landscapeX;
    private double[] landscapeY;
    private Game game;
    private Cannon cannon;
    private Target target;
    private Set<Explosion> explosions = new HashSet<>();
    private boolean gameEnded = false;

    private long previousTime;

    {
        var landscapePoints = List.of(
                new Point2D(0, 100),
                new Point2D(150, 200),
                new Point2D(270, 120),
                new Point2D(500, 300),
                new Point2D(580, 110),
                new Point2D(620, 90),
                new Point2D(790, 350),
                new Point2D(1010, 140),
                new Point2D(1150, 220),
                new Point2D(1280, 130)
        );
        landscapeX = new double[landscapePoints.size() + 2];
        landscapeY = new double[landscapePoints.size() + 2];
        landscapeX[0] = Game.WIDTH;
        landscapeY[0] = Game.HEIGHT;
        landscapeX[1] = 0;
        landscapeY[1] = Game.HEIGHT;
        for (int i = 0; i < landscapePoints.size(); i++) {
            landscapeX[i + 2] = landscapePoints.get(i).getX();
            landscapeY[i + 2] = Game.HEIGHT - landscapePoints.get(i).getY();
        }

        game = new Game(new Landscape(landscapePoints));
        cannon = game.addCannon(150, Cannon.Direction.RIGHT);
        target = new Target(new Point2D(1150, 230), 10);
        game.setTarget(target);
    }

    /** Start program. **/
    public static void main(@NotNull String[] args) {
        Application.launch(args);
    }

    /** Initialize stage. **/
    @Override
    public void start(@NotNull Stage primaryStage) {
        var canvas = new Canvas(Game.WIDTH, Game.HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();
        var root = new Group(canvas);

        var buttons = new ListView<>(FXCollections.observableList(List.of("Small", "Medium", "Large")));
        root.getChildren().add(buttons);

        buttons.setOnMouseClicked(event -> {
            switch (buttons.getSelectionModel().getSelectedIndex()) {
                case 0:
                    cannon.setProjectileType(ProjectileType.SMALL);
                    break;
                case 1:
                    cannon.setProjectileType(ProjectileType.MEDIUM);
                    break;
                case 2:
                    cannon.setProjectileType(ProjectileType.LARGE);
                    break;
                default:
                    break;
            }
        });
        final int ROW_HEIGHT = 24;
        buttons.setPrefHeight(ROW_HEIGHT * 3 + 2);
        buttons.setPrefWidth(100);
        buttons.getSelectionModel().select(0);
        buttons.setFocusTraversable(false);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (previousTime == 0) {
                    previousTime = now;
                    return;
                }

                double deltaTime = (now - previousTime) * 1e-9;
                game.update(deltaTime);
                previousTime = now;

                var exploded = game.getExplodedProjectiles();
                for (var e : exploded) {
                    explosions.add(new Explosion(e.getLocation(), 10 + e.getExplosionRadius()));
                }
                exploded.clear();

                List<Explosion> toDelete = new ArrayList<>();
                for (var e : explosions) {
                    e.update(deltaTime);
                    if (e.isDead()) {
                        toDelete.add(e);
                    }
                }
                for (var e : toDelete) {
                    explosions.remove(e);
                }

                drawGame();

                if (!gameEnded && game.getGameState() == Game.GameState.WIN) {
                    gameEnded = true;
                    cannon.setMoving(false);
                    cannon.setRotateDirection(Cannon.RotateDirection.NO);
                    cannon.setFiring(false);

                    var alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("You win");
                    alert.setContentText("Congratulations!");
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(primaryStage);
                    alert.show();
                    alert.setOnHidden(e -> Platform.exit());
                }
            }
        };

        timer.start();

        var scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    cannon.setDirection(Cannon.Direction.LEFT);
                    cannon.setMoving(true);
                    break;
                case RIGHT:
                    cannon.setDirection(Cannon.Direction.RIGHT);
                    cannon.setMoving(true);
                    break;
                case UP:
                    cannon.setRotateDirection(Cannon.RotateDirection.UP);
                    break;
                case DOWN:
                    cannon.setRotateDirection(Cannon.RotateDirection.DOWN);
                    break;
                case ENTER:
                    cannon.setFiring(true);
                    break;
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT:
                case RIGHT:
                    cannon.setMoving(false);
                    break;
                case UP:
                case DOWN:
                    cannon.setRotateDirection(Cannon.RotateDirection.NO);
                    break;
                case ENTER:
                    cannon.setFiring(false);
                    break;
            }
        });

        primaryStage.setTitle("Cannon");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void drawGame() {
        drawSky();
        drawLandscape();
        drawCannon();
        drawTarget();
        drawProjectiles();
        drawExplosions();
    }

    private void drawSky() {
        graphicsContext.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
        var gradient = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.SLATEBLUE),
                new Stop(1, Color.color(0.9, 0.82, 1.0))
        );
        graphicsContext.setFill(gradient);
        graphicsContext.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
    }

    private void drawLandscape() {
        graphicsContext.setFill(Color.DARKSLATEBLUE);
        graphicsContext.fillPolygon(landscapeX, landscapeY, landscapeX.length);
    }

    private void drawCannon() {
        graphicsContext.setFill(Color.BLACK);
        var cannonLocation = cannon.getLocation();
        graphicsContext.fillOval(
                cannonLocation.getX() - Cannon.WHEEL_RADIUS,
                Game.HEIGHT - cannonLocation.getY() - Cannon.WHEEL_RADIUS,
                Cannon.WHEEL_RADIUS * 2,
                Cannon.WHEEL_RADIUS * 2
        );

        var barrelEnd = cannon.getBarrelEnd();
        graphicsContext.setLineWidth(5);
        graphicsContext.strokeLine(
                cannonLocation.getX(),
                Game.HEIGHT - cannonLocation.getY(),
                barrelEnd.getX(),
                Game.HEIGHT - barrelEnd.getY()
        );
    }

    private void drawProjectiles() {
        graphicsContext.setFill(Color.BLACK);
        for (var p : game.getProjectiles()) {
            var location = p.getLocation();
            graphicsContext.fillOval(
                    location.getX() - p.getRadius(),
                    Game.HEIGHT - location.getY() - p.getRadius(),
                    p.getRadius() * 2,
                    p.getRadius() * 2
            );
        }
    }

    private void drawExplosions() {
        for (var e : explosions) {
            e.draw();
        }
    }

    private void drawTarget() {
        var location = target.getLocation();
        var radius = target.getRadius();
        var gradient = new RadialGradient(
                0,
                0,
                location.getX() - 3,
                Game.HEIGHT - location.getY() - 3,
                radius * 0.7,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.SNOW),
                new Stop(1, Color.SLATEBLUE)
        );
        graphicsContext.setFill(gradient);
        graphicsContext.fillOval(
                location.getX() - radius,
                Game.HEIGHT - location.getY() - radius,
                radius * 2,
                radius * 2
        );
    }

    private class Explosion {
        private static final double LIFE_TIME = 0.8;
        private static final double FADE_AWAY_TIME = 0.4;
        private final Point2D location;
        private final double maximumRadius;
        private double time;

        private Explosion(Point2D location, double maximumRadius) {
            this.location = location;
            this.maximumRadius = maximumRadius;
        }

        private void update(double deltaTime) {
            time += deltaTime;
        }

        private boolean isDead() {
            return time > LIFE_TIME + FADE_AWAY_TIME;
        }

        private void draw() {
            double radius;
            double alpha;
            if (time < LIFE_TIME) {
                radius = maximumRadius * time / LIFE_TIME;
                alpha = 1;
            } else {
                radius = maximumRadius;
                alpha = 1 - (time - LIFE_TIME) / FADE_AWAY_TIME;
            }
            var gradient = new RadialGradient(
                    0,
                    0,
                    location.getX(),
                    Game.HEIGHT - location.getY(),
                    radius,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.color(1.0, 0.3, 0.0, alpha)),
                    new Stop(1, Color.TRANSPARENT)
            );
            graphicsContext.setFill(gradient);
            graphicsContext.fillOval(
                    location.getX() - radius,
                    Game.HEIGHT - location.getY() - radius,
                    radius * 2,
                    radius * 2
            );
        }
    }
}
