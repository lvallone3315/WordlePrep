package org.fdu;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.BeforeAll.*;

class GuessValidationTest {

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
        assertFalse(validator.isWordValid(sixChars), "six char guess, should be invalid");
        assertTrue(validator.isWordValid(fiveMixedCaseV), "mixed case, 5 letters, should be valid");
        assertFalse(validator.isWordValid(fourChars), "four chars, should be invalid");
        assertFalse(validator.isWordValid(numeric), "numeric, should be invalid");
        assertTrue(validator.isWordValid(allUpperV), "upper case, 5 letters, should be valid");
        assertFalse(validator.isWordValid(nonAlpha), "non-alphabetic chars, should be invalid");
        assertFalse(validator.isWordValid(blank), "blank, should be invalid");
        assertTrue(validator.isWordValid(allLowerV), "lower case, 5 letters, should be valid");
        assertTrue(validator.isWordValid(leadingWhiteV), "leading & trailing white space, 5 letters, should be valid");
        assertFalse(validator.isWordValid(inBetweenWhite), "white space in middle of chars, 5 letters, should be invalid");
    }

    @org.junit.jupiter.api.Test
    void normalizeWord() {
        System.out.println("verify leading and trailing white space is removed");
        System.out.println("  & all characters are converted to uppercase");

        String leadingWhitePre = "   Shuffling";
        String leadingWhitePost = "SHUFFLING";
        String trailingWhitePre = "mADNESS   ";
        String trailingWhitePost = "MADNESS";
        String leadingTrailingWhitePre = "\tLoCoM\t\n";
        String leadingTrailingWhitePost = "LOCOM";
        String midWordWhitePre = "Bre ath";
        String midWordWhitePost = "BRE ATH";
        assertEquals(leadingWhitePost, validator.normalizeWord(leadingWhitePre),
                "Leading white space not deleted");
        assertEquals(trailingWhitePost, validator.normalizeWord(trailingWhitePre),
                "Trailing white space not deleted");
        assertEquals(leadingTrailingWhitePost, validator.normalizeWord(leadingTrailingWhitePre),
                "Leading &/or trailing white space not deleted");
        assertEquals(midWordWhitePost, validator.normalizeWord(midWordWhitePre),
                "mid-word white space not handled correctly");
    }
}