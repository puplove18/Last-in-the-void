package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

import java.util.Random;

public class PirateAmbushEvent extends Event {
    private final Random random = new Random();

    public PirateAmbushEvent(UpgradesUI upgrades) {
        super("Robots Pirate Ambush", "Rogue robots hail you, demanding tribute.");
        addChoice("Fight back | " + (40 + upgrades.getHealthLevel() * 3), 40 + upgrades.getHealthLevel() * 3,
                player -> {
                    int materials = 10 + upgrades.getResourcesLevel() * 2;
                    player.addItemToInventory("Common Building Materials", materials);
                    setSuccessMessage("Defeated pirates. Salvaged " + materials + " materials");
                },
                player -> {
                    int dmg = 20 - upgrades.getHealthLevel() * 2;
                    dmg = Math.max(1, dmg);
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setFailureMessage("Hull -" + dmg);
                }
        );
        addChoice("Pay tribute | " + (30 + upgrades.getFuelLevel() * 2), 30 + upgrades.getFuelLevel() * 2,
                player -> {
                    int fuel = 15;
                    player.updateStat(Player.Stats.FUEL, -fuel);
                    setSuccessMessage("Paid " + fuel + " fuel to avoid conflict");
                },
                player -> {
                    setFailureMessage("Pirates attacked despite payment");
                }
        );
    }
}
