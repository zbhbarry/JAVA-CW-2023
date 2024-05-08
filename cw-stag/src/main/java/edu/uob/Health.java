package edu.uob;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Health {
    private static final int MAX_HEALTH = 3;
    private int health;

    public Health() {
        this.health = MAX_HEALTH;
    }

    public void addHealth() {
        this.health = min(this.health + 1, MAX_HEALTH);
    }

    public int getHealth() {
        return this.health;
    }

    public boolean getHurt() {
        this.health--;
        return this.health > 0;
    }
}
