package org.fdu;

import java.util.*;  // for List & ArrayList
import java.util.Random;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Configures and populates the dictionary of valid Wordle solution words. <br>
 * <p>
 * Constructor populates dictionary from an included csv. <br>
 * If the program can't open the csv (or the csv is empty or has zero valid words), <br>
 *   it defaults to a very small dictionary of preSelected words to ensure game integrity. <br>
 * One public method provided: <br>
 * - randomly select and return a new word from the dictionary <br>
 * </p>
 * Three test methods provided:<br>
 * - constructor(dictionary name) - to allow testing of invalid dictionaries <br>
 * - getDictionarySize() - to check if using full dictionary or small version <br>
 * - return the last word selected
 *
 * @author Lee V
 * @version v1.3.5 (1.0.0 was first version with a full dictionary)
 * @since 0.0.1
 */

public class WordleDictionary {
    private static final String SOLUTIONS_DICTIONARY = "valid_solutions.csv";  // must be static for overloaded constructor

    private static final String[] smallWordSet = {"CRANE", "SHINE", "PLANT", "BRICK", "DRAFT"};
    private final List<String> dictionary = new ArrayList<>();

    private final Random random = new Random();

    private String currentWord;



    /**
     * Constructs a dictionary using the default word set. <br>
     * Default constructor, standard "entry" point. <br>
     * if we can't open the dictionary (or can't find it), use a few predefined words <br>
     * <p>
     * <b>Implementation note: </b> Internally calls {@link #WordleDictionary(String)} with the standard dictionary.

     */
    public WordleDictionary() {
        this(SOLUTIONS_DICTIONARY);
    }


    /**
     * Constructs a dictionary from a specified resource file (for testing purposes)<br>
     * Overloaded constructor defined to enable testing of invalid dictionaries <br>
     *
     * @param dictionaryName csv resource to open
     * <b>Implementation note: </b> if the dictionary cannot be loaded or is invalid,
     * the dictionary falls back to {@code smallWordSet} and logs the stack trace. <br>
     * IO exception caught internally (ie not actually thrown)
     */
    public WordleDictionary(String dictionaryName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(dictionaryName)) {
            if (is == null) {
                throw new FileNotFoundException("Dictionary resource not found: " + dictionaryName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                // string should be valid and trimmed - but just to be safe
                String line;
                while ((line = reader.readLine()) != null) {
                    String cleanedWord = line.toUpperCase().trim();
                    if (GuessValidation.validateWord(cleanedWord).isValid()) {
                        dictionary.add(cleanedWord);
                    }
                }
            }
            // if dictionary is empty - throw exception & use preloaded version
            if (dictionary.isEmpty()) {
                throw new FileNotFoundException("Dictionary file was empty");
            }
        } catch (IOException | NullPointerException e) {
            //  if we can't open the external dictionary, use the few words we defined above
            //   on Gemini advice - trying the addAll method for arrays rather than an iteration loop
            e.printStackTrace();
            dictionary.addAll(Arrays.asList(smallWordSet));
        }
    }

    /**
     *     randomly select and return a word from the dictionary<br>
     *     @return randomly selected word or "" if dictionary is empty (ToDo: throw an exception)
     *     <b>Implementation note: </b>  assume lower bound = 0, upper = dictionary size
     *        nextInt maximum = specified parameter ie [0,param]
     */
    public String pickNewWord() {
        if (dictionary.isEmpty()) { return ""; }

        this.currentWord = dictionary.get(random.nextInt(dictionary.size()));
        return this.currentWord;
    }

    // The following methods are intended to support unit testing
    //   intentionally package scope only (ie not public)
    /**
     *     get the word selected from the last call to pickNewWord() <br>
     *     or null if {@link #pickNewWord()} has not yet been called.
     * @return last word selected from dictionary
     */

    String getCurrentWord() {
        return this.currentWord;
    }

    /**
     * For testing - return the size of the dictionary <br>
     * useful to check if full or fallback dictionary loaded
     * @return number of words in the dictionary
     */
    int getDictionarySize() {
        return dictionary.size();
    }
}