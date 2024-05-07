package edu.uob;

import java.util.ArrayList;

public class Location extends GameEntity {
    private ArrayList<Location> pathsOutgoing;
    private ArrayList<Character> characters;
    private ArrayList<Artefact> artefacts;
    private ArrayList<Furniture> furniture;

    public Location(String name, String description) {
        super(name, description);
    }

    // Additional methods and properties as needed
}

