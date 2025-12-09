package org.fdu;

public class WordleUI {
    public WordleUI() {}; // empty constructor

    /**
     * write the output message to the console, user must define newlines in message (as needed)
     * @param message - text to be written to the console
     */
    public void writeMessage(String message) {
        IO.print(message);
    }

    /**
     * Two versions - one with a prompt, one without
     * @return String typed into the console by the user
     */
    public String getUserGuess() {
        return getUserGuess("");
    }
    public String getUserGuess(String prompt) {
        String readln = IO.readln(prompt);
        return readln;
    }

    /**
     * Prints the user guess in the appropriate colors (Green, Yellow, Gray), showing the eval results
     *   Note: resets the colors as a final step
     * Important: the length of the guess and the length of the guessEval must match
     *   ToDo - throw exception if they don't, or iterate through the shortest of the two
     * @param guess -- user's guess (raw or normalized, no requirement)
     * @param guessEval -- array of colors (enum) denoting "correctness" of each character in user's guess
     */
    public void printGuessResult(String guess, GuessEvaluation.Result[] guessEval ) {
        for (int itr = 0; itr < guessEval.length; itr++) {
            IO.print("\t\t");  // offset the text to improve readability
            switch (guessEval[itr]) {
                case GuessEvaluation.Result.GRAY:
                    IO.print(ConsoleColors.GRAY + guess.charAt(itr));
                    break;
                case GuessEvaluation.Result.GREEN:
                    IO.print(ConsoleColors.GREEN + guess.charAt(itr));
                    break;
                case GuessEvaluation.Result.YELLOW:
                    IO.print(ConsoleColors.YELLOW + guess.charAt(itr));
                    break;
                default:
                    IO.print(ConsoleColors.RED + guess.charAt(itr));
                    break;
            }
        }
        IO.print(ConsoleColors.RESET);
    }  // end printGuessResult

}
