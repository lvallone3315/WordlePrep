package org.fdu;

import java.util.*;  // for List & ArrayList
import java.util.Random;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Configures and populates the dictionary of valid Wordle solutions. <br>
 * Includes methods to get a new word and return the last word selected
 *
 * <p>
 * Constructor populates dictionary from an included csv. <br>
 * If the program can't open the csv (or is empty or zero valid words), <br>
 *   it uses a very small dictionary of preSelected words. <br>
 * Two additional methods are provided: <br>
 * - randomly select and return a new word from the dictionary <br>
 * - return the last word selected
 * </p>
 * Two test methods provided:<br>
 * - constructor(dictionary name) - to allow testing of invalid dictionaries <br>
 * - getDictionarySize() - to check if using full dictionary or small version <br>
 *
 * @author Lee V
 * @version v1.0 (first version with a full dictionary)
 * @since 0.0.1
 */

public class WordleDictionary {
    private String currentWord;
    private String[] smallWordSet = {"CRANE", "SHINE", "PLANT", "BRICK", "DRAFT"};
    private final List<String> dictionary = new ArrayList<>();
    private static final String SOLUTIONS_DICTIONARY = "valid_solutions.csv";  // must be static for overloaded constructor


    /**
     * Read the dictionary file into the program,
     *    if can't open the dictionary (or can't find it), use a few predefined words <br>
     * Overloaded constructor so we can test invalid dictionaries
     *
     * <br> IO exception caught internally (ie not actually thrown)
     */
    public WordleDictionary() {
        this(SOLUTIONS_DICTIONARY);
    }
    public WordleDictionary(String dictionaryName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(dictionaryName);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            if (is == null) {
                throw new FileNotFoundException("Dictionary file not found");
            }
            // string should be valid and trimmed - but just to be safe
            String line;
            while ((line = reader.readLine()) != null) {
                String cleanedWord = line.toUpperCase().trim();
                if (GuessValidation.validateWord(cleanedWord).isValid()) {
                    dictionary.add(cleanedWord);
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
     *     randomly select a word from the dictionary, assume lower bound = 0, upper = dictionary size
     *        nextInt maximum = specified parameter ie [0,param]
     * @return randomly selected word or "" if dictionary is empty (ToDo: throw an exception)
     */
    public String pickNewWord() {
        if (dictionary.isEmpty()) { return ""; }

        Random random = new Random();
        this.currentWord = dictionary.get(random.nextInt(dictionary.size()));
        return this.currentWord;
    }

    /**
     *     get the word selected from the last call to pickNewWord()
     * @return last word selected from dictionary
     */

    public String getCurrentWord() {
        return this.currentWord;
    }

    /**
     * For testing - return the size of the dictionary
     * @return number of words in the dictionary
     */
    public int getDictionarySize() {
        return dictionary.size();
    }
}
