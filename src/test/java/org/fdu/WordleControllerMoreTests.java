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

import org.fdu.GameDTOs.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class WordleControllerMoreTests {
    @Autowired
    private RestTestClient restClient;

    private RestTestClient userA;
    private RestTestClient userB;

    @BeforeEach
    void setUp() {
        // Build specific clients with separate sessions, otherwise springboot creates new sessions for each call
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
    @DisplayName("Basic gameplay flow works")
    void basicGameFlowTest() {

        userA.post().uri("/api/wordle/reset").exchange();

        userA.post()
                .uri("/api/wordle/guess?guess=APPLE")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GuessResponse.class)
                .value(r -> System.out.println("After guess: " + r.gameStatus().numGuesses())
                );

        userA.get()
                .uri("/api/wordle/status")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameStatus.class)
                // the following is a lambda version - see firstGuessTest() for full version - .returnResult() ...
                .value(status -> {
                    assertThat(status.numGuesses()).isEqualTo(1);
                    assertThat(status.gameOver()).isFalse();
                });  // end .uri ()

        // .expectBody(GameStatus.class) is equiv GameStatus status objectMapper.readValue(responseBody, GameStatus.class)
        // .value(...) is equiv public GameStatus BodySpec<GameStatus> value(Consumer<GameStatus> consumer)
        // (status ...) is equiv to (GameStatus status) -> {assertThat(status.numGuesses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Verify a first guess is accepted without creating a new game, controller auto-creates a new game")
    void basicGuessTest() {
        userA.post()
                .uri("/api/wordle/guess?guess=APPLE")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GuessResponse.class)
                // the following is a lambda version - see firstGuessTest() for full version - .returnResult() ...
                .value(response -> {
                    assertThat(response.gameStatus().numGuesses()).isEqualTo(1);
                    assertThat(response.gameStatus().gameOver()).isFalse();
                });
    }

    @Test
    @DisplayName("Verify again first guess, no reset - same test without using lambdas")
    void basicGuessCheckResponse() {
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
}