package it.polimi.ingsw.gc07.model_listeners;

import it.polimi.ingsw.gc07.updates.CardHandUpdate;
import it.polimi.ingsw.gc07.updates.ConnectionUpdate;
import it.polimi.ingsw.gc07.updates.StallUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerListener extends Remote {
    /**
     * Method used to send a stall update.
     * @param stallUpdate stall update
     */
    void receiveStallUpdate(StallUpdate stallUpdate) throws RemoteException;

    /**
     * Method used to send a connection update.
     * @param connectionUpdate connection update
     */
    void receiveConnectionUpdate(ConnectionUpdate connectionUpdate) throws RemoteException;

    /**
     * Method used to send a card hand update.
     * @param cardHandUpdate card hand update
     */
    void receiveCardHandUpdate(CardHandUpdate cardHandUpdate) throws RemoteException;
}
