package com.brutushammerfist.text;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Random;

public class Monster {
    private String name;
    private String type;
    private Integer health;
    private JsonArray attacks;

    public Monster() {}

    Monster(String name, String type, Integer health, JsonArray attacks) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.attacks = attacks;
    }

    public void takeDamage(Integer damage) {
        this.health -= damage;
    }

    public JsonObject getAttack() {
        Integer atk = new Random().nextInt(this.attacks.size());
        return this.attacks.get(atk).getAsJsonObject();
    }

    public Integer attack() {
        return this.getAttack().get("power").getAsInt();
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public Integer getHealth() {
        return this.health;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHealth() {
        this.health = health;
    }
}