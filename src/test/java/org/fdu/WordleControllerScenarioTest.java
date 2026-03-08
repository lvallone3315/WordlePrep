package org.fdu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

import org.fdu.GameDTOs.*;
import static org.fdu.GameDTOs.GuessStatus.*;  // guess status enums - e.g. valid and invalid
import static org.fdu.GuessValidation.ValidationReason.*; // failure reason enums - e.g. INVALID_LENGTH, NON_ALPHA

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {   // quiet error messages locally, can change if needed
                "logging.level.root=ERROR",
                "logging.level.org.springframework=ERROR"
        } )
@AutoConfigureRestTestClient

class WordleControllerScenarioTest {
    @Autowired
    private RestTestClient restClient;
    private RestTestClient userA;

    @BeforeEach
    void setUp() {
        // Build specific clients with separate sessions, otherwise springboot creates new sessions for each call
        // .mutate() preserves the server URL and port configuration
        String cookieA = createSessionCookie(restClient);
        userA = restClient.mutate()
                .defaultHeader("Cookie", cookieA)
                .build();
    }

    @Test
    @DisplayName("Start a new game with a single guess sequence")
    void testAGuessSequence() {
        reset(userA);
        GuessResponse response = guess(userA, "APPLE");
        assertThat(response.gameStatus().numGuesses()).isEqualTo(1);

        assertThat(status(userA).numGuesses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Game where first guess is invalid, 2nd valid, 3rd invalid")
    void testInvalidGuessSequence() {
        reset(userA);
        GuessResponse response = guess(userA, "123");  // invalid guess
        // use helper function for game status
        assertGameStatus(response.gameStatus(), 0, false, false);
        assertThat(response.guessResult().guessStatus()).isEqualTo(INVALID);
        assertThat(response.guessResult().guessReason()).isEqualTo(INVALID_LENGTH);

        response = guess(userA, "WoRld");  // valid guess
        assertGameStatus(response.gameStatus(), 1, false, false);
        assertThat(response.guessResult().guessStatus()).isEqualTo(GuessStatus.VALID);
        assertThat(response.guessResult().guessReason()).isEqualTo(GuessValidation.ValidationReason.VALID);

        response = guess(userA, "br8ke");  // invalid guess
        assertGameStatus(response.gameStatus(), 1, false, false);
        assertThat(response.guessResult().guessStatus()).isEqualTo(INVALID);
        assertThat(response.guessResult().guessReason()).isEqualTo(NON_ALPHA);
    }

    @Test
    @DisplayName("Winning on first guess")
    void winOnFirstGuess() {
        GameStatus initial = reset(userA);

        // user wins on first guess, game over and user won both true
        GuessResponse response = guess(userA, initial.secretWord());  // valid guess
        assertGameStatus(response.gameStatus(), 1, true, true);

        response = guess(userA, "WoRld");  // valid guess, but game already over
        assertGameStatus(response.gameStatus(), 1, true, true);
        assertThat(response.guessResult().guessStatus()).isEqualTo(GuessStatus.INVALID);
        assertThat(response.guessResult().guessReason()).isEqualTo(GAME_OVER);
    }

    @Test
    @DisplayName("Winning on last guess")
    void winOnLastGuess() {
        GuessResponse response;
        GameStatus initial = reset(userA);
        int maxGuesses = initial.maxGuesses();

        for (int guess = 1; guess <= maxGuesses-1; guess++) {
            response = guess(userA, "frats");
            assertGameStatus(response.gameStatus(), guess, false, false);
        }
        // winning - both gameOver and userWon should be true
        response = guess(userA, initial.secretWord());  // valid guess
        assertGameStatus(response.gameStatus(), maxGuesses, true, true);
    }

    @Test
    @DisplayName("Failing to guess the word + guesses after game over denied")
    void failureToGuessTheWord() {
        GuessResponse response;
        GameStatus initial = reset(userA);
        int maxGuesses = initial.maxGuesses();

        for (int guess = 1; guess <= maxGuesses-1; guess++) {
            response = guess(userA, "PrePs");   // plurals are valid guesses, but not valid secret words
            assertGameStatus(response.gameStatus(), guess, false, false);
        }
        // losing - gameOver should be true, userWon false
        response = guess(userA, "preps");
        assertGameStatus(response.gameStatus(), maxGuesses, true, false);

        // if game over another guess should be denied (ie invalid)
        response = guess(userA, "PREPS");
        assertGameStatus(response.gameStatus(), maxGuesses, true, false);
        assertThat(response.guessResult().guessStatus()).isEqualTo(GuessStatus.INVALID);
        assertThat(response.guessResult().guessReason()).isEqualTo(GuessValidation.ValidationReason.GAME_OVER);
    }


    /*
     * Helper methods using RestTestClient
     */

    // helper function for setting up the clients -
    private String createSessionCookie(RestTestClient client) {
        var result = client.post().uri("/api/wordle/reset").exchange().returnResult(String.class);
        // Captures "JSESSIONID=XXXXX; Path=/; HttpOnly"
        return result.getResponseHeaders().getFirst("Set-Cookie");
    }

    // helper function for checking game status
    private void assertGameStatus(GameStatus status, int guesses, boolean gameOver, boolean userWon) {
        assertThat(status.numGuesses()).isEqualTo(guesses);
        assertThat(status.gameOver()).isEqualTo(gameOver);
        assertThat(status.userWon()).isEqualTo(userWon);
    }

    //    /reset, /guess, /status
    private GameStatus reset(RestTestClient restClient) {
        return restClient.post()
                .uri("/api/wordle/reset")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GameStatus.class)
                .returnResult()
                .getResponseBody();
    }
    private GuessResponse guess(RestTestClient restClient, String word) {
        return restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/wordle/guess")
                        .queryParam("guess", word)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(GuessResponse.class)
                .returnResult()
                .getResponseBody();
    }
    private GameStatus status(RestTestClient restClient) {
        return restClient.get()
                .uri("/api/wordle/status")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameStatus.class)
                .returnResult()
                .getResponseBody();
    }
}
