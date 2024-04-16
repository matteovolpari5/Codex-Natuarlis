package it.polimi.ingsw.gc07.controller;

import java.io.Serializable;

public abstract class GamesManagerCommand implements Serializable {
    /**
     * Getter method for the nickname of games manager command.
     * Used to find the VirtualView who made the request.
     * @return nickname
     */
    public abstract String getNickname();

    /**
     * Method to execute the game command.
     */
    public abstract void execute(GamesManager gamesManager);
}
