package com.mygdx.objects;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.pong.PongGame;
import java.util.HashMap;
import java.util.Map;

public class Upgrades extends ScreenAdapter {
    private Map<String, Integer> upgradeCost = new HashMap<>(); // Item name -> quantity needed
    private Inventory inventory;
    private String upgradeName;

 
    private PongGame game;
    private Player player;

    private Stage stage;

    public Upgrades(Inventory inventory, Map<String, Integer> upgradeCost) {
        this.inventory = inventory;
        this.upgradeCost = upgradeCost;
        this.upgradeName = upgradeName;
    }

    public boolean canAffordUpgrade() {
        for (Map.Entry<String, Integer> entry : upgradeCost.entrySet()) {
            String itemName = entry.getKey();
            int requiredAmount = entry.getValue();
            int availableAmount = inventory.checkItemQuantity(itemName);
            if (availableAmount < requiredAmount) {
                return false;
            }
        }
        return true;
    }

    public void applyUpgrade() {
        if (canAffordUpgrade()) {
            for (Map.Entry<String, Integer> entry : upgradeCost.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                inventory.removeItem(itemName, quantity);
            }
            System.out.println(upgradeName + " upgrade successful!");
        } else {
            System.out.println("Not enough resources to upgrade.");
        }
    }

    public String getUpgradeName() {
        return upgradeName;
    }
}
