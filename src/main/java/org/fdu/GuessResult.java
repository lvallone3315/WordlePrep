package org.fdu;

/**
 * Data Class for processing user guesses
 * Defines an ENUM for guess status = VALID or INVALID
 * Three data elements:
 *     guessStatus - VALID or INVALID
 *     guessEvaluation - array of Result enums denoting color of each character in guess
 */
public class GuessResult {
    public enum GuessStatus {VALID, INVALID};

    private GuessStatus guessStatus;
    private GuessEvaluation.Result[] guessEval;

    GuessResult(GuessStatus guessStatus,
                GuessEvaluation.Result[] evaluation) {
        this.guessStatus = guessStatus;
        this.guessEval = evaluation;
    }

    // public getters so JSON can access all of the private data elements
    public GuessStatus getGuessStatus() {
        return guessStatus;
    }
    public GuessEvaluation.Result[] getGuessEval() {
        return guessEval;
    }
}