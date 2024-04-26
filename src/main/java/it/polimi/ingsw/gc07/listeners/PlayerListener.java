package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;

import java.util.List;

public interface PlayerListener {
    /**
     * Method used to notify if a player is stalled.
     * @param nickname nickname of the player
     * @param isStalled boolean value for isStalled
     */
    void notifyIsStalled(String nickname, boolean isStalled);

    /**
     * Method used to notify if a player is connected.
     * @param nickname nickname
     * @param isConnected boolean value for isConnected
     */
    void notifyIsConnected(String nickname, boolean isConnected);

    /**
     * Method used to update the card hand.
     * @param cardHand new cardHand
     */
    void updateCardHand(String nickname, List<DrawableCard> cardHand);
}
