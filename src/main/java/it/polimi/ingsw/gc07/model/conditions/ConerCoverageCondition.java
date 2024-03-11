package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.ConditionType;

public class ConerCoverageCondition extends Condition {
    /**
     * Constructor of class CornerCoverageCondtion.
     * @param type type of the condition
     */
    public ConerCoverageCondition(ConditionType type) {
        super(type);
    }
    /**
     * Implementation of numTimesMet.
     * Counts how many times the card covers a corner.
     * @return number of times the list of items is found
     * @param x position of the card (x)
     * @param y position of the card (y)
     */
    public int numTimesMet(GameField gameField, int x, int y) {
        //TODO implementare
        return 0;
    }
}
