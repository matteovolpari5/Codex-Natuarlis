package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exception.CardNotPresentException;

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
    private boolean[][] gameFieldCardsPosition;

    /**
     * Matrix of dimension 80x80, the biggest possible dimension
     * for a player's game field. Each cell contains a placeable card,
     * or null if the place is empty.
     */
    private PlaceableCard[][] gameFieldCardsContent;

    /**
     * Matrix of dimension 80x80, the biggest possible dimension
     * for a player's game field.
     * false: the card has been placed face up
     * true: the card has been placed face down
     */
    private boolean[][] gameFieldCardsFace;

    /**
     * Constructor of the game field: builds an empty game field.
     */
    public GameField() {
        this.gameFieldCardsPosition = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.gameFieldCardsPosition[i][j] = false;
            }
        }
        this.gameFieldCardsContent = new PlaceableCard[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.gameFieldCardsContent[i][j] = null;
            }
        }
        this.gameFieldCardsFace = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.gameFieldCardsFace[i][j] = false;
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
    // TODO: controllo su card ?? Ad esempio deve essere del giusto type
    public void placeCard(PlaceableCard card, int x, int y, boolean way) throws IndexOutOfBoundsException {
        if(x < 0 || x >= 80 || y <0 || y >= 80){
            throw new IndexOutOfBoundsException();
        }
        gameFieldCardsPosition[x][y] = true;
        // PlaceableCard is immutable, I can insert the card I receive
        gameFieldCardsContent[x][y] = card;
        gameFieldCardsFace[x][y] = way;
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
        return gameFieldCardsPosition[x][y];
    }

    /**
     * Returns the card placed in position (x,y).
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return card in position (x,y)
     */
    public PlaceableCard getPlacedCard(int x, int y) throws IndexOutOfBoundsException, CardNotPresentException {
        if(x < 0 || x >= 80 || y <0 || y >= 80){
            throw new IndexOutOfBoundsException();
        }
        if(!gameFieldCardsPosition[x][y]){
            throw new CardNotPresentException();
        }
        // PlaceableCard is immutable, I can return the card without copy
        return gameFieldCardsContent[x][y];
    }

    /**
     * Returns a boolean representing the way a card is placed.
     * @param x x index of the matrix
     * @param y y index of the matrix
     * @return  false: the card has been placed face up
     *          true: the card has been placed face down
     */
    // TODO: controllo posizione
    public boolean getCardWay(int x, int y) throws IndexOutOfBoundsException {
        if(x < 0 || x >= 80 || y <0 || y >= 80){
            throw new IndexOutOfBoundsException();
        }
        return gameFieldCardsFace[x][y];
    }
}
