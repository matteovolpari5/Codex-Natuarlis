package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;

public interface Update {
    /**
     * Method that allows the client to get the update in the message.
     */
    void execute(GameView gameView);
}
