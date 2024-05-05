package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.VirtualView;

/**
 * Concrete command to add a pending player to an existing game.
 */
public class JoinExistingGameCommand implements GamesManagerCommand {
    private final VirtualView virtualView;
    /**
     * Nickname of the player to add.
     */
    private final String nickname;
    /**
     * Token color chosen from the pending player.
     */
    private final TokenColor tokenColor;
    /**
     * Game id of the game to join.
     */
    private final int gameId;

    /**
     * Constructor of the concrete command JoinExistingGameCommand.
     * This constructor takes games manager as parameter, used by the server.
     * @param nickname nickname
     * @param tokenColor token color
     * @param gameId game ids
     */
    public JoinExistingGameCommand(VirtualView virtualView, String nickname, TokenColor tokenColor, int gameId) {
        this.virtualView = virtualView;
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.gameId = gameId;
    }

    /**
     * Getter method for the nickname of the command.
     * @return nickname
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Execute method of the concrete command.
     * Allows to add a player to an existing game.
     */
    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.joinExistingGame(virtualView, nickname, tokenColor, gameId);
    }
}
