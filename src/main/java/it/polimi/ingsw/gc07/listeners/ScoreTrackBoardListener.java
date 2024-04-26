package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.ScoreUpdate;

import java.rmi.RemoteException;

public interface ScoreTrackBoardListener {
    /**
     * Method used to show the client an updated score.
     * @param scoreUpdate score update
     */
    void receiveScoreUpdate(ScoreUpdate scoreUpdate);
}