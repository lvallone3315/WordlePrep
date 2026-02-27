package org.fdu;

import java.util.Scanner;  // for console input

/**
 * Responsible for all input and output to the console, not used in Web version.
 * <p>
 * In addition to basic String input (Scanner) and output (System.out), <br>
 * Uses {@link ConsoleColors} to write guess evaluation using ASCII colors
 */

public class WordleUI {
    // if multiple instances created, all will use the same scanner
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * UI constructor - instantiated for two reasons:<br>
     * Scanner is stateful and improves readability in calling code (e.g. ui.)
     */

    public WordleUI() {} // empty constructor

    /**
     * write the output message to the console, user must define newlines in message (as needed)
     * @param message text to be written to the console
     */
    public void writeMessage(String message) {
        System.out.print(message);
    }

    /**
     * Read user input <br>
     * Two versions - one with a prompt, one without <br>
     *   Non-prompt version: calls prompt version with ""<br>
     * @return String typed into the console by the user (without newline)
     */
    public String getUserGuess() {
        return getUserGuess("");
    }

    /**
     * Write a prompt and then read user input
     * @param prompt to be written to screen
     * @return String typed into the console by the user (without newline)
     */
    public String getUserGuess(String prompt) {

        System.out.print(prompt);
        return SCANNER.nextLine();
    }

    /**
     * Prints the user guess in the appropriate colors (Green, Yellow, Gray), showing the eval results <br>
     * -- then resets the colors as a final step <br>
     *
     * Unexpected colors:
     *   If the guessEvaluation is other than Green, Yellow or Gray, the letter is output in RED
     *
     * @param guess user's guess (raw or normalized, no requirement)
     * @param guessEval array of colors (enum) denoting "correctness" of each character in user's guess
     * @throws IllegalArgumentException if length of guess does not equal the guessEvaluation length
     */
    public void printGuessResult(String guess, GuessEvaluation.Result[] guessEval ) {
        // Fail-fast if the data is corrupted/mismatched
        if (guess.length() != guessEval.length) {
            throw new IllegalArgumentException("Guess text and evaluation array must be the same length.");
        }
        for (int itr = 0; itr < guessEval.length; itr++) {
            System.out.print("\t\t");  // offset the text to improve readability
            switch (guessEval[itr]) {
                case GRAY   -> System.out.print(ConsoleColors.GRAY + guess.charAt(itr));
                case GREEN  -> System.out.print(ConsoleColors.GREEN + guess.charAt(itr));
                case YELLOW -> System.out.print(ConsoleColors.YELLOW + guess.charAt(itr));
                default     -> System.out.print(ConsoleColors.RED + guess.charAt(itr));
            }
        }
        System.out.println(ConsoleColors.RESET);
    }  // end printGuessResult

}
