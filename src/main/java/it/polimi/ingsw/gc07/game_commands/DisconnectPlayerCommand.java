package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.network.VirtualView;

/**
 * Concrete command to disconnect a player from the game.
 */
public class DisconnectPlayerCommand implements GameCommand {
    /**
     * Nickname of the player that has disconnected.
     */
    private final String nickname;

    /**
     * Constructor of the concrete command DisconnectPlayerCommand.
     * @param nickname nickname of the player that has disconnected
     */
    public DisconnectPlayerCommand(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method to disconnect a player from the gameController.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.disconnectPlayer(nickname);
    }
}
