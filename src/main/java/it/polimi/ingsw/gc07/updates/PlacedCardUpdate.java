package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model_view.GameView;

/**
 * Update used to show a new card has been placed in the game field.
 */
public class PlacedCardUpdate implements Update {
    /**
     * Nickname of the player who placed the card.
     */
    private final String nickname;
    /**
     * Placed card.
     */
    private final PlaceableCard card;
    /**
     * Position the card has been placed, axis x.
     */
    private final int x;
    /**
     * Position the card has been placed, axis y.
     */
    private final int y;
    /**
     * Way the card has been placed.
     */
    private final boolean way;
    /**
     * Order position the card has been placed.
     */
    private final int orderPosition;

    /**
     * Constructor of PlacedCardUpdate.
     * @param nickname nickname of the player who placed the card
     * @param card card
     * @param x x
     * @param y y
     * @param way way
     * @param orderPosition order position
     */
    public PlacedCardUpdate(String nickname, PlaceableCard card, int x, int y, boolean way, int orderPosition) {
        this.nickname = nickname;
        this.card = card;
        this.x = x;
        this.y = y;
        this.way = way;
        this.orderPosition = orderPosition;
    }

    /**
     * Execute method of the concrete update: reveals the card that has been placed.
     * @param gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.addCard(nickname, card, x, y, way, orderPosition);
    }
}
