package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class AbandonedStationEvent extends Event {
    private final Random random = new Random();

    public AbandonedStationEvent(UpgradesUI upgrades) {
        super("Abandoned Station", "You find a derelict station drifting silently.");
        addChoice("Salvage supplies | " + (60 + upgrades.getResourcesLevel() * 5), 60 + upgrades.getResourcesLevel() * 5,
                player -> {
                    int oxygen = 15 + upgrades.getOxygenLevel() * 2;
                    player.updateStat(Player.Stats.OXYGEN, oxygen);
                    setSuccessMessage("Recovered " + oxygen + " oxygen");
                },
                player -> {
                    int damage = 10 - upgrades.getHealthLevel();
                    damage = Math.max(1, damage);
                    player.updateStat(Player.Stats.HEALTH, -damage);
                    setFailureMessage("Structural collapse! Hull -" + damage);
                }
        );
        addChoice("Search for fuel | " + (40 + upgrades.getFuelLevel() * 3), 40 + upgrades.getFuelLevel() * 3,
                player -> {
                    player.updateStat(Player.Stats.FUEL, 20);
                    setSuccessMessage("Recovered 20 fuel");
                },
                player -> {
                    setFailureMessage("No usable fuel found");
                }
        );
    }
}
