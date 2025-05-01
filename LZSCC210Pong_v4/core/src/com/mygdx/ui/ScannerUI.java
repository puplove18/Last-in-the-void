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
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.objects.Universe;
import com.mygdx.objects.StarSystem;

import com.badlogic.gdx.InputProcessor;

public class ScannerUI {

    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private Table scannerTable;
    private ScrollPane scrollPane;
    private final Player player;
    private Universe universe;

    private InputProcessor previousProcessor;
    private Texture backgroundTexture;
    private NinePatchDrawable panelBackground;
    private ObjectMap<TextButton, Drawable> originalButtonBackgrounds = new ObjectMap<>();

    private boolean isVisible = false;

    private static final Color TITLE_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private static final Color BUTTON_COLOR = new Color(0.2f, 0.4f, 0.6f, 1f);
    private static final Color BUTTON_HOVER_COLOR = new Color(0.3f, 0.6f, 0.9f, 1f);

    public ScannerUI(Player player, Universe universe) {
        this.player = player;
        this.universe = universe;

        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        initializePanelBackground();
        createUI();
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
        backgroundTexture = new Texture(pixmap);
        panelBackground = new NinePatchDrawable(new NinePatch(backgroundTexture, 0, 0, 0, 0));
        pixmap.dispose();
    }

    private void createUI() {
        // Main Table (similar structure to UpgradesUI)
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(panelBackground);
        mainTable.center();
        stage.addActor(mainTable);

        // Close button (top-right position)
        TextButton closeButton = new TextButton("X", skin);
        Table closeButtonTable = new Table();
        closeButtonTable.add(closeButton).top().right().padTop(5).padRight(5);
        closeButtonTable.top().right();
        closeButtonTable.setFillParent(true);
        closeButton.clearListeners();
        closeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boolean pressed = super.touchDown(event, x, y, pointer, button);
                event.stop();
                return pressed;
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (closeButtonListener != null) {
                    closeButtonListener.onCloseButtonClicked();
                }
            }
        });


        stage.addActor(closeButtonTable);

        // Scanner Table (similar structure to UpgradesUI's upgradesTable)
        scannerTable = new Table(skin);
        scannerTable.top().left().pad(10);
        scannerTable.defaults().pad(5).left();

        int fontSize = 13;
        BitmapFont headerFont = new BitmapFont(); // Use your custom font here
        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, TITLE_COLOR);

        float width = 400;
        scannerTable.add(new Label("Scanner", headerStyle)).width(width * 0.25f).padBottom(10).padLeft(10);
        scannerTable.add(new Label("Location", headerStyle)).width(width * 0.25f).padBottom(10);
        scannerTable.add(new Label("Details", headerStyle)).width(width * 0.25f).padBottom(10);
        scannerTable.add(new Label("Action", headerStyle)).width(width * 0.25f).padBottom(10);
        scannerTable.row();

        // Scrollable container
        scrollPane = new ScrollPane(scannerTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        Table container = new Table();
        container.setFillParent(true);
        container.add(scrollPane).expand().fill();

        mainTable.add(container).expand().fill();
        getAllDestinationsInTable();
    }
    public void getAllDestinationsInTable() {
        scannerTable.clearChildren();

        float width = 400;
        BitmapFont headerFont = new BitmapFont(); // Or your custom font
        Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, TITLE_COLOR);

        scannerTable.add(new Label("Scanner", headerStyle)).width(width * 0.25f).padBottom(10).padLeft(10);
        scannerTable.add(new Label("Location", headerStyle)).width(width * 0.25f).padBottom(10);
        scannerTable.add(new Label("Details", headerStyle)).width(width * 0.25f).padBottom(10);
        scannerTable.add(new Label("Action", headerStyle)).width(width * 0.25f).padBottom(10);
        scannerTable.row();

        StarSystem[] destinations = universe.getDestinations();

        for (int i = 0; i < destinations.length; i++) {
            StarSystem system = destinations[i];

            Label systemName = new Label(system.getName(), skin);
            Label location = new Label("Tier " + system.getTier(), skin);
            Label details = new Label(system.getNumPlanets()-1 + " planets", skin);
            TextButton chooseButton = new TextButton("Choose", skin);

            final int destinationIndex = i;

            chooseButton.clearListeners();
            chooseButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    boolean pressed = super.touchDown(event, x, y, pointer, button);
                    event.stop();
                    return pressed;
                }
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    universe.chooseDestination(destinationIndex);
                    getAllDestinationsInTable();
                }
            });




            scannerTable.add(systemName).width(width * 0.25f);
            scannerTable.add(location).width(width * 0.25f);
            scannerTable.add(details).width(width * 0.25f);
            scannerTable.add(chooseButton).width(width * 0.25f);
            scannerTable.row();
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        mainTable.setVisible(visible);

        if (visible) {
            previousProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(stage);
        }
        else {
            if (previousProcessor != null) {
                Gdx.input.setInputProcessor(previousProcessor);
            }//ensure no access
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
