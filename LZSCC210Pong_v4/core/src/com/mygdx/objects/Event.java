package com.mygdx.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base abstract class for all in-game events that require player interaction.
 * Events display a dialogue box with choices for the player to make.
 * Subclasses must define their specific choices and outcomes.
 */
public abstract class Event {
    protected String title;
    protected String description;
    protected List<Choice> choices;
    protected String successMessage = "";
    protected String failureMessage = "";

    
    // Represents a single choice in an event with its description and consequences.
    public static class Choice {
        private String text;
        private Consumer<Player> successOutcome;
        private Consumer<Player> failureOutcome;
        private int successChance; // 0-100 percentage

        // Mayeb add success/failure description fields here later...

        // This constructor works if the success has both a success and a failure, maybe some outcomes will have no failure? Could make multiple constructors
        public Choice(String text, int successChance, Consumer<Player> successOutcome, Consumer<Player> failureOutcome) {
            this.text = text;
            this.successChance = successChance;
            this.successOutcome = successOutcome;
            this.failureOutcome = failureOutcome;
        }

        public String getText() {
            return text;
        }

        public int getSuccessChance() {
            return successChance;
        }

        /**
         * Executes this choice's outcome on the player based on success chance.
         * @param player The player to apply the outcome to
         * @return True if the choice was successful based on chance, false otherwise
         */
        public boolean execute(Player player) {
            boolean success = Math.random() * 100 < successChance;
            if (success) {
                if (successOutcome != null) {
                    successOutcome.accept(player);
                }
            } else {
                if (failureOutcome != null) {
                    failureOutcome.accept(player);
                }
            }
            return success;
        }

    }

    public Event(String title, String description) {
        this.title = title;
        this.description = description;
        this.choices = new ArrayList<>();
    }

    protected void addChoice(String text, int successChance, Consumer<Player> successOutcome, Consumer<Player> failureOutcome) {
        choices.add(new Choice(text, successChance, successOutcome, failureOutcome));
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Choice> getChoices() {
        return choices;
    }
    
    protected void setSuccessMessage(String message) {
        this.successMessage = message;
    }
    
    protected void setFailureMessage(String message) {
        this.failureMessage = message;
    }
    
    public String getSuccessMessage() {
        return successMessage;
    }
    
    public String getFailureMessage() {
        return failureMessage;
    }
}