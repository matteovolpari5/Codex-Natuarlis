package it.polimi.ingsw.gc07.model;

import java.util.concurrent.locks.Condition;

public class GoldCard extends NonStarterCard{
    private Condition placementCondition;
    private Condition scoringCondition;

    // regular constructor
    public GoldCard(int cardID, CardType cardType, boolean[] frontCorners,
                    GameItem[] frontCornersContent, int placementScore,
                    GameResource permanentResource, Condition placementCondition,
                    Condition scoringCondition) {
        super(cardID, cardType, frontCorners, frontCornersContent, placementScore, permanentResource);
        this.placementCondition = placementCondition;
        this.scoringCondition = scoringCondition;
    }

    // TODO: forse da eliminare, se rendiamo immutabile
    // TODO: attenzione, prendo riferimento dall'esterno
    public GoldCard(Card existingCard, boolean[] frontCorners, GameItem[] frontCornersContent,
                    int placementScore, GameResource permanentResource,
                    Condition placementCondition, Condition scoringCondition) {
        super(existingCard, frontCorners, frontCornersContent, placementScore, permanentResource);
        this.placementCondition = placementCondition;
        this.scoringCondition = scoringCondition;
    }
    // TODO: attenzione, sfugge riferimento a Condition
    private Condition getPlacementCondition() {
        return this.placementCondition;
    };
    // TODO: attenzione, sfugge riferimento a Condition
    private Condition getScoringCondition() {
        return this.scoringCondition;
    };
}
