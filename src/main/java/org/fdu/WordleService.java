package org.fdu;

import org.fdu.GameDTOs.*;

/**
 * Stateless guess evaluation - takes current game state and the user's guess and returns an evaluation of the guess and the new game state. <br>
 *
 * @author Lee V
 * @version v1.2 (game version)
 */
public class WordleService {
    /**
     *   process a user's guess, if valid, evaluate against secret word and return evaluation and updated game status. <br>
     *   Approach: if guess is valid, normalize the guess (all caps and trimmed whitespace) <br>
     *     evaluate the guess and return the color codes (enums) for each character in the results[] <br>
     *   if user won - set the userWon flag <br>
     *   if game over - set the gameOver flag  (userWon and gameOver retrieved from getGameStatus()) <br>
     *   DTOs
     *   GuessResult - guess validity, colored evaluation[], reason for invalid guesses (e.g. INVALID_LENGTH) <br>
     *   GameStatus - updated gameOver, userWon and numGuesses fields, rest (e.g. maxGuesses) copied from original game status DTO <br>
     * @return GuessResponse - data transfer object wrapping both GuessResult & GameStatus DTOs
     */
    public static GameDTOs.GuessResponse processGuess(GameStatus game, String userGuess) {
        // temporary - consider changing guessEval to static
        GuessEvaluation guessEval = new GuessEvaluation();

        // game state data to be updated, other data will be copied from game parameter
        boolean gameOver = game.gameOver();
        boolean userWon = game.userWon();
        int numGuesses = game.numGuesses();

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
        //   user guess is valid
        //     update guess counter, check guess to secret word
        numGuesses = game.numGuesses() + 1;
        GuessEvaluation.Result[] results = guessEval.evaluateGuess(normalizedUserGuess, game.secretWord());

        // 4 scenarios - game isn't over - no message
        //   game over & user won - userWon & gameOver = true, message WINNER
        //   game over & user lost - gameOver = true, userWon = false, message LOSER
        if (guessEval.isGuessCorrect(normalizedUserGuess, game.secretWord())) {
            gameOver = true;
            userWon = true;
        }
        else if (game.maxGuesses() - numGuesses == 0) {
            gameOver = true;
            userWon = false;  // unnecessary, but makes it clear
        }  // else - game remains in progress


        // return the results and game status for display
        GuessResult newResult = new GameDTOs.GuessResult(GameDTOs.GuessStatus.VALID, results,
                validation.reason() );
        // game status uses a wither update based on the original game DTO, returns a new GameStatus record
        GameStatus newStatus = game.withGameUpdates(gameOver, userWon, numGuesses);
        return new GuessResponse(newResult, newStatus);
    }
}
