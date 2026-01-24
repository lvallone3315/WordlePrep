package org.fdu;

import java.io.InputStream;
import java.util.Properties;

import org.fdu.GameDTOs.*;
import org.springframework.stereotype.Component;


/**
 * Create and control a single game of Wordle.  Processes user guess(es); return results and game status.
 *
 * <p>
 * Constructs a new game of Wordle.  Scope includes: processing the player's guess, tracking number of guesses
 *   made and the maximum allowed.  Tracks state such as did the player win, is the game over.<br>
 *   Provide access to the current status of the game (gameStatus).
 * </p>
 *
 * @author Lee V
 * @version 1.0.1  Refactoring towards stateless
 */

@Component
public class WordleGame {

    private final WordleDictionary wordleDictionary = new WordleDictionary();

    private final static int MAX_GUESSES = 6;

    // game state variables
    private String secretWord;
    private int numGuessesTaken = 0;
    private boolean gameOver = false;
    private boolean userWon = false;
    private String gameVersion = "unknown";


    /**
     * Two constructors
     *   WordleGame() - selects a word from the dictionary (standard game play constructor)
     *   WordleGame(String) - sets the secret word to the selected string (for testing)
     */
    public WordleGame() {
        secretWord = wordleDictionary.pickNewWord();
        storeGameVersion();
    }
    /*
    public WordleGame(String secretWord) {
        this.secretWord = secretWord;
        storeGameVersion();
    }
    */


    public GameStatus createNewGame() {
        return createNewGame(wordleDictionary.pickNewWord());
    }
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
     * helper function to retrieve the version information
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

    /* tempoary refactoring version - in favor of getGameVersion() */
    private void storeGameVersion() {
        this.gameVersion = getGameVersion();
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

    /**
     *   @deprecated - part of stateless refactore, replaced by overload takes gameStatus as input
     *   Use {@link #processGuess(GameStatus, String)}
     *   legacy version - check if guess is valid,
     *   normalize the guess, all caps and trimmed whitespace
     *   evaluate the guess and return the color codes (enums) for each character in the results[]
     *   if user won - set the userWon flag
     *   if game over - set the gameOver flag  (userWon and gameOver retrieved from getGameStatus())
     * @return GuessResult - data class with: VALID/INVALID guess, color coded result enums
     */
    @Deprecated(since = "24-Jan, 2026, stateless refactor", forRemoval = true)
    public GuessResult processGuess(String userGuess) {
        // refactor to use WordleService - create game state
        // update local state based on return from WordleService processGuess
        GameStatus game = new GameStatus(gameOver, userWon, secretWord, numGuessesTaken, MAX_GUESSES, gameVersion);
        GuessResponse guessResponse = WordleService.processGuess(game, userGuess);
        gameOver = guessResponse.gameStatus().gameOver();
        userWon = guessResponse.gameStatus().userWon();
        numGuessesTaken = guessResponse.gameStatus().numGuesses();
        return guessResponse.guessResult();
    }

    /**
     * Status is calculated when processing the user's guess
     * @return GameStatus - is game over (true/false), has user won (true/false), secret word for display
     *   and a message to display to the user
     */
    public GameStatus getGameStatus() {
        return new GameStatus(gameOver, userWon, secretWord, numGuessesTaken, MAX_GUESSES, gameVersion);
    }

    /**
     * @deprecated - in favor of using status DTO to retrieve maximum number of guesses
     * Parameterize limit on user guesses
     * @return number of guesses user allowed before game is over
     */
    @Deprecated(since = "24-Jan, 2026, stateless refactor", forRemoval = true)
    public int getMaxUserGuesses() {
        return MAX_GUESSES;
    }

    /**
     * @deprecated - in favor of using status DTO to retrieve number of guesses used
     * For testing and future functionality, allow retrieval of # guesses taken
     * @return number of guesses taken
     */
    @Deprecated(since = "24-Jan, 2026, stateless refactor", forRemoval = true)
    public int getNumGuessesTaken() {
        return numGuessesTaken;
    }

    /**
     * @deprecated - in favor of using status DTO to retrieve secretWord, game Over & userwon info
     * these methods are for testing,
     * @return secret word
     */
    @Deprecated(since = "24-Jan, 2026, stateless refactor", forRemoval = true)
    public String getSecretWord() {
        return secretWord;
    }
    /**
     * @deprecated - use status DTO to retrieve game Over
     * @return true if game is over
     */
    @Deprecated(since = "24-Jan, 2026, stateless refactor", forRemoval = true)
    public boolean isGameOver() {
        return gameOver;
    }
    /**
     * @deprecated - use status DTO to retrieve if user guessed the word
     * @return true if the user won
     */
    @Deprecated(since = "24-Jan, 2026, stateless refactor", forRemoval = true)
    public boolean didUserWin() { return userWon; }  /** @return true if the user won */
}
