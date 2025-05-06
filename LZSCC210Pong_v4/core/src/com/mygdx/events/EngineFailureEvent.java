package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import java.util.Random;
import com.mygdx.ui.UpgradesUI;

public class EngineFailureEvent extends Event {
    private final Random random = new Random();

    public EngineFailureEvent(UpgradesUI upgrades) {
        super("Engine Failure",
                "Your ship’s engines sputter and falter mid-jump!");

        addChoice("Attempt emergency restart | 50", 50,
                player -> {
                    player.updateStat(Player.Stats.FUEL, -15);
                    setSuccessMessage("Engines rebooted! Fuel drained (-15).");
                },
                player -> {
                    player.updateStat(Player.Stats.FUEL, -30);
                    setFailureMessage("Restart failed. Major fuel loss (-30) and systems offline.");
                }
        );

        addChoice("Coast inertially | 30", 30,
                player -> {
                    setSuccessMessage("You drift safely until backup power kicks in.");
                },
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -10);
                    setFailureMessage("Debris collision during drift! Hull damaged (-10).");
                }
        );

        addChoice("Jettison weight to conserve power | 70", 70,
                player -> {
                    player.updateStat(Player.Stats.FUEL, +10);
                    setSuccessMessage("Weight shed; power returns. Fuel efficiency +10.");
                },
                player -> {
                    setFailureMessage("No spare mass to jettison. No effect.");
                }
        );
    }
}
