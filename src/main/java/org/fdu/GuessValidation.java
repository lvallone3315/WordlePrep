package org.fdu;
/**
 * GuessValidation Class
 * Codes validation rules including length (exactly 5 characters), allowable characters (a-z & A-Z)
 * Normalizes the guess (removes leading and trailing white space), shifts all characters to UPPERCASE
 *
 * WORD_LENGTH - defines the length for guesses and secret words
 */
public final class GuessValidation {
    public GuessValidation() {};   // empty constructor - nothing for now

    public static final int WORD_LENGTH = 5;

    /**
     * Validate if guess meets the game requirements (e.g. exactly 5 characters, all alphabetic a-z)
     * @param userGuess
     * @return true - word meets all game requirements for a valid guess, false - otherwise
     */
    public static boolean isWordValid(String userGuess) {
        boolean firstNonWhiteSpaceChar = false;
        int wordLength = 0;

        for (int i = 0; i < userGuess.length(); i++) {
            if (!firstNonWhiteSpaceChar && Character.isWhitespace(userGuess.charAt(i))) continue;  // if leading whitespace, ignore
            // if we got here - then we are past leading white space, so just check letters
            firstNonWhiteSpaceChar = true;
            wordLength++;
            if (!Character.isLetter(userGuess.charAt(i))) {
                return false;
            }
        }
        return wordLength == WORD_LENGTH;  // final check - valid (ie true) if wordLength is 5, false otherwise
    }

    /**
     * removes leading white space & changes all characters to UPPER CASE
     * @param userGuess - word typed in by user, no assumptions made about it being valid
     * @return String without leading whitespace & in CAPs
     */
    public static String normalizeWord(String userGuess) {
        return userGuess.trim().toUpperCase();
    }
}
