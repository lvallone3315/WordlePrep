package org.fdu;

import org.fdu.WordleGame;
import org.fdu.GuessResult;
import org.fdu.GameStatus;
import org.fdu.GuessResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

   /*  @GetMapping("/wordle")
    public String intro() {
        return "Welcome to Wordle (Web Spike)  Version 0.2";
    }
    */

    private WordleGame game = new WordleGame();   // start a new game

    @PostMapping("/reset")
    public void reset() {
        System.out.println("Reset Wordle Game");
        game = new WordleGame();
    }

    @PostMapping("/guess")
    public GuessResponse processGuess(@RequestParam String guess) {
        GuessResult guessResult = game.processGuess(guess);
        GameStatus status = game.getGameStatus();
        return new GuessResponse(guessResult, status);
    }

    @GetMapping("/status")
    public GameStatus getStatus() {
        return game.getGameStatus();
    }
}
