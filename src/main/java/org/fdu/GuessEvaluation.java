package org.fdu;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates the users guess against the secret word (selected from the dictionary)
 * Defines the colors indicating the "correctness" of each character in the user's guess
 *
 * class assumes the users guess has been validated prior to calling here
 *   ToDo - throw exception if the userGuess &/or secretWord are not valid
 *   Or at the very least, return all Gray
 */

public class GuessEvaluation {
    // Empty constructor for now, in the future may allow configurable word size
    public GuessEvaluation() {}

    /**
     * Enum defining the valid colors for guess evaluation
     */
    public enum Result  {GREEN, YELLOW, GRAY}

    /**
     *
     * @param userGuess
     * @param secretWord
     * @return
     */
    public Result[] evaluateGuess(String userGuess, String secretWord) {
        // default results - all Gray
        Result resultArray[] = {Result.GRAY, Result.GRAY, Result.GRAY, Result.GRAY, Result.GRAY};
        // protect ourselves - validate both the userGuess & the secretWord
        if (!GuessValidation.isWordValid(userGuess) ||  !GuessValidation.isWordValid(secretWord)) return resultArray;

        // store the secret word characters in a list
        //   approach -
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

        return resultArray;
    }
}
