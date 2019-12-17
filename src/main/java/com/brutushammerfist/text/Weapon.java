package com.brutushammerfist.text;

import java.util.ArrayList;

public class Weapon extends Item {
    //Place Weapon stats here
    public Weapon(String name) {
        this.name = name;
        this.stackSize = 1;
        this.maxStackSize = 1;

        this.slotTypes = new ArrayList<>();
        this.slotTypes.add(ItemSlot.SlotType.MAIN_HAND);
        this.slotTypes.add(ItemSlot.SlotType.OFF_HAND);
        this.slotTypes.add(ItemSlot.SlotType.ANY);

        this.type = Item.ItemType.WEAPON;
    }
}