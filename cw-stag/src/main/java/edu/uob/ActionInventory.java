package edu.uob;

public class ActionInventory extends ActionBuiltIn {
    public ActionInventory() {
        super("inventory", "Lists all artefacts in your inventory.");
        this.setNoneParameter(true);
    }

    @Override
    public String execute(Player player, String command, GameMap map) {
        return null;
    }
}