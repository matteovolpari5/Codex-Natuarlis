package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.CommandResultUpdate;
import it.polimi.ingsw.gc07.updates.GameModelUpdate;
import it.polimi.ingsw.gc07.updates.PlayerJoinedUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameListener extends Remote {
    /**
     * Method used to notify a game model update.
     * @param gameModelUpdate game model update
     */
    void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) throws RemoteException;

    /**
     * Method used to notify that a player has joined.
     * @param playerJoinedUpdate playerJoinedUpdate
     */
    void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpdate) throws RemoteException;

    /**
     * Method used to notify a command result update.
     * @param commandResultUpdate command result update
     */
    void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) throws RemoteException;
}
