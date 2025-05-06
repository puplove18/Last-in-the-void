package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.events.RadiantNebulaEvent;
import com.mygdx.events.AggressiveRobotsEvent;
import com.mygdx.events.*;
import com.mygdx.helpers.ScreenType;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.objects.Universe;
import com.mygdx.objects.Upgrades;
import com.mygdx.pong.PongGame;
import com.mygdx.screens.GameScreen;
import com.mygdx.ui.InventoryUI;
import com.mygdx.ui.ScannerUI;
import com.mygdx.ui.UpgradesUI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.Arrays;
import java.util.List;
import com.mygdx.objects.Event;

public class UIManager {
    private GameScreen gameScreen;
    private Inventory inventory;
    private Player player;
    private boolean inventoryOpen = false;
    private Upgrades upgrades;
    private boolean showUpgradesGUI = false;
    private boolean scannerOpen = false;
    private Universe universe;

    private int systemJumpCount = 0;
    private UpgradesUI upgradesUI;
    private InventoryUI inventoryUI;
    private ScannerUI scannerUI;
    private Texture upgradesBackground;
    private NinePatchDrawable upgradesPanel;
    // UI components
    private SpriteBatch uiBatch;
    private Stage uiStage;
    private Stage inventoryStage;
    private Stage upgradesStage;
    private Stage scannerStage;
    private Skin skin;
    private TextureAtlas backgroundAtlas;
    private TextureAtlas.AtlasRegion inventoryBackground;


    //events stuff
    private final Random rand = new Random();
    private final List<Supplier<Event>> negativeEvents = Arrays.asList(
            () -> new AggressiveRobotsEvent(upgradesUI),
            () -> new MeteorEvent(upgradesUI),
            () -> new CosmicStormEvent(upgradesUI),
            () -> new DerelictShipEvent(upgradesUI),
            () -> new EngineFailureEvent(upgradesUI),
            () -> new FriendlyTraderEvent(upgradesUI),
            () -> new RadiantNebulaEvent(upgradesUI),
            () -> new SpaceParasitesEvent(upgradesUI)

    );

