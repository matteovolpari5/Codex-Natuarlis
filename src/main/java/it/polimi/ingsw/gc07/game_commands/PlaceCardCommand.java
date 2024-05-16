package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

/**
 * Concrete command to place a card.
 */
public class PlaceCardCommand implements GameControllerCommand {
    /**
     * Nickname of the player that will place the card.
     */
    private final String nickname;
    /**
     * Card that will be placed.
     */
    private final int pos;
    /**
     * Row in the matrix.
     */
    private final int x;
    /**
     * Column in the matrix.
     */
    private final int y;
    /**
     * Way of the card.
     */
    private final boolean way;

    /**
     * Constructor of the concrete command PlaceCardCommand.
     * @param nickname nickname
     * @param pos position in hand
     * @param x row
     * @param y column
     * @param way way
     */
    public PlaceCardCommand(String nickname, int pos, int x, int y, boolean way) {
        this.nickname = nickname;
        this.pos = pos;
        this.x = x;
        this.y = y;
        this.way = way;
    }

    /**
     * Method to place a card in the game field of the current player.
     * This method also removes the card placed from the hand of the current player and calls
     * the method that computes the points scored by placing the card.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.placeCard(nickname, pos, x, y, way);
    }
}
