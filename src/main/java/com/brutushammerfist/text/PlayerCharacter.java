package com.brutushammerfist.text;

public class PlayerCharacter {
    private String name;
    private String playClass;
    private Integer currencyPouch;
    private Integer health;
    private Integer power;

    public PlayerCharacter() {}

    PlayerCharacter(String name, String playClass, Integer currencyPouch, Integer health, Integer power) {
        this.name = name;
        this.playClass = playClass;
        this.currencyPouch = currencyPouch;
        this.health = health;
        this.power = power;
    }

    public void takeDamage(Integer damage) {
        this.health -= damage;
    }

    // Take in attack type and then calculate random number based on attack and power
    public Integer attack() {
        return this.power;
    }

    public String getName() {
        return this.name;
    }

    public String getPlayClass() {
        return this.playClass;
    }

    public Integer getCurrencyPouch() {
        return this.currencyPouch;
    }

    public Integer getHealth() {
        return this.health;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayClass(String playClass) {
        this.playClass = playClass;
    }

    public void setCurrencyPouch(Integer currencyPouch) {
        this.currencyPouch = currencyPouch;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }
}