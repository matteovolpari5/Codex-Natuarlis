package it.polimi.ingsw.gc07.model_listeners;

import it.polimi.ingsw.gc07.updates.*;

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
     * @param playersUpdate player joined update
     * @throws RemoteException remote exception
     */
    void receivePlayersUpdate(PlayersUpdate playersUpdate) throws RemoteException;

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
    void receiveDeckUpdate(DeckUpdate deckUpdate) throws RemoteException;

    /**
     * Method used to notify that the game is ended and to notify winners
     * @param gameEndedUpdate game ended update
     * @throws RemoteException remote exception
     */
    void receiveGameEndedUpdate(GameEndedUpdate gameEndedUpdate) throws RemoteException;
}
