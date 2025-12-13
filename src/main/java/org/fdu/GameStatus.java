package org.fdu;

public class GameStatus {
    private boolean gameOver;
    private boolean userWon;
    private String secretWord;
    private String message;

    GameStatus(boolean gameOver, boolean userWon, String secretWord, String message) {
        this.gameOver = gameOver;
        this.userWon = userWon;
        this.secretWord = secretWord;
        this.message = message;
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
    public String getUserMessage() {
        return message;
    }
}
