package edu.uob;

import java.util.HashSet;

public class ActionWords {
    private HashSet<String> words;
    private GameAction action;

    public ActionWords(GameAction action, HashSet<String> words) {
        this.action = action;
        this.words = words;
    }

    public HashSet<String> getWords() {
        return words;
    }

    public GameAction getAction() {
        return action;
    }
}
