package org.fdu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private static WordleDictionary dictionary;

    @BeforeAll
    static void setup() {
        // Initialize once for all tests in this class
        dictionary = new WordleDictionary();
    }

    // check if full dictionary is getting loaded
    @Test
    void isDefaultDictionaryValid() {
        assertTrue(dictionary.getDictionarySize() > 100, "Small (or no) dictionary populated");
    }

    // generate a few words - visibly check for randomness (future: add code to check for randomness)
    //   first, verify it loaded the full dictionary
    // verify the words returned are valid and
    //   the word returned from pickNewWord() matches getCurrentWord()
    @Test
    void areWordsValid() {
        int size = dictionary.getDictionarySize();
        System.out.println("Dictionary loaded size: " + size);
        assertTrue((size > 100), "full dictionary NOT populated");

        System.out.println("Randomly selecting a few words from the dictionary");
        for (int itr = 0; itr < 5; itr++) {
            String secretWord = dictionary.pickNewWord();
            System.out.println(secretWord);
            assertNotNull(secretWord, "Picked word should not be null");
            assertTrue(GuessValidation.validateWord(secretWord).isValid(),
                    "Word returned from dictionary fails validation: " + secretWord);
            assertEquals(secretWord, dictionary.getCurrentWord(),
                    "stored current word didn't update correctly after pickNewWord()");
        }
    }

    // load a non-existent dictionary - should see an exception, but the test should pass
    @Test
    void checkNonExistentDictionary() {
        WordleDictionary badDictionary = new WordleDictionary("IDontExist.csv");
        System.out.println("checkNonExistentDictionary: Above exception expected");
        int size = badDictionary.getDictionarySize();
        assertTrue((size > 0), "small dictionary NOT populated");
        assertTrue((size < 100), "small dictionary NOT populated");
        // verify can get a non-blank word from the bad dictionary
        String secretWord = badDictionary.pickNewWord();
        assertNotEquals("", secretWord, "No word returned from small dictionary");
    }

    // load a dictionary with a mix of valid and invalid words, only the valid words should be loaded
    @Test
    void checkDictionaryOnlyLoadsValidWords() {
        WordleDictionary invalidWordDictionary = new WordleDictionary("InvalidWords.csv");
        int size = invalidWordDictionary.getDictionarySize();
        System.out.println("invalidWordDictionary loaded size: " + size);
        assertTrue((size > 0), "small dictionary NOT populated");
        assertTrue((size < 10), "small dictionary NOT populated");
        // verify can get a non-blank word from the dictionary
        String secretWord = invalidWordDictionary.pickNewWord();
        assertNotEquals("", secretWord, "No word returned from mixed validity dictionary");
        System.out.println("Word retrieved from mixed validity dictionary: " + secretWord);
    }

    // trying to load an empty dictionary, code should load the small dictionary instead
    @Test
    void loadEmptyDictionary() {
        WordleDictionary emptyDictionary = new WordleDictionary("EmptyDictionary.csv");
        assertFalse((emptyDictionary.getDictionarySize() == 0), "dictionary size should not be zero");
        System.out.println("Word retrieved after loading empty dictionary " + emptyDictionary.pickNewWord());
    }
}