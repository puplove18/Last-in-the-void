package com.mygdx.screens;

import javax.swing.event.ChangeEvent; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.audio.AudioManager;
import com.mygdx.helpers.BodyHelper;
import com.mygdx.helpers.Constants;
import com.mygdx.helpers.ContactType;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.helpers.GameContactListener;
import com.mygdx.helpers.ScreenType;
import com.mygdx.objects.Alien;
import com.mygdx.objects.AlienEncounterEvent;
import com.mygdx.objects.Event;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.objects.SpaceShip;
import com.mygdx.objects.Upgrades;
import com.mygdx.pong.PongGame;

// Implement the listener interface
public class GameScreen extends ScreenAdapter implements EventUI.EventCompletionListener {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private BitmapFont font; 
    private SpaceShip playerShip;
    private Player player;
    private Inventory inventory;
    private boolean inventoryOpen = false;
    private TextureAtlas backgroundAtlas;
    private TextureAtlas.AtlasRegion inventoryBackground;
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

    // Variables for event handling
    private EventUI eventUI;
    private Event sampleEvent; // This is just to test the event
    private Alien sampleAlien; // Needed for the AlienEncounterEvent, just as a test

    // Fonts for player stats 
    private BitmapFont statsFontWhite;
    private BitmapFont statsFontGreen;
    private BitmapFont statsFontYellow;
    private BitmapFont statsFontRed;


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
        this.player = new Player(); // Make sure player is initialized

        // Initialize Event related objects, again, just as a test
        this.sampleAlien = new Alien("<alien type here>"); // Create a sample alien
        this.sampleEvent = new AlienEncounterEvent(sampleAlien); // Create the event
        this.eventUI = new EventUI(player, this); // Create EventUI instance, passing 'this' as listener

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

        // Initialize Fonts - fixes a previous memory leak that caused the program to crash
        FancyFontHelper fontHelper = FancyFontHelper.getInstance();
        this.statsFontWhite = fontHelper.getFont(Color.WHITE, 16);
        this.statsFontGreen = fontHelper.getFont(Color.GREEN, 16);
        this.statsFontYellow = fontHelper.getFont(Color.YELLOW, 16);
        this.statsFontRed = fontHelper.getFont(Color.RED, 16);
        this.font = fontHelper.getFont(Color.WHITE, 20); // For pause message

        this.uiBatch = new SpriteBatch();
        loadInventoryTextures();
        setupInventoryUI();
        this.uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createExitButton();
        createInventoryButton(); 
        Gdx.input.setInputProcessor(uiStage); // Start with the main UI stage
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

    // I had to make changes to this method as the music would stop after an event was triggered
    public void update() {
        if (!eventUI.isVisible()) {
            handleInput();
        }

        if (!paused) {
            // Game logic only runs if not paused
            world.step(1 / 60f, 6, 2);
            camera.update();
            playerShip.update();
        }

        // Manage Music State
        if (paused && !eventUI.isVisible()) {
            AudioManager.getInstance().stopMusic();
        } else {
            AudioManager.getInstance().playMusic();
        }

        if (eventUI.isVisible()) {
             eventUI.render(); 
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            PongGame.getInstance().changeScreen(this, ScreenType.MENU_UI);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            paused = !paused;

        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (!eventUI.isVisible()) { // Prevent opening while event is active
                inventoryOpen = false;
                showUpgradesGUI = !showUpgradesGUI;
                Gdx.input.setInputProcessor(uiStage); // Ensure main UI gets input
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
             if (!eventUI.isVisible()) { // Prevent opening while event is active
                inventoryOpen = !inventoryOpen;
                showUpgradesGUI = false;
                 // Decide which input processor based on inventory state
                 Gdx.input.setInputProcessor(inventoryOpen ? inventoryStage : uiStage);
             }
        }

        // Input for triggering the event 
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (!eventUI.isVisible() && sampleEvent != null) {
                eventUI.showEvent(sampleEvent);
                paused = true; // Pause game when event starts
                inventoryOpen = false; // Close inventory if open
                showUpgradesGUI = false; // Close upgrades if open
                // EventUI sets its own input processor when shown
            }
        }
    }

