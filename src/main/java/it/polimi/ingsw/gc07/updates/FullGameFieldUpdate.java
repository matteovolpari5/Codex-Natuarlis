package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model_view.GameView;

public class FullGameFieldUpdate implements Update {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Player's starter card.
     */
    private final PlaceableCard starterCard;
    /**
     * Each cell of the matrix contains a placeable card, or null if the place is empty.
     */
    private final PlaceableCard[][] cardsContent;
    /**
     * Each cell of the matrix contains a boolean value:
     * false: the card has been placed face up
     * true: the card has been placed face down
     */
    private final Boolean[][] cardsFace;
    /**
     * Matrix representing the placement order of cards.
     */
    private final int [][] cardsOrder;

    /**
     * Constructor of class FullGameFieldUpdate.
     * @param nickname nickname
     * @param starterCard starter card
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    public FullGameFieldUpdate(String nickname, PlaceableCard starterCard, PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        this.nickname = nickname;
        this.starterCard = starterCard;
        this.cardsContent = cardsContent;
        this.cardsFace = cardsFace;
        this.cardsOrder = cardsOrder;
    }

    /**
     * Method that allows the client to get a full game field update, after a disconnection.
     * @param gameView game view
     */
    @Override
    public void execute(GameView gameView) {
        gameView.receiveFullGameFieldUpdate(nickname, starterCard, cardsContent, cardsFace, cardsOrder);
    }
}
