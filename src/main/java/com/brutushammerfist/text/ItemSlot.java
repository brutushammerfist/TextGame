package com.brutushammerfist.text;

public class ItemSlot {
    private Item item;
    private SlotType type;

    public enum SlotType {
        HELM,
        CHEST,
        PANTS,
        BOOTS,
        HANDS,
        MAIN_HAND,
        OFF_HAND,
        BAG,
        INVENTORY;
    }
}
