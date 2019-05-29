package ru.hse.mnmalysheva.test5;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PairGameTest {
    private PairGame game;
    private StringBuilder gameLogger;

    @BeforeEach
    void init() {
        gameLogger = new StringBuilder();
        game = new PairGame(
                4,
                List.of(
                        0, 1, 1, 0,
                        2, 3, 4, 5,
                        5, 4, 3, 2,
                        6, 6, 7, 7
                )
        );
        setListeners();
    }

    private void setListeners() {
        game.setOnCheckedTwoCellsListener((first, second) -> {
            gameLogger.append("checked two cells: ");
            printTwoCells(first, second);
            assertDoesNotThrow(() -> game.get(first));
            assertDoesNotThrow(() -> game.get(second));
        });
        game.setOnMatchingListener((first, second) -> {
            gameLogger.append("matching: ");
            printTwoCells(first, second);
        });
        game.setOnNonMatchingListener((first, second) -> {
            gameLogger.append("non matching: ");
            printTwoCells(first, second);
        });
        game.setOnReleaseListener((first, second) -> gameLogger.append("released\n"));
        game.setOnGameEndedListener(() -> gameLogger.append("game ended\n"));
    }

    private void printCell(@NotNull PairGame.CellCoordinate coordinate) {
        gameLogger.append("(");
        gameLogger.append(coordinate.x);
        gameLogger.append(", ");
        gameLogger.append(coordinate.y);
        gameLogger.append(")");
        gameLogger.append(" = ");
        gameLogger.append(game.get(coordinate));
    }

    private void printTwoCells(@NotNull PairGame.CellCoordinate first, @NotNull PairGame.CellCoordinate second) {
        printCell(first);
        gameLogger.append(", ");
        printCell(second);
        gameLogger.append("\n");
    }

    @Test
    void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new PairGame(-2));
        assertThrows(IllegalArgumentException.class, () -> new PairGame(-1));
        assertThrows(IllegalArgumentException.class, () -> new PairGame(0));
        assertThrows(IllegalArgumentException.class, () -> new PairGame(1));
        assertThrows(IllegalArgumentException.class, () -> new PairGame(3));

        assertDoesNotThrow(() -> new PairGame(2));
        assertDoesNotThrow(() -> new PairGame(4));
        assertDoesNotThrow(() -> new PairGame(6));
        assertDoesNotThrow(() -> new PairGame(30));

        assertDoesNotThrow(() -> new PairGame(2, List.of(0, 1, 1, 0)));
        assertThrows(
                IllegalArgumentException.class,
                () -> new PairGame(4, List.of(0, 1, 1, 0))
        );
    }

    @Test
    void testClick() {
        assertThrows(
                IllegalArgumentException.class,
                () -> game.checkCell(-1, 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> game.checkCell(0, 4)
        );

        assertTrue(game.checkCell(0, 0));
        assertFalse(game.checkCell(0, 0));
        assertTrue(game.checkCell(1, 0));
        assertFalse(game.checkCell(2, 0));
        game.releaseChecked();

        assertTrue(game.checkCell(2, 0));
        assertTrue(game.checkCell(1, 0));

        assertEquals("checked two cells: (0, 0) = 0, (1, 0) = 1\n" +
                "non matching: (0, 0) = 0, (1, 0) = 1\n" +
                "released\n" +
                "checked two cells: (2, 0) = 1, (1, 0) = 1\n" +
                "matching: (2, 0) = 1, (1, 0) = 1\n",
                gameLogger.toString()
        );
    }

    @Test
    void testGet() {
        assertThrows(IllegalArgumentException.class, () -> game.get(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> game.get(0, 4));
        assertThrows(IllegalStateException.class, () -> game.get(0, 0));
        game.checkCell(2, 0);
        assertThrows(IllegalStateException.class, () -> game.get(2, 0));
        game.checkCell(3, 0);
        assertEquals(1, game.get(2, 0));
        assertEquals(0, game.get(3, 0));
        assertThrows(IllegalStateException.class, () -> game.get(1, 0));
    }

    @Test
    void testGameEnded() {
        game = new PairGame(2, List.of(1, 0, 0, 1));
        setListeners();

        game.checkCell(1, 1);
        game.checkCell(1, 0);
        game.releaseChecked();

        game.checkCell(0, 0);
        game.checkCell(1, 1);

        game.checkCell(0, 1);
        game.checkCell(1, 0);

        assertEquals("checked two cells: (1, 1) = 1, (1, 0) = 0\n" +
                        "non matching: (1, 1) = 1, (1, 0) = 0\n" +
                        "released\n" +
                        "checked two cells: (0, 0) = 1, (1, 1) = 1\n" +
                        "matching: (0, 0) = 1, (1, 1) = 1\n" +
                        "checked two cells: (0, 1) = 0, (1, 0) = 0\n" +
                        "matching: (0, 1) = 0, (1, 0) = 0\n" +
                        "game ended\n",
                gameLogger.toString()
        );
    }
}