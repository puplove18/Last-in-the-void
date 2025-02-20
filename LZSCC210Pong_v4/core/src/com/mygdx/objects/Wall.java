package com.mygdx.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.helpers.BodyHelper;
import com.mygdx.helpers.ContactType;
import com.mygdx.pong.PongGame;
import com.mygdx.screens.GameScreen;

public class Wall {
	
	private Body body;
	private float x, y;
	private int width, height;
	private Texture texture;
	
	public Wall(float y, int height, GameScreen gameScreen) {
		this.x = PongGame.getInstance().getWindowWidth() / 2;
		this.y = y;
		this.width = PongGame.getInstance().getWindowWidth();
		this.height = height;
		
		// Pixmap is used to create a texture without using a file
		Pixmap pixmap = new Pixmap(10, 10, Format.RGBA8888);
		pixmap.setBlending(Blending.None);
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0, 0, width, this.height);
		this.texture = new Texture(pixmap);
		pixmap.dispose();
		
		// Walls are the static objects of the game
		this.body = BodyHelper.createRectangularBody(x, y, width, this.height, BodyType.StaticBody, 0, gameScreen.getWorld(), ContactType.WALL);
	}
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, x - (width/2), y - (this.height/2), width, this.height);
	}
	
}
