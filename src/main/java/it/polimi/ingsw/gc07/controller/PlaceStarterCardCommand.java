package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.model.GameField;

/**
 * Concrete command to place the starter card in a certain way.
 */
public class PlaceStarterCardCommand implements GameCommand{
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;

    /**
     * Nickname of the player.
     */
    private final String nickname;

    /**
     * Way of the card.
     */
    private final boolean way;

    /**
     * Constructor of the concrete command PlaceStarterCardCommand.
     * @param game game
     * @param nickname nickname
     * @param way way
     */
    public PlaceStarterCardCommand(Game game, String nickname, boolean way) {
        this.game = game;
        this.nickname = nickname;
        this.way = way;
    }

    /**
     * Method to place the starter card in a certain way.
     */
    @Override
    public void execute() {
        if(!game.getState().equals(GameState.PLAYING)) {
            game.getCommandResultManager().setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        // no check for current player, starter cards can be placed in any order
        assert(game.getPlayersGameField().containsKey(nickname)): "The player is not in the game";
        game.getCommandResultManager().setCommandResult(game.getPlayersGameField().get(nickname).placeCard(game.getPlayersGameField().get(nickname).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, way));
    }
}
