package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

public class ReconnectPlayerCommand implements GamesManagerCommand {

    VirtualView client;

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

    public ReconnectPlayerCommand(VirtualView client, String nickname, boolean connectionType, boolean interfaceType) {
        this.client = client;
        this.nickname = nickname;
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.reconnectPlayer(client, nickname, connectionType, interfaceType);
    }
}
