package com.mygdx.events;

import java.util.Map;
import java.util.Random;
import com.mygdx.objects.Event;
import com.mygdx.objects.Player;

public class MeteorEvent extends Event {
    private final Random random = new Random();

    public MeteorEvent() {
        super("Meteor Storm",
                "A rogue meteor shower is barreling toward your ship! What will you do?");

        addChoice("Brace for impact", 80,
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -5);
                    setSuccessMessage("You weathered the storm. Minor hull damage sustained (-15%).");
                },
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -20);
                    setFailureMessage("A massive rock smashed into you! Significant hull damage (-30%).");
                }
        );

        addChoice("Evade with thrusters", 60,
                player -> {
                    player.updateStat(Player.Stats.FUEL, -10);
                    setSuccessMessage("Thrusters successfully dodged most meteors. Fuel consumed (-10%).");
                },
                player -> {
                    player.updateStat(Player.Stats.FUEL, -20);
                    player.updateStat(Player.Stats.HEALTH, -10);
                    setFailureMessage("Failed evasive maneuver. Fuel wasted (-20%) and hull damaged (-10%).");
                }
        );

    }

    private void jettisonCargo(Player player) {
        Map<String, Integer> items = player.getInventory().getItems();
        for (String item : items.keySet()) {
            if (item.contains("Building Materials")) {
                player.getInventory().removeItem(item, 1);
                return;
            }
        }
    }
}
