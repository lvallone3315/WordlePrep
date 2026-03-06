package org.fdu;

// JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Spring Boot 4.0 Core Test
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

// New Location for MockMvc Autoconfiguration in 4.x
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

// Web Mocking
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

// JsonPath (Used for the "Cheat" logic)
import com.jayway.jsonpath.JsonPath;

// Static Imports (These remain consistent)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

import org.fdu.GameDTOs.*;

// this is not really useful - will deprecate most of these tests in favor of mockMvc

@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    // Use the real game logic instead of a mock
    @Autowired
    private WordleGame wordleGame;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    public void testFullGameFlow() throws Exception {
        // 1. Reset - The server will send back the JSON AND a 'Set-Cookie' header
        MvcResult result = mockMvc.perform(post("/api/wordle/reset")) // .session(session) is no longer needed
                .andExpect(status().isCreated())
                .andExpect(cookie().exists("wordle_state")) // Verify the cookie is actually there
                .andReturn();

        // Get the cookie from the response
        jakarta.servlet.http.Cookie responseCookie = result.getResponse().getCookie("wordle_state");

        // 2. Extract the secret word (the "cheat")
        String json = result.getResponse().getContentAsString();
        String secretWord = JsonPath.read(json, "$.secretWord");

        // 3. Make the guess - You MUST pass the cookie back to the server
        mockMvc.perform(post("/api/wordle/guess")
                        .cookie(responseCookie) // This "restores" the game state
                        .param("guess", secretWord))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus.userWon").value(true))
                .andExpect(jsonPath("$.gameStatus.gameOver").value(true));
    }
}
