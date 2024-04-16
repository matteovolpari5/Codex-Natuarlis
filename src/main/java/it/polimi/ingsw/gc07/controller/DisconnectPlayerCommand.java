package it.polimi.ingsw.gc07.controller;

/**
 * Concrete command to disconnect a player from the game.
 */
public class DisconnectPlayerCommand extends GameCommand {
    /**
     * Nickname of the player that has disconnected.
     */
    String nickname;

    /**
     * Constructor of the concrete command DisconnectPlayerCommand.
     * @param nickname nickname of the player that has disconnected
     */
    public DisconnectPlayerCommand(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method to disconnect a player from the game.
     */
    @Override
    public void execute(Game game) {
        game.disconnectPlayer(nickname);
    }
}
