package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class DBTokenizer {
    public DBTokenizer(){}

    public ArrayList<String> tokenizeCommand(String command) {
        ArrayList<String> tokens = new ArrayList<>();
        String[] fragments = command.split("'");
        for (int i = 0; i < fragments.length; i++) {
            if (i % 2 != 0) { // Inside single quotes
                tokens.add(fragments[i]);
            } else { // Outside single quotes
                tokens.addAll(Arrays.asList(tokenize(fragments[i])));
            }
        }
        return tokens;
    }
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
