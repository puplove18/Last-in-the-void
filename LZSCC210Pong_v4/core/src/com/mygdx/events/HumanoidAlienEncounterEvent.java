package com.mygdx.events;

import java.util.Random;
import com.mygdx.objects.Event;
import com.mygdx.objects.Alien;
import com.mygdx.objects.Planet;
import com.mygdx.objects.Player;


public class HumanoidAlienEncounterEvent extends Event {
    
    private Alien alien;
    private Planet planet;
    private Random random = new Random();
    
    // Resource types based on planet type
    private static final String ORGANIC_RESOURCE = "Biomass";
    private static final String GAS_RESOURCE = "Fuel";
    private static final String MINERAL_RESOURCE = "Building Materials";
    
    public HumanoidAlienEncounterEvent(Alien alien, Planet planet) {
        super("Humanoid Alien Encounter", 
              "You've encountered a friendly humanoid alien on " + planet.getName() + " (Tier " + planet.getTier() + ")." +
              "It appears to be interested in a trade.");
        this.alien = alien;
        this.planet = planet;
        
        // Add trade options based on planet tier
        addTradeChoices();
        addLeaveChoice();
    }
    
    private void addTradeChoices() {
        // Determine resource type based on planet type
        String resourceType = getResourceTypeForPlanet();
        String commonResource = "Common " + resourceType;
        String uncommonResource = "Uncommon " + resourceType;
        String rareResource = "Rare " + resourceType;
        String legendaryResource = "Legendary " + resourceType;
        
        // Get the tier of the planet to determine available trade options
        int planetTier = planet.getTier();
        
        // Add tier-specific trade options
        if (planetTier == 2) {
            addTier2TradeChoice(commonResource, uncommonResource);
        } else if (planetTier == 3) {
            addTier3TradeChoice(uncommonResource, rareResource);
        } else if (planetTier >= 4) {
            addTier4TradeChoice(rareResource, legendaryResource);
        }
    }
    
    private void addTier2TradeChoice(String commonResource, String uncommonResource) {
        String choiceText = "Trade] 80x " + commonResource + "\nfor 40x " + uncommonResource;
        
        addChoice(choiceText, 90, player -> {
            int availableCommon = player.getInventory().checkItemQuantity(commonResource);
            
            if (availableCommon >= 80) {
                player.getInventory().removeItem(commonResource, 80);
                player.addItemToInventory(uncommonResource, 40);
                setSuccessMessage("Trade successful! You received 40x " + uncommonResource);
            } else {
                setSuccessMessage("You don't have enough resources to make this trade.");
            }
            planet.setHarvested(true);
        }, 
        player -> {
            setFailureMessage("The alien seems displeased and refuses your offer.");
            planet.setHarvested(true);
        });
    }
    
    private void addTier3TradeChoice(String uncommonResource, String rareResource) {
        String choiceText = "Trade] 80x " + uncommonResource + "\nfor 40x " + rareResource;
        
        addChoice(choiceText, 90, player -> {
            int availableUncommon = player.getInventory().checkItemQuantity(uncommonResource);
            
            if (availableUncommon >= 80) {
                player.getInventory().removeItem(uncommonResource, 80);
                player.addItemToInventory(rareResource, 40);
                setSuccessMessage("Trade successful! You received 40x " + rareResource);
            } else {
                setSuccessMessage("You don't have enough resources to make this trade.");
            }
            planet.setHarvested(true);
        }, 
        player -> {
            setFailureMessage("The alien seems displeased and refuses your offer.");
            planet.setHarvested(true);
        });
    }
    
    private void addTier4TradeChoice(String rareResource, String legendaryResource) {
        String choiceText = "80x " + rareResource + "\nfor 40x " + legendaryResource;
        
        addChoice(choiceText, 90, player -> {
            int availableRare = player.getInventory().checkItemQuantity(rareResource);
            
            if (availableRare >= 80) {
                player.getInventory().removeItem(rareResource, 80);
                player.addItemToInventory(legendaryResource, 40);
                setSuccessMessage("Trade successful! You received 40x " + legendaryResource);
            } else {
                setSuccessMessage("You don't have enough resources to make this trade.");
            }
            planet.setHarvested(true);
        }, 
        player -> {
            setFailureMessage("The alien seems displeased and refuses your offer.");
            planet.setHarvested(true);
        });
    }
    
    // Option to leave the planet, will still be set to harvested so the player cannot return
    private void addLeaveChoice() {
        addChoice("Leave the alien in peace", 100, player -> {
            setSuccessMessage("You leave the planet.");
            planet.setHarvested(true);
        }, 
        player -> {
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
}