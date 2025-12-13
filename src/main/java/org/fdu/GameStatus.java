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

    GameStatus(boolean gameOver, boolean userWon, String secretWord) {
        this.gameOver = gameOver;
        this.userWon = userWon;
        this.secretWord = secretWord;
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
}
