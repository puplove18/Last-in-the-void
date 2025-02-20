package com.mygdx.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.objects.Player;
import com.mygdx.pong.PongGame;

/**
 *  Tests whether the paddle movement works as expected
 */
public class TestPaddleMovement {
	
	@BeforeEach
	public void initApplication() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		HeadlessApplication headlessApplication = new HeadlessApplication(new BlankApplication(), config);
		Assertions.assertNotNull(Gdx.files);
		
		Gdx.gl = Mockito.mock(GL20.class);
		
		PongGame.getInstance().createForTest(200, 200);
	}

	@Test
	public void testMoveUp() {
		Gdx.input = Mockito.mock(Input.class);
		Mockito.when(Gdx.input.isKeyPressed(Input.Keys.UP)).thenReturn(true);
		Mockito.when(Gdx.input.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
		
		World world = new World(new Vector2(0,0), false);
		
		Body playerBody = BodyHelper.createRectangularBody(10, 50, Constants.AI_PADDLE_WIDTH, Constants.AI_PADDLE_HEIGHT, BodyType.KinematicBody, 1f, world, ContactType.AI);
		
		// Arrange
		Player myPaddle = new Player(10, 50, playerBody);
		
		// Act
		world.step(1, 1, 1);
		myPaddle.update();
		
		// Assert (for pixel positions)
		Assertions.assertEquals(2, myPaddle.getX());
		Assertions.assertEquals(18, myPaddle.getY());
		
		// Act
		world.step(1, 1, 1);
		myPaddle.update();
		
		// Assert (for pixel positions)
		Assertions.assertEquals(2, myPaddle.getX());
		Assertions.assertEquals(50, myPaddle.getY());
	}
	
	@Test
	public void testMoveDown() {
		Gdx.input = Mockito.mock(Input.class);
		Mockito.when(Gdx.input.isKeyPressed(Input.Keys.UP)).thenReturn(false);
		Mockito.when(Gdx.input.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);
		
		World world = new World(new Vector2(0,0), false);
		
		Body playerBody = BodyHelper.createRectangularBody(10, 50, Constants.AI_PADDLE_WIDTH, Constants.AI_PADDLE_HEIGHT, BodyType.KinematicBody, 1f, world, ContactType.AI);
		
		// Arrange
		Player myPaddle = new Player(10, 50, playerBody);
		
		// Act
		world.step(1, 1, 1);
		myPaddle.update();
		
		// Assert (for pixel positions)
		Assertions.assertEquals(2, myPaddle.getX());
		Assertions.assertEquals(18, myPaddle.getY());
		
		// Act
		world.step(1, 1, 1);
		myPaddle.update();
		
		// Assert (for pixel positions)
		Assertions.assertEquals(2, myPaddle.getX());
		Assertions.assertEquals(18, myPaddle.getY(), "Position should stay equal, since paddle is at the end of the screen");
	}
}
