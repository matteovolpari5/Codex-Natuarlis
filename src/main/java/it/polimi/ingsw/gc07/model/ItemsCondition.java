package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model.Condition;
import it.polimi.ingsw.gc07.model.GameField;

import java.util.List;
import java.util.ArrayList;

public class ItemsCondition extends Condition {
    private final List<GameItem> neededItems;

    public ItemsCondition(ConditionType conditionType, List<GameItem> neededItems) {
        super(conditionType);
        this.neededItems = new ArrayList<>(neededItems);
    }
    public List<GameItem> getNeededItems() {
        return new ArrayList<>(this.neededItems);
    }
}
