package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

/**
 * Concrete command used to reconnect a player to a game.
 */
public class ReconnectPlayerCommand implements GamesManagerCommand {
    /**
     * Virtual view associated to a client.
     */
    private final VirtualView client;
    /**
     * Nickname of the player to reconnect.
     */
    private final String nickname;
    /**
     * Connection type value of the player to reconnect.
     */
    private final boolean connectionType;
    /**
     * Interface type value of the player to reconnect.
     */
    private final boolean interfaceType;

    /**
     * Constructor of ReconnectPlayerCommand.
     * @param client client
     * @param nickname nickname
     * @param connectionType connection type
     * @param interfaceType interface type
     */
    public ReconnectPlayerCommand(VirtualView client, String nickname, boolean connectionType, boolean interfaceType) {
        this.client = client;
        this.nickname = nickname;
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
    }

    /**
     * Method used to get the nickname of the player who sent the command.
     * @return nickname of the player who sent the command
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Method to reconnect a player.
     * @param gamesManager games manager
     */
    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.reconnectPlayer(client, nickname, connectionType, interfaceType);
    }
}
