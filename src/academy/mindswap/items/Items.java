package academy.mindswap.items;

public enum Items {

    ORACLE_DOCUMENTATION_BOOK("A ranged weapon to hit your opponents from far. ", 1),
    KEYBOARD("A close combat weapon. ", 1),
    GARBAGE_BIN("A close combat weapon. ", 1),
    TRANSISTOR("A ranged weapon to hit your opponents from far. ", 3),
    PENTIUM_2("A mass destruction weapon. ", 5),
    NOTHING("A big pair of nothing. ", 0);


    private int attackPower;
    private String description;

    /**
     * Constructor Method
     * @param description -> item's description
     * @param attackPower -> item's attack power
     */
    Items(String description, int attackPower) {
        this.attackPower =  attackPower;
        this.description =  description;
    }

    /**
     * Get Attack Power - gets the item's attack power
     * @return -> the item's attack power
     */
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * Get Description - gets the item's description
     * @return -> the item's description
     */
    public String getDescription() {
        return description;
    }
}
