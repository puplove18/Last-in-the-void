import java.util.ArrayList;
import java.util.List;

public class Event {
    private String description;
    private List<String> choices;
    private List<Integer> successChances;

    public Event(String description) {
        this.description = description;
        this.choices = new ArrayList<>();
        this.successChances = new ArrayList<>();
    }

    public void addChoice(String choice, int successChance) {
        choices.add(choice);
        successChances.add(successChance);
    }

    public void triggerEvent() {
        System.out.println("Event: " + description);
        for (int i = 0; i < choices.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + choices.get(i) + " (Success Chance: " + successChances.get(i) + "%)");
        }
    }

    public void resolveEvent(int choiceIndex, int modifier) {
        if (Math.random() * 100 < successChances.get(choiceIndex)-modifier) {
            System.out.println("Success");
        } else {
            System.out.println("You died lol");
        }
    }
}
