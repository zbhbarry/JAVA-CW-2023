package edu.uob;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class GameMap {
    private Location startLocation;
    private final HashMap<String, Location> mapIndexes;
    private HashSet<String> entriesAll;
    private Location storeRoom;

    public GameMap(ArrayList<Graph> sections) {
        ArrayList<Graph> locations = sections.get(0).getSubgraphs();
        ArrayList<Edge> paths = sections.get(1).getEdges();
        this.startLocation = null;
        this.storeRoom = null;
        mapIndexes = new HashMap<>();
        entriesAll = new HashSet<>();
        createMapIndexes(locations, paths);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getStoreRoom() {
        return storeRoom;
    }

    public HashSet<String> getEntriesAll() {
        return entriesAll;
    }

    public Set<String> getLocations() {
        return mapIndexes.keySet();
    }

    public HashMap<String, Location> getMapIndexes() {
        return mapIndexes;
    }

    private Location createLocation(Graph location) {
        Node locationDetails = location.getNodes(false).get(0);
        String locationName = locationDetails.getId().getId();
        String description = locationDetails.getAttribute("description");
        Location loc = new Location(locationName, description);
        this.entriesAll.add(locationName);

        // Add characters, artefacts, furniture
        for (Graph subgraph : location.getSubgraphs()) {
            for (Node item : subgraph.getNodes(false)) {
                String itemName = item.getId().getId();
                this.entriesAll.add(itemName);
                String itemDesc = item.getAttribute("description");
                switch (subgraph.getId().getId()) {
                    case "characters":
                        loc.addCharacter(new Character(itemName, itemDesc));
                        break;
                    case "artefacts":
                        loc.addArtefact(new Artefact(itemName, itemDesc));
                        break;
                    case "furniture":
                        loc.addFurniture(new Furniture(itemName, itemDesc));
                        break;
                }
            }
        }

        // handle storeRoom
        if (locationName.equals("storeroom")) {
            this.storeRoom = loc;
        }

        return loc;
    }

    private void addPath(Edge path) {
        String fromName = path.getSource().getNode().getId().getId();
        String toName = path.getTarget().getNode().getId().getId();
        Location fromLoc = mapIndexes.get(fromName);
        Location toLoc = mapIndexes.get(toName);
        if (fromLoc != null && toLoc != null) {
            fromLoc.addPath(toLoc);
        }
    }

    private void createMapIndexes(ArrayList<Graph> locations, ArrayList<Edge> paths) {
        for (Graph location : locations) {
            Location temp = createLocation(location);
            mapIndexes.put(temp.getName(), temp);
        }
        for (Edge path : paths) {
            addPath(path);
        }
        Node firstNode = paths.get(0).getSource().getNode();
        String fromName = firstNode.getId().getId();
        this.startLocation = mapIndexes.get(fromName);
        if (this.storeRoom == null) {
            this.storeRoom = new Location("storeroom", "storeroom");
            mapIndexes.put("storeroom", this.storeRoom);
        }
        this.entriesAll.add("health");
    }
}