     public void pause() {
        paused = true;
    }

    
    private void createInventoryButton() {
        TextButton inventoryButton = new TextButton("Inventory", skin);
        inventoryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!eventUI.isVisible()) {
                    inventoryOpen = !inventoryOpen;
                    showUpgradesGUI = false;

                    Gdx.input.setInputProcessor(inventoryOpen ? inventoryStage : uiStage);
                }
            }
        });
        
        Table inventoryTable = new Table();
        inventoryTable.setFillParent(true);
        inventoryTable.bottom().left().pad(10);
        inventoryTable.add(inventoryButton).width(120).height(50);
        
        // Add the table to the stage
        uiStage.addActor(inventoryTable);
    }

    private void setupInventoryUI() {
        inventoryStage = new Stage(new FitViewport(
                PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight()));
    }


    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Game World 
        batch.setProjectionMatrix(camera.combined); // Ensure game world uses the camera
        batch.begin();
        batch.draw(backgroundPlanetTexture, 0, 0, PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight());

        // Render ship, planets, etc.
        float screenWidth = PongGame.getInstance().getWindowWidth();
        float screenHeight = PongGame.getInstance().getWindowHeight();

        // Example entity rendering (adjust positions as needed)
        float heroX = 200; 
        float heroY = (screenHeight - heroTexture.getHeight()) / 2;
        batch.draw(heroTexture, heroX + heroTexture.getWidth(), heroY, heroTexture.getWidth(), heroTexture.getHeight());


        float alienX = screenWidth - alienTexture.getWidth();
        float alienY = (screenHeight - alienTexture.getHeight()) / 2 + 100;
        batch.draw(alienTexture, alienX + alienTexture.getWidth(), alienY, -alienTexture.getWidth(), alienTexture.getHeight());


        playerShip.render(batch); 
        drawPlayerStats(); 

        if (showUpgradesGUI && !eventUI.isVisible()) // Only show if event is not active
            upgrades.render(batch);

        if (paused && !eventUI.isVisible()) // Only show "Paused" if event is not the reason
            font.draw(batch, "Paused", screenWidth / 2 - 40, screenHeight / 2);

        batch.end();

        // Render UI Elements 
        // Use uiBatch for elements that should not be affected by the camera
        uiBatch.begin();
        if (inventoryOpen && !eventUI.isVisible()) { // Only show if event is not active
            renderInventoryUI(); // This method should use uiBatch internally now
        }
        uiBatch.end(); 

        // Render the main UI stage (exit button) unless event is showing
        if (!eventUI.isVisible()) {
            uiStage.act(delta);
            uiStage.draw();
        }

        // Render Event UI (already called in update if visible, renders on top)
        if (eventUI.isVisible()) {
             eventUI.render();
        }
    }

    private void drawPlayerStats() {
        // This method now uses the member fonts initialized in the constructor
        float statsX = 10;
        float statsY = PongGame.getInstance().getWindowHeight() - 20;
        double currentHealth = player.getHealth();
        double currentFuel = player.getFuel();
        double currentOxygen = player.getOxygen();

        // Use the pre-initialized white font
        statsFontWhite.draw(batch, "Name: Space Explorer", statsX, statsY); // Placeholder name

        // Select the correct pre-initialized font based on health color
        Color healthColor = getResourceColor(currentHealth);
        BitmapFont healthFont;
        if (healthColor.equals(Color.GREEN)) {
            healthFont = statsFontGreen;
        } else if (healthColor.equals(Color.YELLOW)) {
            healthFont = statsFontYellow;
        } else {
            healthFont = statsFontRed;
        }
        healthFont.draw(batch, "Health: " + (int) currentHealth + "%", statsX, statsY - 20);

        // Select the correct pre-initialized font based on fuel color
        Color fuelColor = getResourceColor(currentFuel);
        BitmapFont fuelFont;
        if (fuelColor.equals(Color.GREEN)) {
            fuelFont = statsFontGreen;
        } else if (fuelColor.equals(Color.YELLOW)) {
            fuelFont = statsFontYellow;
        } else {
            fuelFont = statsFontRed;
        }
        fuelFont.draw(batch, "Fuel: " + (int) currentFuel + "%", statsX, statsY - 40);

        // Select the correct pre-initialized font based on oxygen color
        Color oxygenColor = getResourceColor(currentOxygen);
        BitmapFont oxygenFont;
        if (oxygenColor.equals(Color.GREEN)) {
            oxygenFont = statsFontGreen;
        } else if (oxygenColor.equals(Color.YELLOW)) {
            oxygenFont = statsFontYellow;
        } else {
            oxygenFont = statsFontRed;
        }
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


    private void loadInventoryTextures() {
        backgroundAtlas = new TextureAtlas(Gdx.files.internal("backgrounds.atlas"));
        inventoryBackground = backgroundAtlas.findRegion("InventoryHUD002 (1)");
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

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update(); // Update camera after resizing
        inventoryStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
        eventUI.resize(width, height); // Resize EventUI stage
    }

    @Override
    public void dispose() {
        // Dispose existing resources
        batch.dispose();
        world.dispose();
        uiBatch.dispose();
        inventoryStage.dispose();
        uiStage.dispose();
        skin.dispose();
        if (backgroundPlanetTexture != null) backgroundPlanetTexture.dispose();
        if (heroTexture != null) heroTexture.dispose();
        if (alienTexture != null) alienTexture.dispose();

        // Dispose EventUI resources
        eventUI.dispose();

        // --- Dispose the player stats fonts (Fix) ---
        if (statsFontWhite != null) statsFontWhite.dispose();
        if (statsFontGreen != null) statsFontGreen.dispose();
        if (statsFontYellow != null) statsFontYellow.dispose();
        if (statsFontRed != null) statsFontRed.dispose();
        if (font != null) font.dispose(); // Dispose the 'font' used for pause message

        // Dispose any textures created by Alien or AlienEncounterEvent if necessary
    }

    public World getWorld() {
        return world;
    }

    // --- Implementation for EventCompletionListener ---
    @Override
    public void onEventCompleted() {
        paused = false; // Unpause the game
        Gdx.input.setInputProcessor(uiStage); // Set input back to the main UI stage
        System.out.println("Event completed!");
    }
}