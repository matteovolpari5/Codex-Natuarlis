package it.polimi.ingsw.gc07.network;

/**
 * Interface representing someone who sends pings.
 */
public interface PingSender {
    /**
     * Method used from a client to send a ping to the server.
     */
    void startGamePing();
}