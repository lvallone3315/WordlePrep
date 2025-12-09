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

    private static final String INVALID_ENTRY = "Invalid entry, please re-enter your guess";
    private static final String WINNER = "Congratulations!  You are the Wordle Champ of the day";
    private static final String LOSER = "Sorry!  You didn't guess the word, the word was: ";


    static void main() {

        // construct dictionary & UI objects
        //  ToDo - possibly create as singletons?
        WordleDictionary dictionary = new WordleDictionary();
        WordleUI ui = new WordleUI();
        GuessValidation guessValidation = new GuessValidation();
        GuessEvaluation guessEval = new GuessEvaluation();

        GuessEvaluation.Result[] results;

        // Display intro information
        ui.writeMessage(WORDLE_INTRO);
        ui.writeMessage(GAME_OVERVIEW);
        String secretWord = dictionary.pickNewWord();
        // debug
        ui.writeMessage(secretWord + "\n\n");

        while (!guessEval.isUserOutOfGuesses()) {
            String userGuess = ui.getUserGuess(PROMPT_MESSAGE);
            if (!guessValidation.isWordValid(userGuess)) {
                ui.writeMessage(INVALID_ENTRY);
            }
            String normalizedUserGuess = guessValidation.normalizeWord(userGuess);
            results = guessEval.evaluateGuess(normalizedUserGuess, secretWord);
            ui.printGuessResult(normalizedUserGuess, results);
            // check for winning here
        }
    }
}
