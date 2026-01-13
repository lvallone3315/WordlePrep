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

// JsonPath (Used for the "Cheat" logic)
import com.jayway.jsonpath.JsonPath;

// Static Imports (These remain consistent)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        // 1. Reset (Starts real game with a real random word)
        MvcResult result = mockMvc.perform(post("/api/wordle/reset").session(session))
                .andExpect(status().isCreated())
                .andReturn();

        // 2. Extract the secret word from the response to "cheat" for the test
        String json = result.getResponse().getContentAsString();
        String secretWord = JsonPath.read(json, "$.secretWord");

        // 3. Make a real guess using the real service logic
        mockMvc.perform(post("/api/wordle/guess")
                        .session(session)
                        .param("guess", secretWord))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus.userWon").value(true))
                .andExpect(jsonPath("$.gameStatus.gameOver").value(true));
    }
}
