package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.pong.PongGame;
import com.mygdx.objects.Planet;
import com.mygdx.objects.StarSystem;
import com.mygdx.objects.Universe;
import java.util.EnumMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


public class RenderManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private PlayerManager playerManager;
    private GameWorldManager worldManager;
    private Universe universe;

    //textures
    private Texture backgroundPlanetTexture;
    private Texture systemBackground;
    private Texture heroTexture;
    private Texture alienTexture;
    private Map<Planet.Type, Texture> planetTextures;


    private BitmapFont font;

    private List<Rectangle> planetBounds = new ArrayList<>();

    public RenderManager(OrthographicCamera camera,
                         PlayerManager playerManager,
                         GameWorldManager worldManager) {
        this.camera = camera;
        this.playerManager = playerManager;
        this.worldManager = worldManager;
        this.universe = worldManager.getUniverse();
        this.batch = new SpriteBatch();

        loadTextures();
        this.font = FancyFontHelper.getInstance().getFont(Color.WHITE, 20);
    }

    private void loadTextures() {
        backgroundPlanetTexture = new Texture(Gdx.files.internal("planet1.png"));
        systemBackground       = new Texture(Gdx.files.internal("bg5.jpg"));

        heroTexture  = new Texture(Gdx.files.internal("entities/Astronaut.png"));
        alienTexture = new Texture(Gdx.files.internal("entities/alien.gif"));

        planetTextures = new EnumMap<>(Planet.Type.class);
        planetTextures.put(Planet.Type.Star,    new Texture(Gdx.files.internal("organic0.png")));
        planetTextures.put(Planet.Type.Gas,     new Texture(Gdx.files.internal("organic0.png")));
        planetTextures.put(Planet.Type.Mineral, new Texture(Gdx.files.internal("organic0.png")));
        planetTextures.put(Planet.Type.Organic, new Texture(Gdx.files.internal("organic0.png")));
        planetTextures.put(Planet.Type.Event,   new Texture(Gdx.files.internal("organic0.png")));
    }


    public void renderSystemView() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(systemBackground,
                0, 0,
                PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight());
        renderStarSystem();
        batch.end();
    }


    public void render(boolean paused,
                       boolean eventActive,
                       boolean inventoryOpen,
                       boolean upgradesOpen) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        batch.draw(backgroundPlanetTexture,
                0, 0,
                PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight());


        renderEntities();
        playerManager.renderShip(batch);
        playerManager.renderPlayerStats(batch);

        if (upgradesOpen && !eventActive) {
            playerManager.getInventory().showInventory();
        }
        if (paused && !eventActive) {
            renderPauseMessage();
        }

        batch.end();
    }


    private void renderStarSystem() {
        planetBounds.clear();
        StarSystem current = universe.getCurrentPosition();
        Planet[] planets   = current.getPlanets();

        float sw = PongGame.getInstance().getWindowWidth();
        float sh = PongGame.getInstance().getWindowHeight();
        float spacing = sw / (planets.length + 1);
        float iconSize = sh * 0.10f;

        for (int i = 0; i < planets.length; i++) {
            Planet p = planets[i];
            Texture tex = planetTextures.get(p.getType());
            float x = spacing * (i + 1) - iconSize * 0.5f;
            float y = sh * 0.75f - iconSize * 0.5f;
            planetBounds.add(new Rectangle(x, y, iconSize, iconSize));
            batch.draw(tex, x, y, iconSize, iconSize);
        }
    }


    public int getPlanetIndexAt(float x, float y) {
        for (int i = 0; i < planetBounds.size(); i++) {
            if (planetBounds.get(i).contains(x, y)) return i;
        }
        return -1;
    }


    private void renderEntities() {
        float screenWidth  = PongGame.getInstance().getWindowWidth();
        float screenHeight = PongGame.getInstance().getWindowHeight();

        float heroX = 200;
        float heroY = (screenHeight - heroTexture.getHeight()) / 2;
        batch.draw(heroTexture,
                heroX + heroTexture.getWidth(), heroY,
                heroTexture.getWidth(), heroTexture.getHeight());

        float alienX = screenWidth - alienTexture.getWidth();
        float alienY = (screenHeight - alienTexture.getHeight()) / 2 + 100;
        batch.draw(alienTexture, alienX + alienTexture.getWidth(), alienY, -alienTexture.getWidth(), alienTexture.getHeight());
    }


    private void renderPauseMessage() {
        font.draw(batch, "Paused", PongGame.getInstance().getWindowWidth() / 2 - 40, PongGame.getInstance().getWindowHeight() / 2);
    }


    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);camera.update();batch.setProjectionMatrix(camera.combined);
    }


    public void dispose() {
        if (batch != null) batch.dispose();
        if (backgroundPlanetTexture != null) backgroundPlanetTexture.dispose();
        if (systemBackground != null) systemBackground.dispose();
        if (heroTexture != null) heroTexture.dispose();
        if (alienTexture != null) alienTexture.dispose();
        if (font != null) font.dispose();
        for (Texture t : planetTextures.values()) {
            if (t != null) t.dispose();
        }
    }
}
