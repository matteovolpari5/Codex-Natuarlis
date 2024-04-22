package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.CommandResult;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

public class GameFieldView {
    /**
     * Attribute to save the starter card before the player places it.
     */
    private final PlaceableCard starterCard;

    /**
     * Integer attribute that show the number of cards played in the game field
     */
    private int numPlayedCards;
    /**
     * Matrix of dimension 81x81, the biggest possible dimension
     * for a player's game field. Each cell contains a placeable card,
     * or null if the place is empty.
     */
    private final PlaceableCard[][] cardsContent;

    /**
     * Matrix of dimension 81x81, biggest possible dimension for a player's
     * game field. Represent the placement's order of the cards.
     */
    private final int [][] cardsOrder;


    /**
     * Matrix of dimension 81x81, the biggest possible dimension
     * for a player's game field.
     * false: the card has been placed face up
     * true: the card has been placed face down
     */
    private final Boolean[][] cardsFace;

    /**
     * Constant value representing the max dimension of a player's
     * game field.
     * A player can place, at most, 40 card in one direction starting
     * from position (40,40), which is the position of the starter card.
     */
    private static final int dim = 81;

    /**
     * Constructor of the game field view: builds an empty game field.
     */
    public GameFieldView(PlaceableCard starterCard) {
        this.cardsContent = new PlaceableCard[dim][dim];
        for(int i=0; i < dim; i++){
            for(int j=0; j < dim; j++){
                this.cardsContent[i][j] = null;
            }
        }
        this.cardsFace = new Boolean[dim][dim];
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                this.cardsFace[i][j] = null;
            }
        }
        this.cardsOrder = new int [dim][dim];
        for(int i=0; i < dim; i++){
            for(int j=0; j < dim; j++){
                this.cardsOrder[i][j] = 0;
            }
        }
        this.numPlayedCards = 0;
        this.starterCard = starterCard;
    }
    /**
     * Method to place a card on the game field.
     * @param card card to place
     * @param x row
     * @param y column
     * @param way way
     * @return result of the placement
     */
    public CommandResult placeCard(PlaceableCard card, int x, int y, boolean way) {
        CommandResult result = card.isPlaceable(new GameField(starterCard), x, y, way);
        if(result.equals(CommandResult.SUCCESS)){
            // PlaceableCard is immutable, I can insert the card I receive
            cardsContent[x][y] = card;
            cardsFace[x][y] = way;
            numPlayedCards++;
            cardsOrder[x][y] = numPlayedCards;
        }
        return result;
    }
}
