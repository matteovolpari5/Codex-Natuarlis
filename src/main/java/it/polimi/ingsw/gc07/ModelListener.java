package it.polimi.ingsw.gc07;

import it.polimi.ingsw.gc07.updates.Update;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ModelListener extends Remote {
    void receiveUpdate(Update update) throws RemoteException;
}
