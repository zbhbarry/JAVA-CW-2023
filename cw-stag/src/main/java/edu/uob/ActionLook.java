package edu.uob;

public class ActionLook extends ActionBuiltIn {
    public ActionLook() {
        super("look", "Prints names and descriptions of entities in the current location and lists paths to other locations.");
        this.setNoneParameter(true);
    }

    @Override
    public String execute(Player player, String locName, GameMap map) {
        return null;
    }
}
