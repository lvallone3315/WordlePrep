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
        String secretWord = "BRICK";        String guess = "CRICK";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        GuessEvaluation.Result[] expEvalResult = {GRAY, GREEN, GREEN, GREEN, GREEN};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        secretWord = "SHADE";        guess = "BRICK";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {GRAY, GRAY, GRAY, GRAY, GRAY};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        secretWord = "HELLO";        guess = "HELLO";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {GREEN, GREEN, GREEN, GREEN, GREEN};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));

        secretWord = "AAAAA";        guess = "ABCBA";
        System.out.println(String.format("evaluateGuess: secret word %s, guess is %s\n",  secretWord, guess));
        expEvalResult = new GuessEvaluation.Result[] {GREEN, GRAY, GRAY, GRAY, GREEN};
        assertArrayEquals(expEvalResult, guessEval.evaluateGuess(guess, secretWord));
    }

    @Test
    // deprecated, as can now test through the game class
    void isUserOutOfGuesses() {
        System.out.println("Verify guesses are counted properly");
        assertFalse(guessEval.isUserOutOfGuesses());  // initially not out of guesses
        for (int itr = 0; itr < guessEval.GUESS_LIMIT; itr++) {   // use the define for the guess limit?
            guessEval.evaluateGuess("ABCDE", "VWXYZ");
        }
        assertTrue(guessEval.isUserOutOfGuesses());
        guessEval.evaluateGuess("BRICK", "BRICK");
        assertTrue(guessEval.isUserOutOfGuesses());   // should still be true
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