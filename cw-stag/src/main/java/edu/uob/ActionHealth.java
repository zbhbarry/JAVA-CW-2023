package edu.uob;

public class ActionHealth extends ActionBuiltIn {
    public ActionHealth() {
        super("health", "Show health of yourself.");
        this.setNoneParameter(true);
    }

    @Override
    public String execute(Player player, String artefact, GameMap map) {
        return "Your health is: " + player.getHealth() + ".";
    }
}
