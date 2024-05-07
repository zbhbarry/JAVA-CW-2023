package edu.uob;

import static java.lang.Math.max;

public class Health {
    private static final int MAX_HEALTH = 3;
    private int health;

    public Health() {
        this.health = MAX_HEALTH;
    }

    public void addHealth() {
        this.health = max(this.health + 1, MAX_HEALTH);
    }

    public int getHealth() {
        return this.health;
    }

    public boolean getHurt() {
        this.health--;
        return this.health > 0;
    }
}
