package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

public class CosmicStormEvent extends Event {

    public CosmicStormEvent(UpgradesUI upgrades) {
        super("Cosmic Storm",
                "A sudden cosmic storm batters your ship with radiation.");

        addChoice("Reinforce shields | 50", 50,
                player -> {
                    player.updateStat(Player.Stats.FUEL, -15);
                    setSuccessMessage("Shields held! Fuel drained (-15).");
                },
                player -> {
                    player.updateStat(Player.Stats.OXYGEN, -15);
                    setFailureMessage("Shields failed. Oxygen systems damaged (-15).");
                }
        );

        addChoice("Divert power to engines and outrun it | 40", 40,
                player -> {
                    player.updateStat(Player.Stats.FUEL, -25);
                    setSuccessMessage("You outran the storm! Fuel -25.");
                },
                player -> {
                    player.updateStat(Player.Stats.FUEL, -10);
                    player.updateStat(Player.Stats.HEALTH, -10);
                    setFailureMessage("Too slow. Fuel -10% and hull damaged (-10).");
                }
        );

        addChoice("Ride it out in safe mode | 80", 80,
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -5);
                    setSuccessMessage("Minimal damage (-5 health). Storm subsides.");
                },
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -20);
                    setFailureMessage("Safe mode insufficient. Hull damage (-2%).");
                }
        );
    }
}
