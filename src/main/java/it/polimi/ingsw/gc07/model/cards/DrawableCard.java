package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.enumerations.GameResource;

import java.util.List;

/**
 * Class that represents non-starter cards.
 * Non-starter cards are: GoldCards and ResourceCards.
 */
public class DrawableCard extends PlaceableCard {
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
    public DrawableCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent,
                        boolean[] backCorners, GameItem[] backCornersContent, int placementScore,
                        List<GameResource> permanentResources) {
        super(cardID, cardType, frontCorners, frontCornersContent, backCorners, backCornersContent, permanentResources);
        this.placementScore = placementScore;
    }

    /**
     * Method to check if the card is placeable on a certain game field, in a certain position and way.
     * Used on resource cards.
     * @param gameField game field to place the card
     * @param x row
     * @param y column
     * @param way way
     * @return enum representing the placement result
     */
    @Override
    public CommandResult isPlaceable(GameField gameField, int x, int y, boolean way) {
        boolean found = false;
        int dim = GameField.getDim();
        if(x < 0 || x >= dim || y <0 || y >= dim) {
            return CommandResult.INDEXES_OUT_OF_GAME_FIELD;
        }
        if(gameField.isCardPresent(x,y)){
            return CommandResult.CARD_ALREADY_PRESENT;
        }
        //checking if there is at least one available corner near (x,y)
        if(x - 1 >= 0){
            if(gameField.isCardPresent(x-1, y)){
                //the card would cover two corners of the card above it
                return CommandResult.MULTIPLE_CORNERS_COVERED;
            }
            if(y - 1 >= 0){
                if(gameField.isCardPresent(x,y-1)){
                    //the card would cover two corners of the card on its left
                    return CommandResult.MULTIPLE_CORNERS_COVERED;
                }
                if(gameField.isCardPresent(x-1,y-1)){
                    //a placed card is present
                    found = true;
                    if(!gameField.getCardWay(x - 1, y - 1)){
                        //the placed card is face up
                        if(!gameField.getPlacedCard(x - 1, y - 1).getFrontCorners()[2]){
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }else{
                        //the placed card is face down
                        if(!gameField.getPlacedCard(x - 1, y - 1).getBackCorners()[2]){
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }
                    if(x + 1 <= dim - 1){
                        if(gameField.isCardPresent(x+1,y-1)){
                            if(!gameField.getCardWay(x + 1, y - 1)){
                                if(!gameField.getPlacedCard(x + 1, y - 1).getFrontCorners()[1]){
                                    //the card to be placed covers an unavailable corner of another card
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }else{
                                if(!gameField.getPlacedCard(x + 1, y - 1).getBackCorners()[1]){
                                    //the needed corner is not available
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }
                        }
                    }
                }//the placed card is not present
            }
            if(y + 1 <= dim - 1){
                if(gameField.isCardPresent(x,y+1)){
                    //the card would cover two corners of the card on its right
                    return CommandResult.MULTIPLE_CORNERS_COVERED;
                }
                if(gameField.isCardPresent(x-1,y+1)){
                    //a placed card is present
                    found = true;
                    if(!gameField.getCardWay(x - 1, y + 1)){
                        //the placed card is face up
                        if(!gameField.getPlacedCard(x - 1, y + 1).getFrontCorners()[3]){
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }else{
                        //the placed card is face down
                        if(!gameField.getPlacedCard(x - 1, y + 1).getBackCorners()[3]){
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }
                    if(x + 1 <= dim - 1){
                        if(gameField.isCardPresent(x+1,y+1)){
                            if(!gameField.getCardWay(x + 1, y + 1)){
                                if(!gameField.getPlacedCard(x + 1, y + 1).getFrontCorners()[0]){
                                    //the card to be placed covers an unavailable corner of another card
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }else{
                                if(!gameField.getPlacedCard(x + 1, y + 1).getBackCorners()[0]){
                                    //the needed corner is not available
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }
                        }
                    }
                }//card in position x-1 y+1 is not present
            }
        }
        if(x + 1 <= dim - 1){
            if(gameField.isCardPresent(x+1,y)){
                //the card would cover two corners of the card below it
                return CommandResult.MULTIPLE_CORNERS_COVERED;
            }
            if(y - 1 >= 0){

                if(gameField.isCardPresent(x+1,y-1)){
                    //a placed card is present
                    found = true;
                    if(!gameField.getCardWay(x + 1, y - 1)){
                        //the placed card is face up
                        if(!gameField.getPlacedCard(x + 1, y - 1).getFrontCorners()[1]){
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }else{
                        //the placed card is face down
                        if(!gameField.getPlacedCard(x + 1, y - 1).getBackCorners()[1]){
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }
                    if(x - 1 >= 0){
                        if(gameField.isCardPresent(x-1,y-1)){
                            if(!gameField.getCardWay(x - 1, y - 1)){
                                if(!gameField.getPlacedCard(x - 1, y - 1).getFrontCorners()[2]){
                                    //the card to be placed covers an unavailable corner of another card
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }else{
                                if(!gameField.getPlacedCard(x - 1, y - 1).getBackCorners()[2]){
                                    //the needed corner is not available
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }
                        }
                    }
                }//card in position x+1 y-1 is not present
            }
            if(y + 1 <= dim - 1) {
                if (gameField.isCardPresent(x + 1,y + 1)) {
                    //a placed card is present
                    found = true;
                    if (!gameField.getCardWay(x + 1, y + 1)) {
                        //the placed card is face up
                        if (!gameField.getPlacedCard(x + 1, y + 1).getFrontCorners()[0]) {
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }else{
                        //the placed card is face down
                        if(!gameField.getPlacedCard(x + 1, y + 1).getBackCorners()[0]){
                            //the needed corner is not available
                            return CommandResult.NOT_LEGIT_CORNER;
                        }
                    }
                    if(x - 1 >= 0){
                        if (gameField.isCardPresent(x - 1,y + 1)){
                            if (!gameField.getCardWay(x - 1, y + 1)){
                                if (!gameField.getPlacedCard(x - 1, y + 1).getFrontCorners()[3]){
                                    //the card to be placed covers an unavailable corner of another card
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }else{
                                if(!gameField.getPlacedCard(x - 1, y + 1).getBackCorners()[3]){
                                    //the needed corner is not available
                                    return CommandResult.NOT_LEGIT_CORNER;
                                }
                            }
                        }
                    }
                }//card in position x+1 y+1 is not present
            }
        }
        if(!found){
            return CommandResult.NO_COVERED_CORNER;
        }else{
            return CommandResult.SUCCESS;
        }
    }

    /**
     * Method that calculates the number of points scored by a card.
     * Used on resource cards.
     * @param gameField: gameField of the player
     * @param x: x index of the matrix
     * @param y: x index of the matrix
     * @return points scored by the card
     */
    @Override
    public int getPlacementScore(GameField gameField, int x, int y) {
        if(gameField.isCardPresent(x,y) && !gameField.getCardWay(x,y)) {
            return placementScore;
        }
        return 0;
    }

    //METHOD USED TO HELP PRINT IN Tui VIEW

    /**
     * getter method for points wrote on the card
     * @return points
     */
    @Override
    public int getPoints()
    {
        return placementScore;
    }
}
