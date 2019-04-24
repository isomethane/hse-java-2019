package ru.hse.inclass;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void getAt() {
        var game = new Game();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                assertEquals(Game.State.EMPTY, game.getAt(0, 0));
            }
        }
        game.setAt(0, 0, Game.State.X);
        game.setAt(1, 1, Game.State.X);
        game.setAt(2, 2, Game.State.O);
        assertEquals(Game.State.X, game.getAt(0, 0));
        assertEquals(Game.State.X, game.getAt(1, 1));
        assertEquals(Game.State.O, game.getAt(2, 2));
    }

    @Test
    void setAt() {
        var game = new Game();
        assertTrue(game.setAt(0, 0));
        assertFalse(game.setAt(0, 0));
        assertTrue(game.setAt(1, 1));
        assertTrue(game.setAt(2, 2));
        assertTrue(game.setAt(2, 0));
        assertFalse(game.setAt(2, 2));
        assertEquals(Game.State.X, game.getAt(0, 0));
        assertEquals(Game.State.O, game.getAt(1, 1));
        assertEquals(Game.State.X, game.getAt(2, 2));
        assertEquals(Game.State.O, game.getAt(2, 0));
    }

    @Test
    void getWinner() {
        var game1 = new Game();
        game1.setAt(1, 1);
        assertEquals(Game.State.EMPTY, game1.getWinner());
        game1.setAt(0, 1);
        assertEquals(Game.State.EMPTY, game1.getWinner());
        game1.setAt(0, 0);
        assertEquals(Game.State.EMPTY, game1.getWinner());
        game1.setAt(0 , 2);
        assertEquals(Game.State.EMPTY, game1.getWinner());
        game1.setAt(2, 2);
        assertEquals(Game.State.X, game1.getWinner());

        var game2 = new Game();
        game2.setAt(1, 2);
        assertEquals(Game.State.EMPTY, game2.getWinner());
        game2.setAt(1, 1);
        assertEquals(Game.State.EMPTY, game2.getWinner());
        game2.setAt(0, 1);
        assertEquals(Game.State.EMPTY, game2.getWinner());
        game2.setAt(0, 0);
        assertEquals(Game.State.EMPTY, game2.getWinner());
        game2.setAt(0 , 2);
        assertEquals(Game.State.EMPTY, game2.getWinner());
        game2.setAt(2, 2);
        assertEquals(Game.State.O, game2.getWinner());
    }
}