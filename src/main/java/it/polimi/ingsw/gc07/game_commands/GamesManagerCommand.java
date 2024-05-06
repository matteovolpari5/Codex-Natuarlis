package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;

import java.io.Serializable;

public interface GamesManagerCommand extends Serializable, GameCommand {
    /**
     * Getter method for the nickname of games manager command.
     * Used to find the VirtualView who made the request.
     * @return nickname
     */
    String getNickname();

    /**
     * Method to execute the game command.
     */
    void execute(GamesManager gamesManager);
}
