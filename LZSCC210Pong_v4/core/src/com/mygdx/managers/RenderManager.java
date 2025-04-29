package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import java.util.Random;

public class RenderManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private PlayerManager playerManager;
    private GameWorldManager worldManager;
    private Universe universe;

    private Texture backgroundPlanetTexture;
    private Texture systemBackground;
    private Texture heroTexture;
    private Texture alienTexture;

    private Map<Planet.Type, List<Texture>> planetTextureVariants;
    private List<Texture> currentSystemTextures;

    private List<Rectangle> planetBounds = new ArrayList<>();
    private StarSystem lastSystem;
    private int displayCount;

    private BitmapFont font;
    private static final Random rand = new Random();

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
        heroTexture            = new Texture(Gdx.files.internal("entities/Astronaut.png"));
        alienTexture           = new Texture(Gdx.files.internal("entities/alien.gif"));

        planetTextureVariants = new EnumMap<>(Planet.Type.class);
        loadVariants(Planet.Type.Gas,     "planetTextures/gas/gas");
        loadVariants(Planet.Type.Mineral, "planetTextures/mineral/mineral");
        loadVariants(Planet.Type.Organic, "planetTextures/organic/organic");
        List<Texture> stars = new ArrayList<>();
        stars.add(new Texture(Gdx.files.internal("planetTextures/star/star0.png")));
        planetTextureVariants.put(Planet.Type.Star, stars);

    }

    private void loadVariants(Planet.Type type, String basePath) {
        List<Texture> variants = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            variants.add(new Texture(Gdx.files.internal(basePath + i + ".png")));
        }
        planetTextureVariants.put(type, variants);
    }

    private void prepareSystemView() {
        StarSystem system = universe.getCurrentPosition();
        if (system == lastSystem) return;
        lastSystem = system;

        Planet[] planets = system.getPlanets();
        int maxPlanets = Math.min(planets.length - 1, 4);
        displayCount = 2 + rand.nextInt(Math.max(1, maxPlanets - 1));

        currentSystemTextures = new ArrayList<>();
        currentSystemTextures.add(randomVariant(planets[0].getType()));
        for (int i = 1; i <= displayCount; i++) {
            currentSystemTextures.add(randomVariant(planets[i].getType()));
        }
    }

    private Texture randomVariant(Planet.Type type) {
        List<Texture> list = planetTextureVariants.get(type);
        return list.get(rand.nextInt(list.size()));
    }

    public void renderSystemView() {
        prepareSystemView();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.draw(systemBackground,
                0, 0,
                PongGame.getInstance().getWindowWidth(),
                PongGame.getInstance().getWindowHeight());
        drawOrbitsAndPlanets();
        batch.end();
    }

    private void drawOrbitsAndPlanets() {
        planetBounds.clear();
        float sw = PongGame.getInstance().getWindowWidth();
        float sh = PongGame.getInstance().getWindowHeight();
        float centerX = sw * 0.5f;
        float centerY = sh * 0.5f;

        Texture starTex = currentSystemTextures.get(0);
        float starSize = sh * 0.12f;
        float sx = centerX - starSize * 0.5f;
        float sy = centerY - starSize * 0.5f;
        planetBounds.add(new Rectangle(sx, sy, starSize, starSize));
        batch.draw(starTex, sx, sy, starSize, starSize);

        int count = currentSystemTextures.size() - 1;
        if (count <= 0) return;

        float maxRadius = Math.min(sw, sh) * 0.45f;
        float orbitStep = maxRadius / count;
        float planetSize = sh * 0.08f;

        for (int i = 1; i < currentSystemTextures.size(); i++) {
            float angle = (float)(2 * Math.PI * (i - 1) / count);
            float radius = orbitStep * i;
            float px = centerX + radius * (float)Math.cos(angle) - planetSize * 0.5f;
            float py = centerY + radius * (float)Math.sin(angle) - planetSize * 0.5f;
            planetBounds.add(new Rectangle(px, py, planetSize, planetSize));
            batch.draw(currentSystemTextures.get(i), px, py, planetSize, planetSize);
        }
    }

    public int getPlanetIndexAt(float x, float y) {
        for (int i = 0; i < planetBounds.size(); i++) {
            if (planetBounds.get(i).contains(x, y)) return i;
        }
        return -1;
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
        if (upgradesOpen && !eventActive)
            playerManager.getInventory().showInventory();
        if (paused && !eventActive)
            renderPauseMessage();
        batch.end();
    }

    private void renderEntities() {
        float w = PongGame.getInstance().getWindowWidth();
        float h = PongGame.getInstance().getWindowHeight();
        float heroX = 200;
        float heroY = (h - heroTexture.getHeight()) / 2;
        batch.draw(heroTexture,
                heroX + heroTexture.getWidth(), heroY,
                heroTexture.getWidth(), heroTexture.getHeight());
        float alienX = w - alienTexture.getWidth();
        float alienY = (h - alienTexture.getHeight()) / 2 + 100;
        batch.draw(alienTexture,
                alienX + alienTexture.getWidth(), alienY,
                -alienTexture.getWidth(), alienTexture.getHeight());
    }

    private void renderPauseMessage() {
        font.draw(batch,
                "Paused",
                PongGame.getInstance().getWindowWidth() / 2 - 40,
                PongGame.getInstance().getWindowHeight() / 2);
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void dispose() {
        batch.dispose();
        backgroundPlanetTexture.dispose();
        systemBackground.dispose();
        heroTexture.dispose();
        alienTexture.dispose();
        font.dispose();
        for (List<Texture> list : planetTextureVariants.values()) {
            for (Texture t : list) t.dispose();
        }
    }
}
