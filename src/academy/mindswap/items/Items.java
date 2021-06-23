package academy.mindswap.items;

public enum Items {

    ORACLE_DOCUMENTATION_BOOK("A ranged weapon to hit your opponents from far.", 1),
    KEYBOARD("A close combat weapon.", 1),
    GARBAGE_BIN("A close combat weapon.", 1),
    TRANSISTOR("A ranged weapon to hit your opponents from far.", 3),
    LEGOS("A ranged weapon to hit your opponents from far.", 3),
    PENTIUM_2("A mass destruction weapon.", 5);

    private int attackPower;
    private String description;


    Items(String description, int attackPower) {
        this.attackPower =  attackPower;
        this.description =  description;
    }

}
