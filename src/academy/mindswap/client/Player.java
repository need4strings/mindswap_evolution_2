package academy.mindswap.client;

import academy.mindswap.items.Items;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;
import academy.mindswap.utils.ThreadColor;
import academy.mindswap.utils.Utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class Player {

    // PROPERTIES
    private int healthPoints;
    private int attackPower;
    private int specialChance;
    private String name;
    private boolean isDead;
    private Server server;

    // METHODS

    /**
     * Constructor Method
     * @param name - the name that the user typed
     * @param out - the buffered writer
     * @param server - the server
     */
    public Player(String name, BufferedWriter out, Server server) {
        this.healthPoints = 10;
        this.attackPower = 1;
        this.specialChance = 2 * attackPower;
        this.name = name;
        this.isDead = false;
        this.server = server;
    }

    /**
     * Attack - deal with the attack mechanic. It has a special attack chance
     * @return -> returns the attack damage the player is going to deal
     */
    public int attack() {
        int chance = Utils.random(1,3);
        int attackValue = attackPower;
        if(chance == 1) {
            attackValue = specialChance;
            server.broadcast(ThreadColor.ANSI_YELLOW + Messages.SPECIAL_ATTACK + " by " + this.name + " causing " + specialChance + " points of damage!" + ThreadColor.ANSI_RESET);
        }
        return attackValue;
    }

    /**
     * Search Item - makes the player search for an item based on a random chance
     * @return -> returns the item found
     * @throws IOException
     */
    public Items searchItem() throws IOException {
        int chance = Utils.random(1, 10);
        System.out.println("chance" + chance);
        switch (chance) {
            case 1:
            case 2:
                attackPower += Items.ORACLE_DOCUMENTATION_BOOK.getAttackPower();
                return Items.ORACLE_DOCUMENTATION_BOOK;
            case 3:
            case 4:
                attackPower += Items.KEYBOARD.getAttackPower();
                return Items.KEYBOARD;
            case 5:
            case 6:
                attackPower += Items.GARBAGE_BIN.getAttackPower();
                return Items.GARBAGE_BIN;
            case 7:
                attackPower += Items.TRANSISTOR.getAttackPower();
                return Items.TRANSISTOR;
            case 8:
                attackPower += Items.PENTIUM_2.getAttackPower();
                return Items.PENTIUM_2;
            case 9:
            case 10:
                return Items.NOTHING;
        }
        return null;
    }

    /**
     * Suffer - deal with the suffering of damage
     * @param damage -> the amount of damage to be suffered
     * @return -> returns the remaining health points
     * @throws IOException
     */
    public int suffer(int damage) throws IOException {
        if(healthPoints - damage <= 0) {
            setHealthPoints(0);
            isDead = true;
            server.broadcast(name + " is dead!");
            return 0;
        }
        healthPoints -= damage;
        server.broadcast(ThreadColor.ANSI_RED + name + " has suffered " + damage + " points of damage and now has " + healthPoints + " healthpoints remaining!" + ThreadColor.ANSI_RESET);

        return healthPoints;
    }

    /**
     * Set Full Health - Sets the players health back to full
     */
    public void setFullHealth() {
        healthPoints = 10;
    }

    /**
     * Set Health Points - sets the players health points to a specific value
     * @param healthPoints - the value of health points to be set
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Is Dead - checks if the player is dead
     * @return - true/false
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Get Attack Power - gets the players attack power
     * @return -> players attack power
     */
    public int getAttackPower() {
        return attackPower;
    }

    public static class Rat {

        // PROPERTIES
        private int ratAttackPower;
        private String ratName;

        //METHODS

        /**
         * Constructor Method
         * @param ratName - the name to be given to the rat
         */
        public Rat(String ratName) {
            this.ratName = ratName;
            this.ratAttackPower = 1;
        }

        /**
         * Get Rat Attack Power - gets the rat's attack power
         * @return -> the rat's attack power
         */
        public int getRatAttackPower() {
            return ratAttackPower;
        }
    }
}
