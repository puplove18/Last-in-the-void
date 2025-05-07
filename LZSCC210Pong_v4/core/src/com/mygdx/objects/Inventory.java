package com.mygdx.objects;

import java.util.HashMap;
import java.util.Map;
import com.mygdx.helpers.ResourceType;

public class Inventory {
    private Map<String, Integer> items;
    private int maxSize;
    private boolean avoidInfiniteInventoryEmpty = false;
    private boolean hasPrintedInventory = false;
    private static final int MAX_ITEMS_PER_SLOT = 300; // Maximum number of items per slot
    
    public Inventory(int maxSize) {
        this.items = new HashMap<>();
        this.maxSize = maxSize;
    }

    // Add items
    public boolean addItem(String item) {
        // Validate the resource type first
        if (!ResourceType.isValidResource(item)) {
            System.out.println("Invalid resource : " + item);
            return false;
        }

        if (!items.containsKey(item) && items.size() >= maxSize) {
            System.out.println("Inventory is full. Cannot add " + item);
            return false;
        }
        
        int currentQuantity = items.getOrDefault(item, 0);
        if (currentQuantity >= MAX_ITEMS_PER_SLOT) {
            System.out.println("Slot for " + item + " is full (max " + MAX_ITEMS_PER_SLOT + "). Item discarded.");
            return false;
        }
        
        // Increment the quantity if the item is already in the inventory
        items.put(item, currentQuantity + 1);
        System.out.println(item + " added to inventory.");
        hasPrintedInventory = false; 
        return true;
    }

    // Additional add item method so we can add multiple of an item, you can delete the old method if necessary
    public boolean addItem(String item, int quantity) { 
        // Validate the resource type first
        if (!ResourceType.isValidResource(item)) {
            System.out.println("Invalid resource : " + item);
            return false;
        }
        
        if (quantity <= 0) {
            System.out.println("Cannot add zero or negative quantity of " + item);
            return false; 
        }
    
        if (!items.containsKey(item) && items.size() >= maxSize) {
             System.out.println("Inventory is full (max distinct item types reached). Cannot add new item " + item);
             return false;
        }
    
        // Check current quantity and apply slot limit
        int currentQuantity = items.getOrDefault(item, 0);
        int availableSpace = MAX_ITEMS_PER_SLOT - currentQuantity;
        
        if (availableSpace <= 0) {
            System.out.println("Slot for " + item + " is already full (max " + MAX_ITEMS_PER_SLOT + "). Items discarded.");
            return false;
        }
        
        // Calculate how many items can be added without exceeding the limit
        int actualQuantityToAdd = Math.min(quantity, availableSpace);
        
        // If we're not adding all requested items, inform the player
        if (actualQuantityToAdd < quantity) {
            int discarded = quantity - actualQuantityToAdd;
            System.out.println("Added " + actualQuantityToAdd + "x " + item + " to inventory. " + 
                               discarded + " items discarded (slot limit of " + MAX_ITEMS_PER_SLOT + " reached).");
        } else {
            System.out.println(actualQuantityToAdd + "x " + item + " added to inventory.");
        }
        
        // Update the inventory
        items.put(item, currentQuantity + actualQuantityToAdd);
        hasPrintedInventory = false;
        return true;
    }

    // Remove items
    public boolean removeItem(String item, int quant) {
        if (items.containsKey(item)) {
            int quantity = items.get(item);
            if (quantity - quant < 1) {
                items.remove(item); 
            } else {
                items.put(item, quantity - quant);  
            }
            System.out.println(item + " removed from inventory.");
            hasPrintedInventory = false;
            return true;
        } else {
            System.out.println(item + " not found in inventory.");
            return false;
        }
    }

    // Display contents
    public void showInventory() {
        if (items.isEmpty()) {
            if (!avoidInfiniteInventoryEmpty) {
                System.out.println("Inventory is empty.");
                avoidInfiniteInventoryEmpty = true;
            }
        } else {
            // Only print the inventory once until it's modified
            if (!hasPrintedInventory) {
                System.out.println("Inventory: ");
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    System.out.println(entry.getKey() + " x" + entry.getValue());
                }
                hasPrintedInventory = true; 
            }
        }
    }
    
    // Check if inventory contains an item
    public boolean hasItem(String item) {
        return items.containsKey(item);
    }

    // Check quantity of a specific item
    public Integer checkItemQuantity(String item) {
        return items.getOrDefault(item, 0); // Returns 0 if the item is not found
    }

    public Map<String, Integer> getItems() {
        if (items == null) {
            items = new HashMap<>();
        }
        return items;
    }
    
    // Get the maximum number of items per slot
    public int getMaxItemsPerSlot() {
        return MAX_ITEMS_PER_SLOT;
    }
}
