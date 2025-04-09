package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.helpers.BodyHelper;
import com.mygdx.helpers.Constants;
import com.mygdx.helpers.ContactType;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.helpers.GameContactListener;
import com.mygdx.helpers.ScreenType;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.objects.SpaceShip;
import com.mygdx.objects.Upgrades;
import com.mygdx.pong.PongGame;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private BitmapFont font;
    private SpaceShip playerShip;
    private Player player;
    private double health = 100;
    private double fuel = 100;
    private double oxygen = 100;
    private Inventory inventory;
    private boolean inventoryOpen = false;
    private Texture inventoryHUDTexture;
    private SpriteBatch uiBatch;
    private Stage inventoryStage;
    private Upgrades upgrades;
    private boolean showUpgradesGUI = false;
    private Stage uiStage;
    private Skin skin;
    public boolean paused = false;
    private Texture backgroundPlanetTexture;
    private Texture heroTexture;
    private Texture alienTexture;

    public GameScreen(OrthographicCamera camera) {
        Box2D.init();
        this.camera = camera;
        this.camera.position.set(new Vector3(PongGame.getInstance().getWindowWidth() / 2,
                PongGame.getInstance().getWindowHeight() / 2, 0));
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.world.setContactListener(new GameContactListener(this));
        this.inventory = new Inventory(1000);
        this.upgrades = new Upgrades(inventory, "Iron", 50);
        this.player = new Player();
        Body shipBody = BodyHelper.createRectangularBody(
                PongGame.getInstance().getWindowWidth() / 2,
                PongGame.getInstance().getWindowHeight() / 2,
                Constants.PLAYER_PADDLE_WIDTH,
                Constants.PLAYER_PADDLE_HEIGHT,
                BodyType.KinematicBody, 1f, world, ContactType.PLAYER);
        this.playerShip = new SpaceShip(
                PongGame.getInstance().getWindowWidth() / 2,
                PongGame.getInstance().getWindowHeight() / 2,
                shipBody);
        this.font = FancyFontHelper.getInstance().getFont(Color.WHITE, 20);
        this.uiBatch = new SpriteBatch();
        loadInventoryTextures();
        setupInventoryUI();
        this.uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createExitButton();
        Gdx.input.setInputProcessor(uiStage);
        backgroundPlanetTexture = new Texture(Gdx.files.internal("planet1.png"));
        heroTexture = new Texture(Gdx.files.internal("entities/Astronaut.png"));
        alienTexture = new Texture(Gdx.files.internal("entities/alien.gif"));
    }

    private void createExitButton() {
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PongGame.getInstance().changeScreen(GameScreen.this, ScreenType.MENU_UI);
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.top().right().pad(10);
        table.add(exitButton).width(100).height(50);
        uiStage.addActor(table);
    }

    private void loadInventoryTextures() {
        inventoryHUDTexture = new Texture(Gdx.files.internal("InventoryHUD.png"));
    }

    private void setupInventoryUI() {
        inventoryStage = new Stage(new FitViewport(
                PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight()));
    }

    public void update() {
        handleInput();
        if (!paused) {
            world.step(1 / 60f, 6, 2);
            camera.update();
            playerShip.update();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            PongGame.getInstance().changeScreen(this, ScreenType.MENU);
        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            paused = !paused;
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            inventoryOpen = false;
            showUpgradesGUI = !showUpgradesGUI;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            inventoryOpen = !inventoryOpen;
            showUpgradesGUI = false;
        }
    }

    public void pause() {
        paused = true;
    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundPlanetTexture, 0, 0, PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight());
        float screenWidth = PongGame.getInstance().getWindowWidth();
        float screenHeight = PongGame.getInstance().getWindowHeight();
        float heroX = 200;
        float heroY = (screenHeight - heroTexture.getHeight()) / 2;
        batch.draw(heroTexture, heroX + heroTexture.getWidth(), heroY, heroTexture.getWidth(), heroTexture.getHeight());
        float alienX = screenWidth - alienTexture.getWidth();
        float alienY = (screenHeight - alienTexture.getHeight()) / 2;
        batch.draw(alienTexture, alienX + alienTexture.getWidth(), alienY, -alienTexture.getWidth(), alienTexture.getHeight());
        playerShip.render(batch);
        drawPlayerStats();
        if (showUpgradesGUI)
            upgrades.render(batch);
        if (paused)
            font.draw(batch, "Paused", screenWidth / 2 - 40, screenHeight / 2);
        batch.end();
        if (inventoryOpen) {
            renderInventoryUI();
        }
        uiStage.act(delta);
        uiStage.draw();
    }

    private void drawPlayerStats() {
        float statsX = 10;
        float statsY = PongGame.getInstance().getWindowHeight() - 20;
        double currentHealth = player.getHealth();
        double currentFuel = player.getFuel();
        double currentOxygen = player.getOxygen();
        BitmapFont statsFont = FancyFontHelper.getInstance().getFont(Color.WHITE, 16);
        statsFont.draw(batch, "Name: Space Explorer", statsX, statsY);
        Color healthColor = getResourceColor(currentHealth);
        BitmapFont healthFont = FancyFontHelper.getInstance().getFont(healthColor, 16);
        healthFont.draw(batch, "Health: " + (int) currentHealth + "%", statsX, statsY - 20);
        Color fuelColor = getResourceColor(currentFuel);
        BitmapFont fuelFont = FancyFontHelper.getInstance().getFont(fuelColor, 16);
        fuelFont.draw(batch, "Fuel: " + (int) currentFuel + "%", statsX, statsY - 40);
        Color oxygenColor = getResourceColor(currentOxygen);
        BitmapFont oxygenFont = FancyFontHelper.getInstance().getFont(oxygenColor, 16);
        oxygenFont.draw(batch, "Oxygen: " + (int) currentOxygen + "%", statsX, statsY - 60);
    }

    private Color getResourceColor(double value) {
        if (value > 70)
            return Color.GREEN;
        else if (value > 30)
            return Color.YELLOW;
        else
            return Color.RED;
    }

    private void renderInventoryUI() {
        uiBatch.begin();
        float scaledWidth = inventoryHUDTexture.getWidth() * 0.85f;
        float scaledHeight = inventoryHUDTexture.getHeight() * 0.85f;
        uiBatch.draw(inventoryHUDTexture,
                (PongGame.getInstance().getWindowWidth() - scaledWidth) / 2,
                (PongGame.getInstance().getWindowHeight() - scaledHeight) / 2,
                scaledWidth, scaledHeight);
        uiBatch.end();
        inventoryStage.act(Gdx.graphics.getDeltaTime());
        inventoryStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        inventoryStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        font.dispose();
        uiBatch.dispose();
        inventoryStage.dispose();
        uiStage.dispose();
        skin.dispose();
        backgroundPlanetTexture.dispose();
        heroTexture.dispose();
        alienTexture.dispose();
        inventoryHUDTexture.dispose();
    }

    public World getWorld() {
        return world;
    }
}
