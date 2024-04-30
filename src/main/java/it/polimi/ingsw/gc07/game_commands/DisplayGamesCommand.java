package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;

public class DisplayGamesCommand implements GamesManagerCommand {
    /**
     * Player's nickname.
     */
    private final String nickname;

    /**
     * Constructor of DisplayGamesCommand.
     * @param nickname nickname
     */
    public DisplayGamesCommand(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for nickname.
     * @return nickname
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Execute method: allows to get existing games.
     * @param gamesManager games manager
     */
    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.displayExistingGames(nickname);
    }
}
