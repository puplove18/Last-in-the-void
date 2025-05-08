package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class EngineFailureEvent extends Event {
    private final Random random = new Random();

    public EngineFailureEvent(UpgradesUI upgrades) {
        super(
                "Engine Failure",
                "Your ship’s engines sputter and falter mid-jump!"
        );

        addChoice(
                "Attempt emergency restart | 50",
                50,
                player -> {
                    int cost = upgrades.getFuelLevel() > 2 ? 15 : 30;
                    player.updateStat(Player.Stats.FUEL, -cost);
                    setSuccessMessage("Engines rebooted. Fuel drained " + cost + ".");
                },
                player -> {
                    int loss = upgrades.getFuelLevel() > 2 ? 15 : 30;
                    player.updateStat(Player.Stats.FUEL, -loss);
                    setFailureMessage("Restart failed. Fuel loss " + loss + " and systems offline.");
                }
        );

        addChoice(
                "Just ignore | 30",
                30,
                player -> setSuccessMessage("You drift safely until backup power kicks in."),
                player -> {
                    int dmg = upgrades.getHealthLevel() > 2 ? 5 : 10;
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setFailureMessage("Debris collision during drift. Hull damaged " + dmg + ".");
                }
        );
    }
}
