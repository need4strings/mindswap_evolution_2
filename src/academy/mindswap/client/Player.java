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

    public int attack() {
        int chance = Utils.random(1,3);
        int attackValue = attackPower;
        if(chance == 1) {
            attackValue = specialChance;
        }
        return attackValue;
    }

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

    public void setAcceptedOffer() {

        acceptedOffer = true;
    } // toDo this


    public boolean getAcceptedOffer() {
        return acceptedOffer;
    } // toDo this

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
