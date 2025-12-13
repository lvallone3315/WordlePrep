package org.fdu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    @Test
    void processInvalidGuesses() {
        WordleGame game = new WordleGame("BUGLE");
        GuessResult result;
        GameStatus status;

        System.out.println("Invalid Guesses (mostly), and verifying after 6 of them game still NOT over");
        System.out.println("  also checking that userWon() and game over after secret word guessed correctly");
        // invalid guess - shouldn't increment turn counter (but can't tell here)
        result = game.processGuess("BU LE");
        status = game.getGameStatus();
        assertNotEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "white space in middle, should be invalid");
        assertFalse(status.getGameOver());

        // invalid guess - six letter word
        result = game.processGuess("BUGLEs");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.INVALID, result.getGuessStatus(),
                "6 letter guess, should be invalid");
        assertFalse(status.getGameOver());

        // valid guess -
        result = game.processGuess("abcde");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "lower case 5 letter guess, should be valid");
        assertFalse(status.getGameOver());

        // valid guess with white space
        result = game.processGuess("  BGLEU  ");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "leading & trailing white space, should be valid ");
        assertFalse(status.getGameOver());

        // invalid guess
        result = game.processGuess("he11o");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.INVALID, result.getGuessStatus(),
                "mixed alphas and numerics, should be invalid");
        assertFalse(status.getGameOver());

        // invalid guess
        result = game.processGuess("@bcde");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.INVALID, result.getGuessStatus(),
                "special char, should be invalid");
        assertFalse(status.getGameOver(), "6 guesses, but mostly invalid, game shouldn't be over");
        assertFalse(status.getUserWon(), "user should not have won with above guesses");

        // valid - mixed case
        result = game.processGuess("  bUGle");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "mixed case, corret word, should be valid");
        assertTrue(status.getGameOver(), "mixed case secret word, game should be over");
        assertTrue(status.getUserWon(), "mixed case secret word, player should have won");

    }
}