package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

public interface GameFieldViewListener {
    void receiveGameFieldUpdate(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int [][] cardsOrder);
}
