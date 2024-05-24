package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

import java.util.List;

public interface PlayerViewListener {
    /**
     * Method used to show the player of his starter card.
     * @param starterCard starter card
     */
    void receiveStarterCardUpdate(PlaceableCard starterCard);

    /**
     * Method used to show the player his new card hand.
     * @param hand card hand
     * @param personalObjective personal objective
     */
    void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective);
}
