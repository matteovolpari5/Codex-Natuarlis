package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;

import java.util.List;

public interface PlayerViewListener {
    void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective);
}
