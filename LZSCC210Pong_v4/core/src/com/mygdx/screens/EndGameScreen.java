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

// Screen shown when either the player or the AI has won
public class EndGameScreen extends ScreenAdapter {
	private SpriteBatch batch;
	private BitmapFont font;
	private String message;
	private float initTime;
	
	// message is the message shown on the screen
	public EndGameScreen(String message) {
		
		this.batch = new SpriteBatch();
		this.font = FancyFontHelper.getInstance().getFont(Color.RED, 80);
		this.message = message;
		this.initTime = 0;
		
	}

	public void update() {
		if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY))
			SpaceGame.getInstance().changeScreen(this, ScreenType.MENU);
	}
	
	@Override
	public void render(float delta) {
		this.initTime += delta;
		update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.batch.begin();
		
		this.font.draw(batch, message, (SpaceGame.getInstance().getWindowWidth() / 2) - 200, SpaceGame.getInstance().getWindowHeight() / 2 + 100);
		
		this.batch.end();
		
		// it goes back to the menu screen after one second, even if no key was pressed
		if(this.initTime >= 1)
			SpaceGame.getInstance().changeScreen(this, ScreenType.MENU);
		
	}
}
