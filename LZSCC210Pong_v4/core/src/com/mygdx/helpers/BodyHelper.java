package com.mygdx.helpers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *  Simple helper class for the creation of bodies
 */
public class BodyHelper {
	
	// Creation of rectangular shapes
	public static Body createRectangularBody(float x, float y, float width, float height, BodyType bodyType, float density, World world, ContactType type) {
		Body body = world.createBody(getBodyDef(x, y, bodyType));
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2/Constants.PPM, height/2/Constants.PPM);
		
		FixtureDef fixtureDef = getFixtureDef(shape, density);
		
		body.createFixture(fixtureDef).setUserData(type);
		
		shape.dispose();
		
		return body;
	}
	
	// Creation of circular shapes
	public static Body createCircularBody(float x, float y, float radius, BodyType bodyType, float density, World world, ContactType type) {
		Body body = world.createBody(getBodyDef(x, y, bodyType));
		
		CircleShape circle = new CircleShape();
		circle.setRadius(radius / Constants.PPM);
		circle.setPosition(new Vector2(radius / Constants.PPM, radius / Constants.PPM));
		
		FixtureDef fixtureDef = getFixtureDef(circle, density);
		body.createFixture(fixtureDef).setUserData(type);
		
		circle.dispose();
		
		return body;
	}
	
	// Auxiliary method to define fixtures
	private static FixtureDef getFixtureDef(Shape shape, float density) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 1;
		return fixtureDef;	
	}
	
	// Auxiliary method to define bodies
	private static BodyDef getBodyDef(float x, float y, BodyType type) {
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type = type;
		bodyDef.position.set(x/Constants.PPM, y/Constants.PPM);
		bodyDef.fixedRotation = true;
		
		return bodyDef;
	}
}
