package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model_view.GameFieldView;

public interface GameFieldListener {
    /**
     * Method used to show a player his starter card.
     * @param card starter card
     */
    // TODO devo inviare l'aggiornament solo al player giusto, non devo informare tutti!
    void showStarterCard(PlaceableCard card);

    /**
     * Method used to notify the player that the game field has changed.
     * @param gameFieldView new game field view
     */
    void showUpdatedGameFiled(GameFieldView gameFieldView);
}
