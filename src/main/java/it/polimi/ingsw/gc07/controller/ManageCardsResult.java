package it.polimi.ingsw.gc07.controller;

public enum ManageCardsResult implements CommandResult {
    SUCCESS,
    WRONG_STATE,
    PLAYER_NOT_PRESENT, // TODO (si può verificare?)
    WRONG_PLAYER,
    WRONG_CARD_TYPE,
    CARD_NOT_PRESENT
}
