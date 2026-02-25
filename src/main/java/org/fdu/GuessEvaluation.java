package org.fdu;

import java.util.ArrayList;
import java.util.Arrays;  // to use Arrays.fill utility
import java.util.List;

/**
 * Utility to evaluate the users guess against the secret word (selected from the dictionary).<br>
 * Defines the colors indicating the "correctness" of each character in the user's guess. <br>
 *
 * class assumes the users guess are validated prior to calling here <br>
 *   In current implementation: if the userGuess is invalid (e.g. >5 chars), an all GRAY result is returned <br>
 *   ToDo - throw exception if the userGuess and/or secretWord are not valid
 *
 *  @author Lee V
 *  @version v1.0.0
 */

public class GuessEvaluation {



    // Empty constructor for now, in the future may allow configurable word size
    private GuessEvaluation() {
    }

    /**
     * Enum defining the valid colors for guess evaluation
     */
    public enum Result  {
        /** letter in secret word and in same position */
        GREEN,
        /** letter in secret word, in a different position */
        YELLOW,
        /** letter not in secret word */
        GRAY }

    /**
     * Evaluate the users guess and returns the results, does <b>NOT</b> update the game state<br>
     *    Expects {@code userGuess} to be validated by {@link GuessValidation} prior to calling<br>
     *      - Note: No check if game is won (separate method)<br>
     *    Errors: if the userGuess and/or secretWord are not valid return all GRAY<br>
     *       ToDo: throw an exception instead - since it is expected userGuess is validated prior to calling
     * <p>
     * <b>Algorithm Approach:</b>
     * <ol>
     * <li><b>Phase 1:</b> Identify exact matches ({@link Result#GREEN}). Matching letters are
     * removed from a local pool to handle duplicate letters correctly.</li>
     * <li><b>Phase 2:</b> Identify misplaced matches ({@link Result#YELLOW}) from the
     * remaining letter pool. Otherwise, the result is {@link Result#GRAY}.</li>
     * </ol>
     *
     * @param userGuess string containing the user guess, normalize and validate prior to calling
     * @param secretWord string with dictionary word to compare against (assumed valid)
     * @return - array of {@link Result} enums representing color of each letter (size of the userGuess)
     */
    public static Result[] evaluateGuess(String userGuess, String secretWord) {
        // default results - all Gray - rather than hardcoding, use size of userGuess
        Result[] resultArray = new Result[userGuess.length()];
        Arrays.fill(resultArray, Result.GRAY);

        // validate both the userGuess & the secretWord (hopefully validated prior to calling)
        if (!GuessValidation.validateWord(userGuess).isValid() ||
                !GuessValidation.validateWord(secretWord).isValid()) return resultArray;

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
                letters.remove((Object) userGuess.charAt(itr));
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
     * Checks exact match (letter and position) of user's guess and secret word.  If match, player has won.
     * @param userGuess - normalized (case and whitespace) user word
     * @param secretWord - dictionary word to be compared with the userGuess word
     * @return true - all characters match position, false otherwise
     */
    public static boolean isGuessCorrect(String userGuess, String secretWord) {
        if (!GuessValidation.validateWord(userGuess).isValid()
                ||  !GuessValidation.validateWord(secretWord).isValid())
            return false;  // validity check before proceeding
        for (int itr = 0; itr < secretWord.length(); itr++) {
            if (userGuess.charAt(itr) != secretWord.charAt(itr))
                return false;
        }
        return true; // all characters matched - user WON
    }  // end isGuessCorrect()
}
