package org.fdu;

import static org.junit.jupiter.api.Assertions.*;

class GuessValidationTest {

    @org.junit.jupiter.api.Test
    void isWordValid() {
        GuessValidation validator = new GuessValidation();
        // define a few invalid and valid guesses, trailing V marks test cases expected to pass
        // Valid:  five alphabetic chars, case insensitive, leading & trailing white space ignored
        // Invalid: Numerics, control characters, special characters (e.g. hyphens, etc.), white space between letters
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
    }
}