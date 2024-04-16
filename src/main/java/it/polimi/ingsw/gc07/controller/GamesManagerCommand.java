package it.polimi.ingsw.gc07.controller;

import java.io.Serializable;

public abstract class GamesManagerCommand implements Serializable {
    public abstract String getNickname();

    /**
     * Method to execute the game command.
     */
    public abstract void execute(GamesManager gamesManager);
}
