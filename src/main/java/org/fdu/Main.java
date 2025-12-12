package org.fdu;

import org.fusesource.jansi.AnsiConsole;  // for colors

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
    private static final String WINNER = "\n\nCongratulations!  You are the Wordle Champ of the day";
    private static final String LOSER = "\n\nSorry!  You didn't guess the word, the word was: ";


    static void main() {

        // AnsiConsole.systemInstall(); // to support colors on all terminal types

        // construct dictionary & UI objects
        //  ToDo - possibly create as singletons?
        // WordleDictionary dictionary = new WordleDictionary();
        // GuessValidation guessValidation = new GuessValidation();
        // GuessEvaluation guessEval = new GuessEvaluation();

        /**
         * To transition to WebUI interface - 3 phases
         *   construct the game incl. picking the secret word (WordleGame constructor)
         *   get and process a guess (game.processGuess)
         *   evaluate the results (GuessResult methods)
         */
        WordleGame game = new WordleGame();  // create a new instance of game w/o UI
        WordleUI ui = new WordleUI();

        GuessEvaluation.Result[] results;    // to pass into the UI
        // boolean userWon = false;  -- replace with call to guessResults

        // Display intro information
        ui.writeMessage(WORDLE_INTRO);
        ui.writeMessage(GAME_OVERVIEW);
        // String secretWord = dictionary.pickNewWord();

        // debug help - ToDo: delete this
        ui.writeMessage(game.getSecretWord() + "\n\n");

        /*
         *                     Main game loop
         *        while user hasn't guessed the secret word or exhausted their guesses
         *   getUserGuess()
         *   check isWordValid(), if not print error message, and continue to next guess
         *   change guess to uppercase, trim whitespace
         *   evaluate the guess & print the results (color coded)
         *   if user won - print win message & exit game loop
         */
        GuessResult guessResult = null;
        while (!game.isGameOver()) {
            String userGuess = ui.getUserGuess(PROMPT_MESSAGE);
            guessResult = game.processGuess(userGuess);
            if (guessResult.getGuessStatus() == GuessResult.GuessStatus.INVALID) {
                ui.writeMessage(INVALID_ENTRY);
            }
            results = guessResult.getGuessEval();
            ui.printGuessResult(userGuess, results);
            if (guessResult.isGuessCorrect()) {
                ui.writeMessage(WINNER);
                break;   // user won the game,
            }
        }  // end main game loop
        if (guessResult.isGuessCorrect() == false) {
            ui.writeMessage(LOSER + game.getSecretWord());
        }
    }
}
