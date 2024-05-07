package edu.uob;

public class Player extends Character {
    private Inventory inventory;
    private Health health;

    public Player(String name, String description) {
        super(name, description);
        this.health = new Health();
        this.inventory = new Inventory();
    }

    // Additional methods and properties as needed
}
