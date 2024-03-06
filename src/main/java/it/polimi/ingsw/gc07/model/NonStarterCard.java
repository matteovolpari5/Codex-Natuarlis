package it.polimi.ingsw.gc07.model;

public class NonStarterCard extends PlaceableCard{
    private int placementScore;
    private Resource permanentResource;

    // Regular constructor
    public NonStarterCard(int cardID, CardType cardType, boolean[] frontCorners,
                          GameItem[] frontCornersContent, int placementScore,
                          Resource permanentResource) {
        super(cardID, cardType, frontCorners, frontCornersContent);
        this.placementScore = placementScore;
        this.permanentResource = permanentResource;
    }

    public NonStarterCard(Card existingCard, boolean[] frontCorners, GameItem[] frontCornersContent,
                          int placementScore, Resource permanentResource) {
        super(existingCard, frontCorners, frontCornersContent);
        this.placementScore = placementScore;
        this.permanentResource = permanentResource;
    }
    private int getPlacementScore() {
        return this.placementScore;
    };
    private Resource getPermanentResource() {
        return this.permanentResource;
    };
}
