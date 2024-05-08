package edu.uob;

public class ActionDrop extends ActionBuiltIn {
    public ActionDrop() {
        super("drop", "Put down a specified artefact from your inventory.");
    }

    @Override
    public String execute(Player player, String artefact, GameMap map) {
        Artefact droppedArtefact = player.getInventory().removeArtefact(artefact);
        if (droppedArtefact != null) {
            player.getCurrentLocation().addArtefact(droppedArtefact);
            return "You dropped " + droppedArtefact.getName() + ".";
        } else {
            return "Artefact not found in your inventory.";
        }
    }
}