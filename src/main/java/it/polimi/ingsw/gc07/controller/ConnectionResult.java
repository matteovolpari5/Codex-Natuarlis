package it.polimi.ingsw.gc07.controller;

public enum ConnectionResult implements CommandResult {
    SUCCESS,
    PLAYER_NOT_PRESENT,
    PLAYER_ALREADY_CONNECTED,
    PLAYER_ALREADY_DISCONNECTED
}
