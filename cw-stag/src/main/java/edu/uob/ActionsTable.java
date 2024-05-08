package edu.uob;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class ActionsTable {
    private HashMap<String, HashSet<GameAction>> actions;

    public ActionsTable(NodeList actions) {
        this.actions = new HashMap<>();

    }

}
