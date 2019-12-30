package com.brutushammerfist.text;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PlayerCharacter {
	Gson gson = new Gson();
	
    private String name;
    private String playClass;
    private int currencyPouch;
    private int health;
    private int maxHealth;
    private int power;
	private JsonArray attacks;
	
	private AbstractMap<String, Stat> stats = new HashMap<>();

    private ItemSlot helm;
    private ItemSlot chest;
    private ItemSlot legs;
    private ItemSlot hands;
    private ItemSlot boots;
    private ItemSlot mainHand;
    private ItemSlot offHand;
    private ItemSlot bag;

    private Inventory inventory;

    public PlayerCharacter() {
        this.helm = new ItemSlot(ItemSlot.SlotType.HELM);
        this.chest = new ItemSlot(ItemSlot.SlotType.CHEST);
        this.legs = new ItemSlot(ItemSlot.SlotType.LEGS);
        this.hands = new ItemSlot(ItemSlot.SlotType.HANDS);
        this.boots = new ItemSlot(ItemSlot.SlotType.BOOTS);
        this.mainHand = new ItemSlot(ItemSlot.SlotType.MAIN_HAND);
        this.offHand = new ItemSlot(ItemSlot.SlotType.OFF_HAND);
        this.bag = new ItemSlot(ItemSlot.SlotType.BAG);

        this.inventory = new Inventory();
    }

    PlayerCharacter(String name, String playClass, int currencyPouch, int health, int power, JsonArray attacks) {
        this.name = name;
        this.playClass = playClass;
        this.currencyPouch = currencyPouch;
        this.health = health;
        this.maxHealth = health;
        this.power = power;
		this.attacks = attacks;

        this.helm = new ItemSlot(ItemSlot.SlotType.HELM);
        this.chest = new ItemSlot(ItemSlot.SlotType.CHEST);
        this.legs = new ItemSlot(ItemSlot.SlotType.LEGS);
        this.hands = new ItemSlot(ItemSlot.SlotType.HANDS);
        this.boots = new ItemSlot(ItemSlot.SlotType.BOOTS);
        this.mainHand = new ItemSlot(ItemSlot.SlotType.MAIN_HAND);
        this.offHand = new ItemSlot(ItemSlot.SlotType.OFF_HAND);
        this.bag = new ItemSlot(ItemSlot.SlotType.BAG);

        this.inventory = new Inventory();
    }

    public void takeDamage(int damage) { this.health -= damage; }

    public void heal(int amountToHeal) {
        if ((this.health + amountToHeal) > this.maxHealth) {
            this.health = this.maxHealth;
        } else {
            this.health += amountToHeal;
        }
    }

    public int attack(String name) {
		JsonObject attack = null;
		
		for (int i = 0; i < this.attacks.size(); i++) {
			if (this.attacks.get(i).getAsJsonObject().get("name").getAsString() == name) {
				attack = this.attacks.get(i).getAsJsonObject();
				break;
			}
		}
		
		JsonArray mods = attack.getAsJsonArray("mods");
		
		double modTotal = 0;
		for (int i = 0; i < mods.size(); i++) {
			modTotal = modTotal + this.stats.get(mods.get(i).getAsString()).getLvl();
		}
		modTotal = 1 + (modTotal / (mods.size() * 100));
		
		return (int) (attack.get("base").getAsInt() * modTotal);
	}

    public int getHealth() { return this.health; }
	
	public int getMaxHealth() { return this.maxHealth; }

    public void addItem(Item toAdd) {
        boolean check = this.inventory.addItem(toAdd);
		
		if (!check) {
			System.out.println("No room for item.");
		}
    }
	
	public void equipItem(String name) { 
		Item toEquip = this.inventory.findItem(name);
		
		if (toEquip == null) {
			
		} else {
			this.equip(toEquip);
		}
	}

    public void equip(Item toEquip) {
        switch (toEquip.getType()) {
            case ARMOR:
                switch (toEquip.getSecondaryType()) {
                    case HELM:
                        if (this.helm.getItem() == null) {
                            this.helm.setItem(toEquip);
							for (int i = 0; i < toEquip.getStats().size(); i++) {
								this.stats.get(toEquip.getStats().get(i).getAsJsonObject().get("name").getAsString()).addLvl(toEquip.getStats().get(i).getAsJsonObject().get("lvl").getAsInt());
							}
                        }
                        break;
                    case CHEST:
                        if (this.chest.getItem() == null) {
                            this.chest.setItem(toEquip);
							for (int i = 0; i < toEquip.getStats().size(); i++) {
								this.stats.get(toEquip.getStats().get(i).getAsJsonObject().get("name").getAsString()).addLvl(toEquip.getStats().get(i).getAsJsonObject().get("lvl").getAsInt());
							}
                        }
                        break;
                    case LEGS:
                        if (this.legs.getItem() == null) {
                            this.chest.setItem(toEquip);
							for (int i = 0; i < toEquip.getStats().size(); i++) {
								this.stats.get(toEquip.getStats().get(i).getAsJsonObject().get("name").getAsString()).addLvl(toEquip.getStats().get(i).getAsJsonObject().get("lvl").getAsInt());
							}
                        }
                        break;
                    case HANDS:
                        if (this.hands.getItem() == null) {
                            this.hands.setItem(toEquip);
							for (int i = 0; i < toEquip.getStats().size(); i++) {
								this.stats.get(toEquip.getStats().get(i).getAsJsonObject().get("name").getAsString()).addLvl(toEquip.getStats().get(i).getAsJsonObject().get("lvl").getAsInt());
							}
                        }
                        break;
                    case BOOTS:
                        if (this.boots.getItem() == null) {
                            this.hands.setItem(toEquip);
							for (int i = 0; i < toEquip.getStats().size(); i++) {
								this.stats.get(toEquip.getStats().get(i).getAsJsonObject().get("name").getAsString()).addLvl(toEquip.getStats().get(i).getAsJsonObject().get("lvl").getAsInt());
							}
                        }
                        break;
                }
                break;
            case WEAPON:
                if (this.mainHand.getItem() == null) {
                    this.mainHand.setItem(toEquip);
					for (int i = 0; i < toEquip.getStats().size(); i++) {
						this.stats.get(toEquip.getStats().get(i).getAsJsonObject().get("name").getAsString()).addLvl(toEquip.getStats().get(i).getAsJsonObject().get("lvl").getAsInt());
					}
                } else if (this.offHand.getItem() == null) {
                    this.offHand.setItem(toEquip);
					for (int i = 0; i < toEquip.getStats().size(); i++) {
						this.stats.get(toEquip.getStats().get(i).getAsJsonObject().get("name").getAsString()).addLvl(toEquip.getStats().get(i).getAsJsonObject().get("lvl").getAsInt());
					}
				}
                break;
        }
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
	
	public JsonArray getAttacks() {
		return this.attacks;
	}
	
	public Inventory getInv() {
		return this.inventory;
	}
}