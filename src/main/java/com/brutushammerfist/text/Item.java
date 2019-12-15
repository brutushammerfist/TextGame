package com.brutushammerfist.text;

public class Item {
    private String name;
    private ItemType type;
    private int stackSize;

    public enum ItemType {
        WEAPON,
        CONSUMABLE,
        ARMOR,
        BAG,
        MISC;
    }

    public Item() {}

    public Item(String name, ItemType type, int size) {
        this.name = name;
        this.type = type;
        this.stackSize = size;
    }

    private void setName(String name) { this.name = name; }

    private void setType(ItemType type) { this.type = type; }

    private void setStackSize(int size) { this.stackSize = size; }

    public String getName() { return this.name; }

    public ItemType getType() { return this.type; }

    public int getStackSize() { return this.stackSize; }
}