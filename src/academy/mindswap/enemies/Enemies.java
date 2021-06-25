package academy.mindswap.enemies;

import academy.mindswap.utils.Utils;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class Enemies {

    private int healthPoints;
    private int attackPower;
    private int specialChance;
    private String name;
    private boolean isDead;
    private BufferedWriter out;

    public Enemies(int healthPoints, int attackPower, int specialChance, String name, boolean isDead) {

    }

    public int attack() {
        int chance = Utils.random(1,3);
        int attackValue = attackPower;
        if(chance == 1) {
            attackValue = specialChance;
        }
        return attackValue;
    }

    public void suffer(int damage) throws IOException {
        if(healthPoints - damage <= 0) {
            setHealthPoints(0);
            isDead = true;
            out.write(name + " is dead!");
            return;
        }
        //toDo print healthpoints remaining
        out.write(name + " have " + healthPoints + " healthpoints remaining!");
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
}
