package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

public class AggressiveRobotsEvent extends Event {
    public AggressiveRobotsEvent(UpgradesUI upgrades) {
        super(
                "Aggressive Robots",
                "Your sensors detect a squadron of hostile repair drones homing in on your hull. What do you do?"
        );

        addChoice("Fight the robots | 40", 40,
                player -> {
                    int amount = 10;
                    player.addItemToInventory("Common Building Materials", amount);
                    setSuccessMessage(
                            "You destroy the drones and salvage " + amount + "x Common Building Materials."
                    );
                },
                player -> {
                    int dmg = 25;
                    if (upgrades.getHealthLevel() > 2){
                        player.updateStat(Player.Stats.HEALTH, -dmg / 2);
                        setFailureMessage(
                                "Due to high defense you only got " + dmg / 2
                        );
                    }
                    else{
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setFailureMessage(
                            "The drones swarm and damage your hull by " + dmg + "."
                    );}
                }
        );

        addChoice("Try to flee | 30", 30,
                player -> {
                    int burn = 15;
                    player.updateStat(Player.Stats.FUEL, -burn);
                    setSuccessMessage(
                            "You outrun them, burning " + burn + " of your fuel."
                    );
                },
                player -> {
                    int burn = 20, dmg = 15;
                    player.updateStat(Player.Stats.FUEL, -burn);
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setFailureMessage(
                            "They catch up—Fuel -" + burn + "% and hull -" + dmg + "."
                    );
                }
        );

        addChoice("let cargo as decoy | 80", 80,
                player -> {
                    int thrown = 5;
                    player.getInventory().removeItem("Common Building Materials", thrown);
                    setSuccessMessage(
                            "You dump " + thrown + " units of cargo. The drones focus on the debris."
                    );
                },
                player -> {
                    int thrown = 5, dmg = 10;
                    player.getInventory().removeItem("Common Building Materials", thrown);
                    player.updateStat(Player.Stats.HEALTH, -dmg);
                    setFailureMessage(
                            "They ignore the decoy, shred your cargo and damage your hull by " + dmg + "."
                    );
                }
        );
    }
}
