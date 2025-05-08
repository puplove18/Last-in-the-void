package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class SolarFlareEvent extends Event {
    private final Random random = new Random();

    public SolarFlareEvent(UpgradesUI upgrades) {
        super("Solar Flare", "A massive solar flare erupts nearby.");
        addChoice("Redirect shields | " + (50 + upgrades.getHealthLevel() * 4), 50 + upgrades.getHealthLevel() * 4,
                player -> {
                    int fuel = 10 - upgrades.getFuelLevel();
                    fuel = Math.max(1, fuel);
                    player.updateStat(Player.Stats.FUEL, -fuel);
                    setSuccessMessage("Shields held. Fuel -" + fuel);
                },
                player -> {
                    int oxy = 20 - upgrades.getOxygenLevel() * 2;
                    oxy = Math.max(1, oxy);
                    player.updateStat(Player.Stats.OXYGEN, -oxy);
                    setFailureMessage("Radiation breach. Oxygen -" + oxy);
                }
        );
        addChoice("Evade behind asteroid | " + (30 + upgrades.getInventoryLevel() * 2), 30 + upgrades.getInventoryLevel() * 2,
                player -> {
                    setSuccessMessage("Asteroid blocked most radiation");
                },
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -15);
                    setFailureMessage("Exposure caused hull damage -15");
                }
        );
    }
}
