package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.helpers.FancyFontHelper;
import com.mygdx.helpers.ScreenType;
import com.mygdx.game.SpaceGame;

/**
 *  Simple menu screen to:
 *  - Start the game
 * - Check game information
 * - Quit the game
 */
public class MenuScreen extends ScreenAdapter{

	private SpriteBatch batch;
	private BitmapFont menu;
	private BitmapFont title;
	private BitmapFont footer;
	
	
	public MenuScreen() {
		
		this.batch = new SpriteBatch();
		this.title = FancyFontHelper.getInstance().getFont(Color.RED, 50);
		this.menu = FancyFontHelper.getInstance().getFont(Color.WHITE, 40);
		this.footer = FancyFontHelper.getInstance().getFont(Color.WHITE, 15);
		
	}
	
	// Takes care of user input and screen transitions
	public void update() {
		if(Gdx.input.isKeyPressed(Input.Keys.Q))
			SpaceGame.getInstance().exit(this);
		
		if(Gdx.input.isKeyPressed(Input.Keys.P))
			SpaceGame.getInstance().changeScreen(this, ScreenType.GAME);
		
		if(Gdx.input.isKeyPressed(Input.Keys.I))
			SpaceGame.getInstance().changeScreen(this, ScreenType.INFO);
	}
	
	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.batch.begin();
		
		this.title.draw(batch, "LAST IN THE VOID", 30, SpaceGame.getInstance().getWindowHeight() - 50);
		
		this.menu.draw(batch, getMenuText(), 10, SpaceGame.getInstance().getWindowHeight() / 2);
		
		this.batch.end();
		
	}
	
	private String getMenuText() {
		return "Press:\n"
				+ "   P - to start a new game\n"
				+ "   I - for information\n"
				+ "   Q - to quit the game";
				
	}
}
