package com.brutushammerfist.text;

public class PlayerCharacter {
    private String name;
    private String playClass;
    private int currencyPouch;
    private int health;
    private int maxHealth;
    private int power;

    public PlayerCharacter() {}

    PlayerCharacter(String name, String playClass, int currencyPouch, int health, int power) {
        this.name = name;
        this.playClass = playClass;
        this.currencyPouch = currencyPouch;
        this.health = health;
        this.maxHealth = health;
        this.power = power;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    // Take in attack type and then calculate random number based on attack and power
    public int attack() {
        return this.power;
    }

    public String getName() {
        return this.name;
    }

    public String getPlayClass() {
        return this.playClass;
    }

    public int getCurrencyPouch() {
        return this.currencyPouch;
    }

    public int getHealth() {
        return this.health;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayClass(String playClass) {
        this.playClass = playClass;
    }

    public void setCurrencyPouch(int currencyPouch) {
        this.currencyPouch = currencyPouch;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}