package com.mygdx.objects;
import java.util.Random;
import com.mygdx.objects.Planet.Type;

public class StarSystem{
    private String name;
    private int size;
    private int tier;
    private int maxPlanets;
    private Planet[] planets;
    Random rand = new Random();
    
    com.mygdx.objects.Planet.Type star = Type.Star;

    public Planet[] getPlanets() {
        return this.planets;
    }

    public int getNumPlanets() {
        return this.planets.length;
    }

    public int getTier() {
        return this.tier;
    }

    public String getName() {
        return this.name;
    }

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
        this.name = "Deep Space";
        this.tier = 1;
        this.size = 2000;
        this.maxPlanets = 4;
        this.planets = new Planet[4];

        this.planets[0] = new Planet("Deep Space Star", Type.Star, 2000, 1);
        this.planets[1] = new Planet("Deep Space Mineral Planet", Type.Mineral, 50, "Midplanet", 1);
        this.planets[2] = new Planet("Deep Space Organic Planet", Type.Organic, 50, "Midplanet", 1);
        this.planets[3] = new Planet("Deep Space Gas Planet", Type.Gas, 50, "Midplanet", 1);

    }

    // Method to to populate the star system with planets
    public void genrateStarPlanets(String name, int tier){
        this.name = name;  
        this.tier = tier;
        int sizeLim = this.maxPlanets;
        this.size = rand.nextInt(this.size-200) + 200;
        
        //Initialize the star of random size between set limit and 200, defualt limit is 2000
        this.planets[0] = new Planet(this.name + " 1", star, this.size, "Star",this.tier);

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