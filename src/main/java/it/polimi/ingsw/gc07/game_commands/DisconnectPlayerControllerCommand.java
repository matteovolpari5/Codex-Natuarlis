package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

/**
 * Concrete command to disconnect a player from the game.
 */
public class DisconnectPlayerControllerCommand implements GameControllerCommand {
    /**
     * Nickname of the player that has disconnected.
     */
    private final String nickname;

    /**
     * Constructor of the concrete command DisconnectPlayerControllerCommand.
     * @param nickname nickname of the player that has disconnected
     */
    public DisconnectPlayerControllerCommand(String nickname) {
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
