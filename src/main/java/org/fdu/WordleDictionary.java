package org.fdu;

import java.util.*;  // for List & ArrayList
import java.util.Random;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class WordleDictionary {
    private String currentWord;
    private String[] smallWordSet = {"CRANE", "SHINE", "PLANT", "BRICK", "DRAFT"};
    private final List<String> dictionary = new ArrayList<>();


    /**
     * Read the dictionary file into the program,
     *    if we can't open the dictionary (or can't find it), just use a few predefined words
     * @throws IOException
     */
    public WordleDictionary() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("valid_solutions.csv");
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            if (is == null) {
                throw new FileNotFoundException("Dictionary file not found");
            }
            // string is preloaded trimmed - but just to be safe
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.toUpperCase().trim());
            }
        } catch (IOException e) {
            //  if we can't open the external dictionary, use the few words we defined above
            e.printStackTrace();
            for (int itr = 0; itr < smallWordSet.length; itr++) {
                dictionary.add(smallWordSet[itr]);
            }
        }
    }

    //  randomly select a word from the dictionary, assume lower bound = 0, upper = dictionary size
    //    nextInt max = parameter ie [0,param]
    public String pickNewWord() {
        Random random = new Random();
        this.currentWord = dictionary.get(random.nextInt(dictionary.size()));
        return this.currentWord;
    }

    // get the word selected from the last call to pickNewWord()
    public String getCurrentWord() {
        return this.currentWord;
    }
}
