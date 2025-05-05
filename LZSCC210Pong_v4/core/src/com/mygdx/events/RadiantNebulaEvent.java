package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import java.util.Random;

public class RadiantNebulaEvent extends Event {
    private final Random random = new Random();

    public RadiantNebulaEvent() {
        super("Radiant Nebula",
                "You drift through a glowing nebula that suffuses your ship with energy.");

        addChoice("Absorb nebula energy", 70,
                player -> {
                    double fuelGain = 20 + random.nextInt(11);   // 0–20%?
                    double oxygenGain = 15 + random.nextInt(6);   //5–10%>
                    player.updateStat(Player.Stats.FUEL, fuelGain);
                    player.updateStat(Player.Stats.OXYGEN, oxygenGain);
                    setSuccessMessage("Systems charged: +" + (int)fuelGain + "% fuel, +" +
                            (int)oxygenGain + "% life support.");
                },
                player -> {
                    setFailureMessage("Radiation overload forces systems offline; no gain.");
                }
        );

        addChoice("Take readings for science", 80,
                player -> {
                    setSuccessMessage("Valuable data stored—improves future resource yields.");
                },
                player -> {
                    setFailureMessage("Sensors malfunctioned in the nebula’s glare.");
                }
        );
    }
}
