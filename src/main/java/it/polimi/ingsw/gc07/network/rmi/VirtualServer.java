package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.exceptions.PlayerAlreadyPresentException;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType) throws RemoteException, PlayerAlreadyPresentException;
    // ...
}
