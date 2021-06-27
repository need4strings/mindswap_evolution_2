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

    /**
     * Constructor Method
     * @param healthPoints -> the health point the enemy has
     * @param attackPower -> the attack power the enemy has
     * @param specialChance -> the special chance the enemy has
     * @param name -> the enemy's name
     * @param isDead -> dead enemy flag
     */
    public Enemies(int healthPoints, int attackPower, int specialChance, String name, boolean isDead) {
        this.healthPoints = healthPoints;
        this.attackPower = attackPower;
        this.specialChance = specialChance * attackPower;
        this.name = name;
        this.isDead = isDead;

    }

    /**
     * Attack - enemy's attack method
     * @return -> the amount of damage to be dealt
     */
    public int attack() {
        int chance = Utils.random(1,3);
        int attackValue = attackPower;
        if(chance == 1) {
            attackValue = specialChance;
        }
        return attackValue;
    }

    /**
     * Suffer - enemy's suffer method
     * @param damage -> the amount of damage to be suffered
     * @throws IOException
     */
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

    /**
     * Get Health Points - gets the enemy's health points
     * @return -> returns the health points the enemy has
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Set Health Point - sets the enemy's health points to a specific value
     * @param healthPoints -> the health points to be set on the enemy
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Is Dead - checks if the enemy is dead
     * @return - true/false
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Get Attack Power - gets the enemy's attack power
     * @return -> enemy's attack power
     */
    public int getAttackPower() {
        return this.attackPower;
    }

    /**
     * Get Name - gets the enemy's name
     * @return -> the enemy's name
     */
    public String getName() {
        return name;
    }
}
