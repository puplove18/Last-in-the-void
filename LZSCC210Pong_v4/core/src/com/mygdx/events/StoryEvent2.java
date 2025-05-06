package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;

public class StoryEvent2 extends Event {

    public StoryEvent2() {
        super("Cosmic Energy", 
            "Your ship's sensors detect an unusual energy signature ahead. The energydoesn't appear harmful, but it's completely unknown to your systems.");
        
        addChoice("Study the energy", 100, player -> {
            setSuccessMessage("You move closer. The readings are extraordinary.The energy seems to exist outside normal space-time.");
        },
        player -> {
        });
        
    }
}
