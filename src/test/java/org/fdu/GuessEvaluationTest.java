package org.fdu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.util.Arrays;  // for parameterized test

import static org.fdu.GuessEvaluation.isGuessCorrect;
import static org.junit.jupiter.api.Assertions.*;

import static org.fdu.GuessEvaluation.Result.*;  // color enums
import static org.fdu.GuessEvaluation.evaluateGuess;

/**
 *
 */
class GuessEvaluationTest {

    /*
     * Handful of guesses: one that failed the early algorithm (BRICK & CRICK), no matching chars, 100 percent match
     *   and same character five times.
     *   See below for parameterized version of this test, much tighter and easier to follow
     */
    @Test
    void evaluateGuessTest() {
        String guess = "CRICK";      String secretWord = "BRICK";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        GuessEvaluation.Result[] expEvalResult = {GRAY, GREEN, GREEN, GREEN, GREEN};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));

        guess = "BRICK";             secretWord = "SHADE";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        expEvalResult = new GuessEvaluation.Result[] {GRAY, GRAY, GRAY, GRAY, GRAY};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));

        guess = "HELLO";            secretWord = "HELLO";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        expEvalResult = new GuessEvaluation.Result[] {GREEN, GREEN, GREEN, GREEN, GREEN};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));

        guess = "ABCBA";            secretWord = "AAAAA";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        expEvalResult = new GuessEvaluation.Result[] {GREEN, GRAY, GRAY, GRAY, GREEN};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));

        guess = "BABES";            secretWord = "ABBEY";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        expEvalResult = new GuessEvaluation.Result[] {YELLOW, YELLOW, GREEN, GREEN, GRAY};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));

        guess = "ROBOT";            secretWord = "TABOO";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        expEvalResult = new GuessEvaluation.Result[] {GRAY, YELLOW, GREEN, GREEN, YELLOW};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));

        guess = "ROVER";            secretWord = "VIGOR";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        expEvalResult = new GuessEvaluation.Result[] {GRAY, YELLOW, YELLOW, GRAY, GREEN};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));

        // Note - evaluation is left to right, so the first instance is yellow, second is gray
        guess = "CHEER";            secretWord = "CLOSE";
        System.out.printf("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess);
        expEvalResult = new GuessEvaluation.Result[] {GREEN, GRAY, YELLOW, GRAY, GRAY};
        assertArrayEquals(expEvalResult, evaluateGuess(guess, secretWord));
    }

    @ParameterizedTest
    @CsvSource({
            "CRICK, BRICK, GRAY:GREEN:GREEN:GREEN:GREEN",
            "BABES, ABBEY, YELLOW:YELLOW:GREEN:GREEN:GRAY",
            "ROBOT, TABOO, GRAY:YELLOW:GREEN:GREEN:YELLOW"
    })
    void parameterizedEvaluateTest(String guess, String secret, String expected) {
        // Map the string "GRAY:GREEN..." to a Result[] array
        GuessEvaluation.Result[] expectedArray = Arrays.stream(expected.split(":"))
                .map(GuessEvaluation.Result::valueOf)
                .toArray(GuessEvaluation.Result[]::new);

        assertArrayEquals(expectedArray, evaluateGuess(guess, secret));
    }

    @Test
    void isGuessCorrectTest() {
        System.out.println("Verify system can recognize correct guesses, invalid guesses return false");
        assertFalse(isGuessCorrect("hello", "byeby"));
        assertTrue(isGuessCorrect("total", "total"));
        assertTrue(isGuessCorrect("WHOLE", "WHOLE"));
        assertFalse(isGuessCorrect("", "WRONG"));
    }
}