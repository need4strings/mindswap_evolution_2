package academy.mindswap.client;

public class Player {

    // PROPERTIES
    private int healthPoints;
    private int attackPower;
    private int specialChance;
    private String name;
    private boolean isDead;
    private boolean acceptedOffer;

    // METHODS

    /**
     * Constructor Method
     * @param name - the name that the user typed
     */
    public Player(String name) {
        this.healthPoints = 10;
        this.attackPower = 1;
        this.specialChance = 20;
        this.name = name;
        this.isDead = false;
        this.acceptedOffer = false;
    }

    public void attack() {

    }

    public void searchItem() {

    }

    public void callRat() {

    }

    public class rat {

        // PROPERTIES
        private int attackPower;
        private String ratName;

        //METHODS
        public rat(String ratName) {
            this.ratName = ratName;
        }

    }

    public void setAcceptedOffer() {
        this.acceptedOffer = true;
    }

    public boolean getAcceptedOffer() {
        return this.acceptedOffer;
    }
}
