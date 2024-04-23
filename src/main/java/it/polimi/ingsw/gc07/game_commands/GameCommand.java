package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

import java.io.Serializable;

public abstract class GameCommand implements Serializable {
    /**
     * Method to execute the game command.
     * @param gameController game controller
     */
    public abstract void execute(GameController gameController);
}
