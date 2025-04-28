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
import com.mygdx.objects.Inventory;

public class UpgradesUI {

    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private Table upgradesTable;
    private ScrollPane scrollPane;

    private Texture backgroundTexture;
    private NinePatchDrawable panelBackground;
    private ObjectMap<TextButton, Drawable> originalButtonBackgrounds = new ObjectMap<>();

    private Inventory inventory;

    private boolean isVisible = false;

    private static final Color TITLE_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private static final Color BUTTON_COLOR = new Color(0.2f, 0.4f, 0.6f, 1f);
    private static final Color BUTTON_HOVER_COLOR = new Color(0.3f, 0.6f, 0.9f, 1f);

    public UpgradesUI(Inventory inventory) {
        this.inventory = inventory;
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

        upgradesTable = new Table(skin);
        upgradesTable.top().left().pad(10);
        upgradesTable.defaults().pad(5).left();

        int fontSize = 13;
        BitmapFont headerFont = FancyFontHelper.getInstance().getFont(TITLE_COLOR, fontSize);
        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, TITLE_COLOR);

        float width = 400;

        upgradesTable.add(new Label("Upgrade", headerStyle)).width(width * 0.25f).padBottom(10).padLeft(10);
        upgradesTable.add(new Label("Resources Needed", headerStyle)).width(width * 0.25f).padBottom(10);
        upgradesTable.add(new Label("Effects", headerStyle)).width(width * 0.25f).padBottom(10);
        upgradesTable.add(new Label("Action", headerStyle)).width(width * 0.25f).padBottom(10);
        upgradesTable.row();

        scrollPane = new ScrollPane(upgradesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        Table container = new Table();
        container.setFillParent(true);
        container.add(scrollPane).expand().fill();

        mainTable.add(container).expand().fill();
    }

    private void createUpgradeChain(String[] names, String[] resources, String[] effects) {
        int fontSize = 10;
        BitmapFont font = FancyFontHelper.getInstance().getFont(TEXT_COLOR, fontSize);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, TEXT_COLOR);
        TextButton.TextButtonStyle buttonStyle = createButtonStyle(font);

        final int[] levelToShow = {0}; // start at 0 index
        final int maxLevels = names.length;

        // Create labels and button
        Label nameLabel = new Label(names[levelToShow[0]], labelStyle);
        Label resourcesLabel = new Label(resources[levelToShow[0]], labelStyle);
        Label effectsLabel = new Label(effects[levelToShow[0]], labelStyle);
        TextButton upgradeButton = new TextButton("Upgrade", buttonStyle);

        upgradesTable.add(nameLabel).padLeft(10);
        upgradesTable.add(resourcesLabel);
        upgradesTable.add(effectsLabel);
        upgradesTable.add(upgradeButton).width(120).height(40).padLeft(10);
        upgradesTable.row();

        // Setup button listener
        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelToShow[0] < maxLevels - 1) {
                    levelToShow[0]++;
                    nameLabel.setText(names[levelToShow[0]]);
                    resourcesLabel.setText(resources[levelToShow[0]]);
                    effectsLabel.setText(effects[levelToShow[0]]);
                
                    if (levelToShow[0] == maxLevels - 1) {
                        upgradeButton.setText("Fully Upgraded");
                        upgradeButton.setDisabled(true);
                    }
                }
                
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    upgradeButton.getLabel().setColor(Color.WHITE);
                    upgradeButton.getStyle().up = upgradeButton.getStyle().over;
                }
            }

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

        // Save original background for restoring after hover
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

    private void possibleUpgrades() {
        createUpgradeChain(
            new String[]{"Dark Matter Oven I", "Dark Matter Oven II", "Dark Matter Oven III", "Dark Matter Oven IV"},
            new String[]{
                "100 Dark Matter, 50 Iron",
                "150 Dark Matter, 100 Iron",
                "250 Dark Matter, 150 Iron",
                "400 Dark Matter, 200 Iron"
            },
            new String[]{
                "+20% Cooking speed",
                "+35% Cooking speed",
                "+50% Cooking speed",
                "+70% Cooking speed"
            }
        );

        createUpgradeChain(
            new String[]{"Stronger Walls I", "Stronger Walls II", "Stronger Walls III", "Stronger Walls IV"},
            new String[]{
                "200 Stone, 100 Iron",
                "300 Stone, 200 Iron",
                "450 Stone, 300 Iron",
                "600 Stone, 500 Iron"
            },
            new String[]{
                "+20% Durability",
                "+35% Durability",
                "+50% Durability",
                "+70% Durability"
            }
        );
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
