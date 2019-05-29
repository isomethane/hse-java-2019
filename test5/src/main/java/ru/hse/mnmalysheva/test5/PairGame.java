package ru.hse.mnmalysheva.test5;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/** This class represents pair game logic. **/
public class PairGame {
    private final int size;
    private int pairsLeft;
    private final Cell[][] board;

    private BiConsumer<CellCoordinate, CellCoordinate> onCheckedTwoCellsListener = (a, b) -> {};
    private BiConsumer<CellCoordinate, CellCoordinate> onMatchingListener = (a, b) -> {};
    private BiConsumer<CellCoordinate, CellCoordinate> onNonMatchingListener = (a, b) -> {};
    private BiConsumer<CellCoordinate, CellCoordinate> onReleaseListener = (a, b) -> {};
    private Runnable onGameEndedListener = () -> {};

    private CellCoordinate firstChecked;
    private CellCoordinate secondChecked;

    /**
     * Constructs new game with specified numbers.
     * @param size board size
     * @param numbers numbers to fill board.
     * @throws IllegalArgumentException if size is not positive even number
     * @throws IllegalArgumentException if numbers list size does not match number of cells.
     */
    public PairGame(int size, @Nullable List<Integer> numbers) {
        if (size <= 0 || size % 2 != 0) {
            throw new IllegalArgumentException("size must be positive even number");
        }
        this.size = size;
        pairsLeft = size * size / 2;
        board = new Cell[size][size];

        fillBoard(numbers);
    }

    /**
     * Constructs new game.
     * @param size board size
     * @throws IllegalArgumentException if size is not positive even number
     */
    public PairGame(int size) {
        this(size, null);
    }

    /**
     * Performs click on board.
     * @param coordinate board cell coordinate
     * @return {@code true} if cell was successfully checked
     * @throws IllegalArgumentException if cell is outside board
     */
    public boolean checkCell(@NotNull CellCoordinate coordinate) {
        var cell = getCell(coordinate);
        int numberOfChecked = numberOfCheckedCells();
        if (numberOfChecked == 2 || cell.state != State.UNCHECKED) {
            return false;
        }
        cell.state = State.CHECKED;
        if (numberOfChecked == 0) {
            firstChecked = coordinate;
        } else {
            secondChecked = coordinate;
            onCheckedTwoCells();
        }
        return true;
    }

    /**
     * Performs click on board.
     * @param x board cell x coordinate
     * @param y board cell y coordinate
     * @return {@code true} if cell was successfully checked
     * @throws IllegalArgumentException if cell is outside board
     */
    public boolean checkCell(int x, int y) {
        return checkCell(new CellCoordinate(x, y));
    }

    /**
     * Gets cell value.
     * @param coordinate board cell coordinate
     * @return cell value
     * @throws IllegalArgumentException if cell is outside board
     * @throws IllegalStateException if cell is not visible
     */
    public int get(@NotNull CellCoordinate coordinate) {
        var cell = getCell(coordinate);
        if (cell.state == State.INACTIVE || cell.state == State.CHECKED && secondChecked != null) {
            return cell.number;
        }
        throw new IllegalStateException("cell is inaccessible");
    }

    /**
     * Gets cell value.
     * @param x board cell x coordinate
     * @param y board cell y coordinate
     * @return cell value
     * @throws IllegalArgumentException if cell is outside board
     * @throws IllegalStateException if cell is not visible
     */
    public int get(int x, int y) {
        return get(new CellCoordinate(x, y));
    }

    /**
     * Releases two checked cells.
     * @throws IllegalStateException if number of checked cells is less than two.
     */
    public void releaseChecked() {
        var first = getCell(firstChecked);
        var second = getCell(secondChecked);
        first.state = State.UNCHECKED;
        second.state = State.UNCHECKED;
        onReleaseListener.accept(firstChecked, secondChecked);
        clearChecked();
    }

    public void setOnCheckedTwoCellsListener(@NotNull BiConsumer<CellCoordinate, CellCoordinate> listener) {
        onCheckedTwoCellsListener = listener;
    }

    public void setOnMatchingListener(@NotNull BiConsumer<CellCoordinate, CellCoordinate> listener) {
        onMatchingListener = listener;
    }

    public void setOnNonMatchingListener(@NotNull BiConsumer<CellCoordinate, CellCoordinate> listener) {
        onNonMatchingListener = listener;
    }

    public void setOnReleaseListener(@NotNull BiConsumer<CellCoordinate, CellCoordinate> listener) {
        onReleaseListener = listener;
    }

    public void setOnGameEndedListener(@NotNull Runnable listener) {
        onGameEndedListener = listener;
    }

    private void fillBoard(@Nullable List<Integer> numbers) {
        if (numbers == null) {
            numbers = new ArrayList<>();
            for (int i = 0; i * 2 < size * size; i++) {
                numbers.add(i);
                numbers.add(i);
            }
            Collections.shuffle(numbers);
        } else if (numbers.size() != size * size) {
            throw new IllegalArgumentException("incorrect size of numbers list");
        }
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                board[y][x] = new Cell(numbers.get(y * size + x));
            }
        }
    }

    private int numberOfCheckedCells() {
        return firstChecked == null ? 0 : secondChecked == null ? 1 : 2;
    }

    private void checkCoordinateInsideBoard(@NotNull CellCoordinate coordinate) {
        if (coordinate.x < 0 || coordinate.y < 0 || coordinate.x >= size || coordinate.y >= size) {
            throw new IllegalArgumentException("coordinate outside board");
        }
    }

    private Cell getCell(@NotNull CellCoordinate coordinate) {
        checkCoordinateInsideBoard(coordinate);
        return board[coordinate.y][coordinate.x];
    }

    private void clearChecked() {
        firstChecked = null;
        secondChecked = null;
    }

    private void onCheckedTwoCells() {
        onCheckedTwoCellsListener.accept(firstChecked, secondChecked);

        var first = getCell(firstChecked);
        var second = getCell(secondChecked);

        if (first.number == second.number) {
            first.state = second.state = State.INACTIVE;
            pairsLeft--;
            onMatchingListener.accept(firstChecked, secondChecked);
            clearChecked();
            if (pairsLeft == 0) {
                onGameEndedListener.run();
            }
        } else {
            onNonMatchingListener.accept(firstChecked, secondChecked);
        }
    }

    /** This class represents board cell coordinate. **/
    public static class CellCoordinate {
        public final int x;
        public final int y;

        public CellCoordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Cell {
        private final int number;
        private State state = State.UNCHECKED;

        private Cell(int number) {
            this.number = number;
        }
    }

    private enum State {
        UNCHECKED, CHECKED, INACTIVE
    }
}
