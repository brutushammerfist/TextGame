package com.brutushammerfist.text;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Item {
    protected String name;
    protected ItemType type;
    protected int stackSize;
    protected int maxStackSize;
	protected String description;
	protected JsonArray stats;

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

    public Item(String name, ItemType type, int size, int maxSize, String description) {
        this.name = name;
        this.type = type;
        this.stackSize = size;
        this.maxStackSize = maxSize;
		this.description = description;
    }
	
	public Item(String name, ItemType type, int size, int maxSize, String description, JsonArray stats) {
        this.name = name;
        this.type = type;
        this.stackSize = size;
        this.maxStackSize = maxSize;
		this.description = description;
		this.stats = stats;
    }

    public void setName(String name) { this.name = name; }

    public void setType(ItemType type) { this.type = type; }

    public void setStackSize(int size) { this.stackSize = size; }

    public void setMaxSize(int size) { this.maxStackSize = size; }

    public String getName() { return this.name; }

    public ItemType getType() { return this.type; }

    public int getStackSize() { return this.stackSize; }

    public int getMaxSize() { return this.maxStackSize; }
	
	public JsonArray getStats() { return this.stats; }
}