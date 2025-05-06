package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import java.util.Random;
import com.mygdx.ui.UpgradesUI;

public class DerelictShipEvent extends Event {
    private final Random rand = new Random();

    public DerelictShipEvent(UpgradesUI upgrades) {
        super("Derelict Ship",
                "You discover a derelict freighter drifting in space.");

        addChoice("Board carefully | 40", 70,
                player -> {
                    if (rand.nextBoolean()) {
                        player.updateStat(Player.Stats.HEALTH, 80);
                        setSuccessMessage("You salvaged Rare Tech and repaired your ship!!");
                    } else {
                        player.updateStat(Player.Stats.HEALTH, -10);
                        setSuccessMessage("Salvage succeeded but you sustained minor injuries (-10 health).");
                    }
                },
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -20);
                    setFailureMessage("Hull breach on entry! You take -20 health damage.");
                }
        );

        addChoice("Scan from orbit", 90,
                player -> {
                    player.updateStat(Player.Stats.FUEL, -5);
                    setSuccessMessage("Scan reveals valuable cargo location. Fuel -5 to mark coordinates.");
                },
                player -> {
                    setFailureMessage("Sensor malfunction. No data gained.");
                }
        );

        addChoice("Ignore and move on", 100,
                player -> {
                    setSuccessMessage("You avoid potential danger and continue your journey.");
                },
                player -> {}
        );
    }
}
