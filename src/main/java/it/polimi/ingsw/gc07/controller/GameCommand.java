package it.polimi.ingsw.gc07.controller;

import java.io.Serializable;

public abstract class GameCommand implements Serializable {
    /**
     * Method to execute the game command.
     * @param game game
     */
    public abstract void execute(Game game);
}
