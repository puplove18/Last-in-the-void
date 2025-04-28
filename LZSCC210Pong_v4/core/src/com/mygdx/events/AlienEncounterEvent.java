package com.mygdx.events;

import java.util.function.Consumer;
import com.mygdx.objects.Event;
import com.mygdx.objects.Alien;
import com.mygdx.objects.Player;

/**
 * A simple example event representing an encounter with an alien.
 * The player can choose to trade, attack, or flee.
 */
public class AlienEncounterEvent extends Event {
    
    private Alien alien;
    
    public AlienEncounterEvent(Alien alien) {
        super("Alien Encounter", "You've encountered a " + alien.getType() + " alien. What will you do?");
        this.alien = alien;
        
        // Add choices with different outcomes
        
        addChoice("Trade with the alien", 50, player -> {
            // Give player some fuel as a reward
            player.updateStat(Player.Stats.FUEL, 20);
            // Could have other outcomes such as:
           // player.addItemToInventory("Alien Tech");
        },
        player -> {
            // Failing has not outcome currently 
        });
        
        addChoice("Attack the alien", 25, player -> {
            player.updateStat(Player.Stats.HEALTH, -15);
            // player.addItemToInventory("Alien Remains"); This is just an example of another outcome
        },
        player -> { 
            player.updateStat(Player.Stats.HEALTH, -25); // Example: Lose 25 health on failure
        }
    );
        
        addChoice("Flee from the alien", 25, player -> {
            // Use some fuel to escape
            player.updateStat(Player.Stats.FUEL, -10);
        },
        player -> {
            // Failing has no outcome currently
        });
    }
}
