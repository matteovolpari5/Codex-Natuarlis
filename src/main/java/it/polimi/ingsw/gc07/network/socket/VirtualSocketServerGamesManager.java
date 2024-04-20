package it.polimi.ingsw.gc07.network.socket;

import it.polimi.ingsw.gc07.controller.GamesManagerCommand;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

public class VirtualSocketServerGamesManager implements VirtualServerGamesManager {
    final ObjectOutputStream output;

    public VirtualSocketServerGamesManager(ObjectOutputStream output){
        this.output = output;
    }
    @Override
    public void connect(VirtualView client) throws RemoteException {

    }

    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException {

    }
}
