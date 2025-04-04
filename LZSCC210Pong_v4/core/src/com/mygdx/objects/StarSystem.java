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
import java.util.Random;

import com.mygdx.objects.InteractiveObject;
import com.mygdx.objects.Planet;
import com.mygdx.objects.Planet.Type;

public class StarSystem{
    private String name;
    private int size;
    private int tier;
    private int maxPlanets;
    private Planet[] planets;
    Random rand = new Random();
    
    com.mygdx.objects.Planet.Type star = Type.Star;


    // Constructor of the star system for custom system creation
    public StarSystem(String name,int starSize, int tier, int maxPlanets){
        this.name = name;
        this.tier = tier;
        this.size = starSize;
        this.maxPlanets = maxPlanets;
        this.planets = new Planet[maxPlanets];
    }


    //Default constructor
    public StarSystem(String name, int tier){
        this.name = name;
        this.tier = tier;
        this.size = 2000;
        this.maxPlanets = 6;
        this.planets = new Planet[6];
    }

    //Default constructor with basic limits used for debugging
    public StarSystem(){
        this.name = "Default";
        this.tier = 1;
        this.size = 2000;
        this.maxPlanets = 6;
        this.planets = new Planet[6];
    }
    // Method to to populate the star system with planets
    public void genrateStarPlanets(String name, int tier){
        this.name = name;  
        this.tier = tier;
        int sizeLim = this.maxPlanets;
        this.size = rand.nextInt(this.size) + 200;
        
        //Initialize the star of random size between set limit and 200, defualt limit is 2000
        this.planets[0] = new Planet(this.name + " 1", star, this.size, this.tier);

        for(int i = 1; i <= sizeLim-1; i++)
        {   
            Planet temp = new Planet();
            this.planets[i] = temp.generatePlanet(this.name, this.tier, i+1);
        }
    }

    public static void main(String[] args) {
        
        StarSystem test = new StarSystem("Andromeda", 4);
        test.genrateStarPlanets(test.name, test.tier);
        
        for (int i = 0; i <= test.maxPlanets-1; i++)
        {
            test.planets[i].printPlanet();
        }

    }
}