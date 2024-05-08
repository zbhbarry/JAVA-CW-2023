package edu.uob;

import java.util.HashSet;

public class Player extends Character {
    private Inventory inventory;
    private Health health;
    private Location currentLocation;

    public Player(String name, String description, Location start) {
        super(name, description);
        this.currentLocation = start;
        this.health = new Health();
        this.inventory = new Inventory();
        start.addCharacter(this);
    }

    public HashSet<String> getSubjects() {
        HashSet<String> subjects = this.currentLocation.getSubjectsName();
        subjects.addAll(this.inventory.showArtefactsName());
        subjects.add("health");
        return subjects;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getHealth() {
        return this.health.getHealth();
    }

    public boolean getHurt() {
        return this.health.getHurt();
    }

    public void addHealth() {
        this.health.addHealth();
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public String act(ActionWords playerAction, GameMap map) {
        GameAction action = playerAction.getAction();
        HashSet<String> entities = playerAction.getWords();
        String entity = null;
        if (entities.iterator().hasNext()) {
            entity = entities.iterator().next();
        }

        return action.execute(this, entity, map);
    }

    public void setLocation(Location newLocation) {
        this.currentLocation.removeCharacter(this.getName());
        this.currentLocation = newLocation;
        newLocation.addCharacter(this);
    }

    public void reset(Location startLocation) {
        this.getInventory().showArtefacts().forEach(e -> {
            this.currentLocation.addArtefact(e);
        });
        setLocation(startLocation);
        this.health = new Health();
        this.inventory = new Inventory();
    }
}
