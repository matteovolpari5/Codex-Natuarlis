package it.polimi.ingsw.gc07.controller.enumerations;

public enum CommandResult {
    SUCCESS(""),
    WRONG_STATE(""),
    WRONG_SENDER(""),
    WRONG_RECEIVER(""),
    PLAYER_NOT_PRESENT(""),
    PLAYER_ALREADY_CONNECTED(""),
    PLAYER_ALREADY_DISCONNECTED(""),
    WRONG_PLAYER(""),
    WRONG_CARD_TYPE(""),
    CARD_NOT_PRESENT(""),
    NO_COVERED_CORNER(""),
    NOT_LEGIT_CORNER(""),
    MULTIPLE_CORNERS_COVERED(""),
    CARD_ALREADY_PRESENT(""),
    INDEXES_OUT_OF_GAME_FIELD(""),
    PLACING_CONDITION_NOT_MET("")
    ;

    private String resultMessage;

    private CommandResult(String resultMessage){
        this.resultMessage = resultMessage;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
