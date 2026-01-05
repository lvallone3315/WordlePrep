package org.fdu;

import java.io.InputStream;
import java.util.Properties;

/**
 * Create and control a single game of Wordle.  Processes user guess(es) and return results & game status.
 *
 * <p>
 * Constructs a new game of Wordle.  Scope includes: processing the player's guess, tracking number of guesses
 *   made and the maximum allowed.  Tracks state such as did the player win, is the game over.<br>
 *   Provide access to the current status of the game (gameStatus).
 * </p>
 *
 * @author Lee V
 * @version 1.0
 */

public class WordleGame {

    private final WordleDictionary wordleDictionary = new WordleDictionary();
    private final GuessEvaluation guessEval = new GuessEvaluation();

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
    public WordleGame(String secretWord) {
        this.secretWord = secretWord;
        storeGameVersion();
    }

    /*
     * helper function to retrieve the version information and store it
     */
    private void storeGameVersion() {
        // getResourceAsStream looks in src/main/resources (the classpath root)
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("version.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                this.gameVersion = prop.getProperty("wordle.version", "unknown");
            } else {
                // This happens if the file is missing from the JAR
                this.gameVersion = "unknown-v-file-missing";
            }
        } catch (Exception ex) {
            // Fallback if the file exists but is unreadable
            this.gameVersion = "unknown-error";
        }
    }

    /**
     * these methods are for testing,
     * @return secret word
     */
    public String getSecretWord() {
        return secretWord;
    }
    public boolean isGameOver() {
        return gameOver;
    } /** @return true if game is over */
    public boolean didUserWin() { return userWon; }  /** @return true if the user won */

    /**
     *   check if guess is valid,
     *   normalize the guess, all caps and trimmed whitespace
     *   evaluate the guess and return the color codes (enums) for each character in the results[]
     *   if user won - set the userWon flag
     *   if game over - set the gameOver flag  (userWon and gameOver retrieved from getGameStatus())
     * @return GuessResult - data class with: VALID/INVALID guess, color coded result enums
     */
    public GuessResult processGuess(String userGuess) {
        // ToDo: add check here for game over - e.g. add another GuessStatus enum = GameOver
        if (gameOver) {
            return new GuessResult(GuessResult.GuessStatus.INVALID, null,
                    GuessValidation.ValidationReason.GAME_OVER);
        }
        //  normalize the word (e.g. caps, no whitespace) & validate if meets requirements
        String normalizedUserGuess = GuessValidation.normalizeWord(userGuess);
        var validation = GuessValidation.validateWord(normalizedUserGuess);

        // System.out.println("Secret Word: " + secretWord + "User Guess: " + userGuess);
        if (!validation.isValid()) {
            return new GuessResult(GuessResult.GuessStatus.INVALID, null,
                    validation.reason());
        }
        //   user guess is valid
        //     update guess counter, check guess to secret word
        numGuessesTaken++;
        GuessEvaluation.Result[] results = guessEval.evaluateGuess(normalizedUserGuess, secretWord);

        // 4 scenarios - game isn't over - no message
        //   game over & user won - userWon & gameOver = true, message WINNER
        //   game over & user lost - gameOver = true, userWon = false, message LOSER
        if (guessEval.isGuessCorrect(normalizedUserGuess, secretWord)) {
            gameOver = true;
            userWon = true;
        }
        else if (isUserOutOfGuesses()) {
            gameOver = true;
            userWon = false;  // unnecessary, but makes it clear
        }  // else - game remains in progress

        // return the results and game status for display
        return new GuessResult(GuessResult.GuessStatus.VALID, results,
                validation.reason() );
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
     * checks if player has used the maximum number of guesses
     * @return true if player has used the maximum allowed guesses, false otherwise
     */
    public boolean isUserOutOfGuesses() {
        return getNumGuessesTaken() >= MAX_GUESSES;
    }

    /**
     * Parameterize limit on user guesses
     * @return number of guesses user allowed before game is over
     */
    public int getMaxUserGuesses() {
        return MAX_GUESSES;
    }

    /**
     * For testing and future functionality, allow retrieval of # guesses taken
     * @return number of guesses taken
     */
    public int getNumGuessesTaken() {
        return numGuessesTaken;
    }
}
