package edu.uob;

import java.util.HashMap;

public class Players {
    private HashMap<String, Player> players;

    public Players() {
        players = new HashMap<>();
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public void addPlayer(String username, Player player) {
        players.put(username, player);
    }
}
