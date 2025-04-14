package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.helpers.ScreenType;
import com.mygdx.pong.PongGame;
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
        handleEventInput();
    }
    
    private void handleExitInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            PongGame.getInstance().changeScreen(gameScreen, ScreenType.MENU_UI);
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
                
                // Set appropriate input processor
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
                
                // Ensure main UI gets input
                Gdx.input.setInputProcessor(uiManager.getUIStage());
            }
        }
    }
    
    private void handleEventInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (!eventManager.isEventActive() && eventManager.hasEvent()) {
                eventManager.showCurrentEvent();
                gameScreen.setPaused(true);
                uiManager.closeInventory();
                uiManager.closeUpgrades();
                // EventUI sets its own input processor when shown
            }
        }
    }
}
