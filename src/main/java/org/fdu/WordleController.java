package org.fdu;

import org.fdu.WordleGame;
import org.fdu.GameDTOs.*;
import jakarta.servlet.http.HttpSession;

// used to return specific status
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

/**
 * REST APIs to the Web UI interface (e.g. html and js)
 * RestController annotation - directs method return values to be converted to JSON/XML
 * RequestMapping - routes incoming http requests to specific controller methods
 *    class level defines a base path to all endpoints
 */
@RestController
@RequestMapping("/api/wordle")
public class WordleController {
    private final WordleGame wordleGame;

    // Spring injects the single instance here
    public WordleController(WordleGame wordleGame) {
        this.wordleGame = wordleGame;
    }

    // helper function - get the currently running game, if none, create one
    private GameStatus getGame(HttpSession session) {
        GameStatus game = (GameStatus) session.getAttribute("game");
        if (game == null) {
            game = wordleGame.createNewGame();
            session.setAttribute("game", game);
        }
        return game;
    }

    /**
     * reset - creates a new wordle game instance
     * @param session
     */
    @PostMapping("/reset")
    public ResponseEntity<GameStatus> reset(HttpSession session) {
        GameStatus game = wordleGame.createNewGame();
        session.setAttribute("game", game);
        // Explicitly set the 201 Created status, default is 200 OK status
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    /**
     * Evaluate the users guess, and return the outcome of the guess itself and game changes
     * @param guess
     * @param session
     * @return guessResponse including color coded letters and game status (e.g. over, winner)
     */
    @PostMapping("/guess")
    public GuessResponse guess(@RequestParam String guess, HttpSession session) {
        GameStatus game = getGame(session);
        GuessResponse guessResponse = wordleGame.processGuess(game, guess);
        GuessResult guessResult = guessResponse.guessResult();
        game = guessResponse.gameStatus();
        session.setAttribute("game", game);
        return new GuessResponse(guessResult, game);
    }

    /**
     * Returns the game state (e.g. in-progress/game over, if player won, secret word, ...)
     * @param session
     * @return game state
     */
    @GetMapping("/status")
    public GameStatus getStatus(HttpSession session) {
        GameStatus game = getGame(session);
        // test cases can get all information from WordleGame, but for now, allow access to GameStatus methods
        session.setAttribute("game", game);
        return game;
    }

    /*
     * below was the starter code for this method, displaying Welcome on the brownser window
     * demonstrating the html/js and controller were up and running
     * @GetMapping("/wordle")
     * public String intro() {
     *    return "Welcome to Wordle (Web Spike)  Version 0.2";
     * }
     */
}