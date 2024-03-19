package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.ConditionType;

/**
 * Class representing conditions found on game cards: these conditions can
 * be placing conditions or scoring conditions
 */
public abstract class Condition {
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

    public abstract int numTimesMet(GameField gameField) throws NullPointerException;
}
