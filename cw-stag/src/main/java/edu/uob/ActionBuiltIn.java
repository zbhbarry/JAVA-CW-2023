package edu.uob;

import java.util.HashSet;

public abstract class ActionBuiltIn extends GameAction {
    public ActionBuiltIn(String trigger, String narration) {
        super(trigger, new HashSet<>(), new HashSet<>(), new HashSet<>(), narration);
    }

    // Method to dynamically set subjects
    public void setDynamicSubjects(HashSet<String> newSubjects) {
        this.subjects = newSubjects;
    }
}