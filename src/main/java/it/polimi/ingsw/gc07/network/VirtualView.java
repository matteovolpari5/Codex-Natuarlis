package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.ModelListener;
import it.polimi.ingsw.gc07.updates.ExistingGamesUpdate;
import it.polimi.ingsw.gc07.updates.Update;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing the virtual client.
 */
public interface VirtualView extends Remote, ModelListener {
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

    /**
     * Method used from the server to send a pong to the client.
     * @throws RemoteException remote exception
     */
    void sendPong() throws RemoteException;

    /**
     * Method used to close the connection when a disconnection is detected
     */
    void closeConnection() throws RemoteException;
}
