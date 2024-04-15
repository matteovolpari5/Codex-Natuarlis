package it.polimi.ingsw.gc07.network.rmi;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.controller.GamesManagerCommand;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer implements VirtualServer {
    final GamesManager gamesManager;
    final List<VirtualView> clients;

    public RMIServer(GamesManager gamesManager) throws RemoteException {
        this.gamesManager = gamesManager;
        this.clients = new ArrayList<>();
    }

    @Override
    public synchronized void connect(VirtualView client) throws RemoteException {
        clients.add(client);
        System.err.println("New client connected");
    }

    @Override
    public void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException {
        // TODO
        gamesManagerCommand.setGamesManager(gamesManager);
        gamesManager.setAndExecuteCommand(gamesManagerCommand);
        System.out.println(gamesManager.getCommandResultManager().getCommandResult());
    }
}
