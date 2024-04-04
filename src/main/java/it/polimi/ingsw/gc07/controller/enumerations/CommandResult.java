package it.polimi.ingsw.gc07.controller.enumerations;

public enum CommandResult {
    SUCCESS(""),
    WRONG_STATE("The game is in a wrong state"),
    WRONG_SENDER("The sender is wrong"),
    WRONG_RECEIVER("The receiver is wrong"),
    PLAYER_NOT_PRESENT("The player is not present in the game"),
    PLAYER_ALREADY_CONNECTED("The player is already connected"),
    PLAYER_ALREADY_DISCONNECTED("The player is already disconnected"),
    WRONG_PLAYER("It's not your turn"),
    WRONG_CARD_TYPE("The card is of the wrong type"),
    CARD_NOT_PRESENT("The required card is not present"),
    NO_COVERED_CORNER("The placement is not valid: you must cover at least one corner"),
    NOT_LEGIT_CORNER("Trying to cover a not-corner"),
    MULTIPLE_CORNERS_COVERED("The placement is not valid: you can't cover multiple corners of the same card"),
    CARD_ALREADY_PRESENT("A card is already present"),
    INDEXES_OUT_OF_GAME_FIELD("Provided indexes exceed game field size"),
    PLACING_CONDITION_NOT_MET("The placing condition is not met")
    ;

    private final String resultMessage;

    private CommandResult(String resultMessage){
        this.resultMessage = resultMessage;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
