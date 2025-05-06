package com.mygdx.events;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;

public class StoryEvent4 extends Event {

    public StoryEvent4() {
        super("Earth", 
            "You finally reach Earth, or so you think. Your sensors detect the same strange cosmic energy you encountered before.It appears that the planet it is still inhabited.But what you see before you does not look like the Earth you remember...");
            addChoice("Land and investigate", 100, player -> {
                setSuccessMessage("You land on the surface, but you find your ship swarmed by aliens. But these aliens appear somewhat... human...");
            },
            player -> {
            });
            
        }
    }
