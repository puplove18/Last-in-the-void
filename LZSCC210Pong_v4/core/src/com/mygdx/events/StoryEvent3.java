package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;

public class StoryEvent3 extends Event {

    public StoryEvent3() {
        super("Abandoned Space Station", "You see a space station up ahead in the distance, this means you are getting closer to Earth. The station seems abandoned, similar to the spaceship you found previously, but your sensors detect signs of life. ");
        
        addChoice("Board Space Station", 100, player -> {
            player.updateStat(Player.Stats.HEALTH, -10);
            setSuccessMessage("You board the station and are immediately attacked by an alien. You manage to defeat it but not without taking some damange. After searching the station, you find logs detailing the evacuation of Earth, but the rest of the data seems to be corrupted.");
        },
        player -> {
        });
        addChoice("Leave", 100, player -> {
            setSuccessMessage("As you fly past the station, you notice aliens wandering inside. It's good that you chose to not board the station.");
        },
        player -> {
        });
        
    }
}
