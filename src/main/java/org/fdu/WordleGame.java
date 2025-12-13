package org.fdu;

public class WordleGame {
    private static final String INVALID_ENTRY = "Invalid entry, please re-enter your guess";
    private static final String WINNER = "\n\nCongratulations!  You are the Wordle Champ of the day";
    private static final String LOSER = "\n\nSorry!  You didn't guess the word, the word was: ";

    private WordleDictionary wordleDictionary = new WordleDictionary();
    private GuessValidation guessValidation = new GuessValidation();
    private GuessEvaluation guessEval = new GuessEvaluation();

    private String secretWord;
    private boolean gameOver = false;
    private boolean userWon = false;
    private String message = "";
    private GuessEvaluation.Result[] results;    // to pass into the UI

    /**
     * Two constructors
     *   WordleGame() - selects a word from the dictionary
     *   WordleGame(String) - sets the secret word to the selected string (for testing)
     */
    WordleGame() {
        secretWord = wordleDictionary.pickNewWord();
    }
    WordleGame(String secretWord) {
        this.secretWord = secretWord;
    }

    /**
     * these two methods will ultimately be unnecessary, but for now ..
     * @return
     */
    public String getSecretWord() {
        return secretWord;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     *   check if guess is valid,
     *   normalize the guess, all caps & trimmed whitespace
     *   evaluate the guess & return the color codes (enums) for each character in the results[]
     *   if user won - set the userWon flag
     *   if game over - set the gameOver flag  (userWon & gameOver retrieved from getGameStatus())
     * @return GuessResult - data class with: VALID/INVALID guess, color coded result enums, userWon flag & userMessage
     */
    public GuessResult processGuess(String userGuess) {
        if (!guessValidation.isWordValid(userGuess)) {
            return new GuessResult(GuessResult.GuessStatus.INVALID, null, INVALID_ENTRY);
        }
        String normalizedUserGuess = guessValidation.normalizeWord(userGuess);
        results = guessEval.evaluateGuess(normalizedUserGuess, secretWord);

        // 4 scenarios - game isn't over - no message
        //   game over & user won - userWon & gameOver = true, message WINNER
        //   game over & user lost - gameOver = true, userWon = false, message LOSER
        if (guessEval.isGuessCorrect(normalizedUserGuess, secretWord)) {
            gameOver = true;
            userWon = true;
            message = WINNER;
        }
        else if (guessEval.isUserOutOfGuesses()) {
            gameOver = true;
            userWon = false;  // unnecessary, but makes it clear
            message = LOSER;
        }  // else - game remains in progress
        return new GuessResult(GuessResult.GuessStatus.VALID, results, "");
    }

    /**
     * Status is calculated when processing the user's guess
     * @return GameStatus - is game over (true/false), has user won (true/false), secret word for display
     *   and a message to display to the user
     */
    public GameStatus getGameStatus() {
        return new GameStatus(gameOver, userWon, secretWord, message);
    }
}
