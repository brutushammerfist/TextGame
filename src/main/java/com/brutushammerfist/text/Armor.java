package com.brutushammerfist.text;

import java.util.ArrayList;

public class Armor extends Item {
    public enum ArmorType {
        HELM,
        CHEST,
        LEGS,
        BOOTS,
        HANDS
    }

    public Armor(String name, ArmorType type, String description) {
        this.name = name;
        this.stackSize = 1;
        this.maxStackSize = 1;
		this.description = description;

        this.slotTypes = new ArrayList<>();
        switch (type) {
            case HELM:
                this.slotTypes.add(ItemSlot.SlotType.HELM);
                this.slotTypes.add(ItemSlot.SlotType.ANY);
                break;
            case CHEST:
                this.slotTypes.add(ItemSlot.SlotType.CHEST);
                this.slotTypes.add(ItemSlot.SlotType.ANY);
                break;
            case LEGS:
                this.slotTypes.add(ItemSlot.SlotType.LEGS);
                this.slotTypes.add(ItemSlot.SlotType.ANY);
                break;
            case BOOTS:
                this.slotTypes.add(ItemSlot.SlotType.BOOTS);
                this.slotTypes.add(ItemSlot.SlotType.ANY);
                break;
            case HANDS:
                this.slotTypes.add(ItemSlot.SlotType.HANDS);
                this.slotTypes.add(ItemSlot.SlotType.ANY);
                break;
        }
        this.type = ItemType.ARMOR;
    }

    @Override
    public ItemSlot.SlotType getSecondaryType() {
        for (ItemSlot.SlotType type : this.slotTypes) {
            if (type != ItemSlot.SlotType.ANY) {
                return type;
            }
        }
        return null;
    }
}
