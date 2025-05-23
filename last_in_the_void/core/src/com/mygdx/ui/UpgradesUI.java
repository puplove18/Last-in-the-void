package com.mygdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.managers.AssetManager;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.objects.Universe;
import com.mygdx.objects.Upgrades;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


/**
 * Handles display of the upgrades menu
 */
public class UpgradesUI {

    // UI Components 
    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private Table upgradesTable;
    private ScrollPane scrollPane;
    private final Player player;
    private Universe universe;

    // Levels for each upgrade category 
    private int destinationLevel = 2; 
    private int fuelLevel = 1;
    private int healthLevel = 1;
    private int inventoryLevel = 1;
    private int resourcesLevel = 1; 
    private int oxygenLevel = 1;

    // Dimensions of menu
    public float width = Gdx.graphics.getWidth();
    public float height = Gdx.graphics.getHeight();
    private Texture backgroundTexture;
    private NinePatchDrawable panelBackground;
    private ObjectMap<TextButton, Drawable> originalButtonBackgrounds = new ObjectMap<>();

    private final Inventory inventory;
    private boolean isVisible = false;
    private Upgrades upgrades;

    // Colour scheme
    private static final Color TITLE_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private static final Color BUTTON_COLOR = new Color(0.2f, 0.4f, 0.6f, 1f);
    private static final Color BUTTON_HOVER_COLOR = new Color(0.3f, 0.6f, 0.9f, 1f);

    private InventoryUI inventoryUI;
    private final AssetManager assetManager;

    
    public UpgradesUI(Player player, Inventory inventory, Universe universe, InventoryUI inventoryUI) {
        this.player = player;
        this.inventory = inventory;
        this.universe = universe;
        this.inventoryUI = inventoryUI; 
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.assetManager = new AssetManager();
        Gdx.input.setInputProcessor(stage);
        initializePanelBackground();
        createUI();
        possibleUpgrades();
    }

    public interface CloseButtonListener {
        void onCloseButtonClicked();
    }

    // The x button in the top-right corner to close the menu
    private CloseButtonListener closeButtonListener;
    public void setCloseButtonListener(CloseButtonListener listener) {
        this.closeButtonListener = listener;
    }


