package academy.mindswap.enemies;

public class Enemies {

    private int healthPoints;
    private int attackPower;
    private String name;
    private boolean isDead;

    /**
     * Constructor Method
     * @param healthPoints -> the health point the enemy has
     * @param attackPower -> the attack power the enemy has
     * @param name -> the enemy's name
     * @param isDead -> dead enemy flag
     */
    public Enemies(int healthPoints, int attackPower, String name, boolean isDead) {
        this.healthPoints = healthPoints;
        this.attackPower = attackPower;
        this.name = name;
        this.isDead = isDead;

    }

    /**
     * Suffer - enemy's suffer method
     * @param damage -> the amount of damage to be suffered
     */
    public void suffer(int damage) {

        if(healthPoints - damage <= 0) {
            setHealthPoints(0);
            this.isDead = true;
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
