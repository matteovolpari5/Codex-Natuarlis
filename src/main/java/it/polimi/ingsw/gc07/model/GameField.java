package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

/**
 * Class representing the game field of a single player,
 * it contains all the cards the player has placed,
 * including both their position and the way cards are placed.
 */
public class GameField {
    /**
     * Matrix of dimension 80x80, the biggest possible dimension
     * for a player's game field, containing boolean values.
     * The cell contains true if the game field contains a card in
     * that position, false otherwise.
     */
    private boolean[][] cardsPosition;

    /**
     * Matrix of dimension 80x80, the biggest possible dimension
     * for a player's game field. Each cell contains a placeable card,
     * or null if the place is empty.
     */
    private PlaceableCard[][] cardsContent;

    /**
     * Matrix of dimension 80x80, the biggest possible dimension
     * for a player's game field.
     * false: the card has been placed face up
     * true: the card has been placed face down
     */
    private boolean[][] cardsFace;

    /**
     * Constructor of the game field: builds an empty game field.
     */
    public GameField() {
        this.cardsPosition = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.cardsPosition[i][j] = false;
            }
        }
        this.cardsContent = new PlaceableCard[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.cardsContent[i][j] = null;
            }
        }
        this.cardsFace = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.cardsFace[i][j] = false;
            }
        }
    }

    /**
     * Constructor of the game field: builds a copy of an existing game field.
     */
    public GameField(GameField existingGameField) {
        this.cardsPosition = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.cardsPosition[i][j] = existingGameField.cardsPosition[i][j];
            }
        }
        this.cardsContent = new PlaceableCard[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.cardsContent[i][j] = existingGameField.cardsContent[i][j];
            }
        }
        this.cardsFace = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.cardsFace[i][j] = existingGameField.cardsFace[i][j];
            }
        }
    }

    /**
     * Allows the player to place a card at index (x,y) and
     * in a certain way, face up or face down.
     * @param card card the player wants to place
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @param way false: the card is placed face up, true: the
     *            card is placed face down
     */
    public void placeCard(PlaceableCard card, int x, int y, boolean way) throws IndexOutOfBoundsException, NullPointerException {
        if(x < 0 || x >= 80 || y <0 || y >= 80){
            throw new IndexOutOfBoundsException();
        }
        if(card == null){
            throw new NullPointerException();
        }
        cardsPosition[x][y] = true;
        // PlaceableCard is immutable, I can insert the card I receive
        cardsContent[x][y] = card;
        cardsFace[x][y] = way;
    }

    /**
     * Shows if there is any card in a certain position.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return true: there is a card in position (x,y), false: otherwise
     */
    public boolean isCardPresent(int x, int y) throws IndexOutOfBoundsException{
        if(x < 0 || x >= 80 || y <0 || y >= 80){
            throw new IndexOutOfBoundsException();
        }
        return cardsPosition[x][y];
    }

    /**
     * Returns the card placed in position (x,y).
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return card in position (x,y)
     */
    public PlaceableCard getPlacedCard(int x, int y) throws IndexOutOfBoundsException, CardNotPresentException, NullPointerException {
        if(x < 0 || x >= 80 || y <0 || y >= 80){
            throw new IndexOutOfBoundsException();
        }
        if(!cardsPosition[x][y]){
            throw new CardNotPresentException();
        }
        if(cardsContent[x][y] == null){
            throw new NullPointerException();
        }
        // PlaceableCard is immutable, I can return the card without copy
        return cardsContent[x][y];
    }

    /**
     * Returns a boolean representing the way a card is placed.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return  false: the card has been placed face up
     *          true: the card has been placed face down
     */
    public boolean getCardWay(int x, int y) throws IndexOutOfBoundsException {
        if(x < 0 || x >= 80 || y <0 || y >= 80){
            throw new IndexOutOfBoundsException();
        }
        return cardsFace[x][y];
    }
}
