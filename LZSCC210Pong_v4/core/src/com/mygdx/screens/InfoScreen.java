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
import com.mygdx.pong.PongGame;

/**
 * Simple screen explaining allowed inputs and winning condition
 */
public class InfoScreen extends ScreenAdapter {
	private SpriteBatch batch;
	private BitmapFont menu;
	private BitmapFont title;
	
	
	public InfoScreen() {
		
		this.batch = new SpriteBatch();
		this.title = FancyFontHelper.getInstance().getFont(Color.RED, 80);
		this.menu = FancyFontHelper.getInstance().getFont(Color.WHITE, 30);
		
		
	}
	
	// The user needs to press M to go back to the menu screen
	public void update() {
		if(Gdx.input.isKeyPressed(Input.Keys.M))
			PongGame.getInstance().changeScreen(this, ScreenType.MENU);
	}
	
	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.batch.begin();
		
		this.title.draw(batch, "Game Info", 150, PongGame.getInstance().getWindowHeight() - 50);
		
		this.menu.draw(batch, getMenuText(), 10, PongGame.getInstance().getWindowHeight() / 2 + 50);
		
		this.batch.end();	
	}
	
	private String getMenuText() {
		return "You can press:\n"
				+ "   UP - to move up\n"
				+ "   DOWN - to move down\n"
				+ "   R - to reset the ball (in case of bugs)\n"
				+ "   M - to return to the main menu\n\n"
				+ "The game ends as soon as someone scores\n"
				+ "10 points";
	}
}
