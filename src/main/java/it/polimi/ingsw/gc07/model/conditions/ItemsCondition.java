package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.ConditionType;
import it.polimi.ingsw.gc07.model.GameItem;

import java.util.List;
import java.util.ArrayList;

/**
 * Condition regarding the presence of some given items on the game field.
 */
public class ItemsCondition extends Condition {
    /**
     * List of GameItems that need to be found on the game field in order satisfy the condition.
     */
    private final List<GameItem> neededItems;

    /**
     * Constructor for the class ItemsCondition.
     * @param conditionType type of the condition, must be ITEM_CONDITION
     * @param neededItems List of items that need to be on the game field in
     *                    order satisfy the condition
     */
    public ItemsCondition(ConditionType conditionType, List<GameItem> neededItems) {
        super(conditionType);
        this.neededItems = new ArrayList<>(neededItems);
    }

    /**
     * Getter method returning the List of items required by the condition.
     * @return List of Items of the condition.
     */
    public List<GameItem> getNeededItems() {
        return new ArrayList<>(this.neededItems);
    }

    /**
     * Implementation of numTimesMet.
     * Counts how many times neededItems is found in the gameField.
     * @return number of times the list of items is found
     */
    public int numTimesMet(GameField gameField) {
        // TODO implementare
        // ricorda di guardare non solo le risorse/oggetti negli angoli, ma
        // anche le risoser permanenti
        return 0; // cambiare
    }
}
