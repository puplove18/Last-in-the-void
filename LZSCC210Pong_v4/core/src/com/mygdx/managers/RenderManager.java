package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.pong.PongGame;

/**
 * Manages rendering of game elements
 */
public class RenderManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private PlayerManager playerManager;
    private GameWorldManager worldManager;
    
    // Game textures
    private Texture backgroundPlanetTexture;
    private Texture heroTexture;
    private Texture alienTexture;
    
    // Pause font
    private BitmapFont font;
    
    public RenderManager(OrthographicCamera camera, PlayerManager playerManager, GameWorldManager worldManager) {
        this.camera = camera;
        this.playerManager = playerManager;
        this.worldManager = worldManager;
        this.batch = new SpriteBatch();
        
        loadTextures();
        
        // Initialize pause font
        this.font = FancyFontHelper.getInstance().getFont(Color.WHITE, 20);
    }
    
    private void loadTextures() {
        backgroundPlanetTexture = new Texture(Gdx.files.internal("planet1.png"));
        heroTexture = new Texture(Gdx.files.internal("entities/Astronaut.png"));
        alienTexture = new Texture(Gdx.files.internal("entities/alien.gif"));
    }

        // Method to handle screen size changes
        public void resize(int width, int height) {
            // Update rendering dimensions
            camera.setToOrtho(false, width, height);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
        }
    
    public void render(boolean paused, boolean eventActive, boolean inventoryOpen, boolean upgradesOpen) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Render background
        batch.draw(backgroundPlanetTexture, 0, 0, PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight());
        
        renderEntities();
        
        // Render player ship and stats
        playerManager.renderShip(batch);
        playerManager.renderPlayerStats(batch);
        
        // Render upgrades if open
        if (upgradesOpen && !eventActive) {
            playerManager.getInventory().showInventory();
        }
        
        // Render pause message if paused and not in event
        if (paused && !eventActive) {
            renderPauseMessage();
        }
        
        batch.end();
    }
    
    private void renderEntities() {
        float screenWidth = PongGame.getInstance().getWindowWidth();
        float screenHeight = PongGame.getInstance().getWindowHeight();
        
        // Example entity rendering (adjust positions as needed)
        float heroX = 200;
        float heroY = (screenHeight - heroTexture.getHeight()) / 2;
        batch.draw(heroTexture, heroX + heroTexture.getWidth(), heroY, 
                heroTexture.getWidth(), heroTexture.getHeight());
        
        float alienX = screenWidth - alienTexture.getWidth();
        float alienY = (screenHeight - alienTexture.getHeight()) / 2 + 100;
        batch.draw(alienTexture, alienX + alienTexture.getWidth(), alienY, 
                -alienTexture.getWidth(), alienTexture.getHeight());
    }
    
    private void renderPauseMessage() {
        font.draw(batch, "Paused", 
                PongGame.getInstance().getWindowWidth() / 2 - 40, 
                PongGame.getInstance().getWindowHeight() / 2);
    }
    
    public void dispose() {
        if (batch != null) batch.dispose();
        if (backgroundPlanetTexture != null) backgroundPlanetTexture.dispose();
        if (heroTexture != null) heroTexture.dispose();
        if (alienTexture != null) alienTexture.dispose();
        if (font != null) font.dispose();
    }
}