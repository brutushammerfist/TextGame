package com.brutushammerfist.text;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Random;

public class Monster{
    private String name;
    private String type;
    private String text;
    private int health;
    private int maxHealth;
    private JsonArray attacks;

    public Monster() {}

    Monster(String name, String type, int health, JsonArray attacks) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.maxHealth = health;
        this.attacks = attacks;
        this.text = null;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public JsonObject getAttack() {
        int atk = new Random().nextInt(this.attacks.size());
        return this.attacks.get(atk).getAsJsonObject();
    }

    public int attack() {
        return this.getAttack().get("power").getAsInt();
    }

    public JsonArray getAttacks() { return this.attacks; }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() { return this.maxHealth; }

    public String getText() { return this.text; }

    public void setAttacks(JsonArray atks) { this.attacks = atks; }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public void setText(String text) { this.text = text; }
}