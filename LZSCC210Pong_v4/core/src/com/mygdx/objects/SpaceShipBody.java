package com.mygdx.objects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 *  This is the main PlayerPaddle class. It represents the basis for both Player and PlayerAI classes
 */
public abstract class SpaceShipBody {
	
	// protected variables so that they are accessible to subclasses
	protected final Body body;
	protected float x, y;
	protected Texture texture;
	
	public SpaceShipBody(float x, float y, Body body) {
		this.x = x;
		this.y = y;
		this.body = body;
		
	}
	
	public abstract void update();
	
	public abstract void render(SpriteBatch spriteBatch);
	
	// Setter and getter for score

	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
