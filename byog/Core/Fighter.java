package byog.Core;

import java.util.Random;

public class Fighter {
    private double health;
    private double maxHealth;
    private double mana;
    private double maxMana;
    private int manaRegen;
    private int averageAttack;
    private int fireballDamage;
    private int healthDrainDamage;
    private int healAmount;
    private String name;
    private Random random = new Random();
    public static final int HEALCOST = 2000;
    public static final int HEALTHDRAINCOST = 425;
    public static final int FIREBALLCOST = 500;

    public Fighter(char archetype) {
        if (archetype == 'a') {
            instantiateWizard();
        } else if (archetype == 'd') {
            instantiateHercules();
        } else {
            instantiateBowser();
        }
    }

    /**
     * Creates the player to be focused on magic.
     */
    private void instantiateWizard() {
        this.health = 1500;
        this.maxHealth = health;
        this.mana = 1250;
        this.maxMana = 3000;
        this.manaRegen = 250;
        this.averageAttack = 300;
        this.fireballDamage = 1000;
        this.healthDrainDamage = 500;
        this.healAmount = 1000;

    }

    /**
     * Creates the player to be focused on attacking.
     */
    private void instantiateHercules() {
        this.health = 2500;
        this.maxHealth = health;
        this.mana = 625;
        this.maxMana = 2000;
        this.manaRegen = 125;
        this.averageAttack = 450;
        this.fireballDamage = 750;
        this.healthDrainDamage = 350;
    }

    /**
     * Only used when Bowser is being instantiated as the enemy.
     */
    private void instantiateBowser() {
        this.health = 6000;
        this.maxHealth = health;
        this.mana = Double.MAX_VALUE;
        this.maxMana = mana;
        this.manaRegen = 0;
        this.averageAttack = 200;
        this.fireballDamage = 250;
    }

    /**
     * Causes the fighter to take a specific amount of damage.
     * @param amount double representing the amount of damage being taken
     */
    private void decreaseHealth(double amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Increases the fighter's health by a given amount.
     */
    private void increaseHealth(double amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    /**
     * Decreases the fighter's mana by a given amount if it has enough mana.
     * @return true if the fighter had enough mana to cast the spell
     */
    public boolean decreaseMana(int spellCost) {
        if (this.mana >= spellCost) {
            this.mana -= spellCost;
            return true;
        }
        return false;
    }

    /**
     * Increases the fighter's mana by a given amount
     */
    public void increaseMana() {
        this.mana += this.manaRegen;
        if (this.mana > this.maxMana) {
            this.mana = this.maxMana;
        }
    }

    /**
     * Casts the fireball spell.
     * @param other the fighter that the spell is being cast on
     */
    public double castFireball(Fighter other) {
        if (decreaseMana(FIREBALLCOST)) {
            other.decreaseHealth(fireballDamage);
        }
        return fireballDamage;
    }

    /**
     * Casts the healthDrain spell, which deals damage and heals the fighter half of that.
     * @param other the fighter from which the health is being drained
     */
    public double castHealthDrain(Fighter other) {
        if (decreaseMana(HEALTHDRAINCOST)) {
            other.decreaseHealth(healthDrainDamage);
            this.increaseHealth(healthDrainDamage / 2);
        }
        return healthDrainDamage;
    }

    /**
     * Heals the fighter by their heal amount
     */
    public double castHeal() {
        if (decreaseMana(HEALCOST)) {
            this.increaseHealth(this.healAmount);
        }
        return healAmount;
    }

    /**
     * Deals a random amount of damage (averaged at attack damage) to the other fighter (range of 10).
     */
    public double attack(Fighter other) {
        double amount = 50 * random.nextDouble() + averageAttack - 25;
        other.decreaseHealth(amount);
        return amount;
    }

    /**
     * Returns a boolean representing if the player is still alive.
     */
    public boolean isAlive() {
        return (this.health > 0);
    }

    /**
     * Returns the player's health.
     */
    public double getHealth() {
        return this.health;
    }

    /**
     * Returns the player's mana.
     */
    public double getMana() {
        return this.mana;
    }

    /**
     * Returns the player's max health.
     */
    public double getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Returns the player's max mana.
     */
    public double getMaxMana() {
        return this.maxMana;
    }
}
