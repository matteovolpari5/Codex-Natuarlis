package it.polimi.ingsw.gc07.model;

/**
 * Class representing conditions found on game cards: these conditions can
 * be placing conditions or scoring conditions
 */
public class Condition {
    /**
     * Attribute representing the ConditionType of the Condition
     */
    private final ConditionType type;

    /**
     * Constructor of the class
     * @param type: type of condition
     */
    public Condition(ConditionType type) {
        this.type = type;
    }

    /**
     * Getter method for the condition type
     * @return condition type
     */
    public ConditionType getType() {
        return type;
    }
}
