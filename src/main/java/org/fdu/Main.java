package org.fdu;

import org.fdu.GameDTOs.*;

/**
 * Main class - entry point and behavior for console-based Wordle game.  Not used for Web version.<br>
 * <p>
 * This class handles the orchestration of the game loop, manages console-specific <br>
 * UI messages, and serves as the bridge between the {@link WordleService} <br>
 * and the user's terminal. <br>
 * <p>
 * Note: This implementation is specific to the CLI version; <br>
 * the Web version utilizes a separate entry point. <br>
 */
public class Main {
    private static final String WORDLE_INTRO = "\t\t\tWelcome to FDU Wordle\n";

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

    // Main class constructor, not intended to be instantiated, primarily to quiet JavaDoc warning
    private  Main() {}

    /**
     *  implements the primary game loop.  Not used for Web version.<br>
     *  <p>
     *  Three phases <br>
     *  - initialize the session (ie game) and select secret word via {@link WordleGame} <br>
     *  - read and validate user input (ie guess) via {@link WordleUI} <br>
     *  - Process guesses via {@link WordleService}, update game state and inform user of results until game is over <br>
     * <p>
     * To transition to WebUI interface - 3 phases <br>
     * - construct the game incl. picking the secret word (WordleGame constructor)<br>
     * - get and process a guess (game.processGuess) <br>
     * - valuate the results (GuessResult methods) <br>
     *
     * @param args None - not used at this time
     */
    public static void main(String[] args) {

        // AnsiConsole.systemInstall(); // to support colors on all terminal types, doesn't work on Win 10

        WordleGame game = new WordleGame();  // create a new instance of game
        GameStatus gameDTO =  game.createNewGame();   // stateless version, DTO contains game state, selects secret word
        WordleUI ui = new WordleUI();

        // Display intro information
        ui.writeMessage(WORDLE_INTRO);
        ui.writeMessage(GAME_OVERVIEW);

        // debug help - ToDo: delete this
        ui.writeMessage(gameDTO.secretWord() + "\n\n");

        /*
         *                     Main game loop
         *  Two phases
         *     processGuess() - returns data class GuessResult
         *     GameStatus() - DTO including if user won &/or if game is over, number of guesses, etc.
         *
         *  while user hasn't guessed the secret word or exhausted their guesses
         *     getUserGuess from the UI & send to the game to evaluate
         */
        while (true) {  // as long as gameOver code works, no need to set a condition
            String userGuess = ui.getUserGuess(PROMPT_MESSAGE);
            // WordleService returns both the new game status and the guess evaluation
            GuessResponse response = WordleService.processGuess(gameDTO, userGuess);

            // take care of invalid guesses - print appropriate error message
            if (response.guessResult().guessStatus() == GuessStatus.INVALID) {
                ui.writeMessage(getErrorMessage (response.guessResult().guessReason()));
                continue;
            }

            // valid guess, update game state, overwrite previous game state
            gameDTO = response.gameStatus();

            // retrieve the evaluation and print to the console
            ui.printGuessResult(userGuess, response.guessResult().evaluation());

            // handle game over scenarios - player won or if not, game over -> player lost
            if (gameDTO.userWon()) {
                ui.writeMessage(WINNER);
                break;   // user won the game,
            }
            else if (gameDTO.gameOver()) {
                ui.writeMessage(LOSER + response.gameStatus().secretWord());
                break;
            }
        }  // end game loop
    }

    /*
     * helper method to map the reason code returned by processGuess to the actual user error message
     * note: switch case labels MUST use unqualified name (e.g. NOT ValidationReason.GAME_OVER)
     * default is a double-edge sword:
     *   if we add another reason code, and forget to add it here, the game will still run
     *   but, it will do so without error, so we miss an opportunity to find the issue.
     * ToDo - remove "default", if additional enums are added, the switch statement will NOT compile.
     */
    private static String getErrorMessage(GuessValidation.ValidationReason reasonCode) {
        // J17+ adds an enhanced "switch expression", cutting away boilerplate
        // import of GuessValidation allows access to enums without the qualifiers
        return switch (reasonCode) {
            case INVALID_LENGTH -> GUESS_LENGTH_INCORRECT;
            case GAME_OVER -> GAME_ALREADY_OVER;
            case NON_ALPHA -> GUESS_NON_ALPHA;
            default -> INVALID_ENTRY;
        };
    }
}  // end main class