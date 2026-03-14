package org.fdu;
/**
 * Defines rules for the game - specifically the word length and the maximum number of guesses
 *
 * @author Lee V
 * @version 1.3.11
 */

public final class WordleRules {

    /**
     * Required length for guesses and secret words (ASCII characters), current value is {@value}
     *   Note: code shouldn't break if changed, but the validation JUnit tests absolutely will.
     */
    public static final int WORD_LENGTH = 5;

    /** Maximum guesses allowed in a game, current value is {@value} */
    public static final int MAX_GUESSES = 6;

    private WordleRules() {}
}
