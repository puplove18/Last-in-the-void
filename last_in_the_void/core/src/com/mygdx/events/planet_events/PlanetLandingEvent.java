package com.mygdx.events.planet_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Planet;
import com.mygdx.objects.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Event that occurs when landing on a planet.
 * Allows the player to harvest resources or leave the planet.
 */
public class PlanetLandingEvent extends Event {
    
    private Planet planet;
    private Random random = new Random();
    private Map<String, Integer> harvestedResources = new HashMap<>();
    private String harvestResultMessage = "";
    
    // Resource types based on planet type
    private static final String ORGANIC_RESOURCE = "Biomass";
    private static final String GAS_RESOURCE = "Fuel";
    private static final String MINERAL_RESOURCE = "Building Materials";

    
    private static final String[] RARITIES = {
        "Common",
        "Uncommon",
        "Rare",
        "Legendary"
    };
    
    public PlanetLandingEvent(Planet planet) {
        super("Planet Landing", "You've landed on " + planet.getName() + ", a " + planet.getType().toString().toLowerCase() + 
              " planet of size " + planet.getSize() + " (Tier " + planet.getTier() + "). What would you like to do?");
        this.planet = planet;
        
        addChoice("Harvest resources", 100, player -> {
            // Calculate and give resources
            harvestResources(player);
            
            setSuccessMessage(harvestResultMessage);
            
        }, player -> {
            // No failure outcome 
        });
        
        // Leave the planet without any effect
        addChoice("Leave the planet", 100, player -> {
            setSuccessMessage("You left the planet without harvesting any resources.");
            setReturnToSolarSystem(true); 
        }, player -> {
        });
    }
    
    private void harvestResources(Player player) {
        // Clear previous harvest
        harvestedResources.clear();

        planet.setHarvested(true);
    
        // Determine resource type based on planet type
        String resourceType;
        switch (planet.getType()) {
            case Gas:
                resourceType = GAS_RESOURCE;
                // Direct fuel update removed
                break;
            case Mineral:
                resourceType = MINERAL_RESOURCE;
                break;
            case Organic:
                resourceType = ORGANIC_RESOURCE;
                break;
            default:
                resourceType = "Unknown";
                break;
        }
    
        // Calculate base resource amount based *only* on planet size (and randomness)
        int baseAmount = calculateResourceAmount();
        int baseCost = 10;
        int additionalCost = 0;

        // Determines how much additional cost the player should face for harvesting based on planet tier
        switch (planet.getTier()) {
            case 1:
                additionalCost = 0;
                break;
            case 2:
                additionalCost = 2;
                break;
            case 3:
                additionalCost =4;
                break;
            case 4:
                additionalCost = 8;
                break;
            default:
                System.out.println("Invalid Tier");; // Won't be called
                break;

        }

        int totalCost = baseCost + additionalCost;

        StringBuilder costMessage = new StringBuilder("\n\nHarvesting cost:\n"); // Displays the cost of harvesting to the player
        switch (planet.getType()) {
            case Gas:
                player.updateStat(Player.Stats.HEALTH, -totalCost);
                costMessage.append("Hull Integrity: -").append(totalCost);
                break;
            case Mineral:
                player.updateStat(Player.Stats.OXYGEN, -totalCost);
                costMessage.append("Life Support: -").append(totalCost);
                break;
            case Organic:
                int halfCost = totalCost / 2;
                int remainingCost = totalCost - halfCost;
                player.updateStat(Player.Stats.HEALTH, -halfCost);
                player.updateStat(Player.Stats.FUEL, -remainingCost);
                costMessage.append("Hull Integrity: -").append(halfCost).append("\n");
                costMessage.append("Ship Fuel: -").append(remainingCost);
                break;
            default:
                System.out.println("Invalid planet type");
        }
    
        // Create result message for the player
        StringBuilder resultMessage = new StringBuilder("Resources harvested:\n");
        boolean resourcesFound = false; // Flag to check if anything was harvested
    
        // Determine resource rarities based on planet tier AND PROBABILITY
        for (int i = 0; i < planet.getTier(); i++) {
            if (i >= RARITIES.length) break;
            if (player.getResourcePermissionLevel() < planet.getTier()) break; 
            String rarity = RARITIES[i];
            String resourceName = rarity + " " + resourceType;
    
            // Probability Check 
            double probabilityThreshold;

            if (i+1 == planet.getTier()) {
                probabilityThreshold = 1.0;
            }
            else {
                switch (i) {
                    case 0: probabilityThreshold = 1.0; break;  // Common 
                    case 1: probabilityThreshold = 0.8; break;  // Uncommon
                    case 2: probabilityThreshold = 0.4; break;  // Rare
                    case 3: probabilityThreshold = 0.2; break;  // Legendary
                    default: probabilityThreshold = 0.0; break; // Won't ever be called, just necessary to stop compile error
                }
            }
    
            // Only proceed if the random check passes
            if (random.nextDouble() < probabilityThreshold) {
    
                // Quantity Calculation 
                int baseRarityAmount = Math.max(1, baseAmount / (i + 1));
                int randomVariation = random.nextInt(Math.max(1, baseRarityAmount / 2)) - (baseRarityAmount / 4);
                int rarityAmount = Math.max(1, baseRarityAmount + randomVariation);
    
                // Add to player's inventory
                player.addItemToInventory(resourceName, rarityAmount);
    
                // Store in results map
                harvestedResources.put(resourceName, rarityAmount);
    
                // Add to result message
                resultMessage.append(rarityAmount).append("x ").append(resourceName).append("\n");
                resourcesFound = true; // Mark that we found something
            }
        }
    
        if (!resourcesFound) { 
             resultMessage = new StringBuilder("No significant resources found."); 
        }
        
        resultMessage.append(costMessage);

        harvestResultMessage = resultMessage.toString().trim(); // Trim newline at end
        System.out.println(harvestResultMessage);
        setReturnToSolarSystem(true);
    }

    private int calculateResourceAmount() {
        // Base amount is planet size
        int baseAmount = planet.getSize()/2;
        // Add some randomness (±10%)
        int randomFactor = random.nextInt(Math.max(1, baseAmount / 5)) - (baseAmount / 10);
    
        return Math.max(1, baseAmount + randomFactor );
    }
    
    public Map<String, Integer> getHarvestedResources() {
        return harvestedResources;
    }

    public String getHarvestResultMessage() {
        return harvestResultMessage;
    }
}