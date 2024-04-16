package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;

/**
 * Concrete command to place a card.
 */
public class PlaceCardCommand extends GameCommand {
    /**
     * Nickname of the player that will place the card.
     */
    private final String nickname;
    /**
     * Card that will be placed.
     */
    private final DrawableCard card;
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
     * @param card card
     * @param x row
     * @param y column
     * @param way way
     */
    public PlaceCardCommand(String nickname, DrawableCard card, int x, int y, boolean way) {
        this.nickname = nickname;
        this.card = card;
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
    public void execute(Game game) {
        game.placeCard(nickname, card, x, y, way);
    }
}