    public UIManager(Inventory inventory, Player player, GameScreen gameScreen, Universe universe) {
        this.inventory = inventory;
        this.player = player;
        this.gameScreen = gameScreen;
        this.universe = universe;

        Map<String, Integer> upgradeCost = new HashMap<>();
        


        this.inventoryUI = new InventoryUI(PongGame.getInstance(), player);
        this.upgradesUI = new UpgradesUI(player,player.getInventory(), universe, inventoryUI);
        this.scannerUI = new ScannerUI(player, universe);
        scannerUI.setDestinationListener(idx -> {
            //fuel cost logic for system
            float cost = 25f;
            if (player.getFuel() < cost) {
                System.out.println("Not enough fuel!");
                return;
            }
            player.setFuel(player.getFuel() - cost);

            //actually change
            gameScreen.getWorldManager().travelTo(idx);

            systemJumpCount++;
            if (systemJumpCount % 2 == 0) {
                //every 2nd  jump
                Event e = negativeEvents.get(rand.nextInt(negativeEvents.size())).get();
                gameScreen.getEventManager().setCurrentEvent(e);
                gameScreen.getEventManager().showCurrentEvent();
                gameScreen.setPaused(true);
                return;
            }

            //close scanner
            scannerUI.setVisible(false);
            scannerOpen = false;
            Gdx.input.setInputProcessor(uiStage);
        });
        
        inventoryUI.setCloseButtonListener(new InventoryUI.CloseButtonListener() {
            @Override
            public void onCloseButtonClicked() {
                toggleInventory();
            }
        });

        initializeUI();
        upgradesUI.setCloseButtonListener(new UpgradesUI.CloseButtonListener() {
            @Override
            public void onCloseButtonClicked() {
                toggleUpgrades();
            }
        });

        initializeUI();

        scannerUI.setCloseButtonListener(new ScannerUI.CloseButtonListener() {
            @Override
            public void onCloseButtonClicked() {
                toggleScanner();
            }
        });

        initializeUI();
    }
        
    
    private void initializeUI() {
        this.uiBatch = new SpriteBatch();
        this.uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.upgradesStage = new Stage(new ScreenViewport());
        this.inventoryStage = inventoryUI.getStage();
        this.scannerStage = scannerUI.getStage();
        
        
        createExitButton();
        createInventoryButton();
        createUpgradesButton();
        createScannerButton();
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
                    closeScanner();
                    
                    inventoryUI.setVisible(inventoryOpen);
                    Gdx.input.setInputProcessor(uiStage);
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
                    closeScanner();
    
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
    private void createScannerButton() {
        TextButton scannerButton = new TextButton(" Next System", skin);
        scannerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!gameScreen.getEventManager().isEventActive()) {
                    toggleScanner();
                    closeInventory();
                    closeUpgrades();
    
                    upgradesUI.setVisible(scannerOpen);
                    Gdx.input.setInputProcessor(scannerOpen ? scannerStage : uiStage);

                }
            }
        });
        Table scannerTable = new Table();
        scannerTable.setFillParent(true);
        scannerTable.bottom().left().pad(10).padLeft(300);
        scannerTable.add(scannerButton).width(120).height(50);
        
        uiStage.addActor(scannerTable);
    }
    
    
    public void render(float delta) {
        // Render UI elements
        if (!gameScreen.getEventManager().isEventActive()) {
            if (showUpgradesGUI) {
                Gdx.input.setInputProcessor(upgradesUI.getStage());
                upgradesUI.render(); 
            } 
            else if (inventoryOpen) {
                Gdx.input.setInputProcessor(inventoryUI.getStage());
                inventoryUI.render(); 
            }
            else if (scannerOpen) {
            Gdx.input.setInputProcessor(scannerUI.getStage());
            scannerUI.render();
            }
            else {
                Gdx.input.setInputProcessor(uiStage);
                uiStage.act(delta);
                uiStage.draw();
            }
        }
    }

    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        inventoryStage.getViewport().update(width, height, true);
        upgradesStage.getViewport().update(width, height, true);
        inventoryUI.resize(width, height);
        upgradesUI.resize(width, height);
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
    public boolean isScannerOpen() {
        return scannerOpen;
    }
    public boolean isUpgradesOpen() {
        return showUpgradesGUI;
    }
    public boolean isSomethingOpen() {
        return inventoryOpen || showUpgradesGUI || scannerOpen;
    }
    
    public void toggleInventory() {
        inventoryOpen = !inventoryOpen;
        inventoryUI.setVisible(inventoryOpen);
        if (inventoryOpen) {
            Gdx.input.setInputProcessor(inventoryUI.getStage());
        } else {
            Gdx.input.setInputProcessor(uiStage);
        }
    }
    public void toggleScanner() {
        scannerOpen = !scannerOpen;
        scannerUI.setVisible(scannerOpen);
        if (scannerOpen) {
            Gdx.input.setInputProcessor(scannerUI.getStage());
        } else {
            Gdx.input.setInputProcessor(uiStage);
        }
    }
    
    public void toggleUpgrades() {
        showUpgradesGUI = !showUpgradesGUI;
        upgradesUI.setVisible(showUpgradesGUI);
        if (showUpgradesGUI) {
            Gdx.input.setInputProcessor(upgradesUI.getStage());
        } else {
            Gdx.input.setInputProcessor(uiStage);
        }
    }

    
    public void closeInventory() {
        inventoryOpen = false;
    }
    public void closeScanner() {
        scannerOpen = false;
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
    public Stage getScannerStage() {
        return scannerStage;
    }
    public Upgrades getUpgrades() {
        return upgrades;
    }
    public Stage getUpgradesStage() {
        return upgradesStage;
    }
    
}
