package it.polimi.ingsw.gc07;

import it.polimi.ingsw.gc07.updates.Update;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing a listener of the model.
 */
public interface ModelListener extends Remote {
    /**
     * Method used to send a client a new update of the model.
     * @param update update
     */
    void receiveUpdate(Update update) throws RemoteException;
}
