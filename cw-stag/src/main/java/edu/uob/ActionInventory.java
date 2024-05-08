package edu.uob;

public class ActionInventory extends ActionBuiltIn {
    public ActionInventory() {
        super("inventory", "Lists all artefacts in your inventory.");
        this.setNoneParameter(true);
    }

    @Override
    public String execute(Player player, String command, GameMap map) {
        StringBuilder res = new StringBuilder();
        res.append("All artefacts in your inventory:\n");
        player.getInventory().showArtefacts().forEach(e -> {
            res.append(e.getName()).append(":  ");
            res.append(e.getDescription()).append("\n");
        });
        return res.toString();
    }
}