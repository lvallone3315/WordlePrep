package org.fdu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.MediaType;



import org.fdu.GameDTOs.*;

/*
 * Individual tests of each REST API implemented in the game controller
 *   IMPORTANT Note: this test class does NOT preserve session IDs between API calls
 * each API call within a test will have a new session ID and create a new game
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class WordleControllerTest {
    @Autowired
    private RestTestClient restClient;

    @Test
    @DisplayName("Verify Posting a /reset creates a new game, even if one didn't previously exist")
    // happy path first - /reset returns HTTP status of Created and a new game - e.g. guesses = 0
    void resetTest() {
        // @RestController MediaType defaults to JSON - for this first test, make it explicit
        GameStatus game = restClient.post()
                .uri("/api/wordle/reset").accept(MediaType.APPLICATION_JSON)
                .exchange()         // transition from send to receive
                .expectStatus().isCreated()     // HTTP return code - e.g. ok, created, ...
                .expectBody(GameStatus.class)    // what to expect to be returned
                .returnResult()         // return it
                .getResponseBody();     // extract it
        assertThat(game).isNotNull();
        assertThat(game.numGuesses()).isEqualTo(0);
        assertThat(game.maxGuesses()).isEqualTo(WordleRules.MAX_GUESSES);
        assertThat(game.gameVersion()).isNotEqualTo("");
        assertThat(game.secretWord()).isNotBlank();   // alternative to isNotEqualTo("")
        assertThat(game.gameOver()).isFalse();
        assertThat(game.gameVersion()).isNotNull().isNotEmpty();  // we display game version, verify we have one
    }

    @Test
    @DisplayName("Verify POSTing a /guess?user_guess is accepted and the guess count is incremented")
    void firstGuessTest()
    {
        GuessResponse response = restClient.post()
                .uri("/api/wordle/guess?guess=APPLE")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GuessResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(response).isNotNull();
        assertThat(response.gameStatus().numGuesses()).isEqualTo(1);
        assertThat(response.gameStatus().maxGuesses()).isGreaterThanOrEqualTo(1);  // remaining guesses uses maxGuesses, verify it's real
        assertThat(response.gameStatus().gameOver()).isFalse();
    }

    @Test
    @DisplayName("POSTing an empty guess")
    void emptyGuessTest()
    {
        GuessResponse response = restClient.post()
                .uri("/api/wordle/guess?guess=")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GuessResponse.class)
                .returnResult()
                .getResponseBody();
        // response is valid, but should flag as an invalid guess
        assertThat(response).isNotNull();
        assertThat(response.guessResult().guessStatus()).isEqualTo(GameDTOs.GuessStatus.INVALID);
    }

    @Test
    @DisplayName("Verify GET /status returns the current game state")
    void getGameStatusTest() {
        restClient.get().uri("/api/wordle/status").exchange().expectStatus().isOk()
                .expectBody()
                // Drill into the nested GameStatus record, .jsonPath is an alternative, but not preferred
                .jsonPath("$.numGuesses").isEqualTo(0)
                .jsonPath("$.gameOver").isEqualTo(false);

    }

    @Test
    @DisplayName("Check an invalid scenario such as GET a /reset")
    void resetErrorsTest() {
        restClient.get()
                .uri("/api/wordle/reset").accept(MediaType.APPLICATION_JSON)
                .exchange()         // transition from send to receive
                .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        // and checking via direct return code and message
        String responseBody = restClient.get()
                .uri("/api/wordle/reset").accept(MediaType.APPLICATION_JSON)
                .exchange()         // transition from send to receive
                .expectStatus().isEqualTo(405)
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).containsIgnoringCase("not allowed");
    }
}