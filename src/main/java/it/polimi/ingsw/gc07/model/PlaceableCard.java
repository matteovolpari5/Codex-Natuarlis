package it.polimi.ingsw.gc07.model;

/**
 * class that represents the placeable cards of the game.
 * the placeable cards are: starter cards, gold cards and the resource cards.
 */
public abstract class PlaceableCard extends Card{
    /**
     *  attribute that shows which corners the front of the card has
     */
    private final boolean[] frontCorners;
    /**
     * attribute that shows which game items the front of the card has
     */
    private final GameItem[] frontCornersContent;

    /**
     * contructor of the class PlaceableCard
     * @param cardID : id of the card
     * @param cardType: type of the card
     * @param frontCorners : corners that the front of the card has
     * @param frontCornersContent : game items that the front of the card has
     */
    // regular constructor
    public PlaceableCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent) {
        super(cardID, cardType);
        boolean[] frontCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            frontCornersCopy[i] = frontCorners[i];
        }
        GameItem[] frontCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            frontCornersContentCopy[i] = frontCornersContent[i];
        }
        this.frontCorners = frontCornersCopy;
        this.frontCornersContent = frontCornersContentCopy;
    }

    /**
     * getter method of the attribute frontCorners
     * @return this.frontCorners
     */
    public boolean[] getFrontCorners(){
        boolean[] frontCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            frontCornersCopy[i] = frontCorners[i];
        }
        return frontCornersCopy;
    }

    /**
     * getter method of the attribute frontCornersContent
     * @return this.frontCornersContent
     */
    public GameItem[] getFrontCornersContent(){
        GameItem[] frontCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            frontCornersContentCopy[i] = frontCornersContent[i];
        }
        return frontCornersContentCopy;
    }
}
