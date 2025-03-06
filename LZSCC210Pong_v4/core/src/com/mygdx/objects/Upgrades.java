package com.mygdx.objects;
import java.util.HashMap;
import java.util.Map;

public class Upgrades {
    private Map<String, Integer> tools = new HashMap<>();
    private Integer resourcesNeeded;
    private String itemName;
    private Integer askInventory;
    private boolean allowUpgrade;
    private Inventory inventory;

    public Upgrades(Inventory inventory, String itemName, Integer resourcesNeeded) {
        this.inventory = inventory;
        this.itemName = itemName;
        this.resourcesNeeded = resourcesNeeded;
        this.askInventory = inventory.checkItemQuantity(itemName); 

       
        if (askInventory >= resourcesNeeded) {
            this.allowUpgrade = true;
        } else {
            this.allowUpgrade = false;
        }
    }

    public boolean ifAllowUpgrade() {
        return allowUpgrade;
    }

    public void upgradeItem() {
        if (allowUpgrade) {
            inventory.removeItem(itemName, resourcesNeeded); 
            System.out.println(itemName + " has been upgraded!");
        } else {
            System.out.println("Not enough resources to upgrade " + itemName);
        }
    }
}
