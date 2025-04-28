package com.mygdx.objects;
import java.util.Random;


public class Universe {
    private int maxDest;
    private int universeLength = 100;
    private int depth;
    private StarSystem[] destinations;

    //Player's current universe they're explporing, all on screen events take place here
    private StarSystem currentPosition;
    static Random rand = new Random();

    public StarSystem[] getDestinations(){
        return this.destinations;
    }

    public int getDepth() {
        return this.depth;
    }

    public StarSystem getCurrentPosition() {
        return this.currentPosition;
    }


    //Getters and setters for upgrade functionality
    public int getMaxDest() {
        return this.maxDest;
    }

    public void setMaxDest(int newMax) {
        this.maxDest = newMax;
    }

    //Main constructor to build an empty universe with the starting universe as current destination and generate the first set of choices for travel
    public Universe(){
        this.maxDest = 5;
        this.depth = 0;
        this.currentPosition = new StarSystem();
        this.generateDestinations(1);
    }

    // Generate random name for a star system
    private String generateName() {
        String[] alphabet = {"A", "B", "C", "D","E","F","G","H","I","J","K","L","M","N", "O", "P", "Q","R","S","T","U","V","W","X","Y","Z"};
        String name = "";
        for (int i = 0; i <= 2; i++) {
            int index = rand.nextInt(25);
            name += alphabet[index];
        }
        name += rand.nextInt(100);
        return name;
    }

    // Method to generate three brand new star systems as destinations and add them to a list
    private void generateDestinations(int tier) {

        int systemsNum = rand.nextInt(maxDest-3)+3;
        StarSystem[] newDestinations = new StarSystem[systemsNum];

        for (int i = 0; i <= systemsNum-1; i++){
            String newName = generateName();

            int starSize = rand.nextInt(2000-200)+200;
            int numPlanets = rand.nextInt(9)+3;

            StarSystem dest = new StarSystem(newName, starSize, tier, numPlanets);

            dest.genrateStarPlanets(newName, tier);
            newDestinations[i] = dest;
        }
        this.destinations = newDestinations;
    }

    // Method for selecting a destination universe and generating new choices for the player right away
    private void chooseDestination(int destination) {
        this.depth += 1;

        //Check if the player exceeded maximum universe size, might be removed
        if (depth >= universeLength) {
            System.out.println("Game Over Condition");
            return;
        } 
        //Update current position
        this.currentPosition = this.destinations[destination];
        this.generateDestinations((depth/25)+1);
        return;
    }

    private void printDestinations() {
        for (int i = 0; i <= this.destinations.length-1; i++) {
            StarSystem cur = this.destinations[i];
            int tier = this.destinations[i].getTier();
            System.out.println("System " + cur.getName() + " of size " + cur.getNumPlanets() + " and tier " + tier);
        }
    }

    private void printAllDestinations() {

        for (int i = 0; i <= this.destinations.length-1; i++)
        {
            Planet[] planets = this.destinations[i].getPlanets();
            for (int j = 0; j <= planets.length-1; j++) {
                planets[j].printPlanet();
            
            }
            System.out.println("System Printing Done\n");
        }

    }

    public static void main(String[] args) {
        Universe test = new Universe();
        Planet[] planets = test.currentPosition.getPlanets();

        for (int i = 0; i <= planets.length-1; i ++) {
            planets[i].printPlanet();
        }

        System.out.println("\n");
        test.printDestinations();

        for (int x = 0; x <= 100; x ++) {
            int choice = rand.nextInt(test.getDestinations().length);
            test.chooseDestination(choice);
            System.out.println("Chose destination " + choice);
            test.printDestinations();
            
        }

    }
}
