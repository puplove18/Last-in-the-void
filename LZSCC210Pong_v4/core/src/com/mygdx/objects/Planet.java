package com.mygdx.objects;

//import com.mygdx.assets.Texture;
import java.util.Random;

public class Planet {

    private String name;
    private Type type;
    private int tier;
    private int size;
    public float renderSize;
    private String playerSize;
    private int maxPlanetSize = 100;
    private boolean harvestSatus = false;
    //private Texture texture;
    Random rand = new Random();


    public enum Type {
        Gas,
        Mineral,
        Organic,
        Star,
        Event
    }

    //Set types for easy access
    Type gas = Type.Gas;
    Type min = Type.Mineral;
    Type org = Type.Organic;
    Type star = Type.Star;

    public boolean getHarvest() {
        return this.harvestSatus;
    }

    public String getName() {
        return this.name;
    }
    
    public Type getType() {
        return this.type;
    }

    public int getTier() {
        return this.tier;
    }

    public int getSize() {
        return this.size;
    }

    public String getPlayerSize() {
        return this.playerSize;
    }

    private void calculateRenderSize() {
        float typeMod;

        if (this.type == gas) {
            typeMod = 1.5f;
        }

        else if (this.type == min) {
            typeMod = 0.8f;
        }

        else if (this.type == org) {
            typeMod = 1.0f;
        }

        else { typeMod = 0.1f;}

        this.renderSize = typeMod * 0.0015f;
    }

    

    //Manual planet maker, pass through generate to add more data

    public Planet(String name, Type star2, int size, String pSize, int tier) {
        this.name = name;
        this.type = star2;
        this.size = size;
        this.playerSize = pSize;
        this.tier = tier;
    }

    public Planet(String name, Type star2, int size, int tier) {
        this.name = name;
        this.type = star2;
        this.size = size;
        this.tier = tier;
    }

    //Empty constructor
    public Planet(){
        name = "test";
    }

    //Method for printing planets for debugging purposes
    public void printPlanet(){
        System.out.println("Name: " + this.name);
        System.out.println("Type: " + this.type);
        System.out.println("Size: " + this.size);
        System.out.println("Player Size: " + this.playerSize);
        System.out.println("Tier: " + this.tier);
    }

    //Basic planet consructor for fully random planet with the exception of name and tier which is to be inherited from Star System
    public Planet generatePlanet(String name, int systemTier, int pos) {
        String new_name = name + " " + pos;

        Type planet_type;
        //Generate resource type of the planet
        double typeVal = rand.nextDouble() * 3;
        if (0 <= typeVal && typeVal < 1) 
            {planet_type = gas;}
        else if (1<= typeVal && typeVal < 2) 
            {planet_type = min;}
        else 
            {planet_type = org;}

        //Generates random size for the planet's texture, to be applied later
        int new_size = rand.nextInt(maxPlanetSize-1)+1;

        if (new_size < 20) {
            playerSize = "Dwarf Planet";
        }
        else if (new_size < 40) {
            playerSize = "Small Planet";
        }
        else if (new_size < 60) {
            playerSize = "Midplanet";
        }
        else if (new_size < 80) {
            playerSize = "Giant Planet";
        }
        else {
            playerSize = "Gargantuant Planet";
        }


        //To be implimented later when some way of tracking player progression exists, potentially inherited from Star System
        int planet_tier = 1;
        if (systemTier != 1){
            planet_tier = rand.nextInt(systemTier-1) + 1;
        }

        Planet new_planet = new Planet(new_name, planet_type, new_size, playerSize, planet_tier);
        new_planet.calculateRenderSize();

        return new_planet; // Added to ensure the program compiles, can change later

    }

    public static void main(String[] args){
        //Test the generatePlanet method
        Planet test = new Planet();
        Planet generated = test.generatePlanet("System", 3, 1);
        generated.printPlanet();
    }

}