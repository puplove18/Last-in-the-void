package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.audio.AudioManager;
import com.mygdx.events.AlienEncounterEvent; // Updated import from the new package
import com.mygdx.managers.EventManager;
import com.mygdx.managers.GameWorldManager;
import com.mygdx.managers.InputHandler;
import com.mygdx.managers.PlayerManager;
import com.mygdx.managers.RenderManager;
import com.mygdx.managers.UIManager;
import com.mygdx.objects.Alien;
import com.mygdx.objects.Event;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Player;
import com.mygdx.pong.PongGame;
import com.mygdx.ui.EventUI;

/*
 *  Central control class for game screen
 */
public class GameScreen extends ScreenAdapter implements EventUI.EventCompletionListener {
    private OrthographicCamera camera;
    private GameWorldManager worldManager;
    private PlayerManager playerManager;
    private RenderManager renderManager;
    private UIManager uiManager;
    private InputHandler inputHandler;
    private EventManager eventManager;
    public boolean paused = false;
    
    
    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(PongGame.getInstance().getWindowWidth() / 2,
                PongGame.getInstance().getWindowHeight() / 2, 0));
        
        initializeManagers();
        
        Gdx.input.setInputProcessor(uiManager.getUIStage());
    }
    
    private void initializeManagers() {
        Inventory inventory = new Inventory(1000);
        Player player = new Player();
        
        worldManager = new GameWorldManager(camera, this);
        playerManager = new PlayerManager(player, inventory, worldManager.getWorld());
        renderManager = new RenderManager(camera, playerManager, worldManager);
        uiManager = new UIManager(inventory, player, this);
        eventManager = new EventManager(player, this);
        inputHandler = new InputHandler(this, uiManager, eventManager);
        
        // Tester for the event system - should be removed later
        setupTestEvent();
    }
    
    // To be removed later
    private void setupTestEvent() {
        Alien sampleAlien = new Alien("<alien type here>");
        Event sampleEvent = new AlienEncounterEvent(sampleAlien);
        eventManager.setCurrentEvent(sampleEvent);
    }
    
    public void update() {
        if (!eventManager.isEventActive()) {
            inputHandler.handleInput();
        }

        if (!paused) {
            // Game logic only runs if not paused
            worldManager.update();
            playerManager.update();
        }

        updateAudio();
        
        if (eventManager.isEventActive()) {
            eventManager.render();
        }
    }
    
    private void updateAudio() {
        if (paused && !eventManager.isEventActive()) {
            AudioManager.getInstance().stopMusic();
        } else {
            AudioManager.getInstance().playMusic();
        }
    }
    
    @Override
    public void render(float delta) {
        update();

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Delegate rendering to specialized managers
        renderManager.render(paused, eventManager.isEventActive(), uiManager.isInventoryOpen(), uiManager.isUpgradesOpen());
        
        // Render UI if not showing event
        if (!eventManager.isEventActive()) {
            uiManager.render(delta);
        }
        
        // Render event UI (on top of everything) if active
        if (eventManager.isEventActive()) {
            eventManager.render();
        }
    }
    
    @Override
    public void resize(int width, int height) {
        // Update camera
        camera.setToOrtho(false, width, height);
        camera.update();
        
        // Propagate resize to all managers
        worldManager.resize(width, height);
        renderManager.resize(width, height);
        uiManager.resize(width, height);
        eventManager.resize(width, height);
    }
    
    @Override
    public void dispose() {
        worldManager.dispose();
        renderManager.dispose();
        uiManager.dispose();
        eventManager.dispose();
    }
    
    // Required getters 
    public GameWorldManager getWorldManager() {
        return worldManager;
    }
    
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    public UIManager getUiManager() {
        return uiManager;
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    public void togglePause() {
        this.paused = !this.paused;
    }
    
    // EventCompletionListener implementation
    @Override
    public void onEventCompleted() {
        paused = false;
        Gdx.input.setInputProcessor(uiManager.getUIStage());
        System.out.println("Event completed!");
    }
}