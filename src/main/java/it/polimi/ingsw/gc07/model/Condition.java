package it.polimi.ingsw.gc07.model;

public class Condition {
    private final ConditionType conditionType;
    public Condition(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
    public ConditionType getConditionType() {
        return conditionType;
    }
}
