package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.helpers.ScreenType;
import com.mygdx.screens.EndGameScreen;
import com.mygdx.screens.GameScreen;
import com.mygdx.screens.InfoScreen;
import com.mygdx.screens.MenuScreen;
import com.mygdx.screens.MenuScreenUi;
import com.mygdx.screens.CreditsScreen;
import com.mygdx.screens.DeadGameScreen;


/**
 * A singleton class representing the game. It takes care of
 * - initialising the game
 * - moving among different screens
 * Other classes can call the instance of this class via SpaceGame.getInstance()
 */
public class SpaceGame extends Game {

	private static SpaceGame INSTANCE = null;

	private int windowWidth, windowHeight;

	private OrthographicCamera ortographicCamera;

	private SpaceGame() {
		INSTANCE = this;
	}

	public static SpaceGame getInstance() {
		if(INSTANCE == null)
			INSTANCE = new SpaceGame();

		return INSTANCE;
	}

	public void createForTest(int width, int height) {
		this.windowHeight = width;
		this.windowHeight = height;
	}

	@Override
	public void create () {
		this.windowWidth = Gdx.graphics.getWidth();
		this.windowHeight = Gdx.graphics.getHeight();
		this.ortographicCamera = new OrthographicCamera();
		this.ortographicCamera.setToOrtho(false, this.windowWidth, this.windowHeight);

		// MenuScreen is the starting screen of the game
		setScreen(new MenuScreenUi());
	}

	    // Method to update window dimensions when screen size changes
		@Override
		public void resize(int width, int height) {
			this.windowWidth = width;
			this.windowHeight = height;
			this.ortographicCamera.setToOrtho(false, width, height);
			this.ortographicCamera.update();
			
			// Make sure the resize is passed to the current screen
			if (getScreen() != null) {
				getScreen().resize(width, height);
			}
		}

	// Getter methods for windows width and height
	public int getWindowWidth() {
		return windowWidth;
	}


	public int getWindowHeight() {
		return windowHeight;
	}

	// Two methods for moving among different screens
	public void changeScreen(Screen currentScreen, ScreenType newScreenType) {

		if(newScreenType == ScreenType.GAME)
			setScreen(new GameScreen(this.ortographicCamera));
		if(newScreenType == ScreenType.MENU_UI)
			setScreen(new MenuScreenUi());
		if(newScreenType == ScreenType.INFO)
			setScreen(new InfoScreen());
		if(newScreenType == ScreenType.CREDITS)
			setScreen(new CreditsScreen());
		if(newScreenType == ScreenType.DEAD_GAME)
			setScreen(new DeadGameScreen());
	}

	public void changeScreen(Screen currentScreen, ScreenType newScreenType, String message) {

		if(newScreenType == ScreenType.END_GAME){
			setScreen(new EndGameScreen(message));
		}
	}

	// Exit the game
	public void exit(Screen screen) {
		FancyFontHelper.getInstance().dispose();

		Gdx.app.exit();
	}


}
