package ru.hse.mnmalysheva.test5;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

/** This class represents Pair game GUI. **/
public class PairGUI extends Application {
    private static final int RELEASE_CHECKED_DELAY = 1000;
    private int gameSize;
    private PairGame game;
    private ToggleButton[][] buttons;

    /** Start program. **/
    public static void main(@NotNull String[] args) {
        Application.launch(args);
    }

    /** Initialize stage. **/
    @Override
    public void start(@NotNull Stage primaryStage) {
        var parameters = getParameters().getRaw();
        if (parameters.size() != 1) {
            System.out.println("Incorrect number of arguments. The only argument should be board size.");
            Platform.runLater(Platform::exit);
            return;
        }
        try {
            gameSize = Integer.parseInt(parameters.get(0));
        } catch (NumberFormatException e) {
            System.out.println(
                    "Incorrect argument." +
                    "The only argument should be valid number representing board size."
            );
            Platform.runLater(Platform::exit);
            return;
        }
        try {
            game = new PairGame(gameSize);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            Platform.runLater(Platform::exit);
            return;
        }
        primaryStage.setScene(initScene());
        primaryStage.setTitle("Pair");
        primaryStage.setHeight(gameSize * 100);
        primaryStage.setWidth(gameSize * 100);
        primaryStage.setMinHeight(gameSize * 50);
        primaryStage.setMinWidth(gameSize * 50);
        primaryStage.show();
    }

    private Scene initScene() {
        buttons = new ToggleButton[gameSize][gameSize];

        var gridPane = new GridPane();

        var columnConstraints = new ColumnConstraints();
        var rowConstraints = new RowConstraints();
        columnConstraints.setPercentWidth(100.0 / gameSize);
        rowConstraints.setPercentHeight(100.0 / gameSize);
        for (int i = 0; i < gameSize; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < gameSize; i++) {
            for (int j = 0; j < gameSize; j++) {
                var y = i;
                var x = j;

                var button = new ToggleButton();
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setFocusTraversable(false);
                button.setOnAction(event -> {
                    boolean clicked = game.checkCell(x, y);
                    if (!clicked) {
                        button.setSelected(!button.isSelected());
                    }
                });

                gridPane.add(button, y, x);
                buttons[y][x] = button;
            }
        }
        game.setOnCheckedTwoCellsListener((first, second) -> {
            var firstButton = getButton(first);
            var secondButton = getButton(second);
            firstButton.setText(Integer.toString(game.get(first)));
            secondButton.setText(Integer.toString(game.get(second)));
        });
        game.setOnMatchingListener((first, second) -> {
            var firstButton = getButton(first);
            var secondButton = getButton(second);
            firstButton.setDisable(true);
            secondButton.setDisable(true);
        });
        game.setOnNonMatchingListener((first, second) -> {
            var delay = new PauseTransition(new Duration(RELEASE_CHECKED_DELAY));
            delay.setOnFinished(event -> game.releaseChecked());
            delay.play();
        });
        game.setOnReleaseListener((first, second) -> {
            var firstButton = getButton(first);
            var secondButton = getButton(second);
            firstButton.setText("");
            secondButton.setText("");
            firstButton.setSelected(false);
            secondButton.setSelected(false);
        });
        game.setOnGameEndedListener(() -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You win");
            alert.setContentText("Congratulations!");
            alert.show();
            alert.setOnHidden(e -> Platform.exit());
        });

        return new Scene(gridPane);
    }

    private ToggleButton getButton(PairGame.CellCoordinate coordinate) {
        return buttons[coordinate.y][coordinate.x];
    }
}
