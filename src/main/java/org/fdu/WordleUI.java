package org.fdu;

public class WordleUI {
    public WordleUI() {}; // empty constructor

    // write the output message to the console, user must define newlines in message (as needed)
    public void writeMessage(String message) {
        IO.print(message);
    }

    /**
     * Two versions - one with a prompt, one without
     * @return String typed into the console by the user
     */
    public String getUserGuess() {
        return getUserGuess("");
    }
    public String getUserGuess(String prompt) {
        String readln = IO.readln(prompt);
        return readln;
    }

}
