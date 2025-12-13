package org.fdu;

/**
 * Data Class for processing user guesses
 * Defines an ENUM for guess status = VALID or INVALID
 * Three data elements:
 *     guessStatus - VALID or INVALID
 *     guessEvaluation - array of Result enums denoting color of each character in guess
 *     userMessage - message to display to user with this guess (e.g. Invalid Guess)
 */
public class GuessResult {
    public enum GuessStatus {VALID, INVALID};

    private static GuessStatus guessStatus;
    private static GuessEvaluation.Result[] guessEval;
    private static String userMessage;   // message to send to user

    GuessResult(GuessStatus guessStatus,
                GuessEvaluation.Result[] evaluation,
                String userMessage) {
        this.guessStatus = guessStatus;
        this.guessEval = evaluation;
        this.userMessage = userMessage;
    }

    // public getters so JSON can access all of the private data elements
    GuessStatus getGuessStatus() {
        return guessStatus;
    }
    GuessEvaluation.Result[] getGuessEval() {
        return guessEval;
    }
    String getUserMessage() {
        return userMessage;
    }
}