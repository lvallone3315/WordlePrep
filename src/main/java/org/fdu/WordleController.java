package org.fdu;

import org.fdu.WordleGame;
import org.fdu.GameDTOs.*;
import jakarta.servlet.http.HttpSession;

// used to return specific status
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


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
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String COOKIE_NAME = "wordle_state";
    private static final int ONE_HOUR = 60 * 60;    // set cookie expiration to one hour

    // Spring injects the single instance here
    public WordleController(WordleGame wordleGame) {
        this.wordleGame = wordleGame;
    }

    // helper function - get the currently running game, if none, create one
    // no longer used, but maybe we can refactor back to this?
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
    public ResponseEntity<GameStatus> reset(HttpServletResponse response) throws Exception  {
        GameStatus game = wordleGame.createNewGame();
        setGameCookie(response, game);
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
    public GuessResponse guess(@RequestParam String guess,
                               @CookieValue(name = COOKIE_NAME, defaultValue = "") String cookieValue,
                               HttpServletResponse response) throws Exception {

        // if the cookie is empty - start a new game, otherwise pick up where we left off
        GameStatus game = cookieValue.isEmpty()? wordleGame.createNewGame() : deserializeGame(cookieValue);

        GuessResponse guessResponse = wordleGame.processGuess(game, guess);

        GuessResult guessResult = guessResponse.guessResult();
        game = guessResponse.gameStatus();
        setGameCookie(response, game);
        return new GuessResponse(guessResult, game);
    }

    /**
     * Returns the game state (e.g. in-progress/game over, if player won, secret word, ...)
     * @param session
     * @return game state
     */
    @GetMapping("/status")
    public GameStatus getStatus(@CookieValue(name = COOKIE_NAME, defaultValue = "") String cookieValue) {
        GameStatus game = cookieValue.isEmpty()? wordleGame.createNewGame() : deserializeGame(cookieValue);
        // test cases can get all information from WordleGame, but for now, allow access to GameStatus methods
        return game;
    }


    // Helper: Convert object to Base64 JSON string for the cookie
    private String serializeGame(GameStatus game) throws Exception {
        String json = objectMapper.writeValueAsString(game);
        String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        return URLEncoder.encode(base64, StandardCharsets.UTF_8);
    }

    // Helper: Convert Base64 JSON string back to object
    private GameStatus deserializeGame(String cookieValue) {
        try {
            String decodedBase64 = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
            byte[] decodedBytes = Base64.getDecoder().decode(decodedBase64);
            return objectMapper.readValue(decodedBytes, GameStatus.class);
        } catch (Exception e) {
            // can we use our helper function here instead?
            return wordleGame.createNewGame();// Fallback if cookie is tampered with or old
        }
    }

    // Helper: Utility to attach the cookie to the response
    private void setGameCookie(HttpServletResponse response, GameStatus game) throws Exception {
        Cookie cookie = new Cookie(COOKIE_NAME, serializeGame(game));
        cookie.setPath("/");
        cookie.setHttpOnly(true); // Security: prevents JS from stealing the cookie
        cookie.setMaxAge(ONE_HOUR);
        response.addCookie(cookie);
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