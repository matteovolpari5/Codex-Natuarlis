package it.polimi.ingsw.gc07.model;

/**
 * Class representing conditions found on game cards: these conditions can
 * be placing conditions or scoring conditions
 */
public class Condition {
    /**
     * Attribute representing the ConditionType of the Condition
     */
    private final ConditionType conditionType;

    /**
     * Constructor of the class
     * @param conditionType: type of condition
     */
    public Condition(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    /**
     * Getter method for the condition type
     * @return: condition type
     */
    public ConditionType getConditionType() {
        return conditionType;
    }
}
