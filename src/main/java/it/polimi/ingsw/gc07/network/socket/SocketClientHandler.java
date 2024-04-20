package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.model_view.ModelView;
import it.polimi.ingsw.gc07.network.VirtualServerGame;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.rmi.RemoteException;

public class SocketClientHandler implements VirtualView {
    @Override
    public void setServerGame(VirtualServerGame serverGame) throws RemoteException {

    }
    @Override
    public String getNickname() throws RemoteException {
        return null;
    }

    @Override
    public void updateModelView(ModelView modelView) {

    }
}
