package org.fdu;

// JUnit 5 Basics
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Spring Boot Test Slices
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;

// Web and Session Mocking
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

// Static Imports to allow post() v. MockMvcRequestBuilders.post()
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf; // Added for safety

@WebMvcTest(WordleController.class)
public class WordleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    public void testResetCreatesNewGameInSession() throws Exception {
        // Execute the POST request to /api/wordle/reset, verify CREATED http status returned
        mockMvc.perform(post("/api/wordle/reset")
                        .session(session))
                .andExpect(status().isCreated());

        // Verify that the session now contains a "game" object
        Object game = session.getAttribute("game");
        assertNotNull(game, "Session should contain a 'game' attribute after reset");
        assert(game instanceof WordleGame);
    }
}
