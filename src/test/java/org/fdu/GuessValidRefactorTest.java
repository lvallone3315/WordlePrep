package org.fdu;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GuessValidRefactorTest {

    static GuessValidation validator;

    @BeforeAll
    static void setup() {
        validator = new GuessValidation();
    }

    @org.junit.jupiter.api.Test
    void isWordValid() {

        System.out.println("define invalid and valid guesses, trailing V marks test cases expected to pass");
        System.out.println("  Valid:  five alphabetic chars, case insensitive, leading & trailing white space ignored");
        System.out.println("  Invalid: Numerics, control characters, special characters (e.g. hyphens, etc.), " +
                "white space between letters");
        System.out.println("All guesses return a record including Valid/Invalid and reasons");

        String sixChars = "abcdef";
        String fiveMixedCaseV = "xYZaB";
        String fourChars = "LMNO";
        String numeric = "AB1CD";
        String nonAlpha = "A@_32";
        String blank = "";
        String allLowerV = "hello";
        String allUpperV = "ABCDE";
        String leadingWhiteV = "  PLATE  ";
        String inBetweenWhite = "SH EP";

        System.out.println("Example tests: ");
        System.out.print(sixChars + " ");  System.out.print(fiveMixedCaseV + " ");
        System.out.print(numeric + " "); System.out.println(nonAlpha);

        assertFalse(validator.validateWord(sixChars).isValid(), "six char guess, should be invalid");
        assertEquals(validator.validateWord(sixChars).reason(), GuessValidation.ValidationReason.TOO_SHORT);
        assertTrue(validator.validateWord(fiveMixedCaseV).isValid(), "mixed case, 5 letters, should be valid");
        assertEquals(validator.validateWord(fiveMixedCaseV).reason(), GuessValidation.ValidationReason.VALID);
        assertFalse(validator.validateWord(fourChars).isValid(), "four chars, should be invalid");
        assertEquals(validator.validateWord(fourChars).reason(), GuessValidation.ValidationReason.TOO_SHORT);
        assertFalse(validator.validateWord(numeric).isValid(), "numeric, should be invalid");
        assertEquals(validator.validateWord(numeric).reason(), GuessValidation.ValidationReason.NON_ALPHA);
        assertTrue(validator.validateWord(allUpperV).isValid(), "upper case, 5 letters, should be valid");
        assertFalse(validator.validateWord(nonAlpha).isValid(), "non-alphabetic chars, should be invalid");
        assertEquals(validator.validateWord(nonAlpha).reason(), GuessValidation.ValidationReason.NON_ALPHA);
        //  all blanks returned as too short
        assertFalse(validator.validateWord(blank).isValid(), "blank, should be invalid");
        assertEquals(validator.validateWord(blank).reason(), GuessValidation.ValidationReason.TOO_SHORT);
        assertTrue(validator.validateWord(allLowerV).isValid(), "lower case, 5 letters, should be valid");
        assertTrue(validator.validateWord(leadingWhiteV).isValid(), "leading & trailing white space, 5 letters, should be valid");
        assertFalse(validator.validateWord(inBetweenWhite).isValid(), "white space in middle of chars, 5 letters, should be invalid");
        assertEquals(validator.validateWord(inBetweenWhite).reason(), GuessValidation.ValidationReason.NON_ALPHA);
    }
}
