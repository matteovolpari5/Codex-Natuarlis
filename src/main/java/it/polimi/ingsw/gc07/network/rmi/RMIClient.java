package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.exceptions.PlayerAlreadyPresentException;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;

    protected RMIClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }
}
