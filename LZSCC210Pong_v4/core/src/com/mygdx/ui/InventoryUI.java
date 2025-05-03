package com.mygdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Align;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.objects.Player;
import com.mygdx.pong.PongGame;

import java.util.HashMap;
import java.util.Map;


public class InventoryUI {
    private PongGame game;
    private Player player;
    private SpriteBatch batch;
    private BitmapFont title;
    private BitmapFont itemText;
    
    private Stage stage;
    private Table rootTable;
    
    private TextureAtlas atlas;
    private TextureAtlas itemAtlas;
    private TextureRegionDrawable innerBg;
    private TextureRegionDrawable slotBg;
    
    private Skin skin;
    private Table mainTable;
    private Table inventoryTable;
    private Texture backgroundTexture;
    private NinePatchDrawable panelBackground;
    
    private boolean isVisible = false;
    private String selectedItemName = null;

    private static final Color TITLE_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private Map<String, TextureRegionDrawable> itemIcons = new HashMap<>();
    private int inventoryCapacity = 1;


    public InventoryUI(PongGame game, Player player) {
        this.game = game;
        this.player = player;
        
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        this.atlas = new TextureAtlas(Gdx.files.internal("backgrounds.atlas"));
        this.innerBg = new TextureRegionDrawable(atlas.findRegion("GUI_note"));
        this.slotBg = new TextureRegionDrawable(atlas.findRegion("GUI_slot"));
        
        initializePanelBackground();
        createUI();
        loadItemIcons();
    }
    // Getter for inventoryCapacity
    public int getInventoryCapacity() {
        return inventoryCapacity;
    }

    // Setter for inventoryCapacity
    public void setInventoryCapacity(int inventoryCapacity) {
        if (inventoryCapacity >= 0 && inventoryCapacity <=4) { 
            this.inventoryCapacity = inventoryCapacity;
        }
    }

    public interface CloseButtonListener {
        void onCloseButtonClicked();
    }

    private CloseButtonListener closeButtonListener;

    public void setCloseButtonListener(CloseButtonListener listener) {
        this.closeButtonListener = listener;
    }

