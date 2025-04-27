package com.mygdx.objects;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Integer> items;
    private int maxSize;
    private boolean avoidInfinteInventoryEmpty = false;
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
            return true;
        } else {
            System.out.println(item + " not found in inventory.");
            return false;
        }
    }

    // Display contents
    public void showInventory() {
        if (items.isEmpty()) {
            if (!avoidInfinteInventoryEmpty) {
                System.out.println("Inventory is empty.");
                avoidInfinteInventoryEmpty = true; // Mark it as already reported
            }
        } else {
            avoidInfinteInventoryEmpty = false; // Reset the flag because we have items again
            System.out.println("Inventory: ");
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                System.out.println(entry.getKey() + " x" + entry.getValue());
            }
        }
    }
    

    // Check if inventory contains an item
    public boolean hasItem(String item) {
        return items.containsKey(item);
    }
    public Integer checkItemQuantity(String item) {
        return items.getOrDefault(item, 0); // Returns 0 if the item is not found
    } 
}
