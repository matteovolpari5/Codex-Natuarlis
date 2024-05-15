package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;
/**
 * Concrete command to remove from pending player a player with a certain nickname.
 */
public class RemoveFromPendingCommand implements GamesManagerCommand{
    /**
     * Nickname of the player to add.
     */
    private final String nickname;

    public RemoveFromPendingCommand(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Token color chosen from the pending player.
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.removePlayer(nickname);
    }
}
