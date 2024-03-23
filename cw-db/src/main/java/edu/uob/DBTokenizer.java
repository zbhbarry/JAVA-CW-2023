package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class DBTokenizer {
    // This class is responsible for tokenizing SQL-like commands.
    public DBTokenizer(){}

    // Tokenizes the given command into an ArrayList of String tokens.
    public ArrayList<String> tokenizeCommand(String command) {
        ArrayList<String> tokens = new ArrayList<>();
        String[] fragments = command.split("'");
        for (int i = 0; i < fragments.length; i++) {
            if (i % 2 != 0) { // Inside single quotes
                tokens.add(fragments[i]);
            } else {
                tokens.addAll(Arrays.asList(tokenize(fragments[i])));
            }
        }
        return tokens;
    }

    // Helper method to tokenize a fragment of the command.
    private String[] tokenize(String input) {
        String specialCharacters = "(),;==!=<>";
        String[] replacements = {">=", "<=", "(", ")", ",", ";", "==", "!=", "<", ">"};
        input = input.replaceAll(">=", " #L# ");
        input = input.replaceAll("<=", " #R# ");
        for (String replacement : replacements) {
            input = input.replace(replacement, " " + replacement + " ");
        }
        input = input.replaceAll("#L#", " >= ");
        input = input.replaceAll("#R#", " <= ").trim();
        return input.trim().split("\\s+");
    }
}
