package org.fdu;

import static org.fdu.GuessValidation.ValidationReason.*; // access to the GuessValidation reason code enum
import org.fdu.GameDTOs.*;

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
    private static final String INVALID_ENTRY = "Invalid entry!!  Please re-enter your guess";   // used for unknowns
    private static final String GAME_ALREADY_OVER = "Game Over!! No more guesses";
    private static final String GUESS_LENGTH_INCORRECT = "Guess length is NOT 5 letters, Please re-enter";
    private static final String GUESS_NON_ALPHA = "Guess must contain only letters (a-z or A-Z), Please re-enter";
    private static final String WINNER = "\n\nCongratulations!  You are the Wordle Champ of the day";
    private static final String LOSER = "\n\nSorry!  You didn't guess the word, the word was: ";


    public static void main(String[] args) {

        // AnsiConsole.systemInstall(); // to support colors on all terminal types
        /**
         * To transition to WebUI interface - 3 phases
         *   construct the game incl. picking the secret word (WordleGame constructor)
         *   get and process a guess (game.processGuess)
         *   evaluate the results (GuessResult methods)
         */
        WordleGame game = new WordleGame();  // create a new instance of game w/o UI
        WordleUI ui = new WordleUI();

        GuessEvaluation.Result[] results;    // to pass into the UI

        // Display intro information
        ui.writeMessage(WORDLE_INTRO);
        ui.writeMessage(GAME_OVERVIEW);
        // String secretWord = dictionary.pickNewWord();

        // debug help - ToDo: delete this
        ui.writeMessage(game.getSecretWord() + "\n\n");

        /*
         *                     Main game loop
         *  Two phases
         *     processGuess() - returns data class GuessResult
         *     getGameStatus() - returns if user won &/or if game is over
         *
         *       while user hasn't guessed the secret word or exhausted their guesses
         * getUserGuess from the UI & send to the game to evaluate
         *   if guess is NOT valid, print error message, and continue to next guess
         *   if guess valid print the color coded results
         *   if user won - print win message & exit game loop
         *   else if user lost - print lose message & exit game loop
         */
        while (true) {  // as long as gameOver code works, no need to set a condition
            String userGuess = ui.getUserGuess(PROMPT_MESSAGE);
            GuessResult guessResult = game.processGuess(userGuess);
            if (guessResult.guessStatus() == GuessStatus.INVALID) {
                String message = getErrorMessage (guessResult.guessReason());
                ui.writeMessage(message);
                continue;
            }
            results = guessResult.evaluation();
            ui.printGuessResult(userGuess, results);

            // now get the game status - captures if player won or if game over & player lost
            GameStatus gameStatus = game.getGameStatus();
            if (gameStatus.userWon()) {
                ui.writeMessage(WINNER);
                break;   // user won the game,
            }
            else if (gameStatus.gameOver()) {
                ui.writeMessage(LOSER + gameStatus.secretWord());
                break;
            }
        }  // end game loop
    }  // end main method

    private static String getErrorMessage(GuessValidation.ValidationReason reasonCode) {
        // J17+ adds an enhanced "switch expression", cutting away boiler plate
        // import of GuessValidation allows access to enums without the qualifiers
        String message = switch (reasonCode) {
            case INVALID_LENGTH -> GUESS_LENGTH_INCORRECT;
            case GAME_OVER -> GAME_ALREADY_OVER;
            case NON_ALPHA -> GUESS_NON_ALPHA;
            default -> INVALID_ENTRY;
        };
        return message;
    }
}  // end main class


