package org.fdu;

/**
 * GuessValidation Class
 * Applies validation rules including length (exactly 5 characters), allowable characters (a-z and A-Z) <br>
 * Normalizes the guess (removes leading and trailing white space) and shifts all characters to UPPERCASE
 * </p>
 * WORD_LENGTH - defines the length for guesses and secret words
 */
public final class GuessValidation {
    public GuessValidation() {};   // empty constructor - nothing for now

    /** secret word lencth, # of ASCII characters */
    public static final int WORD_LENGTH = 5;

    /**
     * Validate if guess meets the game requirements (e.g. exactly 5 characters, all alphabetic a-z)
     *   Note: leading and trailing white space is allowed and does not affect the guess
     * @param userGuess - String entered by user, leading/trailing white space ok, mixed case ok
     * @return true - word meets all game requirements for a valid guess, false - otherwise
     * @deprecated - replaced with validateWord() which returns a reason code in addition to validity
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

    /**
     * removes leading white space and changes all characters to UPPER CASE
     * @param userGuess - word typed in by user, no assumptions made about it being valid
     * @return String without leading whitespace and in CAPs
     */
    public static String normalizeWord(String userGuess) {
        return userGuess.trim().toUpperCase();
    }

    /**
     * Record encapsulating results of a Wordle word validation including validity and enum reason details.
     * @param isValid - true if guess is valid, otherwise false (see reason for details)
     * @param reason - details for guess validity, e.g. TOO_SHORT, or even VALID, if null - return UNKNOWN
     */
    public record ValidationResult(boolean isValid, ValidationReason reason) {
        public ValidationResult {
            if (reason == null) reason = ValidationReason.UNKNOWN;
        }
    }

    /**
     * refactored version of isWordValid() to return reason codes if guess doesn't meet game criteria
     * @param userGuess - trimmed and then checked for length and contents (alpha only)
     * @return - two fields, boolean - true (valid), false (invalid); ValidationReason (e.g. TOO_SHORT)
     */
    public static ValidationResult validateWord(String userGuess) {
        String localUserGuess = userGuess.trim();   // trim leading and trailing white space
        ValidationResult result;

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
     * ValidationReason includes: enum and code (ie ASCII string for the enum)
     *   Currently - enum ASCII = enum.name()
     *   ValidationReason constructor - creates each object and populates the String code
     *   getReasonString() - returns String associated with the enum object
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
}


