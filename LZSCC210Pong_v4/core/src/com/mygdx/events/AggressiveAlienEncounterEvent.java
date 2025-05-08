package com.mygdx.events;

import java.util.Random;
import com.mygdx.objects.Event;
import com.mygdx.objects.Alien;
import com.mygdx.objects.Planet;
import com.mygdx.objects.Player;


public class AggressiveAlienEncounterEvent extends Event {
    
    private Alien alien;
    private Planet planet;
    private Random random = new Random();
    private String lootResultMessage = "";
    
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
    
    public AggressiveAlienEncounterEvent(Alien alien, Planet planet) {
        super("Aggressive Alien Encounter", 
              "You've encountered an aggressive xenomorph alien on " + 
              planet.getName() + ". It looks hostile!");
        this.alien = alien;
        this.planet = planet;
        
        addAttackChoice();
        addFleeChoice();
    }
    
    private void addAttackChoice() {
        addChoice("Attack the alien", 50, player -> {
            // Successful attack gives loot
            harvestLoot(player);
            setSuccessMessage("You defeat the alien and collect:\n" + lootResultMessage);
            planet.setHarvested(true);
        },
        player -> {
            // Failed attack causes damage
            int damage = 20 + random.nextInt(10); 
            player.updateStat(Player.Stats.HEALTH, -damage);
            setFailureMessage("The alien overpowers you! You take " + damage + " damage to your hull integreity.");
            planet.setHarvested(true);
        });
    }
    
    private void addFleeChoice() {
        addChoice("Flee from the alien", 100, player -> {
            // Fleeing always succeeds but takes some damage
            int damage = 5 + random.nextInt(5); 
            player.updateStat(Player.Stats.HEALTH, -damage);
            setSuccessMessage("You successfully escape but suffer " + damage + " damage to your hull integrity in the process.");
            planet.setHarvested(true);
        },
        player -> {
            // No failure
            planet.setHarvested(true);
        });
    }
    
    private String getResourceTypeForPlanet() {
        switch (planet.getType()) {
            case Gas:
                return GAS_RESOURCE;
            case Mineral:
                return MINERAL_RESOURCE;
            case Organic:
                return ORGANIC_RESOURCE;
            default:
                return "Unknown";
        }
    }
    
    private void harvestLoot(Player player) {
        // Get resource type based on planet type
        String resourceType = getResourceTypeForPlanet();
        int baseAmount = calculateResourceAmount();
        
        // Create result message for the player
        StringBuilder resultMessage = new StringBuilder();
        boolean resourcesFound = false;
        
        // Determine resource rarities based on planet tier
        for (int i = 0; i < planet.getTier(); i++) {
            if (i >= RARITIES.length) break;
            
            String rarity = RARITIES[i];
            String resourceName = rarity + " " + resourceType;
            
            double probabilityThreshold;
            switch (i) {
                case 0: probabilityThreshold = 1.0; break;  // Common 
                case 1: probabilityThreshold = 0.7; break;  // Uncommon 
                case 2: probabilityThreshold = 0.4; break;  // Rare 
                case 3: probabilityThreshold = 0.2; break;  // Legendary 
                default: probabilityThreshold = 0.0; break;
            }
            
            // Only proceed if the random check passes
            if (random.nextDouble() < probabilityThreshold) {
                // Quantity calculation based on rarity
                int baseRarityAmount = Math.max(1, baseAmount / (i + 1));
                int randomVariation = random.nextInt(Math.max(1, baseRarityAmount / 2)) - (baseRarityAmount / 4);
                int rarityAmount = Math.max(1, baseRarityAmount + randomVariation);
                
                // Add to player's inventory
                player.addItemToInventory(resourceName, rarityAmount);
                
                // Add to result message
                resultMessage.append(rarityAmount).append("x ").append(resourceName).append("\n");
                resourcesFound = true;
            }
        }
        
        if (!resourcesFound) {
            resultMessage.append("No significant resources found.");
        }
        
        lootResultMessage = resultMessage.toString().trim();
    }
    
    private int calculateResourceAmount() {
        // Base amount is planet size
        int baseAmount = planet.getSize()/2;
        // Add some randomness (+-10%)
        int randomFactor = random.nextInt(Math.max(1, baseAmount / 5)) - (baseAmount / 10);
        
        return Math.max(1, baseAmount + randomFactor);
    }
}