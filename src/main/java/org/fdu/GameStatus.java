package org.fdu;

/**
 * Data Class for game status
 * Three data elements:
 *     gameOver - true or false
 *     userWon - true or false
 *     secretWord - string with the word selected from the dictionary
 */
public class GameStatus {
    private boolean gameOver;
    private boolean userWon;
    private String secretWord;
    private int numGuesses;
    private int maxGuesses;

    GameStatus(boolean gameOver, boolean userWon, String secretWord, int numGuesses, int maxGuesses) {
        this.gameOver = gameOver;
        this.userWon = userWon;
        this.secretWord = secretWord;
        this.numGuesses = numGuesses;
        this.maxGuesses = maxGuesses;
    }
    public boolean getGameOver() {
        return gameOver;
    }
    public boolean getUserWon() {
        return userWon;
    }
    public String getSecretWord() {
        return secretWord;
    }
    public int getNumGuesses() { return numGuesses; }
    public int getMaxGuesses() { return maxGuesses; }
}
