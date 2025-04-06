package com.mygdx.objects;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.pong.PongGame;
import java.util.HashMap;
import java.util.Map;

public class Upgrades extends ScreenAdapter{
    private Map<String, Integer> tools = new HashMap<>();
    private Integer resourcesNeeded;
    private String itemName;
    private Integer askInventory;
    private boolean allowUpgrade;
    private Inventory inventory;
    private SpriteBatch batch;
    private Texture upgradeHUD;
    private PongGame game;
    private Player player;

    private Stage stage;
    public Upgrades(Inventory inventory, String itemName, Integer resourcesNeeded) {
        this.inventory = inventory;
        this.itemName = itemName;
        this.resourcesNeeded = resourcesNeeded;
        this.askInventory = inventory.checkItemQuantity(itemName); 
        this.batch = new SpriteBatch();
        upgradeHUD = new Texture("upgradeHUD.png"); 
       
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
    
    
    
    public void render(SpriteBatch batch) {
        batch.draw(upgradeHUD, 20, 20, 555, 581);

    }
    
    
    public void dispose() {
        upgradeHUD.dispose(); 
        stage.dispose();
    }
}
