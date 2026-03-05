package org.fdu;

import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class WordleControllerTest {
    @Autowired
    private RestTestClient restClient;

    private RestTestClient userA;
    private RestTestClient userB;

    @BeforeEach
    void setUp() {
        // Build specific clients with separate sessions
        // .mutate() preserves the server URL and port configuration
        String cookieA = getSessionCookie(restClient);
        userA = restClient.mutate()
                .defaultHeader("Cookie", cookieA)
                .build();

        String cookieB = getSessionCookie(restClient);
        userB = restClient.mutate()
                .defaultHeader("Cookie", cookieB)
                .build();
    }

    // helper function for setting up the clients -
    private String getSessionCookie(RestTestClient client) {
        var result = client.post().uri("/api/wordle/reset").exchange().returnResult(String.class);
        // Captures "JSESSIONID=XXXXX; Path=/; HttpOnly"
        return result.getResponseHeaders().getFirst("Set-Cookie");
    }

    @Test
    @DisplayName("Verify /reset creates a new game, even if one didn't previously exist")
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
        assertThat(game.maxGuesses()).isEqualTo(6);
        assertThat(game.gameVersion()).isNotEqualTo("");
        assertThat(game.secretWord()).isNotEqualTo("");
        assertThat(game.gameOver()).isFalse();
    }

    @Test
    @DisplayName("Verify a simple guess is accepted - userA has sent a reset first")
    void basicGuessTest()
    {
        userA.post()
                .uri("/api/wordle/guess?guess=APPLE")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Verify a simple guess is accepted - userA has sent a reset first")
    void basicGuessCheckResponse()
    {
        GuessResponse response = userA.post()
                .uri("/api/wordle/guess?guess=APPLE")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GuessResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(response).isNotNull();
        assertThat(response.gameStatus().numGuesses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Verify two clients are unique")
    void testIsolation() {
        // Each uses its own headers/session context
        userA.post().uri("/api/wordle/reset").exchange().expectStatus().isCreated();
        basicGuessTest();   // user A will make a guess
        userA.get().uri("/api/wordle/status").exchange().expectStatus().isOk()
                .expectBody()
                // Drill into the nested GameStatus record
                .jsonPath("$.numGuesses").isEqualTo(1);
        userB.get().uri("/api/wordle/status").exchange().expectStatus().isOk()
                .expectBody()
                // Drill into the nested GameStatus record
                .jsonPath("$.numGuesses").isEqualTo(0);
    }

    @Test
    @DisplayName("Check some invalid scenarios such as GET a /reset")
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