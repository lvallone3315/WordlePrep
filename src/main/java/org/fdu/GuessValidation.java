package org.fdu;

/**
 * Provides validation rules for word guesses.
 * <p>
 * Class is stateless and intended to be used as a utility. <br>
 *  No public constructor is provided. <br>
 * It enforces rules such as {@link #WORD_LENGTH} and alphabetical constraints (a-z and A-Z) <br>
 * <p>
 * {@link #normalizeWord(String)} removes leading and trailing white space) and shifts all characters to UPPERCASE <p>
 * <p>
 * WORD_LENGTH - defines the length for guesses and secret words for the entire project (public, static)
 *
 * @author Lee V
 * @version v1.0.0
 */
public final class GuessValidation {

    /** Required length for guesses and secret words (ASCII characters), current value is {@value} */
    public static final int WORD_LENGTH = 5;

    /** private empty constructor, class intended to be used static **/
    private GuessValidation() {}

    /**
     * removes leading and trailing white space and changes all characters to UPPER CASE
     * @param userGuess - word typed in by user, no assumptions made about it being valid
     * @return String without leading and trailing whitespace and in CAPs
     */
    public static String normalizeWord(String userGuess) {
        return userGuess.trim().toUpperCase();
    }


    /**
     * Record (immutable) encapsulating results of word validation including validity and enum reason details. <br>
     *   returned by validateWord() method
     * @param isValid - true if guess is valid, otherwise false (see reason for details)
     * @param reason - details for guess validity, e.g. TOO_SHORT, or even VALID, if null - return UNKNOWN
     */
    public record ValidationResult(boolean isValid, ValidationReason reason) {
        public ValidationResult {
            if (reason == null) reason = ValidationReason.UNKNOWN;
        }
    }

    /**
     * refactored version of {@link #isWordValid(String)} to return reason codes if guess doesn't meet game criteria
     * @param userGuess - trimmed and then checked for length and contents (alpha only)
     * @return - two fields, boolean - true (valid), false (invalid); ValidationReason (e.g. TOO_SHORT)
     */
    public static ValidationResult validateWord(String userGuess) {
        String localUserGuess = userGuess.trim();   // trim leading and trailing white space

        // check length - must be an exact match, ie too long is no good either
        if (localUserGuess.length() != WORD_LENGTH){
            return new ValidationResult(false, ValidationReason.INVALID_LENGTH);
        }

        // verify alphabetic, ie a-z or A-Z, embedded blanks are invalid
        for (int i = 0; i < localUserGuess.length(); i++) {
            if (!Character.isLetter(localUserGuess.charAt(i))) {
                return new ValidationResult(false, ValidationReason.NON_ALPHA);
            }
        }
        // if we got here, met all requirements, return valid
        return new ValidationResult(true, ValidationReason.VALID);  //
    }

    /**
     * ValidationReason enum: includes explicit ASCII string for the enum <br>
     *   Currently - enum ASCII = enum.name() <br>
     *   ValidationReason constructor - creates each object and populates the String code <br>
     *   getReasonString() - returns String associated with the enum object <br>
     *   getReasonEnum() - returns enum object associated with the string, UNKNOWN enum if string not valid
     */
    public enum ValidationReason {
        VALID("VALID"),
        INVALID_LENGTH("INVALID_LENGTH"),
        NON_ALPHA("NON_ALPHA"),
        GAME_OVER("GAME_OVER"),
        UNKNOWN("UNKNOWN");

        private final String code;
        ValidationReason(String code) { this.code = code;}

        public String getReasonString() {
            return this.code;
        }

        /**
         * Given a reason string (e.g. why validation failed), convert to an enum
         * @param reasonString - ASCII representation of a reason code enum
         * @return - reason code enum
         */
        public static ValidationReason getReasonEnum(String reasonString) {
            for (ValidationReason reason : ValidationReason.values()) {
                if (reason.code.equals(reasonString)) {
                    return reason;
                }
            }
            return UNKNOWN;
        }
    }    // end ValidationReason enum

    /**
     * Validate if guess meets the game requirements (e.g. exactly 5 characters, all alphabetic a-z)
     *   Note: leading and trailing white space is allowed and does not affect the guess
     * @param userGuess - String entered by user, leading/trailing white space ok, mixed case ok
     * @return true - word meets all game requirements for a valid guess, false - otherwise
     * @deprecated - replaced with {@link #validateWord(String)} which returns a reason code in addition to validity
     */
    @Deprecated (since = "2025-Jan")
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
}


