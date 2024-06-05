package it.polimi.ingsw.gc07.model_listeners;

import it.polimi.ingsw.gc07.updates.FullGameFieldUpdate;
import it.polimi.ingsw.gc07.updates.PlacedCardUpdate;
import it.polimi.ingsw.gc07.updates.StarterCardUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Listener of the model GameField.
 */
public interface GameFieldListener extends Remote {
    /**
     * Method used to show the client his starter card.
     * @param starterCardUpdate starter card update
     */
    void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate) throws RemoteException;

    /**
     * Method used to notify players that a card has been placed.
     * @param placedCardUpdate placed card update
     */
    void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate) throws RemoteException;

    /**
     * Method used to notify players the full game field after a reconnection.
     * @param fullGameFieldUpdate full game field content
     */
    void receiveFullGameFieldUpdate(FullGameFieldUpdate fullGameFieldUpdate) throws RemoteException;
}
