package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.helpers.Constants;

public class SpaceShip extends SpaceShipBody {
	
	public SpaceShip(float x, float y, Body body) {
		super(x,y, body);
		
		Pixmap pixmap = new Pixmap(Constants.PLAYER_PADDLE_WIDTH, Constants.PLAYER_PADDLE_HEIGHT, Pixmap.Format.RGBA8888); //should change constants and Pixmap.Format
		pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.WHITE); //should change setColors
        pixmap.fill();
        
		this.texture = new Texture(pixmap);
		
		pixmap.dispose();
	}
	
	public void update() {
		if (Gdx.input.is) //if mouse left button clicked(, check for a complete press and release action)
		x = Gdx.input.getX(); //mouse position x
		y = Gdx.input.getY(); //mouse position y
					
	}
	
	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, x, y, Constants.PLAYER_PADDLE_WIDTH, Constants.PLAYER_PADDLE_HEIGHT);
	}

}
