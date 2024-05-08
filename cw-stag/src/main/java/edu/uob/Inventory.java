package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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

    public Set<String> showArtefactsName() {
        return this.artefacts.keySet();
    }

    public Artefact removeArtefact(String artefact) {
        if (this.artefacts.containsKey(artefact)) {
            return this.artefacts.remove(artefact);
        }
        return null;
    }
}
