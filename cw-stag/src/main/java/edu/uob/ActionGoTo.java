package edu.uob;

public class ActionGoTo extends ActionBuiltIn {
    public ActionGoTo() {
        super("goto", "Move to a specified location if there is a path.");
    }

    @Override
    public String execute(Player player, String destination, GameMap map) {
        Location newLocation = player.getCurrentLocation().getPath(destination);
        if (newLocation != null) {
            player.setLocation(newLocation);
            return "You moved to " + destination + ".";
        } else {
            return "Path to " + destination + " not found.";
        }
    }
}
