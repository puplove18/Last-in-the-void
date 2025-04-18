package com.mygdx.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.helpers.GameContactListener;
import com.mygdx.screens.GameScreen;

/**
 * Manages the Box2D physics world and core game elements
 */
public class GameWorldManager {
    private OrthographicCamera camera;
    private World world;
    private GameScreen gameScreen;
    
    public GameWorldManager(OrthographicCamera camera, GameScreen gameScreen) {
        Box2D.init();
        this.camera = camera;
        this.gameScreen = gameScreen;
        
        // Initialize world
        this.world = new World(new Vector2(0, 0), false);
        this.world.setContactListener(new GameContactListener(gameScreen));
    }
    
    public void update() {
        // Update physics
        world.step(1 / 60f, 6, 2);
        camera.update();
    }
    
    // Method to handle screen size changes
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    public World getWorld() {
        return world;
    }
    
    public OrthographicCamera getCamera() {
        return camera;
    }
    
    public void dispose() {
        if (world != null) {
            world.dispose();
        }
    }
}
