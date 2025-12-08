package org.fdu;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static String WORDLE_INTRO = "\t\t\tWelcome to FDU Wordle\n";
      // ToDo - update to screen size independent (not sure what this means yet)
    private static String GAME_OVERVIEW = "\nA five (5) letter US English word will be selected \n" +
            "  your task is to guess the word in six (6) or fewer guesses (valid US English words only) \n" +
            "  After each turn, the game will evaluate your guess as follows: \n" +
            "    Green  - letter is in the word & in the correct position \n" +
            "    Yellow - letter is in the word, but not in the correct position \n" +
            "    Gray   - letter is NOT in the word\n";
    private static String PROMPT_MESSAGE = "\nYour guess (5 letter word): ";


    static void main() {

        // construct dictionary & UI objects
        //  ToDo - possibly create as singletons?
        WordleDictionary dictionary = new WordleDictionary();
        WordleUI output = new WordleUI();

        // Display intro information
        output.writeMessage(WORDLE_INTRO);
        output.writeMessage(GAME_OVERVIEW);
        output.writeMessage(PROMPT_MESSAGE);

        // for testing
        for (int itr = 1; itr < 6; itr++) {
            output.writeMessage(String.format("\nWord %s: %s", itr, dictionary.pickNewWord()));
        }
    }
}
