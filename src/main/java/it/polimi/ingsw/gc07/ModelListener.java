package it.polimi.ingsw.gc07;

import it.polimi.ingsw.gc07.updates.Update;

import java.rmi.RemoteException;

public interface ModelListener {
    void receiveUpdate(Update update) throws RemoteException;
}
