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


    public StarSystem(String name,int starSize, int tier, int maxPlanets){
        this.name = name;
        this.tier = tier;
        this.size = starSize;
        this.maxPlanets = maxPlanets;
        this.planets = new Planet[maxPlanets];
    }



    public StarSystem(String name, int tier){
        this.name = name;
        this.tier = tier;
        this.size = 2000;
        this.maxPlanets = 6;
        this.planets = new Planet[6];
    }

    public StarSystem(){
        this.name = "Deep Space";
        this.tier = 1;
        this.size = 2000;
        this.maxPlanets = 4;
        this.planets = new Planet[4];

        this.planets[0] = new Planet("Deep Space Star", Type.Star, 2000,"Gargantuant", 1);
        this.planets[1] = new Planet("Deep Space Mineral Planet", Type.Mineral, 50, "Midplanet", 1);
        this.planets[2] = new Planet("Deep Space Organic Planet", Type.Organic, 50, "Midplanet", 1);
        this.planets[3] = new Planet("Deep Space Gas Planet", Type.Gas, 50, "Midplanet", 1);

    }

    public void genrateStarPlanets(String name, int tier){
        this.name = name;  
        this.tier = tier;
        int sizeLim = this.maxPlanets;
        this.size = rand.nextInt(this.size-200) + 200;
        
        this.planets[0] = new Planet(this.name + " 1", star, this.size, "Star",this.tier);

        for(int i = 1; i <= sizeLim-1; i++)
        {   
            Planet temp = new Planet();
            this.planets[i] = temp.generatePlanet(this.name, this.tier, i+1);
        }
    }

}