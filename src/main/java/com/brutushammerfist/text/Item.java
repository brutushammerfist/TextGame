package com.brutushammerfist.text;

import java.util.ArrayList;

public class Item {
    protected String name;
    protected ItemType type;
    protected int stackSize;
    protected int maxStackSize;

    protected ArrayList<ItemSlot.SlotType> slotTypes;

    public ItemSlot.SlotType getSecondaryType() { return ItemSlot.SlotType.ANY; }

    public enum ItemType {
        WEAPON,
        CONSUMABLE,
        ARMOR,
        BAG,
        MISC;
    }

    public Item() {}

    public Item(String name, ItemType type, int size, int maxSize) {
        this.name = name;
        this.type = type;
        this.stackSize = size;
        this.maxStackSize = maxSize;
    }

    public void setName(String name) { this.name = name; }

    public void setType(ItemType type) { this.type = type; }

    public void setStackSize(int size) { this.stackSize = size; }

    public void setMaxSize(int size) { this.maxStackSize = size; }

    public String getName() { return this.name; }

    public ItemType getType() { return this.type; }

    public int getStackSize() { return this.stackSize; }

    public int getMaxSize() { return this.maxStackSize; }
}