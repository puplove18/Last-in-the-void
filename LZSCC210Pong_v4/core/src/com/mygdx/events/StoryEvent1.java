package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;

public class StoryEvent1 extends Event {

    public StoryEvent1() {
        super("Mysterious Ship", 
            """
            You come across a ship drifting through space. It appears to have been involved in some sort of battle, but according
            to scans there are no signs of life.
            """);
            addChoice("Board the Ship", 100, player -> {
                player.addItemToInventory("Common Building Materials", 15);
                setSuccessMessage("""
                    Inside you find that the ship had come under attack by aliens and the crew had been devoured.
                    You find some spare parts lying around and take them.
                    +15 Common Building Materials
                    """);
            },
            player -> {
            });
            
            addChoice("Leave", 100, player -> {
                setSuccessMessage("You decide to leave the derelict ship behind, continuing your journey through the void.");
            },
            player -> {
            });
        }
    }