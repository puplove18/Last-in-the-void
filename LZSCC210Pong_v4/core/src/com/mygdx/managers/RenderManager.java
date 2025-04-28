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
        planetTextures.put(Planet.Type.Star,    new Texture(Gdx.files.internal("gas5.png")));
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
        float centerX = sw  * 0.5f;
        float centerY = sh  * 0.5f;

        Texture starTex = planetTextures.get(planets[0].getType());
        float starSize  = sh * 0.12f;
        float starX     = centerX - starSize * 0.5f;
        float starY     = centerY - starSize * 0.5f;
        planetBounds.add(new Rectangle(starX, starY, starSize, starSize));
        batch.draw(starTex, starX, starY, starSize, starSize);

        //remaining planets sit on orbits
        int n = planets.length - 1;
        if (n <= 0) return;

        float maxRadius   = Math.min(sw, sh) * 0.45f;
        float orbitStep   = maxRadius / n;
        float planetSize  = sh * 0.08f;

        for (int i = 1; i < planets.length; i++) {
            Texture tex = planetTextures.get(planets[i].getType());

            //spacing around the star
            float angle  = (float)(2 * Math.PI * (i - 1) / n);
            float radius = orbitStep * (i);

            float px = centerX + radius * (float)Math.cos(angle) - planetSize * 0.5f;
            float py = centerY + radius * (float)Math.sin(angle) - planetSize * 0.5f;

            planetBounds.add(new Rectangle(px, py, planetSize, planetSize));
            batch.draw(tex, px, py, planetSize, planetSize);
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
