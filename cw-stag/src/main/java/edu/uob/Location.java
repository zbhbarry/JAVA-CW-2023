package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Location extends GameEntity {
    private HashMap<String, Location> pathsOutgoing;
    private HashMap<String, Character> characters;
    private HashMap<String, Artefact> artefacts;
    private HashMap<String, Furniture> furniture;

    public Location(String name, String description) {
        super(name, description);
        this.pathsOutgoing = new HashMap<>();
        this.characters = new HashMap<>();
        this.artefacts = new HashMap<>();
        this.furniture = new HashMap<>();
    }

    public void addCharacter(Character character) {
        if (character == null) return;
        this.characters.put(character.getName(), character);
    }

    public Set<String> getCharacters() {
        return this.characters.keySet();
    }

    public Set<String> getPathsOutgoing() {
        return this.pathsOutgoing.keySet();
    }

    public Set<String> getArtefacts() {
        return this.artefacts.keySet();
    }

    public Set<String> getFurniture() {
        return this.furniture.keySet();
    }

    public ArrayList<Artefact> getArtefactValues() {
        return new ArrayList<>(this.artefacts.values());
    }

    public ArrayList<Character> getCharacterValues() {
        return new ArrayList<>(this.characters.values());
    }

    public ArrayList<Location> getPathValues() {
        return new ArrayList<>(this.pathsOutgoing.values());
    }

    public void addArtefact(Artefact artefact) {
        if (artefact == null) return;
        this.artefacts.put(artefact.getName(), artefact);
    }

    public void addFurniture(Furniture furniture) {
        if (furniture == null) return;
        this.furniture.put(furniture.getName(), furniture);
    }

    public void addPath(Location toLocation) {
        if (toLocation == null) return;
        this.pathsOutgoing.put(toLocation.getName(), toLocation);
    }

    public void removePath(String toLocation) {
        this.pathsOutgoing.remove(toLocation);
    }

    public HashSet<String> getSubjectsName() {
        HashSet<String> subjectsName = new HashSet<>();
        subjectsName.addAll(this.characters.keySet());
        subjectsName.addAll(this.artefacts.keySet());
        subjectsName.addAll(this.furniture.keySet());
        subjectsName.addAll(this.pathsOutgoing.keySet());
        subjectsName.add(this.getName());

        return subjectsName;
    }

    public Artefact removeArtefact(String artefact) {
        if (this.artefacts.containsKey(artefact)) {
            return this.artefacts.remove(artefact);
        }
        return null;
    }

    public Location getPath(String destination) {
        return this.pathsOutgoing.get(destination);
    }

    public Character removeCharacter(String character) {
        if (this.characters.containsKey(character)) {
            return this.characters.remove(character);
        }
        return null;
    }

    public Furniture removeFurniture(String consume) {
        if (this.furniture.containsKey(consume)) {
            return this.furniture.remove(consume);
        }
        return null;
    }

    public ArrayList<Furniture> getFurnitureValues() {
        return new ArrayList<>(this.furniture.values());
    }
}

