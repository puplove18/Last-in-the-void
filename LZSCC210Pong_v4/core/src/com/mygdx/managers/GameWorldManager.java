package com.mygdx.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
//import com.mygdx.helpers.GameContactListener;
import com.mygdx.screens.GameScreen;
import com.mygdx.objects.Universe;

import java.lang.reflect.Method;


public class GameWorldManager {
    private OrthographicCamera camera;
    private World world;
    private GameScreen gameScreen;
    private Universe universe;

    public GameWorldManager(OrthographicCamera camera, GameScreen gameScreen) {
        Box2D.init();
        this.camera = camera;
        this.gameScreen = gameScreen;


        this.universe = new Universe();


        //this.world = new World(new Vector2(0, 0), false);
        //this.world.setContactListener(new GameContactListener(gameScreen));
    }


    public void update() {
        world.step(1/60f, 6, 2);
        camera.update();
    }


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


    public Universe getUniverse() {
        return universe;
    }


    public void travelTo(int index) {
        try {
            Method m = Universe.class.getDeclaredMethod("chooseDestination", int.class);
            m.setAccessible(true);
            m.invoke(universe, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dispose() {
        if (world != null) {
            world.dispose();
        }
    }
}