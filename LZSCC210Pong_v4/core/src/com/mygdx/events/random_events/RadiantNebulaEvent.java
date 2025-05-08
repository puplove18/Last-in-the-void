package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class RadiantNebulaEvent extends Event {
    private final Random random = new Random();

    public RadiantNebulaEvent(UpgradesUI upgrades) {
        super(
                "Radiant Nebula",
                "You drift through a glowing nebula that suffuses your ship with energy"
        );

        addChoice(
                "Absorb nebula energy | 70",
                70 + upgrades.getResourcesLevel() * 5,
                player -> {
                    double baseFuel = 20 + random.nextInt(11);
                    double baseOxygen = 15 + random.nextInt(6);
                    double fuelGain = baseFuel * (1 + upgrades.getFuelLevel() * 0.1);
                    double oxygenGain = baseOxygen * (1 + upgrades.getOxygenLevel() * 0.1);
                    player.updateStat(Player.Stats.FUEL, fuelGain);
                    player.updateStat(Player.Stats.OXYGEN, oxygenGain);
                    setSuccessMessage(
                            "Systems charged: +" + (int)fuelGain + " fuel, +" + (int)oxygenGain + " life support"
                    );
                },
                player -> setFailureMessage("Radiation overload forces systems offline")
        );

        addChoice(
                "Take readings for science | 80",
                80 + upgrades.getResourcesLevel() * 3,
                player -> setSuccessMessage("Valuable data stored—improves future resource yields"),
                player -> setFailureMessage("Sensors malfunctioned in the nebula’s glare")
        );
    }
}
