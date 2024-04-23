package it.polimi.ingsw.gc07.model_view;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

public class GameFieldView {
    /**
     * Player's starter card.
     */
    private PlaceableCard starterCard;

    /**
     * Each cell of the matrix contains a placeable card, or null if the place is empty.
     */
    private PlaceableCard[][] cardsContent;
    /**
     * Each cell of the matrix contains a boolean value:
     * false: the card has been placed face up
     * true: the card has been placed face down
     */
    private Boolean[][] cardsFace;
    /**
     * Matrix representing the placement order of cards.
     */
    private int [][] cardsOrder;
    /**
     * Constant value representing the max dimension of a player's game field.
     */
    private static final int dim = 81;

    /**
     * Constructor of the game field view.
     */
    public GameFieldView(PlaceableCard starterCard) {
        this.starterCard = null;
        this.cardsContent = null;
        this.cardsFace = null;
        this.cardsOrder = null;
    }

    /**
     * Getter method for the game field content.
     */
    public PlaceableCard[][] getCardsContent() {return cardsContent;}

    /**
     * Setter method for the game field content.
     * @param cardsContent new game field
     */
    public void setCardsContent(PlaceableCard[][] cardsContent) {this.cardsContent = cardsContent;}

    /**
     * Getter method for the starter card.
     */
    public PlaceableCard getStarterCard() {return starterCard;}

    /**
     * Setter method for the starter card.
     * @param starterCard starter card
     */
    public void setStarterCard(PlaceableCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Getter method for the card's face.
     */
    public Boolean[][] getCardsFace() {return cardsFace;}

    /**
     * Setter method for the card's face.
     * @param cardsFace new matrix of card's face.
     */
    public void setCardsFace(Boolean[][] cardsFace) {this.cardsFace = cardsFace;}

    /**
     * Getter method for the card's order.
     */
    public int[][] getCardsOrder() {return cardsOrder;}

    /**
     * Setter method for the card's order.
     * @param cardsOrder new matrix of card's order.
     */
    public void setCardsOrder(int[][] cardsOrder) {this.cardsOrder = cardsOrder;}

    //TODO: robe per stampare
}
