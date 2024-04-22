package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model_view.GameFieldView;

public interface GameFieldListener {
    /**
     * Method used to notify the player that the game field has changed.
     * @param gameFieldView new game field view
     */
    void showUpdatedGameFiled(GameFieldView gameFieldView);
}
