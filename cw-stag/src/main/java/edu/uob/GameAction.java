package edu.uob;

import java.util.HashSet;

public class GameAction {
    private String trigger;
    protected HashSet<String> subjects;
    private HashSet<String> consumed;
    private HashSet<String> produced;
    private String narration;
    private boolean noneParameter;

    public GameAction(String trigger, HashSet<String> subjects, HashSet<String> consumed, HashSet<String> produced, String narration) {
        this.trigger = trigger;
        this.subjects = subjects;
        this.consumed = consumed;
        this.produced = produced;
        this.narration = narration;
        this.noneParameter = false;
    }

    public void setSubjects(String subject) {
        this.subjects.add(subject);
    }

    public void setConsumed(String consumed) {
        this.consumed.add(consumed);
    }

    public void setProduced(String produced) {
        this.produced.add(produced);
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getTrigger() {
        return trigger;
    }

    public HashSet<String> getSubjects() {
        return subjects;
    }

    public HashSet<String> getConsumed() {
        return consumed;
    }

    public HashSet<String> getProduced() {
        return produced;
    }

    public String getNarration() {
        return narration;
    }


    public String execute(Player player, String destination, GameMap gameMap) {
        StringBuilder result = new StringBuilder();
        result.append(this.getNarration()).append("\n");

        // Prefetch data sets for checking existence
        HashSet<String> gameLocations = new HashSet<>(gameMap.getLocations());
        HashSet<String> outgoingLocations = new HashSet<>(player.getCurrentLocation().getPathsOutgoing());

        // Handle consumption of entities
        for (String consume : this.getConsumed()) {
            if (consume.equals("health")) {
                if (!player.getHurt()) {
                    result.append("You have died. Dropping all items and returning to start location.\n");
                    player.reset(gameMap.getStartLocation());
                    return result.toString();
                }
            } else if (outgoingLocations.contains(consume)) {
                player.getCurrentLocation().removePath(consume);
            } else {
                Artefact artefactInv = player.getInventory().removeArtefact(consume);
                Artefact artefact = player.getCurrentLocation().removeArtefact(consume);
                Character character = player.getCurrentLocation().removeCharacter(consume);
                Furniture furniture = player.getCurrentLocation().removeFurniture(consume);

                if (artefactInv == null && artefact == null && character == null && furniture == null) {
                    result.append("Warning: ").append(consume).append(" not found for consumption.\n");
                } else {
                    gameMap.getStoreRoom().addArtefact(artefactInv);
                    gameMap.getStoreRoom().addArtefact(artefact);
                    gameMap.getStoreRoom().addCharacter(character);
                    gameMap.getStoreRoom().addFurniture(furniture);
                }
            }
        }

        // Handle production of entities
        for (String produce : this.getProduced()) {
            if (produce.equals("health")) {
                player.addHealth();  // Increase player's health
            } else if (gameLocations.contains(produce)) {
                player.getCurrentLocation().addPath(gameMap.getMapIndexes().get(produce));
            } else {
                // Get entities from the storeroom and add to the current location
                Artefact artefact = gameMap.getStoreRoom().removeArtefact(produce);
                Character character = gameMap.getStoreRoom().removeCharacter(produce);
                Furniture furniture = gameMap.getStoreRoom().removeFurniture(produce);

                if (artefact == null && character == null && furniture == null) {
                    result.append("Warning: Could not produce ").append(produce).append(", is it in storeroom?\n");
                } else {
                    player.getCurrentLocation().addArtefact(artefact);
                    player.getCurrentLocation().addCharacter(character);
                    player.getCurrentLocation().addFurniture(furniture);
                }
            }
        }


        return result.toString();
    }

    public boolean isNoneParameter() {
        return noneParameter;
    }

    public void setNoneParameter(boolean noneParameter) {
        this.noneParameter = noneParameter;
    }
}