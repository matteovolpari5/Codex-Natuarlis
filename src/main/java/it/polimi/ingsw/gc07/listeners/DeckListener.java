package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.CommonObjectiveUpdate;
import it.polimi.ingsw.gc07.updates.DeckUpdate;

import java.rmi.RemoteException;

public interface DeckListener {
    /**
     * Method used to notify the client the common objective.
     * @param commonObjectiveUpdate common objective update
    */
    void receiveCommonObjectiveUpdate(CommonObjectiveUpdate commonObjectiveUpdate) throws RemoteException;

    /**
     * Method used to notify the client of a deck update.
     * @param deckUpdate deck update
     */
    void receiveDeckUpdate(DeckUpdate deckUpdate) throws RemoteException;
}
