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
import com.mygdx.pong.PongGame;
import com.mygdx.objects.InteractiveObject;
//import com.mygdx.assets.Texture;
import java.util.Random;

public class Planet extends InteractiveObject{

    private String name;
    private Type type;
    private int tier;
    private int size;
    //private Texture texture;
    Random rand = new Random();


    enum Type {
        Gas,
        Mineral,
        Organic
    }


    //Set types for easy access
    Type gas = Type.Gas;
    Type min = Type.Mineral;
    Type org = Type.Organic;

    //Manual planet maker, pass through generate to add more data
    public Planet(String name) {
        this.name = name;

    }


    //Basic planet consructor for fully random planet with the exception of name which is to be inherited from Star System

    private boolean generatePlanet(String name) {
        this.name = name;

        //Generate resource type of the planet
        double typeVal = rand.nextDouble(3);
        if (0 <= typeVal && typeVal < 1) 
            { this.type = gas;}
        else if (1<= typeVal && typeVal < 2) 
            {this. type = min;}
        else 
            {this.type = org;}
        
        //Generates random size for the planet's texture, to be applied later
        this.size = rand.nextInt(100);

        //To be implimented later when some way of tracking player progression exists, potentially inherited from Star System
        //this.tier = 

    }

}