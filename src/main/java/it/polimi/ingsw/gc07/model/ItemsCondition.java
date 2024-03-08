package it.polimi.ingsw.gc07.model;

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
}
