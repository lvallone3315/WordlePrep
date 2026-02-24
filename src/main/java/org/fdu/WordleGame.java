package org.fdu;

import java.io.InputStream;
import java.util.Properties;

import org.fdu.GameDTOs.*;
import org.springframework.stereotype.Component;


/**
 * Create and control Wordle games - stateless.  Includes DTO creation, game version definition and
 * guess evaluation through {@link WordleService}
 *
 * <p>
 *     <b>Scope: <br> </b>
 *     - constructor - empty <br>
 *     - game creation via - {@link #createNewGame()} returns DTO with default game <br>
 *     - guess evaluation via {@link WordleService}, packages results into guess evaluation and game status DTOs <br>
 *     - Versioning via retrieval of version.properties in resources
 * </p>
 *
 * @author Lee V
 * @version 1.1  Stateless, ToDo - get rid of constructor
 */

@Component
public class WordleGame {

    private final WordleDictionary wordleDictionary = new WordleDictionary();

    private final static int MAX_GUESSES = 6;

    /**
     *   WordleGame() - empty constructor, actual game created with {@link #createNewGame()}
     */
    public WordleGame() {
    }

    /**
     *   selects word from standard dictionary <br>
     *   after selecting word {@link WordleDictionary#pickNewWord()} calls {@link #createNewGame(String)}
     * @return DTO for initial game (e.g. gameOver: false, number guesses taken = 0 ...)
     */
    public GameStatus createNewGame() {
        return createNewGame(wordleDictionary.pickNewWord());
    }

    /**
     *   WordleGame(String) - sets the secret word to the selected string (for testing)
     * @param secretWord string to compare users guesses to
     * @return DTO for initial game (e.g. gameOver: false, number guesses taken = 0 ...)
     */
    public GameStatus createNewGame(String secretWord) {
        return new GameStatus(
                false,  // gameOver
                false,           // userWon
                secretWord,      // secretWord, passed in (possibly created by dictionary version)
                0,               // number of guesses taken
                MAX_GUESSES,     // maximum number of guesses allowed
                getGameVersion());  // derived version of the game
    }

    /*
     * helper function to retrieve the game version information
     */
    private String getGameVersion() {
        String localGameVersion;
        // getResourceAsStream looks in src/main/resources (the classpath root)
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("version.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                localGameVersion = prop.getProperty("wordle.version", "unknown");
            } else {
                // This happens if the file is missing from the JAR
                localGameVersion = "unknown-v-file-missing";
            }
        } catch (Exception ex) {
            // Fallback if the file exists but is unreadable
            localGameVersion = "unknown-error";
        }
        return localGameVersion;
    }


    /**
     * Pass through version - state kept external to WordleGame and passed in <br>
     * WordleService processGuess will: normalize the guess, all caps and trimmed whitespace <br>
     *   evaluate the guess and return the color codes (enums) for each character in the results[] <br>
     *   if user won - set the userWon flag <br>
     *   if game over - set the gameOver flag  (userWon and gameOver retrieved from gameStatus) <br>
     * @param game - current game status, new game status returned in the GuessResponse return
     * @param userGuess - what the user entered
     * @return package of gameStatus and guessStatus
     */
    public GuessResponse processGuess(GameStatus game, String userGuess) {
        return WordleService.processGuess(game, userGuess);
    }
}
