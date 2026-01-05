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
    private GuessValidation.ValidationReason guessReason;

    /**
     * Replaced prior guessStatus to provide a reason for invalid guesses
     * @param guessStatus - valid or invalid, see enum
     * @param evaluation - for valid guesses, indicates status of each letter (G Y Gray)
     * @param guessReason - indicating why the guess is not valid, see GuessValidation.ValidationReason enum
     */
    GuessResult(GuessStatus guessStatus,
                GuessEvaluation.Result[] evaluation,
                GuessValidation.ValidationReason guessReason) {
        this.guessStatus = guessStatus;
        this.guessEval = evaluation;
        this.guessReason = guessReason;
    }

    // public getters so JSON can access all of the private data elements
    public GuessStatus getGuessStatus() {
        return guessStatus;
    }
    public GuessEvaluation.Result[] getGuessEval() {
        return guessEval;
    }
    public GuessValidation.ValidationReason getGuessReason() {return guessReason;}
}