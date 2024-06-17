package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.model.conditions.Condition;
import it.polimi.ingsw.gc07.model.CommandResult;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.CardType;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.GameResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents placeable cards of the game.
 * Placeable cards are: starter cards, gold cards and resource cards.
 */
public class PlaceableCard extends Card {
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
     * Attribute that shows which corners the back of the card has.
     */
    private final boolean[] backCorners;
    /**
     * Attribute that shows which game items the back of the card has in its corners.
     */
    private final GameItem[] backCornersContent;

    /**
     * Constructor of the class PlaceableCard.
     * @param cardID : id of the card
     * @param cardType: type of the card
     * @param frontCorners : corners that the front of the card has
     * @param frontCornersContent : game items that the front of the card has
     * @param permanentResources list of permanent resources
     */
    public PlaceableCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent,
                         boolean[] backCorners, GameItem[] backCornersContent, List<GameResource> permanentResources) {
        super(cardID, cardType);
        boolean[] frontCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            frontCornersCopy[i] = frontCorners[i];
        }
        GameItem[] frontCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            frontCornersContentCopy[i] = frontCornersContent[i];
        }
        boolean[] backCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            backCornersCopy[i] = backCorners[i];
        }
        GameItem[] backCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            backCornersContentCopy[i] = backCornersContent[i];
        }
        this.frontCorners = frontCornersCopy;
        this.frontCornersContent = frontCornersContentCopy;
        this.backCorners = backCornersCopy;
        this.backCornersContent = backCornersContentCopy;
        this.permanentResources = new ArrayList<>(permanentResources);
    }

    /**
     * Getter method of the attribute frontCorners.
     * @return this.frontCorners
     */
    public boolean[] getFrontCorners() {
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
    public GameItem[] getFrontCornersContent() {
        GameItem[] frontCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            frontCornersContentCopy[i] = frontCornersContent[i];
        }
        return frontCornersContentCopy;
    }

    /**
     * Getter method of the attribute backCorners
     * @return an array of booleans
     */
    public boolean[] getBackCorners() {
        boolean[] backCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            backCornersCopy[i] = backCorners[i];
        }
        return backCornersCopy;
    }

    /**
     * Getter method of the attribute backCornersContent
     * @return an array of GameItems
     */
    public GameItem[] getBackCornersContent() {
        GameItem[] backCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            backCornersContentCopy[i] = backCornersContent[i];
        }
        return backCornersContentCopy;
    }

    /**
     * Getter method of the attribute permanentResources
     * @return an arrayList of GameResources
     */
    public  List<GameResource> getPermanentResources() {
        return new ArrayList<>(permanentResources);
    }

    /**
     * Method to check if the card is placeable on a certain game field, in a certain position and way.
     * Used on starter cards.
     * @param gameField game field to place the card
     * @param x row
     * @param y column
     * @param way way
     * @return enum representing the placement result
     */
    public CommandResult isPlaceable(GameField gameField, int x, int y, boolean way) {
        assert(gameField.getNumPlayedCards() == 0): "A card was placed before the starter card";
        return CommandResult.SUCCESS;
    }

    /**
     * Method returning the placement score of the card in position (x,y).
     * Used on starter cards.
     * @param gameField game field
     * @param x row
     * @param y column
     * @return score obtained
     */
    public int getPlacementScore(GameField gameField, int x, int y) {
        assert (gameField.isCardPresent(x, y)) : "There isn't a Card in position (x,y)";
        return 0;
    }


    // methods used in Tui

    /**
     * getter method for placementCondition
     * @return placementCondition
     */
    public Condition getPlacementCondition() {
        return null;
    }

    /**
     * getter method for scoringCondition
     * @return scoringCondition
     */
    public Condition getScoringCondition() {
        return null;
    }

    /**
     * getter method for points wrote on the card
     * @return points
     */
    public int getPoints() {
        return 0;
    }
}
