package org.fdu;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String WORDLE_INTRO = "\t\t\tWelcome to FDU Wordle\n";
      // ToDo - update to screen size independent (not sure what this means yet)
    private static final String GAME_OVERVIEW = """
              
              A five (5) letter US English word will be selected\s
                your task is to guess the word in six (6) or fewer guesses (valid US English words only)\s
                After each turn, the game will evaluate your guess as follows:\s
                  Green  - letter is in the word & in the correct position\s
                  Yellow - letter is in the word, but not in the correct position\s
                  Gray   - letter is NOT in the word
              """;
    private static final String PROMPT_MESSAGE = "\nYour guess (5 letter word)? ";


    static void main() {

        // construct dictionary & UI objects
        //  ToDo - possibly create as singletons?
        WordleDictionary dictionary = new WordleDictionary();
        WordleUI ui = new WordleUI();

        // Display intro information
        ui.writeMessage(WORDLE_INTRO);
        ui.writeMessage(GAME_OVERVIEW);
        ui.writeMessage(PROMPT_MESSAGE);

        // for testing
        for (int itr = 1; itr < 6; itr++) {
            // cycle through colors in word
            char[] letters = dictionary.pickNewWord().toCharArray();
            String coloredOutput = String.format("\nWord %s: ", itr);
            coloredOutput += ConsoleColors.RED + letters[0] +
                    ConsoleColors.GREEN + letters[1] +
                    ConsoleColors.GRAY + letters [2] +
                    ConsoleColors.YELLOW + letters[3] +
                    ConsoleColors.RESET + letters[4];
            ui.writeMessage(coloredOutput);
        }

        // test out reading from the console
        String userGuess = ui.getUserGuess(PROMPT_MESSAGE);
        ui.writeMessage(userGuess);

        // test out validation of input from the console
        GuessValidation guessValidation = new GuessValidation();
        boolean validGuess;
        for (int itr = 0; itr < 5; itr++) {
            userGuess = ui.getUserGuess(PROMPT_MESSAGE);
            ui.writeMessage(userGuess);
            ui.writeMessage(String.format("Guess validity is %s", guessValidation.isWordValid(userGuess) ));
            ui.writeMessage(String.format("\tNormalized guess is %s", guessValidation.normalizeWord(userGuess)));
        }
    }
}
