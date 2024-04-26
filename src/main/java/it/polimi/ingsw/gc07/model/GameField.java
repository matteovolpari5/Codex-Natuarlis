package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.listeners.GameFieldListener;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the game field of a single player,
 * it contains all the cards the player has placed,
 * including both their position and the way cards are placed.
 */
public class GameField {
    /**
     * Matrix of dimension 81x81, the biggest possible dimension
     * for a player's game field. Each cell contains a placeable card,
     * or null if the place is empty.
     */
    private final PlaceableCard[][] cardsContent;
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
     * Matrix of dimension 81x81, biggest possible dimension for a player's
     * game field. Represent the placement's order of the cards.
     */
    private final int [][] cardsOrder;
    /**
     * Integer attribute that show the number of cards played in the game field
     */
    private int numPlayedCards;
    /**
     * Attribute to save the starter card before the player places it.
     */
    private PlaceableCard starterCard;
    /**
     * List of game field listeners.
     */
    private final List<GameFieldListener> gameFieldListeners;

    /**
     * Constructor of the game field: builds an empty game field.
     */
    public GameField() {
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
        this.starterCard = null;
        this.gameFieldListeners = new ArrayList<>();
    }

    /**
     * Constructor of the game field: builds a copy of an existing game field.
     */
    public GameField(GameField existingGameField) {
        this.cardsContent = new PlaceableCard[dim][dim];
        for(int i=0; i < dim; i++){
            for(int j=0; j < dim; j++){
                this.cardsContent[i][j] = existingGameField.cardsContent[i][j];
            }
        }
        this.cardsFace = new Boolean[dim][dim];
        for(int i=0; i < dim; i++){
            for(int j=0; j < dim; j++){
                this.cardsFace[i][j] = existingGameField.cardsFace[i][j];
            }
        }
        this.cardsOrder = new int [dim][dim];
        for(int i=0; i < dim; i++){
            for(int j=0; j < dim; j++){
                this.cardsOrder[i][j] = existingGameField.cardsOrder[i][j];
            }
        }
        this.numPlayedCards = existingGameField.numPlayedCards;
        this.starterCard = existingGameField.starterCard;
        this.gameFieldListeners = existingGameField.gameFieldListeners;
    }

    /**
     * Method to add a game field listener.
     * @param gameFieldListener new game field listener
     */
    public void addListener(GameFieldListener gameFieldListener) {
        gameFieldListeners.add(gameFieldListener);
    }

    /**
     * Method that returns the constant dimension of the game field.
     * @return dimension of the game field
     */
    public static int getDim() {
        return GameField.dim;
    }

    /**
     * Setter method for player's starter card.
     */
    public void setStarterCard(PlaceableCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Method that returns the starter card of the game field, i.e. of the player.
     * @return starter card of the game field
     */
    public PlaceableCard getStarterCard() {
        return starterCard;
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
        CommandResult result = card.isPlaceable(new GameField(this), x, y, way);
        if(result.equals(CommandResult.SUCCESS)){
            // PlaceableCard is immutable, I can insert the card I receive
            cardsContent[x][y] = card;
            cardsFace[x][y] = way;
            numPlayedCards++;
            cardsOrder[x][y] = numPlayedCards;
        }
        return result;
    }

    /**
     * Shows if there is any card in a certain position.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return true: there is a card in position (x,y), false: otherwise
     */
    public boolean isCardPresent(int x, int y) {
        return cardsContent[x][y] != null;
    }

    /**
     * Returns the card placed in position (x,y), null if a card is not present.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return card in position (x,y)
     */
    public PlaceableCard getPlacedCard(int x, int y) {
        // PlaceableCard is immutable, I can return the card without copy
        // if a card is not present, returns null
        return cardsContent[x][y];
    }

    /**
     * Method that allows to remove a placed card from the game field.
     * It is used on copies of the game field for verifying conditions,
     * so it does not change the cards' placement order.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @throws IndexOutOfBoundsException exception thrown if the indexes are not inside the game field
     */
    public void removePlacedCard(int x, int y) {
        cardsContent[x][y] = null;
    }

    /**
     * Returns a boolean representing the way a card is placed.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return  false: the card has been placed face up
     *          true: the card has been placed face down
     */
    public Boolean getCardWay(int x, int y) {
        return cardsFace[x][y];
    }

    /**
     * Returns a matrix representing the order cards have been placed.
     * @return matrix representing the order cards have been placed
     */
    public int[][] getCardsOrder() {
        int[][] cardsOrderCopy = new int[dim][dim];
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                cardsOrderCopy[i][j] = cardsOrder[i][j];
            }
        }
        return cardsOrderCopy;
    }

    /**
     * Getter that returns when the card was placed.
     * @param x x position in the matrix
     * @param y y position in the matrix
     * @return an integer that represents when a card has been placed
     */
    public int getCardsOrder(int x,int y) {
        return cardsOrder[x][y];
    }

    /**
     * Return the number of cards placed on the game field.
     * @return number of cards placed
     */
    public int getNumPlayedCards() {
        return this.numPlayedCards;
    }
}
