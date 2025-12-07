package org.fdu;

import java.util.Random;

public class WordleDictionary {
    private String currentWord;
    private String[] dictionary = {"CRANE", "SHINE", "PLANT", "BRICK", "DRAFT"};

    // constructor - empty for now
    //   in the future, may load the dictionarys into memory
    public WordleDictionary() {
        // empty for now
    }

    //  randomly select a word from the dictionary, assume lower bound = 0, upper = dictionary size
    //    nextInt max = parameter ie [0,param]
    public String pickNewWord() {
        Random random = new Random();
        this.currentWord = dictionary[random.nextInt(dictionary.length)];
        return this.currentWord;
    }

    // get the word selected from the last call to pickNewWord()
    public String getCurrentWord() {
        return this.currentWord;
    }
}
