package edu.uob;

public class ActionGoTo extends ActionBuiltIn {
    public ActionGoTo() {
        super("goto", "Move to a specified location if there is a path.");
    }

    @Override
    public String execute(Player player, String destination, GameMap map) {
        return null;
    }
}
