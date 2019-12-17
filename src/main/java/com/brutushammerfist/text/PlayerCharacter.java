package com.brutushammerfist.text;

public class PlayerCharacter {
    private String name;
    private String playClass;
    private int currencyPouch;
    private int health;
    private int maxHealth;
    private int power;

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

    public void addItem(Item toAdd) {
        this.inventory.addItem(toAdd);
    }

}