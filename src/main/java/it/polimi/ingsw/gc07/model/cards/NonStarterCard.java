package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.model.conditions.Condition;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

import java.util.List;

/**
 * Class that represents non-starter cards.
 * Non-starter cards are: GoldCards and ResourceCards.
 */
public class NonStarterCard extends PlaceableCard {
    /**
     * Attribute that shows how many points the player will score if he places
     * this card.
     * For resource cards, the placementScore is obtained just by placing the
     * card and can be 0.
     * Gold cards can have a scoring condition.
     * If a gold card hasn't got a scoring, the placementScore just by placing the card.
     * If it has got a scoring condition, the placementScore is obtained if
     * the condition is met at the time of placement and the score gained is:
     * (placementScore) x (number of times the condition is met during the placement).
     */
    private final int placementScore;

    /**
     * Constructor of the class NonStarterCard.
     * @param cardID : id of the card
     * @param cardType : type of the card
     * @param frontCorners : corners that the front of the card has
     * @param frontCornersContent : game items that the front of the card has
     * @param placementScore : points obtained placing the card
     * @param permanentResources :  list of permanent game resources on the back of the card
     */
    // Regular constructor
    public NonStarterCard(int cardID, CardType cardType, boolean[] frontCorners,
                          GameItem[] frontCornersContent, int placementScore,
                          List<GameResource> permanentResources) {
        super(cardID, cardType, frontCorners, frontCornersContent, permanentResources);
        this.placementScore = placementScore;
    }

    public Condition getScoringCondition(){
        return null;
    }

    public Condition getPlacementCondition(){
        return null;
    }

    public int getScore(){
        return placementScore;
    }

    public boolean[] getBackCorners(){
        return null;
    }

    public GameItem[] getBackCornersContent(){
        return null;
    }
}
