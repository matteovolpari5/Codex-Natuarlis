package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

public interface GameFieldListener {
    /**
     * Method used to show a player his starter card.
     * @param card starter card
     */
    // TODO devo inviare l'aggiornamento solo al player giusto, non devo informare tutti!
    void showStarterCard(PlaceableCard card);

    /**
     * Method used to notify the player that a new card was placed.
     * @param x x
     * @param y y
     * @param card new card placed
     * @param way way
     * @param orderPosition order position
     */
    void showNewCard(int x, int y, PlaceableCard card, boolean way, int orderPosition);
}
