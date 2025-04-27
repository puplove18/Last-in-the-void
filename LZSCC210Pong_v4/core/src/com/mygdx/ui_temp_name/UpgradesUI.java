package com.mygdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.helpers.FancyFontHelper;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.ObjectMap;

public class UpgradesUI {

    private Stage stage;
    private Skin skin;

    private Table mainTable;
    private Table upgradesTable;
    private ScrollPane scrollPane;

    private Texture backgroundTexture;
    private NinePatchDrawable panelBackground;
    private ObjectMap<TextButton, Drawable> originalButtonBackgrounds = new ObjectMap<>();

    private boolean isVisible = false;

    private static final Color TITLE_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private static final Color BUTTON_COLOR = new Color(0.2f, 0.4f, 0.6f, 1f);
    private static final Color BUTTON_HOVER_COLOR = new Color(0.3f, 0.6f, 0.9f, 1f);

    public UpgradesUI() {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        initializePanelBackground();
        createUI();
        possibleUpgrades();
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
        mainTable.center(); // Ensure the table is centered in the window
        stage.addActor(mainTable);

        upgradesTable = new Table(skin);
        upgradesTable.top().left().pad(10);
        upgradesTable.defaults().pad(5).left();

        // Set a fixed font size (this size will not change when resizing the window)
        int fontSize = 16; // Fixed font size for the labels and buttons

        BitmapFont headerFont = FancyFontHelper.getInstance().getFont(TITLE_COLOR, fontSize);
        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, TITLE_COLOR);

        // Fixed column widths (no scaling with screen size)
        float width = 400; // fixed width for table columns, adjust based on needs

        // Add headers with fixed font size and dynamic width
        upgradesTable.add(new Label("Upgrade", headerStyle)).width(width * 0.25f).padBottom(10).padLeft(10);
        upgradesTable.add(new Label("Resources Needed", headerStyle)).width(width * 0.25f).padBottom(10);
        upgradesTable.add(new Label("Effects", headerStyle)).width(width * 0.25f).padBottom(10);
        upgradesTable.add(new Label("Action", headerStyle)).width(width * 0.25f).padBottom(10);
        upgradesTable.row();

        // Set up scroll pane with updated sizes
        scrollPane = new ScrollPane(upgradesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        Table container = new Table();
        container.setFillParent(true);
        container.add(scrollPane).expand().fill();

        mainTable.add(container).expand().fill();
    }

    public void addUpgrades(String name, String resources, String effects, Runnable onUpgrades) {
        // Fixed font size for labels and buttons
        int fontSize = 12;

        BitmapFont font = FancyFontHelper.getInstance().getFont(TEXT_COLOR, fontSize);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, TEXT_COLOR);
        TextButton.TextButtonStyle buttonStyle = createButtonStyle(font);

        // Fixed button width (no scaling)
        float buttonWidth = 120; // fixed button width in pixels

        Label nameLabel = new Label(name, labelStyle);
        Label resourcesLabel = new Label(resources, labelStyle);
        Label effectsLabel = new Label(effects, labelStyle);
        TextButton upgradeButton = new TextButton("Upgrade", buttonStyle);
        upgradeButton.setSize(buttonWidth, 40); // Set fixed button size
        // Add event listeners and button interactions
        
        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicked Upgrade: " + name);
                onUpgrades.run(); // <-- This was never called before
            }
            @Override
           public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               if (pointer == -1) {
                   TextButton button = (TextButton) event.getListenerActor();
                   button.getLabel().setColor(Color.WHITE);
                   button.getStyle().up = button.getStyle().over;
               }
           }

           @Override
           public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                   TextButton button = (TextButton) event.getListenerActor();
                   button.getLabel().setColor(TEXT_COLOR);
                   Drawable originalBg = originalButtonBackgrounds.get(button);
                   if (originalBg != null) {
                        button.getStyle().up = originalBg;
                   }
               }
           }
       });
        // Add the components to the table
            upgradesTable.add(nameLabel);
            upgradesTable.add(resourcesLabel);
            upgradesTable.add(effectsLabel);
            upgradesTable.add(upgradeButton);
            upgradesTable.row();
            setVisible(true); 

            Gdx.input.setInputProcessor(stage);
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

    private void possibleUpgrades() {
        addUpgrades("Dark Matter Oven", "100 Dark Matter, 50 Iron", "+20% Cooking speed", new Runnable() {
            @Override
            public void run() {
                System.out.println("Applied: Dark Matter Oven");
            }
        });

        addUpgrades("Stronger Walls", "200 Stone, 100 Iron", "+50% wall durability", new Runnable() {
            @Override
            public void run() {
                System.out.println("Applied: Stronger Walls");
            }
        });
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        mainTable.setVisible(visible);

        if (!visible) {
            // Let GameScreen handle input processor reset
        } else {
            Gdx.input.setInputProcessor(stage);
        }
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
             // Dispose the texture used by the NinePatch if it's not shared/managed elsewhere
            try {
                 panelBackground.getPatch().getTexture().dispose();
            } catch (Exception e) {
                 // Handle potential errors if texture is already disposed
                 System.err.println("Error disposing panel background texture: " + e.getMessage());
            }
        }
    }
}
