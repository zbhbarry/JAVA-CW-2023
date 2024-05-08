package edu.uob;

public class ActionLook extends ActionBuiltIn {
    public ActionLook() {
        super("look", "Prints names and descriptions of entities in the current location and lists paths to other locations.");
        this.setNoneParameter(true);
    }

    @Override
    public String execute(Player player, String locName, GameMap map) {
        StringBuilder result = new StringBuilder();
        Location currentLocation = player.getCurrentLocation();

        // location details
        result.append("Location: ").append(currentLocation.getName()).append("\n");
        result.append("Description: ").append(currentLocation.getDescription()).append("\n");

        result.append("Artefacts in this location:\n");
        currentLocation.getArtefactValues().forEach(artefact -> {
            result.append(artefact.getName()).append(":  ").append(artefact.getDescription()).append("\n");
        });

        result.append("Furniture in this location:\n");
        currentLocation.getFurnitureValues().forEach(artefact -> {
            result.append(artefact.getName()).append(":  ").append(artefact.getDescription()).append("\n");
        });

        result.append("Characters in this location:\n");
        currentLocation.getCharacterValues().forEach(character -> {
            if (!character.getName().equals(player.getName()))
                result.append(character.getName()).append(":  ").append(character.getDescription()).append("\n");
        });

        result.append("Paths from this location:\n");
        currentLocation.getPathValues().forEach(destination -> {
            result.append(destination.getName()).append(":  ").append(destination.getDescription()).append("\n");
        });

        return result.toString();
    }
}
