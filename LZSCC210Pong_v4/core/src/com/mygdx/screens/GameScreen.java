package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.audio.AudioManager;
import com.mygdx.events.AlienEncounterEvent;
import com.mygdx.events.PlanetLandingEvent;
import com.mygdx.helpers.ScreenType;
import com.mygdx.managers.EventManager;
import com.mygdx.managers.GameWorldManager;
import com.mygdx.managers.InputHandler;
import com.mygdx.managers.PlayerManager;
import com.mygdx.managers.RenderManager;
import com.mygdx.managers.UIManager;
import com.mygdx.objects.Event;
import com.mygdx.objects.Inventory;
import com.mygdx.objects.Planet;
import com.mygdx.objects.Player;
import com.mygdx.objects.StarSystem;
import com.mygdx.objects.Universe;
import com.mygdx.game.SpaceGame;
import com.mygdx.ui.EventUI;
import java.util.Random;

import javax.swing.event.ChangeEvent;
import com.mygdx.objects.Alien;
import com.mygdx.events.AlienEncounterEvent;

public class GameScreen extends ScreenAdapter implements EventUI.EventCompletionListener {
    private OrthographicCamera camera;
    private GameWorldManager worldManager;
    private PlayerManager playerManager;
    private RenderManager renderManager;
    private UIManager uiManager;
    private InputHandler inputHandler;
    private EventManager eventManager;
    private boolean paused = false;
    private boolean systemView = true;


