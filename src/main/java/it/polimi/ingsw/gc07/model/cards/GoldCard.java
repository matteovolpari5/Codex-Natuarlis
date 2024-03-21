package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.exceptions.PlacementResult;
import it.polimi.ingsw.gc07.exceptions.PlacingConditionNotMetException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

import it.polimi.ingsw.gc07.model.conditions.*;

import java.util.List;

/**
 * Class that represents gold cards.
 */
public final class GoldCard extends DrawableCard {
    /**
     * Attribute that shows the placement condition of the gold card
     */
    private final Condition placementCondition;

    /**
     * Attribute that shows the scoring condition of the gold card
     */
    private final Condition scoringCondition;

    /**
     * Constructor of the gold card class
     * @param cardID : id of the card
     * @param cardType : type of the card
     * @param frontCorners : corners that the front of the card has
     * @param frontCornersContent : game items that the front of the card has
     * @param placementScore : scorable points
     * @param permanentResources : list of permanent game resources that the back of the card has
     * @param placementCondition : placement condition
     * @param scoringCondition : scoring condition
     */
    public GoldCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent,
                    boolean[] backCorners, GameItem[] backCornersContent, int placementScore,
                    List<GameResource> permanentResources, Condition placementCondition,
                    Condition scoringCondition) {
        super(cardID, cardType, frontCorners, frontCornersContent, backCorners, backCornersContent, placementScore, permanentResources);
        this.placementCondition = placementCondition;
        this.scoringCondition = scoringCondition;
    }

    /**
     * Getter method of the attribute placementCondition
     * @return this.placementCondition
     */
    public Condition getPlacementCondition() {
        return this.placementCondition;
    };

    /**
     * Getter method of the attribute scoringCondition
     * @return this.scoringCondition
     */
    public Condition getScoringCondition() {
        return this.scoringCondition;
    };

    public int getScore() {
        return super.getScore();
    }

    public PlacementResult isPlaceable(GameField gameField, int x, int y, boolean way){
        PlacementResult isPlaceableResult = super.isPlaceable(gameField, x, y, way);
        if(isPlaceableResult.equals(PlacementResult.SUCCESS)){
            // indexes are ok
            if(!way){
                //the gold card to be placed is face up
                if(placementCondition.numTimesMet(gameField) <= 0){
                    //the card is a GoldCard and the placement condition is not met
                    isPlaceableResult = PlacementResult.PLACING_CONDITION_NOT_MET;
                }
            }
        }
        return isPlaceableResult;
    }
}
