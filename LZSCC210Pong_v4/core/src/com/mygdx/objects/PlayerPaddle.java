package com.mygdx.objects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.helpers.Constants;
import com.mygdx.pong.PongGame;

/**
 *  This is the main PlayerPaddle class. It represents the basis for both Player and PlayerAI classes
 */
public abstract class PlayerPaddle {
	
	// protected variables so that they are accessible to subclasses
	protected final Body body;
	protected float x, y;
	protected int velY, score;
	protected Texture texture;
	
	public PlayerPaddle(float x, float y, Body body) {
		this.x = x;
		this.y = y;
		this.body = body;
		this.score = 0;
	}
	
	public abstract void update();
	
	public abstract void render(SpriteBatch spriteBatch);
	
	// Setter and getter for score
	public void updateScrore() {
		this.score += 1;
	}

	public int getScore() {
		return this.score;
	}
	
	// Computes the potential new velocity based on where the paddle wants to move and its maximal velocity
	public int getNewVelocity(int direction, int maxVelocity) {
		int velocity = this.velY;
		return Math.abs(velocity + direction) <= maxVelocity ? velocity + direction : velocity;
	}
	
	// Set new velocity based on the position of the paddle 
	protected void setNewVelocity(int paddleHeight) {
		
		if(tooCloseToBorder(paddleHeight)) {
			this.velY = 0;
			this.body.setLinearVelocity(0,0);
		} else
			body.setLinearVelocity(0, this.velY);
	}
	
	/** 
	 * Check if the paddle is too close to a border. This is required as kinematic objects (paddles) ignore collisions with static objects (walls)
	 * @param paddleHeight
	 * @return
	 */
	private boolean tooCloseToBorder(int paddleHeight) {
		float positionY = this.body.getWorldCenter().y * Constants.PPM;
		boolean tooCloseToUpperWall = this.velY > 0 && positionY + (paddleHeight / 2) > PongGame.getInstance().getWindowHeight() - Constants.UPPER_WALL_SIZE - 10;
		boolean tooCloseToLowerWall = this.velY < 0 && positionY - (paddleHeight / 2) < Constants.LOWER_WALL_SIZE + 10;
		return tooCloseToUpperWall || tooCloseToLowerWall;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
