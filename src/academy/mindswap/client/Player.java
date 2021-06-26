package academy.mindswap.client;

import academy.mindswap.items.Items;
import academy.mindswap.server.Server;
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
    private boolean acceptedOffer;
    private BufferedWriter out;
    private Server server;

    // METHODS

    /**
     * Constructor Method
     * @param name - the name that the user typed
     */
    public Player(String name, BufferedWriter out, Server server) {
        this.healthPoints = 10;
        this.attackPower = 1;
        this.specialChance = 2;
        this.name = name;
        this.isDead = false;
        this.out = out;
        this.acceptedOffer = false;
        this.server = server;
    }

    public int attack() {
        int chance = Utils.random(1,3);
        int attackValue = attackPower;
        if(chance == 1) {
            attackValue = specialChance;
        }
        return attackValue;
    }

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

    public int suffer(int damage) throws IOException {
        if(healthPoints - damage <= 0) {
            setHealthPoints(0);
            isDead = true;
            server.broadcast(name + " is dead!");
            return 0;
        }
        healthPoints -= damage;
        server.broadcast(name + " has suffered " + damage + " points of damage and now has " + healthPoints + " healthpoints remaining!");

        return healthPoints;
    }

    public BufferedWriter getOut(){
        return this.out;
    }

    public void setFullHealth() {
        healthPoints = 10;
    }

    public void setAcceptedOffer() {

        acceptedOffer = true;
    } // toDo this


    public boolean getAcceptedOffer() {
        return acceptedOffer;
    } // toDo this

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public static class Rat {

        // PROPERTIES
        private int ratAttackPower;
        private String ratName;

        //METHODS
        public Rat(String ratName) {
            this.ratName = ratName;
            this.ratAttackPower = 1;
        }

        public int getRatAttackPower() {
            return ratAttackPower;
        }

        public String getRatName() {
            return ratName;
        }

        public void setRatName(String ratName) {
            this.ratName = ratName;
        }
    }
}
