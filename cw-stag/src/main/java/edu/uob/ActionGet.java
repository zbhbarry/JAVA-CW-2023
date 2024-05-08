package edu.uob;

public class ActionGet extends ActionBuiltIn {
    public ActionGet() {
        super("get", "Pick up a specified artefact from the current location.");
    }

    @Override
    public String execute(Player player, String artefact, GameMap map) {
        Artefact pickedArtefact = player.getCurrentLocation().removeArtefact(artefact);
        if (pickedArtefact != null) {
            player.getInventory().addArtefact(pickedArtefact);
            return "You picked up " + pickedArtefact.getName() + ".";
        } else {
            return "Artefact not found in the current location.";
        }
    }
}