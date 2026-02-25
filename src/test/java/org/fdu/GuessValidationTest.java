package org.fdu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.fdu.GuessValidation.ValidationReason.*;

class GuessValidationTest {

    @Test
    void isWordValid() {

        System.out.println("*** To Be DEPRECATED ***");
        System.out.println("define invalid and valid guesses, trailing V marks test cases expected to pass");
        System.out.println("  Valid:  five alphabetic chars, case insensitive, leading & trailing white space ignored");
        System.out.println("  Invalid: Numerics, control characters, special characters (e.g. hyphens, etc.), " +
                        "white space between letters");

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

        // ToDo: missing test case for control chars
        assertFalse(GuessValidation.isWordValid(sixChars), "six char guess, should be invalid");
        assertTrue(GuessValidation.isWordValid(fiveMixedCaseV), "mixed case, 5 letters, should be valid");
        assertFalse(GuessValidation.isWordValid(fourChars), "four chars, should be invalid");
        assertFalse(GuessValidation.isWordValid(numeric), "numeric, should be invalid");
        assertTrue(GuessValidation.isWordValid(allUpperV), "upper case, 5 letters, should be valid");
        assertFalse(GuessValidation.isWordValid(nonAlpha), "non-alphabetic chars, should be invalid");
        assertFalse(GuessValidation.isWordValid(blank   ), "blank, should be invalid");
        assertTrue(GuessValidation.isWordValid(allLowerV), "lower case, 5 letters, should be valid");
        assertTrue(GuessValidation.isWordValid(leadingWhiteV), "leading & trailing white space, 5 letters, should be valid");
        assertFalse(GuessValidation.isWordValid(inBetweenWhite), "white space in middle of chars, 5 letters, should be invalid");

    }

    @Test
    void normalizeWord() {
        System.out.println("*** verify leading and trailing white space is removed");
        System.out.println("  & all characters are converted to uppercase");

        String leadingWhitePre = "   Shuffling";
        String leadingWhitePost = "SHUFFLING";
        String trailingWhitePre = "mADNESS   ";
        String trailingWhitePost = "MADNESS";
        String leadingTrailingWhitePre = "\tLoCoM\t\n";
        String leadingTrailingWhitePost = "LOCOM";
        String midWordWhitePre = "Bre ath";
        String midWordWhitePost = "BRE ATH";
        assertEquals(leadingWhitePost, GuessValidation.normalizeWord(leadingWhitePre),
                "Leading white space not deleted");
        assertEquals(trailingWhitePost, GuessValidation.normalizeWord(trailingWhitePre),
                "Trailing white space not deleted");
        assertEquals(leadingTrailingWhitePost, GuessValidation.normalizeWord(leadingTrailingWhitePre),
                "Leading &/or trailing white space not deleted");
        assertEquals(midWordWhitePost, GuessValidation.normalizeWord(midWordWhitePre),
                "mid-word white space not handled correctly");
    }

    @Test
    void validateWordTest() {

        System.out.println("*** define invalid and valid guesses, trailing V marks test cases expected to pass");
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

        assertFalse(GuessValidation.validateWord(sixChars).isValid(), "six char guess, should be invalid");
        assertEquals( INVALID_LENGTH, GuessValidation.validateWord(sixChars).reason());
        assertTrue(GuessValidation.validateWord(fiveMixedCaseV).isValid(), "mixed case, 5 letters, should be valid");
        assertEquals(VALID, GuessValidation.validateWord(fiveMixedCaseV).reason());
        assertFalse(GuessValidation.validateWord(fourChars).isValid(), "four chars, should be invalid");
        assertEquals(INVALID_LENGTH, GuessValidation.validateWord(fourChars).reason());
        assertFalse(GuessValidation.validateWord(numeric).isValid(), "numeric, should be invalid");
        assertEquals(NON_ALPHA, GuessValidation.validateWord(numeric).reason());
        assertTrue(GuessValidation.validateWord(allUpperV).isValid(), "upper case, 5 letters, should be valid");
        assertFalse(GuessValidation.validateWord(nonAlpha).isValid(), "non-alphabetic chars, should be invalid");
        assertEquals(NON_ALPHA, GuessValidation.validateWord(nonAlpha).reason());
        //  all blanks returned as too short
        assertFalse(GuessValidation.validateWord(blank).isValid(), "blank, should be invalid");
        assertEquals(INVALID_LENGTH, GuessValidation.validateWord(blank).reason());
        assertTrue(GuessValidation.validateWord(allLowerV).isValid(), "lower case, 5 letters, should be valid");
        assertTrue(GuessValidation.validateWord(leadingWhiteV).isValid(), "leading & trailing white space, 5 letters, should be valid");
        assertFalse(GuessValidation.validateWord(inBetweenWhite).isValid(), "white space in middle of chars, 5 letters, should be invalid");
        assertEquals(NON_ALPHA, GuessValidation.validateWord(inBetweenWhite).reason());
        // Testing a control character (ASCII 7 is "Bell")
        String bellChar = "ABC" + (char)7 + "E";
        assertFalse(GuessValidation.validateWord(bellChar).isValid(), "Control char should be invalid");
        assertEquals(NON_ALPHA, GuessValidation.validateWord(bellChar).reason());
    }

    // the tests below appear to execute correctly, but code coverage not showing the methods (e.g. getReasonString)
    //   as being executed
    @Test
    void getReasonStringTest() {
        System.out.println("*** Testing reason strings for ValidationReason enums, such as: invalid length: " + INVALID_LENGTH.getReasonString());
        System.out.println("INVALID_LENGTH.name() " + INVALID_LENGTH.name() + "\n");

        assertEquals("INVALID_LENGTH", INVALID_LENGTH.getReasonString(), "enum string doesn't match expected value");
        assertEquals("VALID", VALID.getReasonString(), "enum string doesn't match expected value");
        assertEquals("NON_ALPHA",  NON_ALPHA.getReasonString(), "enum string doesn't match expected value");
        assertEquals("GAME_OVER",  GAME_OVER.getReasonString(), "enum string doesn't match expected value");
        assertEquals("UNKNOWN",  UNKNOWN.getReasonString(), "enum string doesn't match expected value");
    }

    @Test
    void getREasonEnumTest() {
        System.out.println("*** Testing mapping of strings to enums match requirements\n");
        assertEquals(VALID, getReasonEnum("VALID"));
        assertEquals(INVALID_LENGTH, getReasonEnum("INVALID_LENGTH"));
        assertEquals(NON_ALPHA, getReasonEnum("NON_ALPHA"));
        assertEquals(GAME_OVER, getReasonEnum("GAME_OVER"));
        assertEquals(UNKNOWN, getReasonEnum("UNKNOWN"));
        assertEquals(UNKNOWN, getReasonEnum("garbage"));
    }
}