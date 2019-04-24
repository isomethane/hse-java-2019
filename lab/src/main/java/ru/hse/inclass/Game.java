package ru.hse.inclass;

public class Game {
    private State[][] board;
    private final int N = 3;
    private State currentPlayer = State.X;

    Game() {
        board = new State[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = State.EMPTY;
            }
        }
    }

    State getAt(int i, int j) {
        return board[i][j];
    }

    boolean setAt(int i, int j, State state) {
        if(!board[i][j].equals(State.EMPTY)) {
            return false;
        }
        board[i][j] = state;
        return true;
    }

    boolean setAt(int i, int j) {
        if(!setAt(i, j, currentPlayer)) {
            return false;
        }
        if(currentPlayer.equals(State.X)) {
            currentPlayer = State.O;
        } else {
            currentPlayer = State.X;
        }
        return true;
    }

    State getWinner() {
        for(int i = 0; i < N; i++) {
            if(!board[i][0].equals(State.EMPTY) && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return board[i][0];
            }
            if(!board[0][i].equals(State.EMPTY) && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return board[i][0];
            }
        }
        if(!board[0][0].equals(State.EMPTY) && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return board[0][0];
        }
        if(!board[0][2].equals(State.EMPTY) && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return board[0][2];
        }
        return State.EMPTY;
    }

    enum State {
        X,
        O,
        EMPTY
    }
}
