package it.polimi.ingsw.gc07.controller;

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
     * @return command result
     */
    @Override
    public CommandResult execute() {
        assert(game.getPlayersGameField().containsKey(nickname)): "The player is not in the game";
        return game.getPlayersGameField().get(nickname).placeCard(game.getPlayersGameField().get(nickname).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, way);
    }
}
