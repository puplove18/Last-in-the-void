package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class MeteorEvent extends Event {
    private final Random random = new Random();

    public MeteorEvent(UpgradesUI upgrades) {
        super("Meteor Storm", "A rogue meteor shower is barreling toward your ship");

        addChoice("Brace for impact | 80", 80,
                player -> {
                    int dmg = upgrades.getHealthLevel() > 2 ? 3 : 5;
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setSuccessMessage("You weathered the storm. Hull damage " + dmg);
                },
                player -> {
                    int dmg = upgrades.getHealthLevel() > 2 ? 15 : 20;
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setFailureMessage("A massive rock smashed into you. Hull damage " + dmg);
                }
        );

        addChoice("Evade with thrusters | 60", 60,
                player -> {
                    int cost = upgrades.getFuelLevel() > 2 ? 8 : 10;
                    player.updateStat(Player.Stats.FUEL, -cost);
                    setSuccessMessage("Evasive maneuvers successful. Fuel consumed " + cost);
                },
                player -> {
                    int cost = upgrades.getFuelLevel() > 2 ? 15 : 20;
                    int dmg = upgrades.getHealthLevel() > 2 ? 5 : 10;
                    player.updateStat(Player.Stats.FUEL, -cost);
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setFailureMessage("Evasion failed. Fuel " + cost + " and hull damage " + dmg);
                }
        );
    }
}
