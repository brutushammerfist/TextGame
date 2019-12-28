package com.brutushammerfist.text;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

public class Monster{
    private String name;
    private String type;
	private String spawnText;
    private String text;
    private int health;
    private int maxHealth;
    private JsonArray attacks;
	private JsonArray attackMods;
	
	private AbstractMap<String, Stat> stats = new HashMap<>();

    public Monster() {}

    Monster(String name, String type, int health, JsonArray attacks) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.maxHealth = health;
        this.attacks = attacks;
        this.text = null;
		this.spawnText = null;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public JsonObject getAttack() {
        int atk = new Random().nextInt(this.attacks.size());
        return this.attacks.get(atk).getAsJsonObject();
    }

    public int attack() {
		JsonObject attack = this.getAttack();
		
		this.text = this.spawnText + "\n\n\n" + attack.get("text").getAsString();
		
		JsonArray mods = attack.getAsJsonArray("mods");
		
		double modTotal = 0;
		for (int i = 0; i < mods.size(); i++) {
			modTotal = modTotal + this.stats.get(mods.get(i).getAsString()).getLvl();
		}
		modTotal = 1 + (modTotal / (mods.size() * 100));
    
		return (int) (attack.get("base").getAsInt() * modTotal);
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

    public void setText(String text) { 
		this.text = text;
		this.spawnText = text;
	}
	
	public ArrayList<Stat> getStats() {
        ArrayList<Stat> tempList = new ArrayList<>();
		
		Set<String> keys = this.stats.keySet();
		String[] keysArray = keys.toArray(new String[0]);
		
		for (String key : keysArray) {
			tempList.add(this.stats.get(key));
		}
		
		return tempList;
    }
	
	public void addStat(Stat toAdd) {
		this.stats.put(toAdd.getName(), toAdd);
	}
}