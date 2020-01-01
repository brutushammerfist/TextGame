package com.brutushammerfist.text;

import java.util.ArrayList;

public class Inventory {
    ArrayList<ItemSlot> inventory;
    int maxSize;

    public Inventory() {
        this.inventory = new ArrayList<>();
        this.maxSize = 100;

        for (int i = 0; i < this.maxSize; i++) {
            this.inventory.add(new ItemSlot(ItemSlot.SlotType.ANY));
        }
    }

    public Inventory(int size) {
        this.inventory = new ArrayList<>();
        this.maxSize = size;

        for (int i = 0; i < this.maxSize; i++) {
            this.inventory.add(new ItemSlot(ItemSlot.SlotType.ANY));
        }
    }

    public boolean addItem(Item toAdd) {
        // Do we have room to add a new item, if it's not already there?
        /*if (this.inventory.size() < this.maxSize) {
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
                } else if (itemSlot.getItem() == null) {
                    itemSlot.setItem(toAdd);
                    notAdded = false;
                }
            }
            if (notAdded) {
                this.inventory.add(new ItemSlot(toAdd, ItemSlot.SlotType.ANY));
            }
            return true;
        } else {
            return false;
        }*/

        boolean notAdded = true;

        // Search ItemSlots for same item
        for (ItemSlot slot : this.inventory) {
            if (slot.getItem() != null) {
                // If matching item found...
                if (slot.getItem().getName().equals(toAdd.getName()) && slot.getItem().getType() == toAdd.getType()) {
                    // Check to ensure not overflowing, else handle overflow
                    if ((slot.getItem().getStackSize() + toAdd.getStackSize()) <= slot.getItem().getMaxSize()) {
                        slot.getItem().setStackSize(slot.getItem().getStackSize() + toAdd.getStackSize());
                        notAdded = false;
                        break;
                    } else {
                        toAdd.setStackSize(toAdd.getStackSize() - (slot.getItem().getMaxSize() - slot.getItem().getStackSize()));
                        slot.getItem().setStackSize(slot.getItem().getMaxSize());

                        for (ItemSlot slot2 : this.inventory) {
                            if (slot2.getItem() == null) {
                                slot2.setItem(toAdd);
                            }
                        }

                        notAdded = false;
                        break;
                    }
                }
            }
        }
        // Matching item not found in inventory...
        if (notAdded) {
            // Search for next available slot
            for (ItemSlot slot : this.inventory) {
                // If slot found...
                if (slot.getItem() == null) {
                    slot.setItem(toAdd);
                    notAdded = false;
                    break;
                }
            }
        }

        return !notAdded;
    }

    public void removeItem(Item toRemove) {
        for (ItemSlot itemSlot : this.inventory) {
            if (itemSlot.getItem() != null) {
                if (itemSlot.getItem().getName().equals(toRemove.getName()) && itemSlot.getItem().getType() == toRemove.getType()) {
                    itemSlot.setItem(null);
                    break;
                }
            }
        }
    }
	
	public int getMaxSize() {
		return this.maxSize;
	}
	
	public int size() {
		return this.inventory.size();
	}
	
	public Item findItem(String name) {
		for (ItemSlot slot : this.inventory) {
		    if (slot.getItem() != null) {
		        if (slot.getItem().getName().equals(name)) {
		            return slot.getItem();
                }
            }
        }

		return null;
	}
	
	public ItemSlot get(int index) {
		if (this.inventory.size() == 0) {
			return null;
		}
		return this.inventory.get(index);
	}
}
