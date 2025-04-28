package com.mygdx.objects;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Integer> items;
    private int maxSize;
    private boolean avoidInfiniteInventoryEmpty = false;
    private boolean hasPrintedInventory = false; // Added flag to track if inventory has been printed

    public Inventory(int maxSize) {
        this.items = new HashMap<>();
        this.maxSize = maxSize;
    }

    // Add items
    public boolean addItem(String item) {
        if (items.size() >= maxSize) {
            System.out.println("Inventory is full. Cannot add " + item);
            return false;
        }
        
        // Increment the quantity if the item is already in the inventory
        items.put(item, items.getOrDefault(item, 0) + 1);
        System.out.println(item + " added to inventory.");
        hasPrintedInventory = false; // Reset flag when inventory changes
        return true;
    }

    // Additional add item method so we can add multiple of an item, you can delete the old method if necessary
    public boolean addItem(String item, int quantity) { // Added quantity parameter
        if (quantity <= 0) {
            System.out.println("Cannot add zero or negative quantity of " + item);
            return false; 
        }
    
        if (!items.containsKey(item) && items.size() >= maxSize) {
             System.out.println("Inventory is full (max distinct item types reached). Cannot add new item " + item);
             return false;
        }
    
        // Increment the quantity if the item is already in the inventory, or add it.
        items.put(item, items.getOrDefault(item, 0) + quantity); 
        System.out.println(quantity + "x " + item + " added to inventory.");
        hasPrintedInventory = false; // Reset flag when inventory changes
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
            hasPrintedInventory = false; // Reset flag when inventory changes
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
                avoidInfiniteInventoryEmpty = true; // Mark it as already reported
            }
        } else {
            // Only print the inventory once until it's modified
            if (!hasPrintedInventory) {
                System.out.println("Inventory: ");
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    System.out.println(entry.getKey() + " x" + entry.getValue());
                }
                hasPrintedInventory = true; // Mark that the inventory has been printed
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
}
