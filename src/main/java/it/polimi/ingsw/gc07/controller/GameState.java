package it.polimi.ingsw.gc07.controller;

/**
 * Enum representing the possible states of a game.
 */
public enum GameState {
    GAME_STARTING,    // before the game has starter
    PLACING_STARTER_CARDS, // players have to place stater cards
    PLAYING,    // players can play
    WAITING_RECONNECTION,   // only one player is connected, waiting for the reconnection of one player
    NO_PLAYERS_CONNECTED,   // no player is connected, waiting for the reconnection of two players
    GAME_ENDED      // the game is ended
}
