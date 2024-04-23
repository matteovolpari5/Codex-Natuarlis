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
    public GameFieldView() {
        this.starterCard = null;
        this.cardsContent = null;
        this.cardsFace = null;
        this.cardsOrder = null;
    }

    /**
     * Setter method for the starter card.
     * @param starterCard starter card
     */
    public void setStarterCard(PlaceableCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Method to add a new card to the game field view.
     * @param x x
     * @param y y
     * @param card new card
     * @param way way
     * @param orderPosition order position
     */
    public void addCard(int x, int y, PlaceableCard card, boolean way, int orderPosition) {
        cardsContent[x][y] = card;
        cardsFace[x][y] = way;
        cardsOrder[x][y] = orderPosition;
    }

    //TODO: robe per stampare
}
