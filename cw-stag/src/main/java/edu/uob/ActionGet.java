package edu.uob;

public class ActionGet extends ActionBuiltIn {
    public ActionGet() {
        super("get", "Pick up a specified artefact from the current location.");
    }

    @Override
    public String execute(Player player, String artefact, GameMap map) {
        return null;
    }
}