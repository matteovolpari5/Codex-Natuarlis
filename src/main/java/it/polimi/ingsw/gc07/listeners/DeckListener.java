package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.CommonObjectiveUpdate;
import it.polimi.ingsw.gc07.updates.DeckUpdate;

import java.util.List;

public interface DeckListener {
    /**
     * Method used to notify the client the common objective.
     * @param commonObjectiveUpdate common objective update
    */
    void receiveCommonObjectiveUpdate(CommonObjectiveUpdate commonObjectiveUpdate);

    /**
     * Method used to notify the client of a deck update.
     * @param deckUpdate deck update
     */
    void receiveDeckUpdate(DeckUpdate deckUpdate);
}
