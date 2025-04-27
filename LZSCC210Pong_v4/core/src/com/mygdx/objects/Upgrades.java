package com.mygdx.objects;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.pong.PongGame;
import java.util.HashMap;
import java.util.Map;

public class Upgrades extends ScreenAdapter {
    private Map<String, Integer> upgradeCost = new HashMap<>(); // Item name -> quantity needed
    private Inventory inventory;
    private SpriteBatch batch;
    private Texture upgradeHUD;
    private PongGame game;
    private Player player;

    private Stage stage;

    public Upgrades(Inventory inventory, Map<String, Integer> upgradeCost) {
        this.inventory = inventory;
        this.upgradeCost = upgradeCost;
        this.batch = new SpriteBatch();
        upgradeHUD = new Texture("upgradeHUD.png"); 
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

    public void upgradeItem() {
        if (canAffordUpgrade()) {
            for (Map.Entry<String, Integer> entry : upgradeCost.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                inventory.removeItem(itemName, quantity);
            }
            System.out.println("Upgrade successful!");
        } else {
            System.out.println("Not enough resources to upgrade.");
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(upgradeHUD, 20, 20, 555, 581);
    }

    public void dispose() {
        upgradeHUD.dispose();
        if (stage != null) {
            stage.dispose();
        }
    }
}
