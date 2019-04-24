package ru.hse.inclass;

class Game {
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

    GameResult getWinner() {
        for(int i = 0; i < N; i++) {
            if(!board[i][0].equals(State.EMPTY) && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return board[i][0].toGameResult();
            }
            if(!board[0][i].equals(State.EMPTY) && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return board[0][i].toGameResult();
            }
        }
        if(!board[0][0].equals(State.EMPTY) && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return board[0][0].toGameResult();
        }
        if(!board[0][2].equals(State.EMPTY) && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return board[0][2].toGameResult();
        }
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(board[i][j].equals(State.EMPTY)) {
                    return GameResult.IN_PROGRESS;
                }
            }
        }
        return GameResult.DRAW;
    }

    enum State {
        X,
        O,
        EMPTY;

        @Override
        public String toString() {
            switch (this) {
                case X:
                    return "X";
                case O:
                    return "O";
                case EMPTY:
                    return "";

            }
            return "WTF????";
        }

        GameResult toGameResult() {
            switch (this) {
                case X:
                    return GameResult.X_WIN;
                case O:
                    return GameResult.O_WIN;
            }
            return GameResult.IN_PROGRESS;
        }
    }

    enum GameResult {
        IN_PROGRESS,
        X_WIN,
        O_WIN,
        DRAW;

        @Override
        public String toString() {
            switch (this) {
                case X_WIN:
                    return "X won!";
                case O_WIN:
                    return "O won!";
                case DRAW:
                    return "Draw!";
            }
            return "WTF???";
        }
    }
}
