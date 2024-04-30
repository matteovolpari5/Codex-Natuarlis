package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.CommandResultUpdate;
import it.polimi.ingsw.gc07.updates.DeckUpdate;
import it.polimi.ingsw.gc07.updates.GameModelUpdate;
import it.polimi.ingsw.gc07.updates.PlayerJoinedUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameListener extends Remote {
    /**
     * Method used to notify a game model update.
     * @param gameModelUpdate game model update
     * @throws RemoteException remote exception
     */
    void receiveGameModelUpdate(GameModelUpdate gameModelUpdate) throws RemoteException;

    /**
     * Method used to notify a player joined the game.
     * @param playerJoinedUpdate player joined update
     * @throws RemoteException remote exception
     */
    void receivePlayerJoinedUpdate(PlayerJoinedUpdate playerJoinedUpdate) throws RemoteException;

    /**
     * Method used to notify a command result update.
     * @param commandResultUpdate command result update
     * @throws RemoteException remote exception
     */
    void receiveCommandResultUpdate(CommandResultUpdate commandResultUpdate) throws RemoteException;

    /**
     * Method used to notify the client of a deck update.
     * @param deckUpdate deck update
     */
    void receiveDecksUpdate(DeckUpdate deckUpdate) throws RemoteException;
}
