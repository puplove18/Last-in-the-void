package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class SpaceParasitesEvent extends Event {
    private final Random random = new Random();

    public SpaceParasitesEvent(UpgradesUI upgrades) {
        super(
                "Parasite Infestation",
                "Microscopic space parasites breach your hull plating!"
        );

        addChoice(
                "Seal affected compartments | " + (60 + upgrades.getHealthLevel() * 5),
                60 + upgrades.getHealthLevel() * 5,
                player -> {
                    int loss = 10 - upgrades.getHealthLevel() * 2;
                    loss = Math.max(1, loss);
                    player.updateStat(Player.Stats.OXYGEN, -loss);
                    setSuccessMessage("Compartment sealed. Oxygen loss " + loss);
                },
                player -> {
                    int loss = 20 - upgrades.getHealthLevel() * 2;
                    loss = Math.max(1, loss);
                    player.updateStat(Player.Stats.OXYGEN, -loss);
                    setFailureMessage("Seal failed; oxygen loss " + loss);
                }
        );

        addChoice(
                "Use bio-filter dispensers | " + (40 + upgrades.getResourcesLevel() * 3),
                40 + upgrades.getResourcesLevel() * 3,
                player -> {
                    int loss = 10 - upgrades.getOxygenLevel();
                    loss = Math.max(1, loss);
                    player.updateStat(Player.Stats.OXYGEN, -loss);
                    setSuccessMessage("Filters worked. Oxygen loss " + loss);
                },
                player -> {
                    int damage = 15 - upgrades.getHealthLevel() * 3;
                    damage = Math.max(1, damage);
                    player.updateStat(Player.Stats.HEALTH, -damage);
                    setFailureMessage("Filters overloaded. Health -" + damage);
                }
        );

        addChoice(
                "Eject contaminated systems | " + (30 + upgrades.getInventoryLevel() * 2),
                30 + upgrades.getInventoryLevel() * 2,
                player -> {
                    int burn = 10 - upgrades.getFuelLevel();
                    burn = Math.max(1, burn);
                    player.updateStat(Player.Stats.FUEL, -burn);
                    setSuccessMessage("Systems ejected. Fuel loss " + burn);
                },
                player -> setFailureMessage("Ejection failed. Parasites remain")
        );
    }
}
