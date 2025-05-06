package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import java.util.Random;
import com.mygdx.ui.UpgradesUI;

public class SpaceParasitesEvent extends Event {
    private final Random random = new Random();

    public SpaceParasitesEvent(UpgradesUI upgrades) {
        super("Parasite Infestation",
                "Microscopic space parasites breach your hull plating!");

        addChoice("Seal affected compartments", 60,
                player -> {
                    int stat = -10;
                    player.updateStat(Player.Stats.OXYGEN, stat);
                    setSuccessMessage("Compartment sealed. Oxygen loss " + stat  + " but infestation contained.");
                },
                player -> {
                    int stat = 20;
                    player.updateStat(Player.Stats.OXYGEN, stat);
                    setFailureMessage("Seal failed; parasites spread. Oxygen loss " + stat  + ").");
                }
        );

        addChoice("Use bio-filter dispensers", 40,
                player -> {
                    player.updateStat(Player.Stats.OXYGEN, -5);
                    setSuccessMessage("Filters worked. Minor loss of oxygen (-5).");
                },
                player -> {
                    player.updateStat(Player.Stats.HEALTH, -15);
                    setFailureMessage("Filters overloaded. Crew exposure! Health -15.");
                }
        );

        addChoice("Eject contaminated systems", 30,
                player -> {
                    player.updateStat(Player.Stats.FUEL, -10);
                    setSuccessMessage("Systems ejected. Fuel tanks vented (-10%). Infestation gone.");
                },
                player -> {
                    setFailureMessage("Ejection failed. Parasites remain undisturbed.");
                }
        );
    }
}
