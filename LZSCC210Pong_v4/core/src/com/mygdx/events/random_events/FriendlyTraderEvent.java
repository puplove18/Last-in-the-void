package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import java.util.Random;
import com.mygdx.ui.UpgradesUI;

public class FriendlyTraderEvent extends Event {
    private final Random random = new Random();

    public FriendlyTraderEvent(UpgradesUI upgrades) {
        super("Friendly Traders",
                "A convoy of robotraders hails you and offers assistance.");

        addChoice("Accept spare fuel", 90,
                player -> {
                    double amount = 35 + random.nextInt(11); //20–30%?
                    player.updateStat(Player.Stats.FUEL, amount);
                    setSuccessMessage("They transfer " + (int)amount + " fuel to your tanks.");
                },
                player -> {
                    setFailureMessage("They apologize—supplies ran out before transfer.");
                }
        );

        addChoice("Request medical supplies", 75,
                player -> {
                    double amount = 15 + random.nextInt(6); //15–20%??
                    player.updateStat(Player.Stats.HEALTH, amount);
                    setSuccessMessage("You receive medical packs restoring " + (int)amount + " hull integrity.");
                },
                player -> {
                    setFailureMessage("Their med-bay is empty. They wish you luck.");
                }
        );

        addChoice("Share trading tips", 80,
                player -> {
                    setSuccessMessage("They share routes that improve your next fuel cost by 10%? Smth like that.");
                },
                player -> {
                    setFailureMessage("They have no new tips for you right now.");
                }
        );
    }
}
