package org.fdu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.fdu.GameDTOs.*;

class StatelessWordleGameTest {
    static WordleGame wordleGame;
    @BeforeEach
    void setUp() {
        wordleGame = new WordleGame();
    }

    @Test
    void createNewGame() {
    }

    @Test
    void initialGameStatus() {
        GameStatus status = wordleGame.createNewGame();

        assertFalse(status.gameOver());
        assertFalse(status.userWon());
        assertEquals(0, status.numGuesses());
        assertEquals(6, status.maxGuesses());
    }
    @Test
        // verifies there is a valid game version (doesn't include "unknown")
    void getGameVersion() {
        GameStatus status = wordleGame.createNewGame("BIBLE");
        String gameVersion = status.gameVersion();
        System.out.println("Game version: " + gameVersion);
        assertFalse(gameVersion.toLowerCase().contains("unknown"));
    }
    @Test
    void testInvalidGuesses() {
        GameStatus status = wordleGame.createNewGame("BUGLE");
        GuessResult result;
        GuessResponse response;
        final int ZERO_GUESS_COUNTER = 0;

        System.out.println("Invalid Guesses (except for one) and verifying after 6 of them game still NOT over");
        // invalid guess - shouldn't increment turn counter
        response = wordleGame.processGuess(status,"BU LE");
        status = response.gameStatus();
        result = response.guessResult();
        assertNotEquals(GuessStatus.VALID, result.guessStatus(),
                "white space in middle, should be invalid");
        assertFalse(status.gameOver());
        assertEquals(ZERO_GUESS_COUNTER, status.numGuesses());

        // invalid guess - six letter word
        response = wordleGame.processGuess(status,"BUGLEs");
        status = response.gameStatus();
        result = response.guessResult();
        assertEquals(GuessStatus.INVALID, result.guessStatus(),
                "6 letter guess, should be invalid");
        assertFalse(status.gameOver());
        assertEquals(ZERO_GUESS_COUNTER, status.numGuesses());

        // valid guess - just to mix things up here
        response = wordleGame.processGuess(status,"abcde");
        status = response.gameStatus();
        result = response.guessResult();
        assertEquals(GuessStatus.VALID, result.guessStatus(),
                "lower case 5 letter guess, should be valid");
        assertFalse(status.gameOver());
        assertNotEquals(ZERO_GUESS_COUNTER, status.numGuesses());  // since we now have a valid guess

        // invalid guess
        response = wordleGame.processGuess(status,"he11o");
        status = response.gameStatus();
        result = response.guessResult();
        assertEquals(GuessStatus.INVALID, result.guessStatus(),
                "mixed alphas and numerics, should be invalid");
        assertFalse(status.gameOver());
        assertNotEquals(ZERO_GUESS_COUNTER, status.numGuesses());  // since we had a valid guess above
        System.out.println("he11o guess: " + status.toString());

        // invalid guess
        response = wordleGame.processGuess(status,"@bcde");
        status = response.gameStatus();
        result = response.guessResult();
        assertEquals(GuessStatus.INVALID, result.guessStatus(),
                "special char, should be invalid");
        assertFalse(status.gameOver(), "game shouldn't be over");
        assertFalse(status.userWon(), "user should not have won with above guess");

        // invalid guess - 6th total guess
        response = wordleGame.processGuess(status,"b!rdy");
        status = response.gameStatus();
        result = response.guessResult();
        assertEquals(GuessStatus.INVALID, result.guessStatus(),
                "special char, should be invalid");
        assertFalse(status.gameOver(), "6 guesses, mostly invalid, game shouldn't be over");
        assertFalse(status.userWon(), "user should not have won with above guesses");
    }

    @Test
    void validGuessesInclWinGame() {
        GameStatus status = wordleGame.createNewGame("BUGLE");
        GuessResult result;
        GuessResponse response;
        int guessCounter = 0;

        System.out.println("  also checking that userWon() and game over after secret word guessed correctly");

        // valid guess with white space, validate number of guesses increments as well
        response = wordleGame.processGuess(status,"  BGLEU  ");   guessCounter++;
        status = response.gameStatus();
        result = response.guessResult();
        assertEquals(GuessStatus.VALID, result.guessStatus(),
                "leading & trailing white space, should be valid ");
        assertFalse(status.gameOver());
        assertEquals(status.numGuesses(), guessCounter);

        // valid - mixed case and leading white space
        response = wordleGame.processGuess(status,"  bUGle");
        status = response.gameStatus();
        result = response.guessResult();
        assertEquals(GuessStatus.VALID, result.guessStatus(),
                "mixed case, corret word, should be valid");
        assertTrue(status.gameOver(), "mixed case secret word, game should be over");
        assertTrue(status.userWon(), "mixed case secret word, player should have won");
    }


