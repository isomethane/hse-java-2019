package ru.hse.mnmalysheva.cannon;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
    private static final String SMALL_PROJECTILE_BUTTON = "Small";
    private static final String MEDIUM_PROJECTILE_BUTTON = "Medium";
    private static final String LARGE_PROJECTILE_BUTTON = "Large";
    private static final double NANOSECONDS_IN_SECOND = 1_000_000_000;
    private static final List<Point2D> LANDSCAPE_POINTS = List.of(
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
        game = new Game(new Landscape(LANDSCAPE_POINTS));
        cannon = game.addCannon(150, Cannon.Direction.RIGHT);
        target = new Target(new Point2D(1150, 230), 10);
        game.setTarget(target);

        initLandscapePolygonByPoints();
    }

    /** Start program. **/
    public static void main(@NotNull String[] args) {
        Application.launch(args);
    }

    /** Initialize stage. **/
    @Override
    public void start(@NotNull Stage primaryStage) {
        var canvas = new Canvas(Game.WIDTH, Game.HEIGHT);
        var root = new Group(canvas);
        var scene = new Scene(root);

        graphicsContext = canvas.getGraphicsContext2D();

        initButtons(root);
        initKeyHandlers(scene);
        initAnimationTimer(primaryStage);

        primaryStage.setTitle("Cannon");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initLandscapePolygonByPoints() {
        landscapeX = new double[LANDSCAPE_POINTS.size() + 2];
        landscapeY = new double[LANDSCAPE_POINTS.size() + 2];
        landscapeX[0] = Game.WIDTH;
        landscapeY[0] = Game.HEIGHT;
        landscapeX[1] = 0;
        landscapeY[1] = Game.HEIGHT;
        for (int i = 0; i < LANDSCAPE_POINTS.size(); i++) {
            landscapeX[i + 2] = LANDSCAPE_POINTS.get(i).getX();
            landscapeY[i + 2] = Game.HEIGHT - LANDSCAPE_POINTS.get(i).getY();
        }
    }

    private void initButtons(Group root) {
        var buttons = new ListView<>(FXCollections.observableArrayList(
                SMALL_PROJECTILE_BUTTON,
                MEDIUM_PROJECTILE_BUTTON,
                LARGE_PROJECTILE_BUTTON
        ));

        final int ROW_HEIGHT = 24;
        buttons.setPrefHeight(ROW_HEIGHT * 3 + 2);
        buttons.setPrefWidth(100);
        buttons.getSelectionModel().select(0);
        buttons.setFocusTraversable(false);

        buttons.setOnMouseClicked(event -> {
            switch (buttons.getSelectionModel().getSelectedItem()) {
                case SMALL_PROJECTILE_BUTTON:
                    cannon.setProjectileType(ProjectileType.SMALL);
                    break;
                case MEDIUM_PROJECTILE_BUTTON:
                    cannon.setProjectileType(ProjectileType.MEDIUM);
                    break;
                case LARGE_PROJECTILE_BUTTON:
                    cannon.setProjectileType(ProjectileType.LARGE);
                    break;
                default:
                    break;
            }
        });

        root.getChildren().add(buttons);
    }

    private void initKeyHandlers(Scene scene) {
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
    }

    private void initAnimationTimer(Stage primaryStage) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (previousTime == 0) {
                    previousTime = now;
                    return;
                }
                double deltaTime = (now - previousTime) / NANOSECONDS_IN_SECOND;
                previousTime = now;

                game.update(deltaTime);
                registerExplosions();
                updateExplosions(deltaTime);

                drawGame();

                if (!gameEnded && game.getGameState() == Game.GameState.WIN) {
                    endGame(primaryStage);
                }
            }
        };

        timer.start();
    }

    private void registerExplosions() {
        var exploded = game.getExplodedProjectiles();
        for (var e : exploded) {
            explosions.add(new Explosion(e.getLocation(), 10 + e.getExplosionRadius()));
        }
        exploded.clear();
    }

    private void updateExplosions(double deltaTime) {
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
    }

    private void endGame(Stage primaryStage) {
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

    private void drawExplosions() {
        for (var e : explosions) {
            drawExplosion(e);
        }
    }

    private void drawExplosion(Explosion explosion) {
        double x = explosion.location.getX();
        double y = explosion.location.getY();
        double radius = explosion.getCurrentRadius();
        double alpha = explosion.getCurrentOpacity();

        var gradient = new RadialGradient(
                0,
                0,
                x,
                Game.HEIGHT - y,
                radius,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.color(1.0, 0.3, 0.0, alpha)),
                new Stop(1, Color.TRANSPARENT)
        );
        graphicsContext.setFill(gradient);
        graphicsContext.fillOval(
                x - radius,
                Game.HEIGHT - y - radius,
                radius * 2,
                radius * 2
        );
    }
}
