package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;

public class StoryEvent0 extends Event {

    public StoryEvent0() {
        super("System Reboot", 
            """
            Good morning, Captain, you have been in cryrosleep for some time. Ship records indicate we've been adrift for *error*. 
            Last communication with Earth: *error*. Primary directive: *error*. Emergency directive activated: return to Earth. 
            Sensors detect nearby planets with resources. We should investigate.
            """);
        
        addChoice("Begin exploration", 100, 
            player -> {
                setSuccessMessage("Continue...");
            }, 
            player -> {}
        );
    }
}