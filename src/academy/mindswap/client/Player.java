package academy.mindswap.client;

import academy.mindswap.items.Items;
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
    Rat rat = new Rat("minder"); // toDo set rat name when asked during storyline
    private BufferedWriter out;

    // METHODS

    /**
     * Constructor Method
     * @param name - the name that the user typed
     * @param out - passed in buffered writer
     */
    public Player(String name, BufferedWriter out) {
        this.healthPoints = 10;
        this.attackPower = 1;
        this.specialChance = 20;
        this.name = name;
        this.isDead = false;
        this.out = out;
        this.acceptedOffer = false;
    }

    /**
     * Attack Method - Calculates the power of the attack
     * @return -> the value to decrement to opponents health
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
     * Search Item Method - makes the player look for an item in a fight situation. The item he finds is decided based
     * on a random number.
     * @throws IOException
     */
    public void searchItem() throws IOException {
        int chance = Utils.random(0,10);
        switch(chance) {
            case 1:
            case 2:
                attackPower += Items.ORACLE_DOCUMENTATION_BOOK.getAttackPower();
                out.write(Items.ORACLE_DOCUMENTATION_BOOK.getDescription()); //toDo
                break;
            case 3:
            case 4:
                attackPower += Items.KEYBOARD.getAttackPower();
                out.write(Items.KEYBOARD.getDescription()); //toDo
                break;
            case 5:
            case 6:
                attackPower += Items.GARBAGE_BIN.getAttackPower();
                out.write(Items.GARBAGE_BIN.getDescription()); //toDo
                break;
            case 7:
                attackPower += Items.TRANSISTOR.getAttackPower();
                out.write(Items.TRANSISTOR.getDescription()); //toDo
                break;
            case 8:
                attackPower += Items.PENTIUM_2.getAttackPower();
                out.write(Items.PENTIUM_2.getDescription()); //toDo
                break;
            case 9:
            case 10:
                out.write(name + " couldn't find anything...");
                break;
        }
    }

    public int callRat() {
        return rat.getRatAttackPower();
    }

    /**
     *
     * @param damage -> the amount of damage the target is going to suffer
     * @throws IOException
     */
    public void suffer(int damage) throws IOException {
        if(healthPoints - damage <= 0) {
            setHealthPoints(0);
            isDead = true;
            out.write(name + " is dead!");
            return;
        }
        out.write(name + " have " + healthPoints + " healthpoints remaining!");
        //toDo print healthpoints remaining
    }

    /**
     * Set Accepted Offer - In case the player accepted the offer, this variable is changed to true
     */
    public void setAcceptedOffer() {

        acceptedOffer = true;
    } // toDo this


    /**
     * Get Accepted Offer - check if the player accepted the offer
     * @return -> true/false
     */
    public boolean getAcceptedOffer() {
        return acceptedOffer;
    } // toDo this

    /**
     * Set Health Points - Sets the health points to the passed in argument
     * @param healthPoints -> the new health points to be set
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }


    public static class Rat {

        // PROPERTIES
        private int ratAttackPower;
        private String ratName;

        //METHODS
        public Rat(String ratName) {
            this.ratName = ratName;
        }

        /**
         *
         * @return -> Rat's attack power
         */
        public int getRatAttackPower() {
            return ratAttackPower;
        }

        /**
         *
         * @return -> Rat's name
         */
        public String getRatName() {
            return ratName;
        }

        /**
         *
         * @param ratName -> The rat name to be set
         */
        public void setRatName(String ratName) {
            this.ratName = ratName;
        }
    }
}
