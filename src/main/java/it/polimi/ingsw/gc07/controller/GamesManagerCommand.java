package it.polimi.ingsw.gc07.controller;

import java.io.Serializable;

public abstract class GamesManagerCommand implements Serializable {
    /**
     * Reference to the games manager object.
     */
    private GamesManager gamesManager;

    /**
     * Setter for attribute game.
     * @param gamesManager games manager
     */
    public void setGamesManager(GamesManager gamesManager) {
        this.gamesManager = gamesManager;
    }
    /**
     * Getter for games manager.
     * @return games manager
     */
    GamesManager getGamesManager() {
        return gamesManager;
    }
    /**
     * Method to execute the game command.
     */
    public abstract void execute();
}
