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
        loadActions(actions);
        initializeBuiltInActions();  // Load built-in actions as well
    }

    private void loadActions(NodeList actionsList) {
        for (int i = 0; i < actionsList.getLength(); i++) {
            Element action = (Element) actionsList.item(i);
            if (action.getNodeType() != action.ELEMENT_NODE) continue;

            NodeList triggers = action.getElementsByTagName("keyphrase");
            NodeList subjects = action.getElementsByTagName("subjects").item(0).getChildNodes();
            NodeList consumers = action.getElementsByTagName("consumed").item(0).getChildNodes();
            NodeList producers = action.getElementsByTagName("produced").item(0).getChildNodes();
            String narration = action.getElementsByTagName("narration").item(0).getTextContent();

            HashSet<String> subjectsSet = createSetFromNodeList(subjects);
            HashSet<String> consumedSet = createSetFromNodeList(consumers);
            HashSet<String> producedSet = createSetFromNodeList(producers);

            for (int j = 0; j < triggers.getLength(); j++) {
                String triggerPhrase = triggers.item(j).getTextContent().trim();
                GameAction newAction = new GameAction(triggerPhrase, subjectsSet, consumedSet, producedSet, narration);

                HashSet<GameAction> actionSet = actions.getOrDefault(triggerPhrase, new HashSet<>());
                actionSet.add(newAction);
                actions.put(triggerPhrase, actionSet);
            }
        }
    }

    private HashSet<String> createSetFromNodeList(NodeList nodeList) {
        HashSet<String> result = new HashSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String content = nodeList.item(i).getTextContent().trim().toLowerCase();
                result.add(content);
            }
        }
        return result;
    }

    private void initializeBuiltInActions() {
        // Here, add built-in actions by creating instances of each action class
        this.actions.computeIfAbsent("drop", k -> new HashSet<>()).add(new ActionDrop());
        this.actions.computeIfAbsent("get", k -> new HashSet<>()).add(new ActionGet());
        this.actions.computeIfAbsent("goto", k -> new HashSet<>()).add(new ActionGoTo());
        this.actions.computeIfAbsent("health", k -> new HashSet<>()).add(new ActionHealth());
        this.actions.computeIfAbsent("inventory", k -> new HashSet<>()).add(new ActionInventory());
        this.actions.computeIfAbsent("inv", k -> new HashSet<>()).add(new ActionInventory());
        this.actions.computeIfAbsent("look", k -> new HashSet<>()).add(new ActionLook());
    }

    private HashSet<GameAction> getAction(final String name) {
        return this.actions.get(name);
    }

    public ActionWords interpret(String action, Player player, HashSet<String> entriesAll) throws Exception {
        HashSet<String> subjects = player.getSubjects();
        String[] words = action.split("\\s+"); // Split the action into words
        // we have entries in action
        HashSet<String> actionEntries = new HashSet<>(List.of(words));
        actionEntries.retainAll(entriesAll);

        // store debug info
        ArrayList<Integer> results = new ArrayList<>();
        GameAction matchedAction = null;

        // Check for potential action matches based on action words
        for (String word : words) {
            if (this.actions.containsKey(word)) { // if action has a trigger
                // for build-in, we should fix its subjects
                this.prepareAction(word, player);

                for (GameAction gameAction : actions.get(word)) {
                    int result = matchesAction(actionEntries, gameAction, subjects);
                    if (result == 1) {
                        if (matchedAction != null) {
                            throw new Exception("There is more than one action possible.");
                        }
                        matchedAction = gameAction; // Save the matched action
                    }
                    results.add(result);
                }
                // if more than one trigger, must wrong
                break;
            }
        }

        // Handle kinds of errors
        if (matchedAction != null) {
            return new ActionWords(matchedAction, actionEntries);
        } else if (!results.isEmpty()) {
            throw new Exception("Command error: " + getErrorMessage(results));
        } else {
            throw new Exception("No applicable action found for the command.");
        }
    }

    private int matchesAction(HashSet<String> wordsEntries, GameAction action, HashSet<String> subjects) {
        if (wordsEntries.isEmpty() && !action.isNoneParameter()) {
            return 0;
        }

        // here we have considered co-location
        HashSet<String> coSubjects = new HashSet<>(action.getSubjects());
        coSubjects.addAll(action.getConsumed());

        if (!subjects.containsAll(coSubjects)) {
            return -1; // Required subjects not available
        }

        // check if Extraneous Entities
        HashSet<String> intersection = new HashSet<>(wordsEntries);
        intersection.retainAll(action.getSubjects());

        if (!intersection.equals(wordsEntries)) {
            return -2; // Command contains Extraneous Entities
        }

        if (action instanceof ActionBuiltIn && wordsEntries.size() > 1) {
            return -3; // Composite Commands
        }

        return 1; // Command matches the action correctly
    }

    private String getErrorMessage(ArrayList<Integer> results) {
        return results.stream().min(Integer::compare).map(errorCode -> {
            return switch (errorCode) {
                case 0 -> "No Entities to interpret";
                case -1 -> "Necessary subjects not present";
                case -2 -> "Command contains Extraneous Entities";
                case -3 -> "Composite Commands";
                default -> "Unknown command error";
            };
        }).orElse("Unknown command error");
    }

    private void prepareAction(String word, Player player) {
        GameAction action = this.getAction(word).iterator().next(); // Get the first matching action

        if (action instanceof ActionBuiltIn) {
            HashSet<String> newSubjects = new HashSet<>();
            switch (word) {
                case "get":
                    newSubjects.addAll(player.getCurrentLocation().getArtefacts());
                    break;
                case "drop":
                    newSubjects.addAll(player.getInventory().showArtefactsName());
                    break;
                case "goto":
                    newSubjects.addAll(player.getCurrentLocation().getPathsOutgoing());
                    break;
                case "look":
                    newSubjects.add(player.getCurrentLocation().getName());
                    break;
                case "health":
                    newSubjects.add("health");
                    break;
                default:
                    // inv
                    break;
            }
            // Set subjects for the action
            ((ActionBuiltIn) action).setDynamicSubjects(newSubjects);
        }
    }


}
