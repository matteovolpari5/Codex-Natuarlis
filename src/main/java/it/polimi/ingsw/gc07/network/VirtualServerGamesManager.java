package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GamesManagerCommand;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServerGamesManager extends Remote {
    /**
     * Method to connect a new client.
     * @param client client to connect
     * @throws RemoteException remote exception
     */
    void connect(VirtualView client) throws RemoteException;

    /**
     * Method to set a command and execute games manager command.
     * @param gamesManagerCommand games manager command to set and execute
     * @throws RemoteException remote exception
     */
    void setAndExecuteCommand(GamesManagerCommand gamesManagerCommand) throws RemoteException //TODO sollevare invece networkException, nei metodi di rmi e socket rilanciarle come remoteexception o ioexception;
}
