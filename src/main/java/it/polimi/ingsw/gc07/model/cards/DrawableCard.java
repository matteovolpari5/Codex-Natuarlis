package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.exceptions.PlacementResult;
import it.polimi.ingsw.gc07.exceptions.WrongPlayerException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

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
    // Regular constructor
    public DrawableCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent,
                        boolean[] backCorners, GameItem[] backCornersContent, int placementScore,
                        List<GameResource> permanentResources) {
        super(cardID, cardType, frontCorners, frontCornersContent, backCorners, backCornersContent, permanentResources);
        this.placementScore = placementScore;
    }
    public int getScore(){
        return placementScore;
    }


    // TODO
    // usare i getter anzichè accedere direttamente ai campi
    // aggiungere il controllore fronte / retro

    @Override
    public PlacementResult isPlaceable(GameField gameField, int x, int y, boolean way) {
        boolean found = false;
        int dim = GameField.getDim();
        if(x < 0 || x >= dim || y <0 || y >= dim) {
            return PlacementResult.INDEXES_OUT_OF_GAME_FIELD;
        }
        try{
            if(gameField.isCardPresent(x,y)){
                return PlacementResult.CARD_ALREADY_PRESENT;
            }
            //checking if there is at least one available corner near (x,y)
            if(x - 1 >= 0){
                if(gameField.isCardPresent(x-1, y)){
                    //the card would cover two corners of the card above it
                    return PlacementResult.MULTIPLE_CORNERS_COVERED;
                }
                if(y - 1 >= 0){
                    if(gameField.isCardPresent(x,y-1)){
                        //the card would cover two corners of the card on its left
                        return PlacementResult.MULTIPLE_CORNERS_COVERED;
                    }
                    if(gameField.isCardPresent(x-1,y-1)){
                        //a placed card is present
                        found = true;
                        if(gameField.getCardWay(x-1,y-1) == false){
                            //the placed card is face up
                            if(gameField.getPlacedCard(x-1, y-1).getFrontCorners()[2] == false){
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }else{
                            //the placed card is face down
                            if(gameField.getPlacedCard(x-1, y-1).getBackCorners()[2] == false){
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                        if(x + 1 <= dim - 1){
                            if(gameField.isCardPresent(x+1,y-1)){
                                if(gameField.getCardWay(x+1,y-1) == false){
                                    if(gameField.getPlacedCard(x+1,y-1).getFrontCorners()[1] == false){
                                        //the card to be placed covers an unavailable corner of another card
                                        return PlacementResult.NOT_LEGIT_CORNER;
                                    }
                                }else{
                                    if(gameField.getPlacedCard(x+1, y-1).getBackCorners()[1] == false){
                                        //the needed corner is not available
                                        return PlacementResult.NOT_LEGIT_CORNER;
                                    }
                                }
                            }
                        }
                    }//the placed card is not present
                }
                if(y + 1 <= dim - 1){
                    if(gameField.isCardPresent(x,y+1)){
                        //the card would cover two corners of the card on its right
                        return PlacementResult.MULTIPLE_CORNERS_COVERED;
                    }
                    if(gameField.isCardPresent(x-1,y+1)){
                        //a placed card is present
                        found = true;
                        if(gameField.getCardWay(x-1,y+1) == false){
                            //the placed card is face up
                            if(gameField.getPlacedCard(x-1,y+1).getFrontCorners()[3] == false){
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }else{
                            //the placed card is face down
                            if(gameField.getPlacedCard(x-1, y+1).getBackCorners()[3] == false){
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                        if(x + 1 <= dim - 1){
                            if(gameField.isCardPresent(x+1,y+1)){
                                if(gameField.getCardWay(x+1,y+1) == false){
                                    if(gameField.getPlacedCard(x+1,y+1).getFrontCorners()[0] == false){
                                        //the card to be placed covers an unavailable corner of another card
                                        return PlacementResult.NOT_LEGIT_CORNER;
                                    }
                                }else{
                                    if(gameField.getPlacedCard(x+1, y+1).getBackCorners()[0] == false){
                                        //the needed corner is not available
                                        return PlacementResult.NOT_LEGIT_CORNER;
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
                    return PlacementResult.MULTIPLE_CORNERS_COVERED;
                }
                if(y - 1 >= 0){
                    if(gameField.isCardPresent(x+1,y-1)){
                        //a placed card is present
                        found = true;
                        if(gameField.getCardWay(x+1,y-1) == false){
                            //the placed card is face up
                            if(gameField.getPlacedCard(x+1,y-1).getFrontCorners()[1] == false){
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }else{
                            //the placed card is face down
                            if(gameField.getPlacedCard(x+1, y-1).getBackCorners()[1] == false){
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                        if(x - 1 >= 0){
                            if(gameField.isCardPresent(x-1,y-1)){
                                if(gameField.getCardWay(x-1,y-1) == false){
                                    if(gameField.getPlacedCard(x-1,y-1).getFrontCorners()[2] == false){
                                        //the card to be placed covers an unavailable corner of another card
                                        return PlacementResult.NOT_LEGIT_CORNER;
                                    }
                                }else{
                                    if(gameField.getPlacedCard(x-1, y-1).getBackCorners()[2] == false){
                                        //the needed corner is not available
                                        return PlacementResult.NOT_LEGIT_CORNER;
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
                        if (gameField.getCardWay(x + 1,y + 1) == false) {
                            //the placed card is face up
                            if (gameField.getPlacedCard(x + 1,y + 1).getFrontCorners()[0] == false) {
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }else{
                            //the placed card is face down
                            if(gameField.getPlacedCard(x+1, y+1).getBackCorners()[0] == false){
                                //the needed corner is not available
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                        if(x - 1 >= 0){
                            if (gameField.isCardPresent(x - 1,y + 1)){
                                if (gameField.getCardWay(x - 1,y + 1) == false){
                                    if (gameField.getPlacedCard(x - 1,y + 1).getFrontCorners()[3] == false){
                                        //the card to be placed covers an unavailable corner of another card
                                        return PlacementResult.NOT_LEGIT_CORNER;
                                    }
                                }else{
                                    if(gameField.getPlacedCard(x-1, y+1).getBackCorners()[3] == false){
                                        //the needed corner is not available
                                        return PlacementResult.NOT_LEGIT_CORNER;
                                    }
                                }
                            }
                        }
                    }//card in position x+1 y+1 is not present
                }
            }
        }
        catch(CardNotPresentException e){
            // for loop not working properly
            e.printStackTrace(); // TODO sostituire eccezione
        }
        if(found == false){
            return PlacementResult.NO_COVERED_CORNER;
        }else{
            return PlacementResult.SUCCESS;
        }
    }

    /**
     * Method that calculates the number of points scored by a card
     * @param gameField: gameField of the player
     * @param x: x index of the matrix
     * @param y: x index of the matrix
     * @return int value that is the points scored by the card.
     * @throws CardNotPresentException
     */
    @Override
    public int getPlacementScore(GameField gameField, int x, int y) {
        //TODO modificare gestione assert/eccezioni
        try{
            if (!gameField.getCardWay(x, y)) {
                return placementScore;
            }
            else{
                return 0;
            }
        }
        catch(CardNotPresentException e){
            return 0;
        }
    }
}
