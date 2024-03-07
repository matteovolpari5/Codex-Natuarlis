package it.polimi.ingsw.gc07.model;

/**
 * class that represents the no starter cards.
 * the no starter cards are: gold cards and resource cards.
 */
public class NonStarterCard extends PlaceableCard{
    /**
     * attribute that shows how many points the player will score if he places this card
     */
    private final int placementScore;
    /**
     * attribute that shows the permanent game items that the back of the card has
     */
    private final GameResource permanentResource;

    /**
     * constructor of the class NonStarterCard
     * @param cardID : id of the card
     * @param cardType : type of the card
     * @param frontCorners : corners that the front of the card has
     * @param frontCornersContent : game items that the front of the card has
     * @param placementScore : points scoreables
     * @param permanentResource : permanent game items that the back of the card has
     */
    // Regular constructor
    public NonStarterCard(int cardID, CardType cardType, boolean[] frontCorners,
                          GameItem[] frontCornersContent, int placementScore,
                          GameResource permanentResource) {
        super(cardID, cardType, frontCorners, frontCornersContent);
        this.placementScore = placementScore;
        this.permanentResource = permanentResource;
    }

    /**
     * getter method of the attribute placementScore
     * @return this.placementScore
     */
    private int getPlacementScore() {
        return this.placementScore;
    };

    /**
     * getter method of the attribute permanentResource
     * @return this.permanentResource
     */
    private GameResource getPermanentResource() {
        return this.permanentResource;
    };
}
