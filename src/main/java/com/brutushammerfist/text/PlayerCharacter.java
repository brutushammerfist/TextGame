package com.brutushammerfist.text;

import java.util.*;

public class PlayerCharacter {
    private String name;
    private String playClass;
    private int currencyPouch;
    private int health;
    private int maxHealth;
    private int power;
	
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

    PlayerCharacter(String name, String playClass, int currencyPouch, int health, int power) {
        this.name = name;
        this.playClass = playClass;
        this.currencyPouch = currencyPouch;
        this.health = health;
        this.maxHealth = health;
        this.power = power;

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

    public int attack() { return this.power; }

    public int getHealth() { return this.health; }
	
	public int getMaxHealth() { return this.maxHealth; }

    public void addItem(Item toAdd) {
        this.inventory.addItem(toAdd);
    }

    public void equip(Item toEquip) {
        switch (toEquip.getType()) {
            case ARMOR:
                switch (toEquip.getSecondaryType()) {
                    case HELM:
                        if (this.helm.getItem() == null) {
                            this.helm.setItem(toEquip);
                        }
                        break;
                    case CHEST:
                        if (this.chest.getItem() == null) {
                            this.chest.setItem(toEquip);
                        }
                        break;
                    case LEGS:
                        if (this.legs.getItem() == null) {
                            this.chest.setItem(toEquip);
                        }
                        break;
                    case HANDS:
                        if (this.hands.getItem() == null) {
                            this.hands.setItem(toEquip);
                        }
                        break;
                    case BOOTS:
                        if (this.boots.getItem() == null) {
                            this.hands.setItem(toEquip);
                        }
                        break;
                }
                break;
            case WEAPON:
                if (this.mainHand.getItem() == null) {
                    this.mainHand.setItem(toEquip);
                } else if (this.offHand.getItem() == null) {
                    this.offHand.setItem(toEquip);
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
}