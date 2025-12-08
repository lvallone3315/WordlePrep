package org.fdu;


public class GuessEvaluation {
    // Empty constructor for now, in the future may allow configurable word size
    public GuessEvaluation() {}

    public enum Result  {GREEN, YELLOW, GRAY}

    public Result[] evaluateGuess(String userGuess, String secretWord) {
        Result resultArray[] = {Result.GRAY, Result.GREEN, Result.GREEN, Result.YELLOW, Result.GRAY};
        // convert guess & secret word into character arrays
        // also store the secret word characters in a list
        // walk through the guess
        //    if character in same location in secret word array
        //       Result[letter location] = Green
        //    if not and character in secret word list
        //       Result[letter location] = Yellow & delete character from list
        //    else
        //       Result[letter location] = Gray

        return resultArray;
    }
}
