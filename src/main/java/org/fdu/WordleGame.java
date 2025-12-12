package org.fdu;

public class WordleGame {
    private WordleDictionary wordleDictionary = new WordleDictionary();
    private GuessValidation guessValidation = new GuessValidation();
    private GuessEvaluation guessEval = new GuessEvaluation();

    private String secretWord;
    private boolean gameOver = false;
    private boolean userWon = false;
    private GuessEvaluation.Result[] results;    // to pass into the UI

    WordleGame() {
        secretWord = wordleDictionary.pickNewWord();
    }

    public String getSecretWord() {
        return secretWord;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GuessResult processGuess(String userGuess) {
        if (!guessValidation.isWordValid(userGuess)) {
            return new GuessResult(GuessResult.GuessStatus.INVALID, null, false, "");
        }
        String normalizedUserGuess = guessValidation.normalizeWord(userGuess);
        results = guessEval.evaluateGuess(normalizedUserGuess, secretWord);
        if (guessEval.isGuessCorrect(normalizedUserGuess, secretWord)) {
            userWon = true;
        }
        return new GuessResult(GuessResult.GuessStatus.VALID, results, userWon, "");
    }
}
