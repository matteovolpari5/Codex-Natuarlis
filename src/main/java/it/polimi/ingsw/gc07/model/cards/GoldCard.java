package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
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
     * @param frontCorners : corners that the front of the card has
     * @param frontCornersContent : game items that the front of the card has
     * @param placementScore : placement score
     * @param permanentResources : list of permanent game resources that the back of the card has
     * @param placementCondition : placement condition
     * @param scoringCondition : scoring condition
     */
    public GoldCard(int cardID, boolean[] frontCorners, GameItem[] frontCornersContent,
                    boolean[] backCorners, GameItem[] backCornersContent, int placementScore,
                    List<GameResource> permanentResources, Condition placementCondition,
                    Condition scoringCondition) {
        super(cardID, CardType.GOLD_CARD, frontCorners, frontCornersContent, backCorners, backCornersContent, placementScore, permanentResources);
        this.placementCondition = placementCondition;
        this.scoringCondition = scoringCondition;
    }

    /**
     * Method to check if the card is placeable on a certain game field, in a certain position and way.
     * Used on gold cards.
     * @param gameField game field to place the card
     * @param x row
     * @param y column
     * @param way way
     * @return enum representing the placement result
     */
    public CommandResult isPlaceable(GameField gameField, int x, int y, boolean way) {
        CommandResult isPlaceableResult = super.isPlaceable(gameField, x, y, way);
        if(isPlaceableResult.equals(CommandResult.SUCCESS)){
            // indexes are ok
            if(!way){
                //the gold card to be placed is face up
                if(placementCondition.numTimesMet(gameField) <= 0){
                    //the card is a GoldCard and the placement condition is not met
                    isPlaceableResult = CommandResult.PLACING_CONDITION_NOT_MET;
                }
            }
        }
        return isPlaceableResult;
    }

    /**
     * Method returning the placement score of the card in position (x,y).
     * Used on gold cards.
     * @param gameField game field
     * @param x row
     * @param y column
     * @return score obtained
     */
    @Override
    public int getPlacementScore(GameField gameField, int x, int y) {
        if (scoringCondition == null){
            return super.getPlacementScore(gameField, x, y);
        }
        else{
            int cardScore = super.getPlacementScore(gameField, x, y);
            return cardScore * scoringCondition.numTimesMet(gameField);
        }
    }

    //METHOD USED TO HELP PRINT IN TUI VIEW

    /**
     * getter method for placementCondition
     * @return placementCondition
     */
    @Override
    public Condition getPlacementCondition() {
        return placementCondition;
    }

    /**
     * getter method for scoringCondition
     * @return scoringCondition
     */
    @Override
    public Condition getScoringCondition() {
        return scoringCondition;
    }
}
