package org.fdu;

public class GuessResult {
    public enum GuessStatus {VALID, INVALID};

    private static GuessStatus guessStatus;
    private static GuessEvaluation.Result[] guessEval;
    private static boolean isGuessCorrect;
    private static String userMessage;   // message to send to user

    GuessResult(GuessStatus guessStatus,
                GuessEvaluation.Result[] evaluation,
                boolean isGuessCorrect,
                String userMessage) {
        this.guessStatus = guessStatus;
        this.guessEval = evaluation;
        this.isGuessCorrect = isGuessCorrect;
        this.userMessage = userMessage;
    }

    // public getters so JSON can access all of the private data elements
    GuessStatus getGuessStatus() {
        return guessStatus;
    }
    GuessEvaluation.Result[] getGuessEval() {
        return guessEval;
    }
    boolean isGuessCorrect() {
        return isGuessCorrect;
    }
    String getUserMessage() {
        return userMessage;
    }
}
