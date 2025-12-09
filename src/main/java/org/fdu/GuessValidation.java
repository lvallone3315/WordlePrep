package org.fdu;
/**
 * GuessValidation Class
 * Codes validation rules including length (exactly 5 characters), allowable characters (a-z and A-Z)
 * Normalizes the guess (removes leading and trailing white space), shifts all characters to UPPERCASE
 *
 * WORD_LENGTH - defines the length for guesses and secret words
 */
public final class GuessValidation {
    public GuessValidation() {};   // empty constructor - nothing for now

    public static final int WORD_LENGTH = 5;

    /**
     * Validate if guess meets the game requirements (e.g. exactly 5 characters, all alphabetic a-z)
     *   Note: leading and trailing white space is allowed and does not affect the guess
     * @param userGuess
     * @return true - word meets all game requirements for a valid guess, false - otherwise
     */
    public static boolean isWordValid(String userGuess) {
        String localUserGuess = userGuess.trim();  // trim leading and trailing white space

        if (localUserGuess.length() != WORD_LENGTH) return false;  // first trim
        for (int i = 0; i < localUserGuess.length(); i++) {
            if (!Character.isLetter(localUserGuess.charAt(i))) {
                return false;
            }
        }
        return true;  // already checked length - all else looks valid
    }

    /**
     * removes leading white space and changes all characters to UPPER CASE
     * @param userGuess - word typed in by user, no assumptions made about it being valid
     * @return String without leading whitespace and in CAPs
     */
    public static String normalizeWord(String userGuess) {
        return userGuess.trim().toUpperCase();
    }
}
