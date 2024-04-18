package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.rmi.RemoteException;

public class SocketClient implements VirtualView {
    @Override
    public void setServerGame(VirtualServerGame serverGame) throws RemoteException {

    }
    @Override
    public String getNickname() throws RemoteException {
        return null;
    }
}
