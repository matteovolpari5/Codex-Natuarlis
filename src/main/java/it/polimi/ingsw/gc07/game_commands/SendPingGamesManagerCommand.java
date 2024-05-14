package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;

public class SendPingGamesManagerCommand implements GamesManagerCommand {
    /**
     * Nickname of the player who sent the ping.
     */
    private final String nickname;

    public SendPingGamesManagerCommand(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.receivePing(nickname);
    }
}
