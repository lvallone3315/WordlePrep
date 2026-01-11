package org.fdu;

/**
 * Data Class for processing user guesses
 *
 * Three DTOs (though one is a package of the other two)
 *     GameStatus - defines game state
 *     GuessResult - evaluation of the user's guess including failure reason if invalid
 *     GuessResponse - DTO packaging GameStatus and GuessResult
 *
 * Defines an ENUM for guess status = VALID or INVALID
 */
public class GameDTOs {
    public enum GuessStatus {VALID, INVALID};

    /**
     * Data Class for game status
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
                             String gameVersion) { }/**

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