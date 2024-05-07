package edu.uob;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.util.ArrayList;
import java.util.HashMap;

public final class GameMap {
    private Location startLocation;
    private HashMap<String, Location> mapIndexes;

    public GameMap(ArrayList<Graph> sections) {
        ArrayList<Graph> locations = sections.get(0).getSubgraphs();
        ArrayList<Edge> paths = sections.get(1).getEdges();
        this.startLocation = null;
        mapIndexes = new HashMap<>();
    }

    private Location createLocation(Graph location) {
        Node locationDetails = location.getNodes(false).get(0);
        String locationName = locationDetails.getId().getId();
        return new Location(locationName, null);
        // TODO
    }

    private void addPath(Edge path) {
        // TODO
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
    }
}
