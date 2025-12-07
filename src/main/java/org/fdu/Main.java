package org.fdu;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        IO.println(String.format("Welcome to Wordle (prep)!"));

        WordleDictionary dictionary = new WordleDictionary();
        for (int itr = 0; itr < 10; itr++) {
            IO.println(String.format("Word %s: %s", itr, dictionary.pickNewWord()));
        }
    }
}
