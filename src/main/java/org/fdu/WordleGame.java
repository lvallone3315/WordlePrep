package org.fdu;

public class WordleGame {


    private final WordleDictionary wordleDictionary = new WordleDictionary();
    private final GuessValidation guessValidation = new GuessValidation();
    private final GuessEvaluation guessEval = new GuessEvaluation();

    private final static int MAX_GUESSES = 7;

    private String secretWord;
    private int numGuessesTaken = 0;
    private boolean gameOver = false;
    private boolean userWon = false;


    /**
     * Two constructors
     *   WordleGame() - selects a word from the dictionary (standard game play constructor)
     *   WordleGame(String) - sets the secret word to the selected string (for testing)
     */
    public WordleGame() {
        secretWord = wordleDictionary.pickNewWord();
    }
    public WordleGame(String secretWord) {
        this.secretWord = secretWord;
    }

    /**
     * these two methods will ultimately be unnecessary, but for now ..
     * @return secret word
     */
    public String getSecretWord() {
        return secretWord;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     *   check if guess is valid,
     *   normalize the guess, all caps and trimmed whitespace
     *   evaluate the guess and return the color codes (enums) for each character in the results[]
     *   if user won - set the userWon flag
     *   if game over - set the gameOver flag  (userWon and gameOver retrieved from getGameStatus())
     * @return GuessResult - data class with: VALID/INVALID guess, color coded result enums
     */
    public GuessResult processGuess(String userGuess) {
        // ToDo: add check here for game over - e.g. add another GuessStatus enum = GameOver
        if (gameOver) {
            return new GuessResult(GuessResult.GuessStatus.INVALID, null);
        }
        // System.out.println("Secret Word: " + secretWord + "User Guess: " + userGuess);
        if (!guessValidation.isWordValid(userGuess)) {
            return new GuessResult(GuessResult.GuessStatus.INVALID, null);
        }
        //   user guess is valid
        //     update guess counter, normalize the word (e.g. caps, no whitespace), check guess to secret word
        numGuessesTaken++;
        String normalizedUserGuess = guessValidation.normalizeWord(userGuess);
        GuessEvaluation.Result[] results = guessEval.evaluateGuess(normalizedUserGuess, secretWord);

        // 4 scenarios - game isn't over - no message
        //   game over & user won - userWon & gameOver = true, message WINNER
        //   game over & user lost - gameOver = true, userWon = false, message LOSER
        if (guessEval.isGuessCorrect(normalizedUserGuess, secretWord)) {
            gameOver = true;
            userWon = true;
        }
        else if (isUserOutOfGuesses()) {
            gameOver = true;
            userWon = false;  // unnecessary, but makes it clear
        }  // else - game remains in progress

        // return the results and game status for display
        return new GuessResult(GuessResult.GuessStatus.VALID, results);
    }

    /**
     * Status is calculated when processing the user's guess
     * @return GameStatus - is game over (true/false), has user won (true/false), secret word for display
     *   and a message to display to the user
     */
    public GameStatus getGameStatus() {
        return new GameStatus(gameOver, userWon, secretWord, numGuessesTaken, MAX_GUESSES);
    }

    public boolean isUserOutOfGuesses() {
        return getNumGuessesTaken() >= MAX_GUESSES;
    }

    /**
     * Parameterize limit on user guesses
     * @return number of guesses user allowed before game is over
     */
    public int getMaxUserGuesses() {
        return MAX_GUESSES;
    }

    /**
     * For testing and future functionality, allow retrieval of # guesses taken
     * @return number of guesses taken
     */
    public int getNumGuessesTaken() {
        return numGuessesTaken;
    }
}
