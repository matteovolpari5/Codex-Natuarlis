package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.model_listeners.*;
import it.polimi.ingsw.gc07.updates.ExistingGamesUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote, ChatListener, GameFieldListener, GameListener, PlayerListener, BoardListener {
    /**
     * Method to get the nickname associated with the VirtualView.
     * @return nickname
     * @throws RemoteException remote exception
     */
    String getNickname() throws RemoteException;
    /**
     * Method to the set the game server and controller.
     * @param gameId game id
     * @throws RemoteException remote exception
     */
    void setServerGame(int gameId) throws RemoteException;
    /**
     * Method to receive existing games update.
     * @param existingGamesUpdate existing games update
     * @throws RemoteException remote exception
     */
    void receiveExistingGamesUpdate(ExistingGamesUpdate existingGamesUpdate) throws RemoteException;
    /**
     * Method to notify the client if the game joining was successful.
     * @throws RemoteException remote exception
     */
    void notifyJoinNotSuccessful() throws RemoteException;
}
