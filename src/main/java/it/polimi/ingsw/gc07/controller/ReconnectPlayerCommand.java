package it.polimi.ingsw.gc07.controller;

/**
 * Concrete command to reconnect a player to the game.
 */
public class ReconnectPlayerCommand extends GameCommand {
    /**
     * Nickname of the player that will be reconnected to the game.
     */
    private final String nickname;

    /**
     * Constructor of the concrete command.
     * @param nickname nickname of the player to reconnect
     */
    public ReconnectPlayerCommand(String nickname) {
        this.nickname=nickname;
    }
    /**
     * Method to execute the concrete command reconnectPlayerCommand.
     */
    @Override
    public void execute(Game game) {
        game.reconnectPlayer(nickname);
    }
}
