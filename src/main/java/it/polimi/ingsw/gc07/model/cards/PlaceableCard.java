package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.model.conditions.Condition;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents placeable cards of the game.
 * Placeable cards are: starter cards, gold cards and resource cards.
 */
public abstract class PlaceableCard extends Card {
    /**
     * Attribute that shows which permanent game items the card has (on the back)
     */
    private final List<GameResource> permanentResources;
    /**
     *  Attribute that shows which corners the front of the card has.
     */
    private final boolean[] frontCorners;
    /**
     * Attribute that shows which game items the front of the card has.
     */
    private final GameItem[] frontCornersContent;

    /**
     * Constructor of the class PlaceableCard.
     * @param cardID : id of the card
     * @param cardType: type of the card
     * @param frontCorners : corners that the front of the card has
     * @param frontCornersContent : game items that the front of the card has
     * @param permanentResources list of permanent resources
     */
    public PlaceableCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent, List<GameResource> permanentResources) {
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
        this.permanentResources = new ArrayList<>(permanentResources);
    }

    /**
     * Getter method of the attribute frontCorners.
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
     * Getter method of the attribute frontCornersContent.
     * @return this.frontCornersContent
     */
    public GameItem[] getFrontCornersContent(){
        GameItem[] frontCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            frontCornersContentCopy[i] = frontCornersContent[i];
        }
        return frontCornersContentCopy;
    }

    /**
     * Getter method of the attribute permanentResources
     * @return an arrayList of GameResources
     */
    public  List<GameResource> getPermanentResources(){
        return new ArrayList<>(permanentResources);
    }
}
