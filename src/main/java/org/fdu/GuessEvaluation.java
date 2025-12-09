package org.fdu;

import java.util.ArrayList;
import java.util.Arrays;  // to use Arrays.fill utility
import java.util.List;

/**
 * Evaluates the users guess against the secret word (selected from the dictionary)
 * Defines the colors indicating the "correctness" of each character in the user's guess
 * Tracks the number of guesses by the user, initialized by constructor, incremented by evaluater
 *   method to check if user has used all of the allowable guesses
 *
 * class assumes the users guess has been validated prior to calling here
 *   ToDo - throw exception if the userGuess &/or secretWord are not valid
 *   Or at the very least, return all Gray
 */

public class GuessEvaluation {
    // private data declarations
    private int numGuesses = 0;    // tracks # of guesses the user has made
    private static final int guessLimit = 6;

    // Empty constructor for now, in the future may allow configurable word size
    public GuessEvaluation() {
        numGuesses = 0;
    }

    /**
     * Enum defining the valid colors for guess evaluation
     */
    public enum Result  {GREEN, YELLOW, GRAY}

    /**
     * Evaluate the users guess
     *    Returns an array (size of userGuess, should be equal to WORD_LENGTH)
     *    If arguments valid, will increment # user turn count, does not validate (caller responsibility)
     *    Errors: if the userGuess &/or secretWord are not valid (isWordValid()) return all GRAY
     *       ToDo: throw an exception instead - since it is expected userGuess is validated prior to calling
     *
     * @param userGuess - string containing the user guess, validate prior to calling
     * @param secretWord - string with dictionary word, assumed valid
     * @return - array of Result enums (colors), the size of the userGuess
     */
    public Result[] evaluateGuess(String userGuess, String secretWord) {
        // default results - all Gray - rather than hardcoding, use size of userGuess
        Result[] resultArray = new Result[userGuess.length()];
        Arrays.fill(resultArray, Result.GRAY);

        // protect ourselves - validate both the userGuess & the secretWord
        if (!GuessValidation.isWordValid(userGuess) ||  !GuessValidation.isWordValid(secretWord)) return resultArray;

        // update guess count - if we got here, have a valid guess
        numGuesses++;

        /*
         * approach: loop through each character in user guess,
         *   exact placement match comparisons use: userGuess and secretWord Strings
         *   if in secretWord list, remove the letter from the local list (ie handles multiples)
         *   e.g. if userGuess has two As, and secretWord only one, 1st check will remove, 2nd A won't
         *     be in the remaining chars, so will be flagged as not found (GRAY)
         */
        // store the secret word characters in a locally defined list
        List<Character> letters = new ArrayList<Character>();
        for  (char ch : secretWord.toCharArray()) {
            letters.add(ch);
        }

        // walk through the guess
        //    if character in same location in secret word array
        //       Result[letter location] = Green & delete that character at that location
        //    if not and character in secret word list
        //       Result[letter location] = Yellow & delete character from list
        //    else
        //       Result[letter location] = Gray (no deletion)
        for (int itr = 0; itr < userGuess.length(); itr++) {
            // System.out.println("Secret Word List is: " + letters);
            if (userGuess.charAt(itr) == secretWord.charAt(itr)) {
                resultArray[itr] = Result.GREEN;
                letters.remove((Object) userGuess.charAt(itr));  // this may be a problem - possible to remove wrong letter?
            }
            else if (letters.remove((Object) userGuess.charAt(itr))) {
                resultArray[itr] = Result.YELLOW;
            }
            else resultArray[itr] = Result.GRAY;
        }

        return resultArray;  // done - return the results
    }  // end evaluateGuess

    /**
     *  check if user has used all of their guesses
     * @return - true - out of guesses
     */
    public boolean isUserOutOfGuesses() {
        return numGuesses >= guessLimit;
    }
}
