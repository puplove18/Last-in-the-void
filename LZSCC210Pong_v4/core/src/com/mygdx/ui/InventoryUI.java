package com.mygdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.helpers.ScreenType;
import com.mygdx.objects.Player;
import com.mygdx.pong.PongGame;

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
    private TextureRegionDrawable innerBg;
    private TextureRegionDrawable slotBg;
    
    private Skin skin;
    private Table mainTable;
    private Table inventoryTable;
    private Texture backgroundTexture;
    private NinePatchDrawable panelBackground;
    
    private boolean isVisible = false;

    private static final Color TITLE_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private static final Color SLOT_COLOR = new Color(0.2f, 0.3f, 0.4f, 1f);
    
    // Fixed dimensions for inventory
    private static final int INVENTORY_WIDTH = 600;
    private static final int INVENTORY_HEIGHT = 400;
    

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
    
    private void createUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(panelBackground);
        mainTable.center(); 
        stage.addActor(mainTable);
        
        Table headerTable = new Table();
        Label titleLabel = new Label("INVENTORY", new Label.LabelStyle(
            FancyFontHelper.getInstance().getFont(TITLE_COLOR, 20), TITLE_COLOR));
        headerTable.add(titleLabel).expandX().left().padLeft(30);

        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });
        headerTable.add(closeButton).size(30, 30).right().padRight(10);
        
        // inventory grid 
        inventoryTable = new Table();
        
        Table panel = new Table();
        panel.setBackground(innerBg);
        panel.add(headerTable).growX().height(50).padTop(10).row();
        panel.add(inventoryTable).grow().pad(20);
        
        // 2nd window
        mainTable.add(panel).width(700).height(550);
        
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
    
    private void refreshInventoryGrid() {
        inventoryTable.clear();
        
        Map<String, Integer> items = player.getInventory().getItems();
       
        // Create inventory slots
        int index = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                // Create slot container
                Table itemContainer = new Table();
                Table slotContainer = new Table();
                slotContainer.setBackground(slotBg);
                
                // Add item if this slot has one
                if (items != null && index < items.size()) {
                    String itemName = (String) items.keySet().toArray()[index];
                    int quantity = items.get(itemName);
                    
                    // Item name (top)
                    Label nameLabel = new Label(itemName, new Label.LabelStyle(FancyFontHelper.getInstance().getFont(TEXT_COLOR, 12), TEXT_COLOR));
                    itemContainer.add(nameLabel).center().padBottom(5).row();
                    itemContainer.add(slotContainer).size(65, 65).row();

                    // Quantity (bottom)
                    Label quantityLabel = new Label(String.valueOf(quantity), new Label.LabelStyle(FancyFontHelper.getInstance().getFont(TEXT_COLOR, 12), TEXT_COLOR));
                    itemContainer.add(quantityLabel).bottom().padBottom(5);
                    
                    index++;
                } else {
                    // Empty slot
                    itemContainer.add().height(20).row(); // Empty space for consistency
                    itemContainer.add(slotContainer).size(65, 65).row();
                    itemContainer.add().height(20); // Empty space for consistency
                }

                // Add slot to grid
                inventoryTable.add(itemContainer).pad(6).padLeft(20).padRight(20);
            }
            inventoryTable.row();
        }
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        mainTable.setVisible(visible);
        
        // Set input processor when visible
        if (visible) {
            refreshInventoryGrid();
            Gdx.input.setInputProcessor(stage);
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
