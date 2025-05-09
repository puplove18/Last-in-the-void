package com.mygdx.managers;

import com.mygdx.objects.Event;
import com.mygdx.objects.Player;
import com.mygdx.ui.EventUI;
import com.mygdx.screens.GameScreen;

/**
 * Manages in-game events and the event UI
 */
public class EventManager {
    private EventUI eventUI;
    private Event currentEvent;
    private Player player;
    
    public EventManager(Player player, EventUI.EventCompletionListener listener) {
        this.player = player;
        this.eventUI = new EventUI(player, listener);
    }
    
    public void render() {
        if (isEventActive()) {
            eventUI.render();
        }
    }
    
    public void resize(int width, int height) {
        eventUI.resize(width, height);
    }
    
    public void dispose() {
        eventUI.dispose();
    }
    
    public boolean isEventActive() {
        return eventUI.isVisible();
    }
    
    public boolean hasEvent() {
        return currentEvent != null;
    }
    
    public void setCurrentEvent(Event event) {
        this.currentEvent = event;
    }
    
    public void showCurrentEvent() {
        if (currentEvent != null) {
            eventUI.showEvent(currentEvent);
        }
    }
    
    public void hideEvent() {
        eventUI.hideEvent();
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }
}