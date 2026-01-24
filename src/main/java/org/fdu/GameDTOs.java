package org.fdu;

/**
 * Data Class for processing user guesses and game state<p>
 *
 * Three DTOs (Guess Response is a package of the other two) <br>
 *     GameStatus - defines game state <br>
 *     GuessResult - evaluation of the user's guess including failure reason if invalid <br>
 *     GuessResponse - DTO packaging GameStatus and GuessResult <br>
 *<p>
 * Defines an ENUM for guess status = VALID or INVALID
 * <p>
 * For code clarity, most classes that use one or more of the DTOs should import this class: org.fdu.GameDTOs.*
 */
public class GameDTOs {
    /**
     * Enum describing if user's guess meets game requirements (VALID).  If not, INVALID.
     */
    public enum GuessStatus {VALID, INVALID};

    /**
     * DTO (data transfer object) for game status
     * @param  gameOver - true if the game is over, true if the player lost or won.
     * @param  userWon - true if the player has won, false if the game is still running, or over and the player lost
     * @param secretWord - word the player is trying guess
     * @param numGuesses - number of guesses so far, remaining guesses = maxGuesses - numGuesses
     * @param maxGuesses - maximum number of guess users can make before gameis over
     * @param gameVersion - dynamically populated version number for the game
     */
    public record GameStatus(boolean gameOver,
                             boolean userWon,
                             String secretWord,
                             int numGuesses,
                             int maxGuesses,
                             String gameVersion) {

        /**
         * "Wither" pattern for GameStatus.  Return a new instance with only the dynamic elements changed.
         * @param isOver - updated value for gameOver
         * @param won - updated value for userWon
         * @param guesses - new number of guesses (typically previous +1)
         * @return - updated GameStatus DTO with updates + original values
         */
        public GameStatus withGameUpdates(boolean isOver, boolean won, int guesses) {
            return new GameStatus(
                    isOver,    // updated
                    won,       // updated
                    this.secretWord,
                    guesses,   // updated
                    this.maxGuesses,
                    this.gameVersion);
        }
        /**
         * checks if player has used the maximum number of guesses
         * @return true if player has used the maximum allowed guesses, false otherwise
         */
        public boolean isUserOutOfGuesses() {
            return numGuesses >= maxGuesses;
        }
        public int getRemainingGuesses() {
            return maxGuesses - numGuesses;
        }
    }

     /**
     * DTO for processing the results of a user's guess
     * @param guessStatus - valid or invalid, see enum
     * @param evaluation - for valid guesses, indicates status of each letter (G Y Gray)
     * @param guessReason - indicating why the guess is not valid, see GuessValidation.ValidationReason enum
     */
    public record GuessResult(GuessStatus guessStatus,
                              GuessEvaluation.Result[] evaluation,
                              GuessValidation.ValidationReason guessReason) {    }

    /**
     * Package guessResult and the gameStatus into a single DTO
     * @param guessResult - results of the users guess, e.g. validity, reason for failures, evaluation of guess
     * @param gameStatus - game state including number guesses taken, game over?, user won, max guesses and game version
     */
    public record GuessResponse(
            GuessResult guessResult,
            GameStatus gameStatus) {  }
}