    private void initializePanelBackground() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.15f, 0.15f, 0.2f, 0.9f);
        pixmap.fill();
        Texture bgTex = new Texture(pixmap);
        panelBackground = new NinePatchDrawable(new NinePatch(bgTex, 0, 0, 0, 0));
        backgroundTexture = bgTex;
        pixmap.dispose();
    }

    private void loadItemIcons() {
        itemAtlas = new TextureAtlas(Gdx.files.internal("resources.atlas"));
        // For the biomass images, i used the resources from;
        // Free Game Assets. (2023, June 23). Free 40 Loot Icons 32x32 Pixel Art. Itch.io. Retrieved [2025/4/30],
        // from https://free-game-assets.itch.io/free-40-loot-icons-pixel-art
        // Biomass resources  !! "epic" in atlas = "Legendary" in game !!
        itemIcons.put("Common Biomass", new TextureRegionDrawable(itemAtlas.findRegion("common_biomass")));
        itemIcons.put("Uncommon Biomass", new TextureRegionDrawable(itemAtlas.findRegion("uncommon_biomass")));
        itemIcons.put("Rare Biomass", new TextureRegionDrawable(itemAtlas.findRegion("rare_biomass")));
        itemIcons.put("Legendary Biomass", new TextureRegionDrawable(itemAtlas.findRegion("epic_biomass")));
        
        // For the fuel images, i used the resources from;
        // Free Game Assets. (2021, May 4). Free Paint Pixel Art Icons. Itch.io. Retrieved [2025/4/30],
        // from https://free-game-assets.itch.io/free-paint-pixel-art-icon-pack
        // Fuel resources
        itemIcons.put("Common Fuel", new TextureRegionDrawable(itemAtlas.findRegion("common_fuel")));
        itemIcons.put("Uncommon Fuel", new TextureRegionDrawable(itemAtlas.findRegion("uncommon_fuel")));
        itemIcons.put("Rare Fuel", new TextureRegionDrawable(itemAtlas.findRegion("rare_fuel")));
        itemIcons.put("Legendary Fuel", new TextureRegionDrawable(itemAtlas.findRegion("epic_fuel")));
        
        // For the building material images, i drew them myself using the piskelapp.com
        // Building Materials resources
        itemIcons.put("Common Building Materials", new TextureRegionDrawable(itemAtlas.findRegion("common_building_material")));
        itemIcons.put("Uncommon Building Materials", new TextureRegionDrawable(itemAtlas.findRegion("uncommon_buidling_material")));
        itemIcons.put("Rare Building Materials", new TextureRegionDrawable(itemAtlas.findRegion("rare_buidling_material"))); 
        itemIcons.put("Legendary Building Materials", new TextureRegionDrawable(itemAtlas.findRegion("epic_building_material")));
        
        TextureRegionDrawable defaultIcon = new TextureRegionDrawable(itemAtlas.findRegion("defaultIcon"));
        itemIcons.put("default", defaultIcon);
    }
    
    private void createUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(panelBackground);
        mainTable.center(); 
        stage.addActor(mainTable);
        
        // title label "INVENTORY"
        Table headerTable = new Table();
        Label titleLabel = new Label("INVENTORY", new Label.LabelStyle(
            FancyFontHelper.getInstance().getFont(TITLE_COLOR, 20), TITLE_COLOR));
        headerTable.add(titleLabel).expandX().left().padLeft(30);

        // Inventory instruction label
        Table instructionTable = new Table();
        Label instructionLabel = new Label("Click on item to use or remove it", new Label.LabelStyle(
            FancyFontHelper.getInstance().getFont(TEXT_COLOR, 12), TEXT_COLOR));
        instructionTable.add(instructionLabel).center().padBottom(5);
        
        // close button
        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(null);
                isVisible = false;
                mainTable.setVisible(false);
                if (closeButtonListener != null) {
                    closeButtonListener.onCloseButtonClicked();
                }
            }
        });
        headerTable.add(closeButton).size(30, 30).right().padRight(10);
        
        // inventory grid 
        inventoryTable = new Table();
        
        Table panel = new Table();
        panel.setBackground(innerBg);
        panel.add(headerTable).growX().height(50).padTop(10).row();
        panel.add(instructionTable).growX().padBottom(10).row();
        panel.add(inventoryTable).grow().pad(20);
        
        // inner window
        mainTable.add(panel).width(750).height(580);
        
        refreshInventoryGrid();
    }
    
    private NinePatchDrawable createPanelBackground(float r, float g, float b, float a) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, a);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new NinePatchDrawable(new NinePatch(texture, 0, 0, 0, 0));
    }

    // Gets the status of the item based on its name
    private Player.Stats getStats(String itemName) {
        if (itemName.contains("Fuel")) {
            return Player.Stats.FUEL;
        } else if (itemName.contains("Biomass")) {
            return Player.Stats.OXYGEN;
        } else if (itemName.contains("Building Materials")) {
            return Player.Stats.HEALTH;
        }
        return null;
    }

    private int getRecoveryAmount(String itemName) {
        // tier^2 is the recovery amount
        // Common = 1, Uncommon = 4, Rare = 8, Legendary = 16
        if (itemName.contains("Common")) {
            return 1;
        } else if (itemName.contains("Uncommon")) {
            return 4;
        } else if (itemName.contains("Rare")) {
            return 8;
        } else if (itemName.contains("Legendary")) {
            return 16;
        }
        return 0;
    }
    
    private void refreshInventoryGrid() {
        inventoryTable.clear();
        Map<String, Integer> items = player.getInventory().getItems();
        System.out.println("DEBUG: Inventory items: " + items);
       
        // Create inventory slots
        int index = 0;
        for (int row = 0; row < inventoryCapacity; row++) {
            for (int col = 0; col < 6; col++) {
                // Create slot container
                Table itemContainer = new Table();
                Table slotContainer = new Table();
                slotContainer.setBackground(slotBg);
                slotContainer.setTouchable(Touchable.enabled);            

                // Add item if this slot has one
                if (items != null && index < items.keySet().size()) {
                    String itemName = (String) items.keySet().toArray()[index];
                    int quantity = items.get(itemName);
                    
                    TextureRegionDrawable icon = itemIcons.getOrDefault(itemName, itemIcons.get("missing"));
                    Image iconImage = new Image(icon);
                    slotContainer.add(iconImage).size(48, 48).pad(5);
                    final String currentItemName = itemName;
                    final int currentQuantity = quantity;
                    slotContainer.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            selectedItemName = currentItemName;
                            showItemActionDialog(currentItemName, currentQuantity);
                        }
                    });
                    
                    // Item name (top)
                    String displayString = itemName.length() > 10 ? itemName.substring(0, 6) + "..." : itemName;
                    Label nameLabel = new Label(displayString, new Label.LabelStyle(FancyFontHelper.getInstance().getFont(TEXT_COLOR, 12), TEXT_COLOR));
                    itemContainer.add(nameLabel).center().padBottom(5).row();
                    itemContainer.add(slotContainer).size(65, 65).row();

                    // Quantity (bottom)
                    Label quantityLabel = new Label(String.valueOf(quantity), new Label.LabelStyle(FancyFontHelper.getInstance().getFont(TEXT_COLOR, 12), TEXT_COLOR));
                    itemContainer.add(quantityLabel).bottom().padBottom(5);
                    
                    index++;
                } else {
                    // Empty slots
                    itemContainer.add().height(20).row();
                    itemContainer.add(slotContainer).size(65, 65).row();
                    itemContainer.add().height(20); 
                }

                // Add slot to grid
                inventoryTable.add(itemContainer).width(105).pad(8);
            }
            inventoryTable.row();
        }
    }

    private void showItemActionDialog(String itemName, int maxQuantity) {
        final Dialog dialog = new Dialog("", skin);
        Table content = new Table();
        content.pad(20);
        
        // Item name for removal
        Label titleLabel = new Label("Item: " + itemName,
            new Label.LabelStyle(FancyFontHelper.getInstance().getFont(TITLE_COLOR, 16), TITLE_COLOR));
        content.add(titleLabel).colspan(3).padBottom(20).row();

        // quantity slection
        final int[] quantity = {1};
        final Label quantityLabel = new Label("1", skin);
        quantityLabel.setAlignment(Align.center);

        // + / - buttons
        TextButton plusButton = new TextButton("+", skin);
        plusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quantity[0] = Math.min(maxQuantity, quantity[0] + 1);
                quantityLabel.setText(String.valueOf(quantity[0]));
            }
        });
        TextButton minusButton = new TextButton("-", skin);
        minusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quantity[0] = Math.max(1, quantity[0] - 1);
                quantityLabel.setText(String.valueOf(quantity[0]));
            }
        });
        
        content.add(minusButton).width(40).height(40);
        content.add(quantityLabel).width(60).height(40).pad(0, 10, 0, 10);
        content.add(plusButton).width(40).height(40).row();
        
        Label helpLabel = new Label("Amount to use", skin);
        content.add(helpLabel).colspan(3).padTop(5).padBottom(20).row();
        

        // gets the status of the item based on its name from the player class
        Player.Stats stat = getStats(itemName);
        int recoveryAmount = getRecoveryAmount(itemName);
        final String systemType = stat == Player.Stats.HEALTH ? "Hull Integrity" :
                                  stat == Player.Stats.FUEL ? "Ship Fuel" :
                                  stat == Player.Stats.OXYGEN ? "Life Support" : "";

        // recover/remove/cancel buttons
        if (stat != null) {
        Label recoveryLabel = new Label("Use to recover " + recoveryAmount + " " + systemType + " per unit",
            new Label.LabelStyle(FancyFontHelper.getInstance().getFont(TEXT_COLOR, 12), TEXT_COLOR));
        content.add(recoveryLabel).colspan(3).padBottom(20).row();
        
        TextButton recoverButton = new TextButton("Recover", skin);
            recoverButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                double currentStat = 0;
                double maxCapacity = 0;
                if (stat == Player.Stats.FUEL) {
                    currentStat = (double) player.getFuel();
                    maxCapacity = (double) player.getFuelLim();
                    System.out.println("DEBUG: Current fuel: " + currentStat + " / " + maxCapacity);
                } else if (stat == Player.Stats.OXYGEN) {
                    currentStat = (double) player.getOxygen();
                    maxCapacity = (double) player.getOxygenLim();
                    System.out.println("DEBUG: Current life support: " + currentStat + " / " + maxCapacity);
                } else if (stat == Player.Stats.HEALTH) {
                    currentStat = (double) player.getHealth();
                    maxCapacity = (double) player.getHealthLim();
                    System.out.println("DEBUG: Current hull: " + currentStat + " / " + maxCapacity);
                }

                // Check if the stats is already 100%
                if (currentStat >= maxCapacity) {
                    Dialog warningDialog = new Dialog("Warning", skin);
                    warningDialog.text("Your " + systemType + " is already full.");
                    warningDialog.setModal(false);
                    TextButton okButton = new TextButton("OK", skin);
                    okButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            warningDialog.hide();
                            Gdx.input.setInputProcessor(stage);
                        }
                    });
                Table buttonTable = new Table();
                buttonTable.add(okButton).width(120).height(40);
                warningDialog.getContentTable().add(buttonTable).padTop(20);
                warningDialog.getContentTable().pad(20);

                warningDialog.show(stage);
                return;
            }

            int totalPossibleRecovery = getRecoveryAmount(itemName) * quantity[0];
            double actualRecovery = Math.min(totalPossibleRecovery, maxCapacity - currentStat);

            if (actualRecovery > 0) {
                player.updateStat(stat, actualRecovery);
                System.out.println("Used " + quantity[0] + " " + itemName + " to recover " + actualRecovery + " of " + stat);
                System.out.println("DEBUG: Afterrecovery " + systemType + ": " + currentStat + " / " + maxCapacity + "=" + (currentStat / maxCapacity) * 100 + "%");
                try {
                    if (player != null) {
                        double newValue = 0;
                        double maxValue = 0;
                        // the variable differ from the one above because it is just to check the value after recovery
                        if (stat == Player.Stats.FUEL) {
                            newValue = player.getFuel();
                            maxValue = player.getFuelLim();
                        } else if (stat == Player.Stats.OXYGEN) {
                            newValue = player.getOxygen();
                            maxValue = player.getOxygenLim();
                        } else if (stat == Player.Stats.HEALTH) {
                            newValue = player.getHealth();
                            maxValue = player.getHealthLim();
                        }
                        
                        System.out.println("DEBUG: After recovery - " + systemType + ": " + newValue + " / " + maxValue + "=" + (newValue / maxValue) * 100 + "%");
                    }
                } catch (Exception e) {
                    System.out.println("Error in debug output: " + e.getMessage());
                }
                player.getInventory().removeItem(itemName, quantity[0]);
            }

            dialog.hide();
            selectedItemName = null;
            refreshInventoryGrid();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Gdx.input.setInputProcessor(stage);
                }
            });
        }
    });

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                selectedItemName = null;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.input.setInputProcessor(stage);
                    }
                });
            }
        });
        
        TextButton removeButton = new TextButton("Remove", skin);
        removeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.getInventory().removeItem(itemName, quantity[0]);
                dialog.hide();
                selectedItemName = null;
                refreshInventoryGrid();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.input.setInputProcessor(stage);
                    }
                });
            }
        });

        // Show buttons
        Table buttonTable = new Table();
        buttonTable.add(recoverButton).width(120).height(40).padRight(20);
        buttonTable.add(removeButton).width(120).height(40).padRight(20);
        buttonTable.add(cancelButton).width(120).height(40);
        content.add(buttonTable).colspan(3).padTop(20);
        
        dialog.getContentTable().add(content);
        dialog.show(stage);
        }
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        mainTable.setVisible(visible);

        if (visible) {
            refreshInventoryGrid();
            Gdx.input.setInputProcessor(stage);
        } else {
            if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
            }
        }
    }
    
    public void render() {
        if (isVisible) {
            // Draw full-screen darkening background
            stage.getBatch().begin();
            stage.getBatch().setColor(1, 1, 1, mainTable.getColor().a);
            stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.getBatch().setColor(1, 1, 1, 1);
            stage.getBatch().end();
            
            // Draw UI elements
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }
    
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        if (panelBackground != null && panelBackground.getPatch() != null) {
            try {
                panelBackground.getPatch().getTexture().dispose();
            } catch (Exception e) {
                System.err.println("Error disposing: " + e.getMessage()); 
            }
        }
    }
}
