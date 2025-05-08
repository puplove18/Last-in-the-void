package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.events.PlanetLandingEvent;
import com.mygdx.helpers.ScreenType;
import com.mygdx.objects.Alien;
import com.mygdx.objects.Event;
import com.mygdx.objects.Planet;
import com.mygdx.game.SpaceGame;
import com.mygdx.screens.GameScreen;

/**
 * Handles all input processing for the game screen
 */
public class InputHandler {
    private GameScreen gameScreen;
    private UIManager uiManager;
    private EventManager eventManager;
    
    public InputHandler(GameScreen gameScreen, UIManager uiManager, EventManager eventManager) {
        this.gameScreen = gameScreen;
        this.uiManager = uiManager;
        this.eventManager = eventManager;
    }
    
    public void handleInput() {
        handleExitInput();
        handlePauseInput();
        handleInventoryInput();
        handleUpgradesInput();
    }
    
    private void handleExitInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.postRunnable(() -> {
                SpaceGame.getInstance().changeScreen(gameScreen, ScreenType.MENU_UI);
            });
        }
    }
    
    private void handlePauseInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            gameScreen.togglePause();
        }
    }
    
    private void handleInventoryInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (!eventManager.isEventActive()) {
                uiManager.toggleInventory();
                uiManager.closeUpgrades();
                
                Gdx.input.setInputProcessor(uiManager.isInventoryOpen() ? 
                        uiManager.getInventoryStage() : uiManager.getUIStage());
            }
        }
    }
    
    private void handleUpgradesInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (!eventManager.isEventActive()) {
                uiManager.closeInventory();
                uiManager.toggleUpgrades();
    
                Gdx.input.setInputProcessor(uiManager.isUpgradesOpen() ?
                    uiManager.getUpgradesStage() : uiManager.getUIStage());
            }
        }
    }
}
