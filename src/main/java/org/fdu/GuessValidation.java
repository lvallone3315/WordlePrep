package org.fdu;
/**
 * GuessValidation Class
 * Codes validation rules including length (exactly 5 characters), allowable characters (a-z & A-Z)
 * Normalizes the guess (removes leading and trailing white space), shifts all characters to UPPERCASE
 */
public class GuessValidation {
    public GuessValidation() {};   // empty constructor - nothing for now

    /**
     * Validate if guess meets the game requirements (e.g. exactly 5 characters, all alphabetic a-z)
     * @param userGuess
     * @return true - word meets all game requirements for a valid guess, false - otherwise
     */
    public boolean isWordValid(String userGuess) {
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
        if (wordLength == 5) {
            return true;  // if we got here, all
        } else {
            return false;
        }
    }

    /**
     * removes leading white space & changes all characters to UPPER CASE
     * @param userGuess - word typed in by user, no assumptions made about it being valid
     * @return String without leading whitespace & in CAPs
     */
    public String normalizeWord(String userGuess) {
        String normalizedString = userGuess.trim().toUpperCase();
        return normalizedString;
    }
}
