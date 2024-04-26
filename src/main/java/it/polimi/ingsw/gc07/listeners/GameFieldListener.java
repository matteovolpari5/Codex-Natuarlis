package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.PlacedCardUpdate;
import it.polimi.ingsw.gc07.updates.StarterCardUpdate;

public interface GameFieldListener {
    /**
     * Method used to show the client his starter card.
     * @param starterCardUpdate starter card update
     */
    void receiveStarterCardUpdate(StarterCardUpdate starterCardUpdate);

    /**
     * Method used to notify players that a card has been placed.
     * @param placedCardUpdate placed card update
     */
    void receivePlacedCardUpdate(PlacedCardUpdate placedCardUpdate);
}
