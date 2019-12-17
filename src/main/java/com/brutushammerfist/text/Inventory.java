package com.brutushammerfist.text;

import java.util.ArrayList;

public class Inventory {
    ArrayList<ItemSlot> inventory;
    int maxSize;

    public Inventory() {
        this.inventory = new ArrayList<>();
        this.maxSize = 3;
    }

    public Inventory(int size) {
        this.inventory = new ArrayList<>();
        this.maxSize = size;
    }

    public boolean addItem(Item toAdd) {
        // Do we have room to add a new item, if it's not already there?
        if (this.inventory.size() < this.maxSize) {
            boolean notAdded = true;
            // Search list for any already matching items
            for (ItemSlot itemSlot : this.inventory) {
                // If matching item found
                if (itemSlot.getItem().getName().equals(toAdd.getName()) && itemSlot.getItem().getType() == toAdd.getType()) {
                    // If not overflowing (i.e. adding more than max)
                    if ((itemSlot.getItem().getStackSize() + toAdd.getStackSize()) <= itemSlot.getItem().getMaxSize()) {
                        itemSlot.getItem().setStackSize(itemSlot.getItem().getStackSize() + toAdd.getStackSize());
                        notAdded = false;
                    } else {
                        // Overflowed, check if room for another item
                        if (this.inventory.size() < this.maxSize) {
                            // Add new item if there is room

                            // Calculate left over items
                            toAdd.setStackSize(toAdd.getStackSize() - (itemSlot.getItem().getMaxSize() - itemSlot.getItem().getStackSize()));
                            itemSlot.getItem().setStackSize(itemSlot.getItem().getMaxSize());

                            // Add leftovers
                            this.inventory.add(new ItemSlot(toAdd, ItemSlot.SlotType.ANY));
                            notAdded = false;
                        }
                    }
                }
            }
            if (notAdded) {
                this.inventory.add(new ItemSlot(toAdd, ItemSlot.SlotType.ANY));
            }
            return true;
        } else {
            return false;
        }
    }

    public void removeItem(Item toRemove) {
        for (ItemSlot itemSlot : this.inventory) {
            if (itemSlot.getItem().getName().equals(toRemove.getName()) && itemSlot.getItem().getType() == toRemove.getType()) {
                itemSlot.removeItem(toRemove);
            }
        }
    }
}
