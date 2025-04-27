package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.helpers.ScreenType;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.objects.Upgrades;
import com.mygdx.pong.PongGame;
import com.mygdx.screens.GameScreen;
import com.mygdx.ui.UpgradesUI;
import java.util.HashMap;
import java.util.Map;


/**
 * Manages all UI elements including inventory, upgrades, and buttons
 */
public class UIManager {
    private GameScreen gameScreen;
    private Inventory inventory;
    private Player player;
    private boolean inventoryOpen = false;
    private Upgrades upgrades;
    private boolean showUpgradesGUI = false;

    private UpgradesUI upgradesUI;
    private Texture upgradesBackground;
    private NinePatchDrawable upgradesPanel;
    // UI components
    private SpriteBatch uiBatch;
    private Stage uiStage;
    private Stage inventoryStage;
    private Stage upgradesStage;
    private Skin skin;
    private TextureAtlas backgroundAtlas;
    private TextureAtlas.AtlasRegion inventoryBackground;
    
    public UIManager(Inventory inventory, Player player, GameScreen gameScreen) {
        this.inventory = inventory;
        this.player = player;
        this.gameScreen = gameScreen;

        // Create a Map for upgrade costs, here it's just an example with "Iron" needing 50
        Map<String, Integer> upgradeCost = new HashMap<>();
        upgradeCost.put("Iron", 50); // Example, you can add more items and costs as needed

        // Pass the map into the Upgrades constructor
        this.upgrades = new Upgrades(inventory, upgradeCost);

        this.upgradesUI = new UpgradesUI(inventory);

        initializeUI();
    }

    
    private void initializeUI() {
        this.uiBatch = new SpriteBatch();
        loadInventoryTextures();
        setupInventoryUI();
        this.uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.upgradesStage = new Stage(new ScreenViewport());
        

    
        
        createExitButton();
        createInventoryButton();
        createUpgradesButton();
    }
    
    
    private void loadInventoryTextures() {
        backgroundAtlas = new TextureAtlas(Gdx.files.internal("backgrounds.atlas"));
        inventoryBackground = backgroundAtlas.findRegion("InventoryHUD002 (1)");
    }
    
    private void setupInventoryUI() {
        inventoryStage = new Stage(new FitViewport(
                PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight()));
    }
    
    private void createExitButton() {
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PongGame.getInstance().changeScreen(gameScreen, ScreenType.MENU_UI);
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.top().right().pad(10);
        table.add(exitButton).width(100).height(50);
        uiStage.addActor(table);
    }
    
    private void createInventoryButton() {
        TextButton inventoryButton = new TextButton("Inventory", skin);
        inventoryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!gameScreen.getEventManager().isEventActive()) {
                    toggleInventory();
                    closeUpgrades();
                    
                    Gdx.input.setInputProcessor(inventoryOpen ? inventoryStage : uiStage);
                }
            }
        });
        
        Table inventoryTable = new Table();
        inventoryTable.setFillParent(true);
        inventoryTable.bottom().left().pad(10);
        inventoryTable.add(inventoryButton).width(120).height(50);
        
        uiStage.addActor(inventoryTable);
    }

    private void createUpgradesButton() {
        TextButton upgradesButton = new TextButton("Upgrades", skin);
        upgradesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!gameScreen.getEventManager().isEventActive()) {
                    toggleUpgrades();
                    closeInventory();
    
                    upgradesUI.setVisible(showUpgradesGUI);
                    Gdx.input.setInputProcessor(showUpgradesGUI ? upgradesStage : uiStage);

                }
            }
        });
    
        Table upgradesTable = new Table();
        upgradesTable.setFillParent(true);
        upgradesTable.bottom().left().pad(10).padLeft(150); // Shift a bit right of inventory
        upgradesTable.add(upgradesButton).width(120).height(50);
    
        uiStage.addActor(upgradesTable);
    }
    
    
    public void render(float delta) {
    // Render UI elements
        uiBatch.begin();
        if (inventoryOpen && !gameScreen.getEventManager().isEventActive()) {
            renderInventoryUI();
        }
        uiBatch.end();

        // Render the main UI stage unless event is showing
        if (!gameScreen.getEventManager().isEventActive()) {
            if (showUpgradesGUI) {
                Gdx.input.setInputProcessor(upgradesUI.getStage()); // <- force input here
                upgradesUI.render(); // Handle background + UI draw
            } else {
                Gdx.input.setInputProcessor(uiStage); // switch back
                uiStage.act(delta);
                uiStage.draw();
            }
        }
    }

    
    
    private void renderInventoryUI() {
        float scaledWidth = inventoryBackground.getRegionWidth() * 0.85f;
        float scaledHeight = inventoryBackground.getRegionHeight() * 0.85f;
        
        uiBatch.draw(inventoryBackground,
            (PongGame.getInstance().getWindowWidth() - scaledWidth) / 2,
            (PongGame.getInstance().getWindowHeight() - scaledHeight) / 2,
            scaledWidth, scaledHeight);
        
        BitmapFont font = FancyFontHelper.getInstance().getFont(Color.WHITE, 16);
        
        float startX = (PongGame.getInstance().getWindowWidth() - scaledWidth) / 2 + 50;
        float startY = (PongGame.getInstance().getWindowHeight() + scaledHeight) / 2 - 50;
        
        font.draw(uiBatch, "INVENTORY testing", startX, startY);

        // close button
        float closeX = (PongGame.getInstance().getWindowWidth() + scaledWidth) / 2 - 30;
        float closeY = (PongGame.getInstance().getWindowHeight() + scaledHeight) / 2 - 30;
        
        font.draw(uiBatch, "X", closeX, closeY);

        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.input.getY();

            touchY = PongGame.getInstance().getWindowHeight() - touchY;
            
            if (touchX >= closeX - 15 && touchX <= closeX + 15 && 
                touchY >= closeY - 15 && touchY <= closeY + 15) {
                inventoryOpen = false;
                Gdx.input.setInputProcessor(uiStage);
            }
        }
        
        inventoryStage.act(Gdx.graphics.getDeltaTime());
        inventoryStage.draw();
    }
    
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        inventoryStage.getViewport().update(width, height, true);
    }
    
    public void dispose() {
        uiBatch.dispose();
        uiStage.dispose();
        inventoryStage.dispose();
        if (skin != null) skin.dispose();
        if (backgroundAtlas != null) backgroundAtlas.dispose();
        if (upgrades != null) upgrades.dispose();
        if (upgradesUI != null) upgradesUI.dispose();
    }
    
    // Getters and state management
    public boolean isInventoryOpen() {
        return inventoryOpen;
    }
    
    public boolean isUpgradesOpen() {
        return showUpgradesGUI;
    }
    
    public void toggleInventory() {
        inventoryOpen = !inventoryOpen;
    }
    
    public void toggleUpgrades() {
        showUpgradesGUI = !showUpgradesGUI;
    }
    
    public void closeInventory() {
        inventoryOpen = false;
    }
    
    public void closeUpgrades() {
        showUpgradesGUI = false;
    }
    
    public Stage getUIStage() {
        return uiStage;
    }
    
    public Stage getInventoryStage() {
        return inventoryStage;
    }
    
    public Upgrades getUpgrades() {
        return upgrades;
    }
    public Stage getUpgradesStage() {
        return upgradesStage;
    }
    
}
