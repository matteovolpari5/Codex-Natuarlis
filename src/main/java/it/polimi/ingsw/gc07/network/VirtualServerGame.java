package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.game_commands.GameCommand;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServerGame extends Remote {
    /**
     * Method to connect a new client.
     * @param client client to connect
     * @throws RemoteException remote exception
     */
    void connect(VirtualView client) throws RemoteException;

    /**
     * Method to set a command and execute game command.
     * @param gameCommand game command to set and execute
     * @throws RemoteException remote exception
     */
    void setAndExecuteCommand(GameCommand gameCommand) throws RemoteException;
}
