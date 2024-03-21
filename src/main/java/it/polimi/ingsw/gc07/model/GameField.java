package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.CardAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.exceptions.PlacementResult;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

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
    private PlaceableCard[][] cardsContent;

    /**
     * Matrix of dimension 81x81, the biggest possible dimension
     * for a player's game field.
     * false: the card has been placed face up
     * true: the card has been placed face down
     */
    private boolean[][] cardsFace;

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
    private int [][] cardsOrder;

    /**
     * Integer attribute that show the number of cards played in the game field
     */
    private int numPlayedCards;

    /**
     * Attribute to save the starter card before the player places it.
     */
    private PlaceableCard starterCard;

    /**
     * Constructor of the game field: builds an empty game field.
     */
    public GameField(PlaceableCard starterCard) {
        this.cardsContent = new PlaceableCard[dim][dim];
        for(int i=0; i < dim; i++){
            for(int j=0; j < dim; j++){
                this.cardsContent[i][j] = null;
            }
        }
        this.cardsFace = new boolean[dim][dim];
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                this.cardsFace[i][j] = false;
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
     * Constructor of the game field: builds a copy of an existing game field.
     */
    public GameField(GameField existingGameField) {
        this.cardsContent = new PlaceableCard[dim][dim];
        for(int i=0; i < dim; i++){
            for(int j=0; j < dim; j++){
                this.cardsContent[i][j] = existingGameField.cardsContent[i][j];
            }
        }
        this.cardsFace = new boolean[dim][dim];
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
    }

    public int getDim(){
        return GameField.dim;
    }

    public PlaceableCard getStarterCard(){
        return starterCard;
    }

    public void placeCard(PlaceableCard card, int x, int y, boolean way) throws NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        assert(card != null) : "Card has value null";
        PlacementResult result = card.isPlaceable(new GameField(this), x, y, way);

        // VANNO TOLTE LE ECCEZIONI ?
        switch (result){
            case PlacementResult.NO_COVERED_CORNER:
                throw new NoCoveredCornerException();
                case PlacementResult.CARD_ALREADY_PRESENT:
                    throw new CardAlreadyPresentException();
                case PlacementResult.MULTIPLE_CORNERS_COVERED:
                    throw new MultipleCornersCoveredException();
                case PlacementResult.NOT_LEGIT_CORNER:
                    throw new NotLegitCornerException();
                case PlacementResult.INDEXES_OUT_OF_GAME_FIELD:
                    throw new IndexesOutOfGameFieldException();
                default:
                    //caso base
        }

        if(result.equals(PlacementResult.SUCCESS)){
            // PlaceableCard is immutable, I can insert the card I receive
            cardsContent[x][y] = card;
            cardsFace[x][y] = way;
            numPlayedCards++;
            cardsOrder[x][y] = numPlayedCards;
        }
    }

    /**
     * Shows if there is any card in a certain position.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return true: there is a card in position (x,y), false: otherwise
     */
    public boolean isCardPresent(int x, int y) throws IndexOutOfBoundsException {
        if(x < 0 || x >= dim || y <0 || y >= dim){
            throw new IndexOutOfBoundsException();
        }
        if(cardsContent[x][y] == null){
            return false;
        }
        return true;
    }

    /**
     * Returns the card placed in position (x,y).
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return card in position (x,y)
     */
    public PlaceableCard getPlacedCard(int x, int y) throws IndexOutOfBoundsException, CardNotPresentException {
        if(x < 0 || x >= dim || y <0 || y >= dim){
            throw new IndexOutOfBoundsException();
        }
        if(cardsContent[x][y] == null){
            throw new CardNotPresentException();
        }
        // PlaceableCard is immutable, I can return the card without copy
        return cardsContent[x][y];
    }

    /**
     * Method that allows to remove a placed card from the game field.
     * It is used on copies of the game field for verifying conditions,
     * so it does not change the cards' placement order.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @throws IndexOutOfBoundsException
     * @throws CardNotPresentException
     */
    public void removePlacedCard(int x, int y) throws IndexOutOfBoundsException, CardNotPresentException {
        if(x < 0 || x >= dim || y <0 || y >= dim){
            throw new IndexOutOfBoundsException();
        }
        if(cardsContent[x][y] == null){
            throw new CardNotPresentException();
        }
        cardsContent[x][y] = null;
    }

    /**
     * Returns a boolean representing the way a card is placed.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return  false: the card has been placed face up
     *          true: the card has been placed face down
     */
    public boolean getCardWay(int x, int y) throws IndexOutOfBoundsException, CardNotPresentException {
        if(x < 0 || x >= dim || y <0 || y >= dim){
            throw new IndexOutOfBoundsException();
        }
        if(cardsContent[x][y] == null){
            throw new CardNotPresentException();
        }
        return cardsFace[x][y];
    }

    public int[][] getCardsOrder() {
        int[][] cardsOrderCopy = new int[dim][dim];
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                cardsOrderCopy[i][j] = cardsOrder[i][j];
            }
        }
        return cardsOrderCopy;
    }

    public int getNumPlayedCards() {
        return this.numPlayedCards;
    }

    // TODO
    // aggiungere getter per cardsContent e cardsFace
}
