package it.polimi.ingsw.gc07.model_listeners;

import it.polimi.ingsw.gc07.updates.PlacedCardUpdate;
import it.polimi.ingsw.gc07.updates.StarterCardUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
}
