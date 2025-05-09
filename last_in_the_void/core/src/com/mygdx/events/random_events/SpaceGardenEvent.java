package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class SpaceGardenEvent extends Event {
    private final Random random = new Random();

    public SpaceGardenEvent(UpgradesUI upgrades) {
        super("Space Garden", "You encounter a drifting hydroponic module still producing food.");
        addChoice("Harvest food | " + (60 + upgrades.getResourcesLevel() * 4), 60 + upgrades.getResourcesLevel() * 4,
                player -> {
                    int biomass = 20 + upgrades.getOxygenLevel() * 2;
                    player.addItemToInventory("Common Biomass", biomass);
                    setSuccessMessage("Collected " + biomass + " biomass");
                },
                player -> {
                    setFailureMessage("Module systems failed before harvest");
                }
        );
        addChoice("Collect seeds | " + (50 + upgrades.getInventoryLevel() * 3), 50 + upgrades.getInventoryLevel() * 3,
                player -> {
                    player.addItemToInventory("Uncommon Biomass", 10);
                    setSuccessMessage("Recovered 5 uncommon seeds");
                },
                player -> {
                    setFailureMessage("Seeds spoiled in vacuum");
                }
        );
    }
}