    private void initializePanelBackground() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.15f, 0.15f, 0.2f, 0.9f);
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
        panelBackground = new NinePatchDrawable(new NinePatch(backgroundTexture, 0, 0, 0, 0));
        pixmap.dispose();
    }

    private void createUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(panelBackground);
        mainTable.center();
        stage.addActor(mainTable);
    
        // Adds the close button to top right
        TextButton closeButton = new TextButton("X", skin);
        Table closeButtonTable = new Table();
        closeButtonTable.add(closeButton).top().right().padTop(5).padRight(5);
        closeButtonTable.top().right();
        closeButtonTable.setFillParent(true);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (closeButtonListener != null) {
                    closeButtonListener.onCloseButtonClicked();
                }
            }
        });
        stage.addActor(closeButtonTable);

        // Table where the upgrades will be slected from
        upgradesTable = new Table(skin);
        upgradesTable.top().left().pad(10).center();
        upgradesTable.defaults().pad(5).left();
    
        int fontSize = 10;
        BitmapFont headerFont = FancyFontHelper.getInstance().getFont(TITLE_COLOR, fontSize);
        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, TITLE_COLOR);
    
        upgradesTable.add(new Label("Upgrade", headerStyle)).width(100).padBottom(10).padLeft(10); 
        upgradesTable.add(new Label("Resources Needed", headerStyle)).width(100).padBottom(10);
        upgradesTable.add(new Label("Effects", headerStyle)).width(100).padBottom(10);
        upgradesTable.add(new Label("Action", headerStyle)).width(100).padBottom(10);
        upgradesTable.row();
    
        scrollPane = new ScrollPane(upgradesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
    
        Table container = new Table();
        container.setFillParent(true);
        container.add(scrollPane).expand().fill();

        mainTable.add(container).expand().fill();
    }
    
    // Creates a series of upgrades, so when one upgrade is purchased the next level becomes available 
    private void createUpgradeChain(String[] names, String[] resources, String[] effects, Runnable upgradeAction) {
        int fontSize = 8;
        BitmapFont font = FancyFontHelper.getInstance().getFont(TEXT_COLOR, fontSize);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, TEXT_COLOR);
        TextButton.TextButtonStyle buttonStyle = createButtonStyle(font);
        final int[] levelToShow = {0};
        
        final int maxLevels = names.length;
    
        Label nameLabel = new Label(names[levelToShow[0]], labelStyle);
        final Table resourcesTable = new Table(); 
        updateResourceCells(resourcesTable, resources[levelToShow[0]], labelStyle); 
        Label effectsLabel = new Label(effects[levelToShow[0]], labelStyle);
        TextButton upgradeButton = new TextButton("Upgrade", buttonStyle);
    
        upgradesTable.add(nameLabel).padLeft(10);
        upgradesTable.add(resourcesTable).width(150); 
        upgradesTable.add(effectsLabel).width(200);
        Cell<TextButton> buttonCell = upgradesTable.add(upgradeButton).width(width*0.2f).height(width*0.05f);
        upgradesTable.row();
    
        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String currentUpgradeName = names[levelToShow[0]];
                String requiredResources = resources[levelToShow[0]];
                Map<String, Integer> resourceMap = parseResourceString(requiredResources);
                
                Upgrades upgrade = new Upgrades(inventory, resourceMap, currentUpgradeName); 
                if (upgrade.canAffordUpgrade()) {
                    upgrade.applyUpgrade();
                    if (upgradeAction != null) {
                        upgradeAction.run();
                    }
                    // Updates the level of upgrade
                    levelToShow[0]++;
                    if (levelToShow[0] < maxLevels) {
                        nameLabel.setText(names[levelToShow[0]]);
                        updateResourceCells(resourcesTable, resources[levelToShow[0]], labelStyle); 
                        effectsLabel.setText(effects[levelToShow[0]]);
                    }
                    if (levelToShow[0] == maxLevels) {
                        Label upgradedLabel = new Label("Fully Upgraded", labelStyle);
                        buttonCell.clearActor();
                        upgradeButton.remove();
                        buttonCell.setActor(upgradedLabel);
                    }
                }
            }

            // Creates an effect with the mouse hovers over the button
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    upgradeButton.getLabel().setColor(Color.WHITE);
                    upgradeButton.getStyle().up = upgradeButton.getStyle().over;
                }
            }

            // Ensures taht when the mouse isn't hovering over the button, it is no longer highlighted
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    upgradeButton.getLabel().setColor(TEXT_COLOR);
                    Drawable originalBg = originalButtonBackgrounds.get(upgradeButton);
                    if (originalBg != null) {
                        upgradeButton.getStyle().up = originalBg;
                    }
                }
            }
        });

        originalButtonBackgrounds.put(upgradeButton, upgradeButton.getStyle().up);
    }

    private TextButton.TextButtonStyle createButtonStyle(BitmapFont font) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = TEXT_COLOR;

        Pixmap up = createButtonPixmap(BUTTON_COLOR);
        Pixmap over = createButtonPixmap(BUTTON_HOVER_COLOR);
        Pixmap down = createButtonPixmap(BUTTON_COLOR.cpy().mul(0.8f));

        style.up = new NinePatchDrawable(new NinePatch(new Texture(up), 0, 0, 0, 0));
        style.over = new NinePatchDrawable(new NinePatch(new Texture(over), 0, 0, 0, 0));
        style.down = new NinePatchDrawable(new NinePatch(new Texture(down), 0, 0, 0, 0));

        up.dispose();
        over.dispose();
        down.dispose();

        return style;
    }

    private Pixmap createButtonPixmap(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return pixmap;
    }

    // Ensures that the resource requirements and their icons for each upgrade level get updated
    private void updateResourceCells(Table table, String resourceString, Label.LabelStyle style) {
        table.clear();
        String[] requirements = resourceString.split(", ");
        
        for (String entry : requirements) {
            String[] req = entry.split(" ", 2);
            if (req.length != 2) continue;
            
            int quantity = Integer.parseInt(req[0]);
            String itemName = req[1];
            
            Table itemGroup = new Table();
            TextureRegionDrawable icon = assetManager.getItemIcon(itemName);
            
            itemGroup.add(new Image(icon)).size(24, 24).padRight(2);
            itemGroup.add(new Label(String.valueOf(quantity), style)).padRight(8);
            table.add(itemGroup);
        }
    }

    // Creates the availabe upgrades + chain of upgrades
    private void possibleUpgrades() {

        // Fuel capacity
        createUpgradeChain(
            new String[]{"Fuel Capacity I", "Fuel Capacity II", "Fuel Capacity III", "Fuel Capacity IV"},
            new String[]{
            "10 Common Building Materials, 20 Common Fuel",
            "10 Uncommon Building Materials, 15 Uncommon Fuel",
            "10 Rare Building Materials, 10 Rare Fuel",
            "10 Legendary Building Materials, 10 Legendary Fuel"
            },
            new String[]{
            "Double Fuel Capacity",
            "Double Fuel Capacity",
            "Double Fuel Capacity",
            "Double Fuel Capacity"
            },
            new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                player.upgradeFuel(); 
                fuelLevel++;
                } else {
                System.out.println("Player object is not initialized!");
                }
            }
            }
        );
            // Hull integrity
            createUpgradeChain(
                new String[]{"Health I", "Health II", "Health III", "Health IV"},
                new String[]{
                "20 Common Building Materials, 20 Common Biomass", 
                "15 Uncommon Building Materials, 15 Uncommon Biomass",
                "10 Rare Building Materials, 10 Rare Biomass",
                "10 Legendary Building Materials, 10 Legendary Biomass"
                },
                new String[]{
                "Double Health",
                "Double Health",
                "Double Health",
                "Double Health"
                },
                new Runnable() {
                @Override
                public void run() {
                    if (player != null) {
                    player.upgradeHealth(); 
                    healthLevel++;
                    } else {
                    System.out.println("Player object is not initialized!");
                    }
                }
                }
        );

        // Life support
        createUpgradeChain(
                new String[]{"Oxygen I", "Oxygen II", "Oxygen III", "Oxygen IV"},
                new String[]{
                "10 Common Building Materials, 20 Common Biomass",
                "10 Uncommon Building Materials, 15 Uncommon Biomass",
                "10 Rare Building Materials, 10 Rare Biomass",
                "10 Legendary Building Materials, 10 Legendary Biomass"
                },
                new String[]{
                "Double Oxygen",
                "Double Oxygen",
                "Double Oxygen",
                "Double Oxygen"
                },
                new Runnable() {
                @Override
                public void run() {
                    if (player != null) {
                    player.upgradeOxygen(); 
                    oxygenLevel++;
                    } else {
                    System.out.println("Player object is not initialized!");
                    }
                }
                }
        );

        // System scanner
        createUpgradeChain(
            new String[]{
                "Destination Scanner I",
                "Destination Scanner II",
                "Advanced Navigation",
                "Precision Landing System"
            },
            new String[]{
                "20 Common Building Materials",
                "15 Uncommon Building Materials",
                "10 Rare Building Materials",
                "10 Legendary Building Materials"
            },
            new String[]{
                "Choose between 1 locations",
                "Choose between 2 locations",
                "Choose between 3 locations",
                "Choose between 4 locations"
            },
            new Runnable() {
                @Override
                public void run() {
                    if (universe != null) {
                        destinationLevel++;
                    universe.setMaxDest(destinationLevel);
                    } else {
                        System.out.println("Player object is not initialized!");
                    }
                }
            }
);      
        // Inventory capacity
        createUpgradeChain(
            new String[]{
            "Inventory Capacity I",
            "Inventory Capacity II",
            "Inventory Capacity III",
            "Inventory Capacity IV"
            },
            new String[]{
            "60 Common Building Materials",
            "50 Uncommon Building Materials",
            "40 Rare Building Materials",
            "30 Legendary Building Materials"
            },
            new String[]{
            "More Inventory Space",
            "More Inventory Space", 
            "More Inventory Space",
            "More Inventory Space"
            },
            new Runnable() {
            @Override
            public void run() {
                if (universe != null) {
                inventoryLevel++; 
                inventoryUI.setInventoryCapacity(inventoryLevel);

                } else {
                System.out.println("Player object is not initialized!");
                }
            }
            }
        );

        // Amount of resources that can be harvested
        createUpgradeChain(
            new String[]{
                "Resources Level I",
                "Resources Level II",
                "Resources Level III",
                
            },
            new String[]{
                "50 Common Building Materials",
                "40 Uncommon Building Materials",
                "30 Rare Building Materials",
                
            },
            new String[]{
                "+1 resource level",
                "+1 resource level", 
                "+1 resource level"
                
            },
            new Runnable() {
                @Override
                public void run() {
                    if (universe != null) {
                        resourcesLevel++; 
                        player.setResourcePermissionLevel(resourcesLevel);

                    } else {
                        System.out.println("Player object is not initialized!");
                    }
                }
            }
        );

        
    }
    // Get the the amount and resources name from the string in possibleUpgrades
    private Map<String, Integer> parseResourceString(String resourceString) {
    Map<String, Integer> resources = new HashMap<>();
    String[] parts = resourceString.split(", ");
    for (String part : parts) {
        String[] requirementFormat = part.split(" ", 2);
        if (requirementFormat.length != 2) {
            System.out.println("Invalid resource format: " + part);
            continue;
        }
        try {
            int quantity = Integer.parseInt(requirementFormat[0]);
            String itemName = requirementFormat[1];
            resources.put(itemName, quantity);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity in resource: " + part);
        }
    }
    return resources;
}

    public void render() {
        if (isVisible) {
            stage.getBatch().begin();
            stage.getBatch().setColor(1, 1, 1, mainTable.getColor().a);
            stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.getBatch().setColor(1, 1, 1, 1);
            stage.getBatch().end();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    // Getters and setters

    public int getDestinationLevel() {
        return destinationLevel;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public int getHealthLevel() {
        return healthLevel;
    }

    public int getInventoryLevel() {
        return inventoryLevel;
    }

    public int getResourcesLevel() {
        return resourcesLevel;
    }

    public int getOxygenLevel() {
        return oxygenLevel;
    }
    
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        mainTable.setVisible(visible);

        if (visible) {
            Gdx.input.setInputProcessor(stage);
        }
    }

    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        if (panelBackground != null && panelBackground.getPatch() != null) {
            try {
                panelBackground.getPatch().getTexture().dispose();
            } catch (Exception e) {
                System.err.println("Error disposing panel background texture: " + e.getMessage());
            }
        }
    }
}
