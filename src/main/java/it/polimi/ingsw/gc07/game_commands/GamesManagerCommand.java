package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;

/**
 * Interface representing a games manager command.
 */
public interface GamesManagerCommand extends GameCommand {
    /**
     * Getter method for the nickname of games manager command.
     * Used to find the VirtualView who made the request.
     * @return nickname
     */
    String getNickname();

    /**
     * Method to execute the game command.
     * @param gamesManager games manager
     */
    void execute(GamesManager gamesManager);
}
