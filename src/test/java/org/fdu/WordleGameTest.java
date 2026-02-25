package org.fdu;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.fdu.GameDTOs.*;


class WordleGameInitializationTest {

    static WordleGame game;

    @BeforeAll
    static void setup () {
        game = new WordleGame();
    }

    @DisplayName ("Create the WordleGame singleton object including generating version #" )
    // does not cover some game version exception cases - e.g. missing property version.properties file
    @Test
    void initialGameStatus() {
        GameStatus gameStatus = game.createNewGame();

        assertFalse(gameStatus.gameOver(), "Initial game state showing game is Over");
        assertFalse(gameStatus.userWon(), "Initial game state showing user already won");
        assertEquals(0, gameStatus.numGuesses(), "Initial game state number remaining guesses not zero");
        assertEquals(6, gameStatus.maxGuesses(), "Initial game state, max user guesses != 6");
        System.out.println("Game Version #: " + gameStatus.gameVersion());
        assertFalse(gameStatus.gameVersion().toLowerCase().contains("unknown"));
        // local versions include snapshot - this may fail for production builds
        // assertTrue(gameStatus.gameVersion().contains("SNAPSHOT"), "SNAPSHOT not in local game version #");
    }
}