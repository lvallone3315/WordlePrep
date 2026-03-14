package org.fdu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

// parameterized testing imports
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import org.fdu.GameDTOs.*;
import static org.fdu.GuessEvaluation.Result.*;



// Test DTO capture of a game's history
//   e.g. initially history should be empty, after a guess - history has the raw guess string and evaluation
class WordleGameHistoryTest {

    // mirror Spring Boot - create the game bean only once, reinitialize game in each test case
    static final WordleGame game = new WordleGame();

    @DisplayName("Verify initial game history (array list) is empty" )
    // does not cover some game version exception cases - e.g. missing property version.properties file
    @Test
    void newGameHistoryTest() {
        GameStatus gameStatus = game.createNewGame();
        assertNotNull(gameStatus);
        assertTrue(gameStatus.guesses().isEmpty());
    }

    @DisplayName("Make a guess, verify history matches guess and evaluation" )
    @Test
    void firstGuessHistoryTest() {
        GameStatus gameStatus = game.createNewGame("TIPPY");
        String guess = "fRanK";
        GuessEvaluation.Result[] expEvalResult = {GRAY, GRAY, GRAY, GRAY, GRAY};
        GuessResponse response = game.processGuess(gameStatus, guess);
        assertArrayEquals(expEvalResult, response.guessResult().evaluation());  // direct query of evaluation

        GuessRow row = response.gameStatus().guesses().getFirst();  // returns first row in an array list
        assertEquals(guess, row.guess());
        assertArrayEquals(expEvalResult, row.evaluation());

    }

    @DisplayName("Invalid guesses shouldn't be captured in game history" )
    @Test
    void invalidGuessHistoryTest() {
        GameStatus gameStatus = game.createNewGame("WORLD");
        String guess = "";
        GuessResponse response = game.processGuess(gameStatus, guess);
        assertTrue(response.gameStatus().guesses().isEmpty());  // empty strings should be invalid, no history captured
    }

    @DisplayName("Play a complete, losing game, checking each history row" )
    @Test
    void fullGameHistoryTest() {
        GameStatus gameStatus = game.createNewGame("CORNY");
        GuessEvaluation.Result[] expEvalResult = {GRAY, YELLOW, YELLOW, GREEN, GRAY};
        String guess = "WRONG";
        GuessResponse response;
        GameStatus newGameStatus = gameStatus;
        //  loop through a game, verifying each history row is updated correctly
        System.out.println("*** Game DTO for a full game ***");
        for (int i =0; i < gameStatus.maxGuesses(); i++) {
            response = game.processGuess(newGameStatus, guess);
            newGameStatus = response.gameStatus();

            System.out.println(newGameStatus);  // noisy, but for now show the game DTO for a full game
            GuessRow row = response.gameStatus().guesses().get(i);
            assertEquals(guess, row.guess());
            assertArrayEquals(expEvalResult, row.evaluation());
        }
        // verify history not updated for a max+1 guess
        response = game.processGuess(newGameStatus, "EXTRA");
        GameStatus extraGameStatus = response.gameStatus();
        assertEquals(gameStatus.maxGuesses(), extraGameStatus.guesses().size());
        System.out.println(extraGameStatus);
    }

    // let's try a parameterized test, courtesy of chatgpt
    @ParameterizedTest(name = "secret={0}, guesses={1}")
    @MethodSource("historyProvider")
    void guessHistorySequenceTest(String secret, String[] guesses) {

        GameStatus status = game.createNewGame(secret);

        for (int i = 0; i < guesses.length; i++) {
            GuessResponse response = game.processGuess(status, guesses[i]);
            status = response.gameStatus();

            // verify history size and order
            List<GuessRow> history = status.guesses();
            assertEquals(i + 1, history.size(), "History size should match number of guesses");

            // verify ordering
            GuessRow row =history.get(i);
            assertEquals(guesses[i], row.guess());
        }
    }
    static Stream<Arguments> historyProvider() {
        return Stream.of(
                arguments("CORNY", new String[]{"WRONG"}),
                arguments("CORNY", new String[]{"WRONG", "FRANK"}),
                arguments("APPLE", new String[]{"ALERT", "ANGLE", "AMPLE"}),
                arguments("MANGO", new String[]{}) // empty sequence test
        );
    }
}