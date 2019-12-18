package com.brutushammerfist.text;

public class ItemSlot {
    private Item item;
    private SlotType type;

    public enum SlotType {
        HELM,
        CHEST,
        LEGS,
        BOOTS,
        HANDS,
        MAIN_HAND,
        OFF_HAND,
        BAG,
        ANY;
    }

    public ItemSlot(SlotType type) {
        this.item = null;
        this.type = type;
    }

    public ItemSlot(Item item, SlotType type) {
        this.item = item;
        this.type = type;
    }

    public void addItem(Item toAdd) {
        if (this.item == null) {
            this.item = toAdd;
        }
    }

    public Item getItem() {
        return this.item;
    }

    public void removeItem(Item toRemove) {
        if ((this.item.getStackSize() - toRemove.getStackSize()) >= 0) {
            this.item.setStackSize(this.item.getStackSize() - toRemove.getStackSize());
        }
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
