package it.polimi.ingsw.gc07.model_listeners;

import it.polimi.ingsw.gc07.updates.ScoreUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Listener of the model Board.
 */
public interface BoardListener extends Remote {
    /**
     * Method used to show the client an updated score.
     * @param scoreUpdate score update
     */
    void receiveScoreUpdate(ScoreUpdate scoreUpdate) throws RemoteException;
}