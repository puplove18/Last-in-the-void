package com.mygdx.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.helpers.BodyHelper;
import com.mygdx.helpers.Constants;
import com.mygdx.helpers.ContactType;
import com.mygdx.pong.PongGame;
import com.mygdx.screens.GameScreen;

public class Ball {
	
	protected Body body;
	protected float x, y;
	protected float radius;
	protected Texture texture;
	
	public Ball(GameScreen gameScreen) {
		this.x = PongGame.getInstance().getWindowWidth() /2;
		this.y = PongGame.getInstance().getWindowHeight() /2;
				
		this.radius = Constants.BALL_RADIUS;
		
		// Pixmap is used to create the texture without need of a file
		Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
		pixmap.setBlending(Blending.None);
		pixmap.setColor(Color.WHITE);
		pixmap.fillCircle(8, 8, 8);
		texture = new Texture(pixmap);
		pixmap.dispose();
		
		// The ball is the only dynamic body of the game
		this.body = BodyHelper.createCircularBody(x, y, radius, BodyType.DynamicBody, 0.1f, gameScreen.getWorld(), ContactType.BALL);
		
		// Applies an initial force to start the ball movement
		body.applyForceToCenter(getInitialVelocityVector(), false);
	}
	

	
	public void update() {
		x = body.getPosition().x * Constants.PPM - (radius);
		y = body.getPosition().y * Constants.PPM - (radius);
	}
	
	// The reset method put the ball back to the centre and applies a new initial force
	public void reset() {
		
		x = PongGame.getInstance().getWindowWidth() / 2;
		y = PongGame.getInstance().getWindowHeight() / 2;
		
		// Stop the ball
		body.setLinearVelocity(0, 0);
		
		// Re-position the ball
		body.setTransform(x / Constants.PPM, y / Constants.PPM, 0);
		
		// Apply new initial force
		body.applyForceToCenter(getInitialVelocityVector(), false);
		
	}
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, x + radius, y + radius, radius*2, radius*2);
	}
	
	// Three getter methods for x, y and radius
	public float getY() {
		return this.y;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	// Returns the linear velocity of the ball
	public Vector2 getLinearVelocity() {
		return this.body.getLinearVelocity();
	}
	
	// Apply an impulse to the ball
	public void applyImpulse(Vector2 vector) {
		this.body.applyLinearImpulse(vector, this.body.getLocalCenter(), false);
	}
	
	// Create an initial velocity vector of length BALL_SPEED
	private Vector2 getInitialVelocityVector() {
		// coordinates vectors length
		float vectorLength1 = (float)Math.random();
		float vectorLength2 = (float)Math.random();
		
		// vectors directions
		int xSign = Math.random() < 0.5 ? -1 : 1;
		int ySign = Math.random() < 0.5 ? -1 : 1;
		
		// ensuring the ball will move horizontally more than vertically
		if(Math.abs(vectorLength1) > Math.abs(vectorLength2))
			return new Vector2(xSign * vectorLength1, ySign * vectorLength2).setLength(Constants.BALL_SPEED); 
		
		return new Vector2(xSign * vectorLength2, ySign * vectorLength1).setLength(Constants.BALL_SPEED);
	}
}
