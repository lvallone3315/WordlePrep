package org.fdu;

import java.util.ArrayList;
import java.util.Arrays;  // to use Arrays.fill utility
import java.util.List;

/**
 * Evaluates the users guess against the secret word (selected from the dictionary).
 * Defines the colors indicating the "correctness" of each character in the user's guess.
 * Tracks the number of guesses by the player, initialized to zero by constructor, incremented by guess evaluater.
 *   method available to check if user has used all of the allowable guesses (hardcoded as 6 in GUESS_LIMIT)
 *
 * class assumes the users guess are validated prior to calling here
 *   In current implementation: if the userGuess is invalid (e.g. >5 chars), an all GRAY result is returned
 *   ToDo - throw exception if the userGuess and/or secretWord are not valid
 */

public class GuessEvaluation {

    public static final int GUESS_LIMIT = 6;

    // private data declarations
    private int numGuesses = 0;    // tracks # of guesses the user has made

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
     *    Returns an array (size of userGuess, equal to WORD_LENGTH)
     *    If arguments valid (caller to validate prior), increments user turn count
     *      Note: Does not check if game is won (separate method)
     *    Errors: if the userGuess and/or secretWord are not valid (isWordValid()) return all GRAY
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

        // validate both the userGuess & the secretWord (hopefully validated prior to calling)
        if (!GuessValidation.isWordValid(userGuess) ||  !GuessValidation.isWordValid(secretWord)) return resultArray;

        // update guess count - if we got here, valid guess
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
        //  FIRST phase - exact match of letter & location
        //       Result[letter location] = Green & delete character at that location
        //  SECOND phase - for all non-exact matches
        //    if character in same location in secret word array
        //    if not and character in secret word list
        //       Result[letter location] = Yellow & delete character from list
        //    else
        //       Result[letter location] = Gray (no deletion)
        for (int itr = 0; itr < userGuess.length(); itr++) {
            if (userGuess.charAt(itr) == secretWord.charAt(itr)) {
                resultArray[itr] = Result.GREEN;
                letters.remove((Object) userGuess.charAt(itr));  // this may be a problem - possible to remove wrong letter?
            }
        }
        for (int itr = 0; itr < userGuess.length(); itr++) {
            if (resultArray[itr] != Result.GREEN) {
                if (letters.remove((Object) userGuess.charAt(itr))) {
                    resultArray[itr] = Result.YELLOW;
                }
                else resultArray[itr] = Result.GRAY;
            }// exact matches handled in first phase
        }

        return resultArray;  // done - return the results
    }  // end evaluateGuess


    /**
     *  check if user has used all of their guesses
     * @return - true - out of guesses
     */
    public boolean isUserOutOfGuesses() {
        return numGuesses >= GUESS_LIMIT;
    }


    /**
     * If every letter in secret word matches corresponding guess, player has won
     * @return true - all characters match position, false otherwise
     */
    public boolean isGuessCorrect(String userGuess, String secretWord) {
        if (!GuessValidation.isWordValid(userGuess) ||  !GuessValidation.isWordValid(secretWord))
            return false;  // validity check before proceeding
        for (int itr = 0; itr < secretWord.length(); itr++) {
            if (userGuess.charAt(itr) != secretWord.charAt(itr))
                return false;
        }
        return true; // all characters matched - user WON
    }  // end isGuessCorrect()
}
