package edu.uob;

public class ActionDrop extends ActionBuiltIn {
    public ActionDrop() {
        super("drop", "Put down a specified artefact from your inventory.");
    }

    @Override
    public String execute(Player player, String artefact, GameMap map) {
        return null;
    }
}