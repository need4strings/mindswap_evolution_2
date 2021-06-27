package academy.mindswap.enemies;

import academy.mindswap.utils.Utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class Enemies {

    private int healthPoints;
    private int attackPower;
    private int specialChance;
    private String name;
    private boolean isDead;
    private BufferedWriter out;

    public Enemies(int healthPoints, int attackPower, int specialChance, String name, boolean isDead) {
        this.healthPoints = healthPoints;
        this.attackPower = attackPower;
        this.specialChance = specialChance * attackPower;
        this.name = name;
        this.isDead = isDead;

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
            System.out.println("Companhia");
            setHealthPoints(0);
            this.isDead = true;
            System.out.println("is dead " + this.isDead);
            return;
        }

        healthPoints -= damage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public boolean isDead() {
        System.out.println("batatinha");
        return isDead;
    }

    public int getAttackPower() {
        return this.attackPower;
    }

    public String getName() {
        return name;
    }
}
