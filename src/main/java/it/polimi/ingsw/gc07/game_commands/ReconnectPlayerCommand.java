package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;

public class ReconnectPlayerCommand implements GamesManagerCommand {
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

    private final VirtualView client ;

    public ReconnectPlayerCommand(String nickname, VirtualView client, boolean connectionType, boolean interfaceType) {
        this.nickname = nickname;
        this.client = client;
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.reconnectPlayer(nickname, client, connectionType, interfaceType);
    }
}
