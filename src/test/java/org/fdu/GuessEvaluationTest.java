package org.fdu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.fdu.GuessEvaluation.Result.*;  // color enums

/**
 *
 */
class GuessEvaluationTest {

    static GuessEvaluation guessEval;

    @BeforeEach
    void setUp() {
        guessEval = new GuessEvaluation();
    }

    /**
     * Handful of guesses: one that failed the early algorithm (BRICK & CRICK), no matching chars, 100 percent match
     *   and same character five times.
     *   ToDo: array approach with a lot more combinations
     */
    @Test
    void evaluateGuess() {
        String guess = "CRICK";      String secretWord = "BRICK";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        GuessEvaluation.Result[] expEvalResult = {GRAY, GREEN, GREEN, GREEN, GREEN};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        guess = "BRICK";             secretWord = "SHADE";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {GRAY, GRAY, GRAY, GRAY, GRAY};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        guess = "HELLO";            secretWord = "HELLO";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {GREEN, GREEN, GREEN, GREEN, GREEN};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        guess = "ABCBA";            secretWord = "AAAAA";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {GREEN, GRAY, GRAY, GRAY, GREEN};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        guess = "BABES";            secretWord = "ABBEY";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {YELLOW, YELLOW, GREEN, GREEN, GRAY};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        guess = "ROBOT";            secretWord = "TABOO";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {GRAY, YELLOW, GREEN, GREEN, YELLOW};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));
    }

    @Test
    void isGuessCorrect() {
        System.out.println("Verify system can recognize correct guesses, invalid guesses return false");
        assertFalse(guessEval.isGuessCorrect("hello", "byeby"));
        assertTrue(guessEval.isGuessCorrect("total", "total"));
        assertTrue(guessEval.isGuessCorrect("WHOLE", "WHOLE"));
        assertFalse(guessEval.isGuessCorrect("", "WRONG"));
    }
}