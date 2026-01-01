package org.fdu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll.*;

class WordleGameTest {

    @Test
    void initialGameStatus() {
        WordleGame game = new WordleGame("APPLE");
        GameStatus status = game.getGameStatus();

        assertFalse(status.getGameOver());
        assertFalse(status.getUserWon());
        assertEquals(0, status.getNumGuesses());
        assertEquals(game.getMaxUserGuesses(), status.getMaxGuesses());
    }

    @Test
    // verifies there is a valid game version (doesn't include "unknown")
    void getGameVersion() {
        WordleGame game = new WordleGame("BIBLE");
        GameStatus status = game.getGameStatus();
        String gameVersion = status.getGameVersion();
        System.out.println("Game version: " + gameVersion);
        assertFalse(gameVersion.toLowerCase().contains("unknown"));
    }

    @Test
    void testInvalidGuesses() {
        WordleGame game = new WordleGame("BUGLE");
        GuessResult result;
        GameStatus status;
        final int ZERO_GUESS_COUNTER = 0;

        System.out.println("Invalid Guesses (except for one) and verifying after 6 of them game still NOT over");
        // invalid guess - shouldn't increment turn counter
        result = game.processGuess("BU LE");
        status = game.getGameStatus();
        assertNotEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "white space in middle, should be invalid");
        assertFalse(status.getGameOver());
        assertEquals(ZERO_GUESS_COUNTER, status.getNumGuesses());

        // invalid guess - six letter word
        result = game.processGuess("BUGLEs");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.INVALID, result.getGuessStatus(),
                "6 letter guess, should be invalid");
        assertFalse(status.getGameOver());
        assertEquals(ZERO_GUESS_COUNTER, status.getNumGuesses());

        // valid guess - just to mix things up here
        result = game.processGuess("abcde");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "lower case 5 letter guess, should be valid");
        assertFalse(status.getGameOver());
        assertNotEquals(ZERO_GUESS_COUNTER, status.getNumGuesses());  // since we now have a valid guess

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
        assertFalse(status.getGameOver(), "game shouldn't be over");
        assertFalse(status.getUserWon(), "user should not have won with above guess");

        // invalid guess - 6th total guess
        result = game.processGuess(" b!rdy");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.INVALID, result.getGuessStatus(),
                "special char, should be invalid");
        assertFalse(status.getGameOver(), "6 guesses, mostly invalid, game shouldn't be over");
        assertFalse(status.getUserWon(), "user should not have won with above guesses");
    }

    @Test
    void validGuessesInclWinGame() {
        WordleGame game = new WordleGame("BUGLE");
        GuessResult result;
        GameStatus status;
        int guessCounter = 0;

        System.out.println("  also checking that userWon() and game over after secret word guessed correctly");

        // valid guess with white space, validate number of guesses increments as well
        result = game.processGuess("  BGLEU  ");  guessCounter++;
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "leading & trailing white space, should be valid ");
        assertFalse(status.getGameOver());
        assertEquals(status.getNumGuesses(), guessCounter);

        // valid - mixed case and leading white space
        result = game.processGuess("  bUGle");
        status = game.getGameStatus();
        assertEquals(GuessResult.GuessStatus.VALID, result.getGuessStatus(),
                "mixed case, corret word, should be valid");
        assertTrue(status.getGameOver(), "mixed case secret word, game should be over");
        assertTrue(status.getUserWon(), "mixed case secret word, player should have won");
    }

    @Test
    // make maximum number of allowed incorrect guesses, verify game is over
    //   make one more guess after game is over and verify game still over
    void isUserOutOfGuesses() {
        WordleGame game = new WordleGame("LOCAL");
        System.out.println("Verify guesses are counted properly");
        assertFalse(game.isUserOutOfGuesses());  // initially not out of guesses
        for (int itr = 0; itr < game.getMaxUserGuesses(); itr++) {
            game.processGuess("ABCDE");
        }
        assertTrue(game.isUserOutOfGuesses());
        game.processGuess("LOCAL");
        assertTrue(game.isUserOutOfGuesses());   // should still be true
    }

    @Test
    //
    void winOnLastGuess() {
        WordleGame game = new WordleGame("FECAL");
        for (int itr = 0; itr < (game.getMaxUserGuesses()-1); itr++) {
            game.processGuess("ABCDE");
        }
        assertFalse(game.isUserOutOfGuesses(), "max - 1 guesses, user should have a guess left");  // user should be out of guesses, but also
        GameStatus status = game.getGameStatus();
        assertFalse(status.getUserWon(), "user should not have won");
        assertFalse(status.getGameOver(), "game should not be over, user still has a guess");

        // last guess - correctly guess the secret word
        game.processGuess("FECAL");
        assertTrue(game.isUserOutOfGuesses());  // user should be out of guesses, but also
        status = game.getGameStatus();
        assertEquals(status.getMaxGuesses(), game.getNumGuessesTaken(), "number of guesses!= max, should be");
        assertTrue(status.getUserWon(), "user should have won on last move, but didn't?");
        assertTrue(status.getGameOver(), "user won, but, game isn't over");
    }

    @Test
    void testGuessCounter() {
        WordleGame game = new WordleGame("TAKEN");
        int guessCounter = 0;
        // a few invalid tests - should not increment game counter
        game.processGuess("BAD");
        game.processGuess("BUN NY");
        game.processGuess("@DAWN");
        assertEquals(guessCounter, game.getNumGuessesTaken());
        // now a few valid tests - should increment guess counter
        game.processGuess("HORSE");  guessCounter++;
        game.processGuess("PINTO");  guessCounter++;
        game.processGuess("frank");  guessCounter++;
        assertEquals(guessCounter, game.getNumGuessesTaken());
        // another couple of invalid tests
        game.processGuess("");
        game.processGuess("SUPERCALIFRAG");
        assertEquals(guessCounter, game.getNumGuessesTaken());
        // and valid guesses until the max is hit, e.g. if max = 6, 3 more
        for (int guessNum = guessCounter+1; guessNum <= game.getMaxUserGuesses(); guessNum++ ) {
            assertFalse(game.isUserOutOfGuesses());  // do this before the guess
            game.processGuess("lynda");  guessCounter++;
            assertEquals(guessCounter, game.getNumGuessesTaken());
        }
        // user should be out of guesses now & game should be over
        GameStatus status = game.getGameStatus();
        assertFalse(status.getUserWon(), "user should not have won");
        assertTrue(status.getGameOver(), "game should be over, but isn't");
        assertEquals(guessCounter, status.getMaxGuesses());

        // now game is over - additional guesses should not be accepted
        //   guessCounter should be at max, verify anyway
        //   guess the secret word, but nothing should change
        //     # guesses taken, user hasn't won & game is still over
        //   also verifying the status update instead of calling game directly
        game.processGuess("TAKEN");
        status = game.getGameStatus();
        assertEquals(guessCounter, status.getNumGuesses(), "game over, guess counter should not have incremented");
        assertFalse(status.getUserWon(), "game over before guess, user should not have won");
        assertTrue(status.getGameOver(), "game is still over");
    }
}