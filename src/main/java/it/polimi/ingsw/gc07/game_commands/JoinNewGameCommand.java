package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GamesManager;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.VirtualView;

/**
 * Concrete command to add a pending player to a new game.
 */
public class JoinNewGameCommand implements GamesManagerCommand {
    VirtualView virtualView;
    /**
     * Nickname of the player to add.
     */
    private final String nickname;
    /**
     * Token color chosen from the pending player.
     */
    private final TokenColor tokenColor;
    /**
     * Number of players for the new game.
     */
    private final int playersNumber;

    /**
     * Constructor of the concrete command JoinNewGameCommand.
     * This constructor takes games manager as parameter, used by the server.
     * @param nickname nickname
     * @param tokenColor token color
     * @param playersNumber players number
     */
    public JoinNewGameCommand(VirtualView virtualView, String nickname, TokenColor tokenColor, int playersNumber) {
        this.virtualView = virtualView;
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.playersNumber = playersNumber;
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
     * Execute method for the concrete command.
     * Creates a new game and adds the player to the newly created game.
     */
    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.joinNewGame(virtualView, nickname, tokenColor, playersNumber);
    }
}