    // helper function to submit a guess and return the game status (in scenarios where not checking the results)
    private GameStatus submit(GameStatus current, String guess) {
        return wordleGame.processGuess(current, guess).gameStatus();
    }

    @Test
        // make maximum number of allowed incorrect guesses, verify game is over
        //   make one more guess after game is over and verify game still over
    void isUserOutOfGuesses() {
        GameStatus status = wordleGame.createNewGame("LOCAL");

        System.out.println("Verify guesses are counted properly");
        assertFalse(status.isUserOutOfGuesses());  // initially not out of guesses
        for (int itr = 0; itr < status.maxGuesses(); itr++) {
            status = submit(status,"ABCDE");
        }
        // verify game is over and the user didn't win
        assertTrue(status.isUserOutOfGuesses());
        assertFalse(status.userWon());
        // now verify even though we give the correct word, we still didn't win
        status = submit(status,"LOCAL");
        assertTrue(status.isUserOutOfGuesses());   // should still be true
        assertFalse(status.userWon());
    }

    @Test
        //
    void winOnLastGuess() {
        GameStatus status = wordleGame.createNewGame("FECAL");
        for (int itr = 0; itr < (status.maxGuesses()-1); itr++) {
            status = submit(status,"ABCDE");
        }
        assertFalse(status.isUserOutOfGuesses(), "max - 1 guesses, user should have a guess left");  // user should be out of guesses, but also
        assertFalse(status.userWon(), "user should not have won");
        assertFalse(status.gameOver(), "game should not be over, user still has a guess");

        // last guess - correctly guess the secret word
        status = submit(status, "FECAL");
        assertTrue(status.isUserOutOfGuesses());  // user should be out of guesses, but also
        assertEquals(status.maxGuesses(), status.numGuesses(), "number of guesses!= max, should be");
        assertTrue(status.userWon(), "user should have won on last move, but didn't?");
        assertTrue(status.gameOver(), "user won, but, game isn't over");
    }

    @Test
    void testGuessCounter() {
        GameStatus status = wordleGame.createNewGame("TAKEN");
        int guessCounter = 0;
        // one valid test
        status = submit(status, "PINTO");  guessCounter++;
        // a few invalid tests - should not increment game counter
        status = submit(status,"BAD");
        status = submit(status,"BUN NY");
        status = submit(status,"@DAWN");
        assertEquals(guessCounter, status.numGuesses());
        // now a couple of valid tests - should increment guess counter
        status = submit(status, "HORSE");  guessCounter++;
        status = submit(status, "frank");  guessCounter++;
        assertEquals(guessCounter, status.numGuesses());
        // another couple of invalid tests
        status = submit(status,"");
        status = submit(status,"SUPERCALIFRAG");
        assertEquals(guessCounter, status.numGuesses());
        // and valid guesses until the max is hit, e.g. if max = 6, 3 more
        for (int guessNum = guessCounter+1; guessNum <= status.maxGuesses(); guessNum++ ) {
            assertFalse(status.isUserOutOfGuesses());  // do this before the guess
            status = submit(status, "lynda");  guessCounter++;
            assertEquals(guessCounter, status.numGuesses());
        }
        // user should be out of guesses now & game should be over
        assertFalse(status.userWon(), "user should not have won");
        assertTrue(status.gameOver(), "game should be over, but isn't");
        assertEquals(guessCounter, status.maxGuesses());

        // now game is over - additional guesses should not be accepted
        //   guessCounter should be at max, verify anyway
        //   guess the secret word, but nothing should change
        //     # guesses taken, user hasn't won & game is still over
        //   also verifying the status update instead of calling game directly
        status = submit(status,"TAKEN");
        assertEquals(guessCounter, status.numGuesses(), "game over, guess counter should not have incremented");
        assertFalse(status.userWon(), "game over before guess, user should not have won");
        assertTrue(status.gameOver(), "game is still over");
    }
}