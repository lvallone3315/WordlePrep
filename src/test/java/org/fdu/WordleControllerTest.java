package org.fdu;

// JUnit 5 Basics
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Spring Boot Test Slices
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;

// Web and Session Mocking
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;

// Static Imports to allow post() v. MockMvcRequestBuilders.post()
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(WordleController.class)
public class WordleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        // Consider - Pre-populate the session so the game exists before the first hit
        //   though, this means we don't test out the empty container scenario
        // session.setAttribute("game", new WordleGame());
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

    @Test
    public void showInitialGameStatus() throws Exception {
        // game start - should show 0 guesses, and no one has won
        mockMvc.perform(get("/api/wordle/status")
                        .session(session))
                .andExpect(status().isOk())
                // $.numGuesses looks for the field in the JSON response
                .andExpect(jsonPath("$.numGuesses").value(0))
                .andExpect(jsonPath("$.gameOver").value(false))
                .andExpect(jsonPath("$.userWon").value(false));
        WordleGame game = (WordleGame) session.getAttribute("game");
        printGameStatus(game);
    }

    @Test
    public void testFiveLetterGuess() throws Exception {
        // 1. Reset the game to ensure it's fresh
        mockMvc.perform(post("/api/wordle/reset").session(session));

        // 2. Make a guess and capture the result
        mockMvc.perform(post("/api/wordle/guess")
                        .session(session)
                        .param("guess", "WORDS"))
                .andExpect(status().isOk())
                .andDo(print()) // prints the JSON to the console showing all private fields
                .andExpect(jsonPath("$.gameStatus.numGuesses").value(1));

        // 3. Verify the session object was actually updated
        WordleGame game = (WordleGame) session.getAttribute("game");
        assertNotNull(game);
        // Traditional JUnit test, redundant to .andExpect above
        assertEquals(1, game.getNumGuessesTaken());
    }

    @Test
    public void winOnFirstGuess() throws Exception {
        mockMvc.perform(post("/api/wordle/reset").session(session));
        WordleGame game = (WordleGame) session.getAttribute("game");
        assertNotNull(game);
        String secretWord = game.getSecretWord();
        // "Guess" the actual secret word
        mockMvc.perform(post("/api/wordle/guess")
                        .session(session)
                        .param("guess", secretWord))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.gameStatus.gameOver").value(true))
                .andExpect(jsonPath("$.gameStatus.userWon").value(true))
                .andExpect(jsonPath("$.gameStatus.numGuesses").value(1));
    }

    @Test
    public void loserTest() throws Exception {
        mockMvc.perform(post("/api/wordle/reset").session(session));
        WordleGame game = (WordleGame) session.getAttribute("game");
        assertNotNull(game);
        // start iterating at one - allows direct comparison with numGuesses
        for (int itr = 1; itr < game.getMaxUserGuesses();  itr++) {
            mockMvc.perform(post("/api/wordle/guess")
                            .session(session)
                            .param("guess", "WRONG"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.gameStatus.numGuesses").value(itr))
                    .andExpect(jsonPath("$.gameStatus.userWon").value(false))
                    .andExpect(jsonPath("$.gameStatus.gameOver").value(false));
        }
        // last guess - game should be over, and user has NOT won
        mockMvc.perform(post("/api/wordle/guess")
                        .session(session)
                        .param("guess", "WRONG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus.numGuesses").value(game.getMaxUserGuesses()))
                .andExpect(jsonPath("$.gameStatus.userWon").value(false))
                .andExpect(jsonPath("$.gameStatus.gameOver").value(true));
    }

    // Another approach - set-up the game outside the http requests
    @Test
    public void testStatusReflectsInjectedState() throws Exception {
        WordleGame game = new WordleGame();
        game.processGuess("APPLE");
        session.setAttribute("game", game);
        // now verify game is still in progress after one guess
        mockMvc.perform(get("/api/wordle/status").session(session))
                .andExpect(jsonPath("$.numGuesses").value(1))
                .andExpect(jsonPath("$.gameOver").value(false));
    }



    // helper function to print gameStatus
    void printGameStatus(WordleGame game) {
        System.out.println("numGuesses: " + game.getNumGuessesTaken() +
                "\tmaxGuesses: " + game.getMaxUserGuesses() +
                "\tgameOver: " + game.isGameOver() +
                "\tuserWon: " + game.didUserWin() +
                "\tsecretWord: " + game.getSecretWord());
    }
}