    private int starJumpCount = 0;
    private Skin skin;
    private TextButton nextButton;
    private static final Random rand = new Random();

    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(
                SpaceGame.getInstance().getWindowWidth() / 2f,
                SpaceGame.getInstance().getWindowHeight() / 2f,
                0
        );
        initializeManagers();
        Gdx.input.setInputProcessor(uiManager.getUIStage());
    }

    private void initializeManagers() {
        Inventory inventory = new Inventory(1000);
        Player player = new Player();

        worldManager = new GameWorldManager(camera, this);
        Universe universe = worldManager.getUniverse();
        playerManager = new PlayerManager(player, inventory, worldManager.getWorld());
        renderManager = new RenderManager(camera, playerManager, worldManager);
        uiManager = new UIManager(inventory, player, this, universe);
        eventManager = new EventManager(player, this);
        inputHandler = new InputHandler(this, uiManager, eventManager);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        nextButton = new TextButton("Next System1", skin);
        nextButton.setPosition(
                SpaceGame.getInstance().getWindowWidth() - 800,
                SpaceGame.getInstance().getWindowHeight() - 50
        );


        //this button is not used!!!


        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Choose a random next system:
                StarSystem[] dests = worldManager.getUniverse().getDestinations();
                int idx = rand.nextInt(dests.length);

                // Compute your fuel cost (for example, 10 units per jump)
                float cost = 10f;
                Player ship = playerManager.getPlayer();

                // Only jump if you have enough fuel
                if (ship.getFuel() >= cost) {
                    // Deduct fuel
                    ship.setFuel(ship.getFuel() - cost);

                    // Now actually travel
                    renderManager.setSelectedBackgroundType(
                            dests[idx].getPlanets()[0].getType()
                    );
                    worldManager.travelTo(idx);

                    //starJumpCount++;

//                    if (starJumpCount % 5 == 0) {
//                        //Event e = eventManager.nextPlotEvent();
//                        //eventManager.setCurrentEvent(e);
//                        //eventManager.showCurrentEvent();
//                        //setPaused(true);
//                    } else if (starJumpCount % 2 == 0) {
//                        Event e = eventManager.randomEventForCurrentSystem();
//                        eventManager.setCurrentEvent(e);
//                        eventManager.showCurrentEvent();
//                        setPaused(true);
//                    }
                    starJumpCount++;

                    if (starJumpCount % 2 == 0) {
                        //event
                        System.out.println("2 jump!");
                    }

                    systemView = false;
                    camera.position.set(
                            SpaceGame.getInstance().getWindowWidth()/2f,
                            SpaceGame.getInstance().getWindowHeight()/2f,
                            0
                    );
                    camera.update();
                } else {
                    // Optional: feedback to player
                    System.out.println("Not enough fuel to jump!");
                }
            }
        });


        uiManager.getUIStage().addActor(nextButton);
    }

    public void update() {
        if (!eventManager.isEventActive()) {
            inputHandler.handleInput();
        }
        if (!paused) {
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (systemView) {
            if (!uiManager.isSomethingOpen() && Gdx.input.justTouched()) {
                Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);
                int idx = renderManager.getPlanetIndexAt(touch.x, touch.y);
                if (idx >= 0) {
                    Planet clicked = renderManager.getCurrentSystemPlanets().get(idx);
                if (clicked.getType() == Planet.Type.Star) {
                    System.out.println("This is a star planet!");
                    System.out.println("Not enough fuel!");
                    System.out.println("You were lost in space");

                    SpaceGame.getInstance().changeScreen(this, ScreenType.DEAD_GAME);
                    return;
                }else if (!clicked.getHarvest()) {
                    float cost = 10f; //cost of travel planet
                    if (playerManager.getPlayer().getFuel() >= cost) {
                        playerManager.getPlayer().setFuel(playerManager.getPlayer().getFuel() - cost);
                        renderManager.setSelectedBackgroundType(clicked.getType());
                        // (Commented out in order to fix events) worldManager.travelTo(idx);
                        systemView = false;
                        camera.position.set(
                                SpaceGame.getInstance().getWindowWidth()/2f,
                                SpaceGame.getInstance().getWindowHeight()/2f,
                                0
                        );
                        camera.update();
                        
                        // Calls the planet landing event or alien encounter event when a planet is clicked on
                        if (!eventManager.isEventActive()) {
                            Event planetEvent;
                            
                            if (clicked.getHasAlien()) {
                                // Create a random alien type
                                String[] alienTypes = {"Humanoid", "Aggressive Xenomorph"};
                                String alienType = alienTypes[rand.nextInt(alienTypes.length)];
                                Alien alien = new Alien(alienType);

                                // Create the alien event
                                planetEvent = new AlienEncounterEvent(alien);
                            } else {
                                // If the planet has no alien then trigger planet landing event
                                planetEvent = new PlanetLandingEvent(clicked);
                            }
                            
                            eventManager.setCurrentEvent(planetEvent);
                            eventManager.showCurrentEvent();
                            setPaused(true);
                            uiManager.closeInventory();
                            uiManager.closeUpgrades();
                        }
                    } else {
                        System.out.println("Not enough fuel!");
                        System.out.println("You were lost in space");

                        SpaceGame.getInstance().changeScreen(this, ScreenType.DEAD_GAME);
                        return;
                    }
                } else {
                    System.out.println("This planet has already been visited and harvested!");
                }
            }
        }

            renderManager.renderSystemView();
            uiManager.render(delta);
            if (eventManager.isEventActive()) {
                eventManager.render();
            }
            return;
        }

        renderManager.render(
                paused,
                eventManager.isEventActive(),
                uiManager.isInventoryOpen(),
                uiManager.isUpgradesOpen()
        );
        if (!eventManager.isEventActive()) {
            uiManager.render(delta);
        }
        if (eventManager.isEventActive()) {
            eventManager.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
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
        skin.dispose();
    }

    @Override
    public void onEventCompleted() {
        paused = false;
        systemView = true;

        // close any open panels (especially the scanner)
        uiManager.closeScanner();
        uiManager.closeInventory();
        uiManager.closeUpgrades();

        // reset camera back to center of your system view
        camera.position.set(
                SpaceGame.getInstance().getWindowWidth()/2f,
                SpaceGame.getInstance().getWindowHeight()/2f,
                0
        );
        camera.update();

        // restore input to the main UI stage
        Gdx.input.setInputProcessor(uiManager.getUIStage());

        System.out.println("Event completed!");
    }

    private void returnToSystemView() {
        systemView = true;
        camera.position.set(
            SpaceGame.getInstance().getWindowWidth()/2f,
            SpaceGame.getInstance().getWindowHeight()/2f,
            0
        );
        camera.update();
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void togglePause() {
        this.paused = !this.paused;
    }

    public GameWorldManager getWorldManager() {
        return worldManager;
    }
}
