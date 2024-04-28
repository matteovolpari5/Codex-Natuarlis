package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.DeckUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DeckListener extends Remote {

    /**
     * Method used to notify the client of a deck update.
     * @param deckUpdate deck update
     */
    void receiveDeckUpdate(DeckUpdate deckUpdate) throws RemoteException;
}
