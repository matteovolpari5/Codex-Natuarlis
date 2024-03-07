package it.polimi.ingsw.gc07.model;

/**
 * Enum class for condition types.
 * A layout condition is a condition regarding cards placement on the
 * game field and their color, i.e. the permanent resource on the back of
 * GoldCards and ResourceCards.
 * An item condition is a condition regarding the presence of some given
 * items on the game field.
 * The corner coverage condition can't be detailed further and concerns
 * the number of angles covered by the card being placed.
 */
public enum ConditionType {
    LAYOUT_CONDITION, ITEM_CONDITION, CORNER_COVERAGE_CONDITION;
}
