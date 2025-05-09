package com.mygdx.events.random_events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.UpgradesUI;

public class AggressiveRobotsEvent extends Event {
    public AggressiveRobotsEvent(UpgradesUI upgrades) {
        super(
                "Aggressive Robots",
                "Your sensors detect a squadron of hostile repair drones homing in on your hull. What do you do?"
        );

        int healthLvl    = upgrades.getHealthLevel();
        int fuelLvl      = upgrades.getFuelLevel();
        int resourcesLvl = upgrades.getResourcesLevel();

        int fightWeight = 40 + healthLvl * 10;
        int fleeWeight  = 30 + fuelLvl   * 5;
        int decoyWeight = 80 + resourcesLvl * 5;

        addChoice(
                "Fight the robots | " + fightWeight,
                fightWeight,
                player -> {
                    int salvage = 10;
                    player.addItemToInventory("Common Building Materials", salvage);
                    setSuccessMessage(
                            "You destroy the drones and salvage " + salvage + "x Common Building Materials."
                    );
                },
                player -> {
                    int baseDmg = 25;
                    if (healthLvl > 2) {
                        int reduced = baseDmg / 2;
                        player.updateStat(Player.Stats.HEALTH, -reduced);
                        setFailureMessage(
                                "Your reinforced hull takes only " + reduced + " damage."
                        );
                    } else {
                        player.updateStat(Player.Stats.HEALTH, -baseDmg);
                        setFailureMessage(
                                "The drones swarm and damage your hull by " + baseDmg + "."
                        );
                    }
                }
        );

        addChoice(
                "Try to flee | " + fleeWeight,
                fleeWeight,
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
                            "They catch up—Fuel -" + burn + " and hull -" + dmg + "."
                    );
                }
        );

        addChoice(
                "Drop cargo as decoy | " + decoyWeight,
                decoyWeight,
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
