package it.polimi.ingsw.gc07.model;

import java.util.concurrent.locks.Condition;

public class GoldCard extends NonStarterCard{
    private final Condition placementCondition;
    private final Condition scoringCondition;

    // regular constructor
    public GoldCard(int cardID, CardType cardType, boolean[] frontCorners,
                    GameItem[] frontCornersContent, int placementScore,
                    GameResource permanentResource, Condition placementCondition,
                    Condition scoringCondition) {
        super(cardID, cardType, frontCorners, frontCornersContent, placementScore, permanentResource);
        this.placementCondition = placementCondition;
        this.scoringCondition = scoringCondition;
    }

    public Condition getPlacementCondition() {
        return this.placementCondition;
    };
    public Condition getScoringCondition() {
        return this.scoringCondition;
    };
}
