package org.fdu;

import org.fdu.GameDTOs.*;

/**
 * Stateless guess evaluation - input: current game state and user's guess and
 * returns: an evaluation of the guess and the new game state. <br>
 *
 * @author Lee V
 * @version v1.2 (game version)
 */
public class WordleService {
    /**
     *   process player's guess and return guess result and updated game state<br>
     *   Approach: if guess is valid, normalize the guess (all caps and trimmed whitespace) <br>
     *     evaluate the guess and return the color codes (enums) for each character in the results[] <br>
     *   if user won - set the userWon flag <br>
     *   if game over - set the gameOver flag  (userWon and gameOver retrieved from getGameStatus()) <br>
     *   DTOs<br>
     *   GuessResult - guess validity, colored evaluation[], reason for invalid guesses (e.g. INVALID_LENGTH) <br>
     *   GameStatus - updated gameOver, userWon and numGuesses fields, rest (e.g. maxGuesses) copied from original game status DTO <br>
     * @return GuessResponse - data transfer object wrapping both GuessResult and GameStatus DTOs
     */
    public static GameDTOs.GuessResponse processGuess(GameStatus game, String userGuess) {

        // if game over, ignore the guess, return the original game object (no change)
        if (game.gameOver()) {
            GuessResult result = new GameDTOs.GuessResult(GameDTOs.GuessStatus.INVALID, null,
                    GuessValidation.ValidationReason.GAME_OVER);
            return new GuessResponse(result, game);
        }
        //  normalize the word (e.g. caps, no whitespace) & validate if meets requirements
        String normalizedUserGuess = GuessValidation.normalizeWord(userGuess);
        var validation = GuessValidation.validateWord(normalizedUserGuess);

        // System.out.println("Secret Word: " + secretWord + "User Guess: " + userGuess);
        if (!validation.isValid()) {
            GuessResult result = new GameDTOs.GuessResult(GameDTOs.GuessStatus.INVALID, null,
                    validation.reason());
            return new GuessResponse(result, game);
        }

        boolean gameOver = false;   // verified above to be false
        boolean userWon = game.userWon();   // should also be false, but might as well grab actual state

        //   user guess is valid
        //     update guess counter, check guess to secret word
        int newNumGuesses = game.numGuesses() + 1;
        GuessEvaluation.Result[] results = GuessEvaluation.evaluateGuess(normalizedUserGuess, game.secretWord());

        // 4 scenarios - game isn't over - no message
        //   game over & user won - userWon & gameOver = true, message WINNER
        //   game over & user lost - gameOver = true, userWon = false, message LOSER
        if (GuessEvaluation.isGuessCorrect(normalizedUserGuess, game.secretWord())) {
            gameOver = true;
            userWon = true;
        }
        else if (game.maxGuesses() - newNumGuesses <= 0) {
            gameOver = true;
            userWon = false;  // unnecessary, but makes it clear
        }  // else - game remains in progress

        // return the results and game status for display
        GuessResult newResult = new GameDTOs.GuessResult(GameDTOs.GuessStatus.VALID, results,
                validation.reason() );
        // game status uses a wither update based on the original game DTO, returns a new GameStatus record
        //    only updates the values passed (e.g. gaveOver), the other values are copied by the DTO handler
        GameStatus newStatus = game.withGameUpdates(gameOver, userWon, newNumGuesses);
        return new GuessResponse(newResult, newStatus);
    }
}
