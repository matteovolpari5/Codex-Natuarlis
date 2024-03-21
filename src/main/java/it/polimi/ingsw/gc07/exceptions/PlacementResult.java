package it.polimi.ingsw.gc07.exceptions;

/**
 * Enumerated class containing the possible ways a card placement can go wrong.
 * SUCCESS: placement successful
 * NO_COVERED_CORNER: the card doesn't cover any corner
 * NOT_LEGIT_CORNER: the card covers a non-corner
 * MULTIPLE_CORNERS_COVERED: the card covers multiple corners of the same card
 * CARD_ALREADY_PRESENT: there is a card in the specified indexes
 * INDEXES_OUT_OF_GAME_FIELD: specified indexes exceed the game field size
 *
 */
public enum PlacementResult {
    SUCCESS, // no exception
    NO_COVERED_CORNER, // NoCoveredCornerException
    NOT_LEGIT_CORNER, // NotLegitCornerException
    MULTIPLE_CORNERS_COVERED, // MultipleCornersCoveredException
    CARD_ALREADY_PRESENT, // CardAlreadyPresentException
    INDEXES_OUT_OF_GAME_FIELD, // IndexesOutOfGameFieldException
    PLACING_CONDITION_NOT_MET // PlacingConditionNotMetException
}
