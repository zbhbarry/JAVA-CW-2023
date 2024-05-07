package edu.uob;

import java.util.ArrayList;
import java.util.TreeMap;

public class Inventory {
    private TreeMap<String, Artefact> artefacts;

    public Inventory() {
        this.artefacts = new TreeMap<>();
    }

    public void addArtefact(Artefact artefact) {
        this.artefacts.put(artefact.getName(), artefact);
    }

    public void delArtefact(String name) {
        this.artefacts.remove(name);
    }

    public ArrayList<Artefact> showArtefacts() {
        return new ArrayList<>(this.artefacts.values());
    }
}
