package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

import java.util.List;

public interface PlayerViewListener {
    /**
     * Method used to show the player of his starter card.
     * @param nickname nickname
     * @param starterCard starter card
     */
    void receiveStarterCardUpdate(String nickname, PlaceableCard starterCard);

    /**
     * Method used to show the player his new card hand.
     * @param nickname nickname
     * @param hand card hand
     * @param personalObjective personal objective
     */
    void receiveCardHandUpdate(String nickname, List<DrawableCard> hand, List<ObjectiveCard> personalObjective);

    /**
     * Method used to show a connection update.
     * @param nickname nickname
     * @param connection true if the player is connected, false otherwise
     */
    void receiveConnectionUpdate(String nickname, boolean connection);

    /**
     * Method used to show a stall update.
     * @param nickname nickname
     * @param stall true if the player is stalled, false otherwise
     */
    void receiveStallUpdate(String nickname, boolean stall);
}
