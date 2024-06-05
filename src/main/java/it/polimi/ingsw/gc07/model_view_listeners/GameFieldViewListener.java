package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

/**
 * Listener of the client's GameField copy.
 */
public interface GameFieldViewListener {
    /**
     * Method used to receive a game field update, return all game field structures.
     * @param nickname nickname
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    void receiveGameFieldUpdate(String nickname, PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int [][] cardsOrder);
}
