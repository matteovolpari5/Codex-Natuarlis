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
        if(x < 0 || x >= dim || y <0 || y >= dim) {
            return PlacementResult.INDEXES_OUT_OF_GAME_FIELD;
        }
        if(cardsContent[x][y] != null){
            return PlacementResult.CARD_ALREADY_PRESENT;
        }
        //checking if there is at least one available corner near (x,y)
        if(x-1 >= 0){
            if(cardsContent[x-1][y] != null){
                //the card would cover two corners of the card above it
                return PlacementResult.MULTIPLE_CORNERS_COVERED;
            }
            if(y-1 >= 0){
                if(cardsContent[x][y-1] != null){
                    //the card would cover two corners of the card on its left
                    return PlacementResult.MULTIPLE_CORNERS_COVERED;
                }
                if(cardsContent[x-1][y-1] != null){
                    //a placed card is present
                    found = true;
                    if(cardsFace[x-1][y-1] == false){
                        //the placed card is face up
                        if(cardsContent[x-1][y-1].getFrontCorners()[2] == false){
                            //the needed corner is not available
                            return PlacementResult.NOT_LEGIT_CORNER;
                        }
                    }//the placed card is face down, the corner needed is available for sure
                }//the placed card is not present
                if(x+1 <= 80){
                    if(cardsContent[x+1][y-1] != null){
                        if(cardsFace[x+1][y-1] == false){
                            if(cardsContent[x+1][y-1].getFrontCorners()[1] == false){
                                //the card to be placed covers an unavailable corner of another card
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                    }
                }
            }
            if(y+1 <= 80){
                if(cardsContent[x][y+1] != null){
                    //the card would cover two corners of the card on its right
                    return PlacementResult.MULTIPLE_CORNERS_COVERED;
                }
                if(cardsContent[x-1][y+1] != null){
                    //a placed card is present
                    found = true;
                    if(cardsFace[x-1][y+1] == false){
                        //the placed card is face up
                        if(cardsContent[x-1][y+1].getFrontCorners()[3] == false){
                            //the needed corner is not available
                            return PlacementResult.NOT_LEGIT_CORNER;
                        }
                    }//the placed card is face down, the corner needed is available for sure
                }//card in position x-1 y+1 is not present
                if(x+1 <= 80){
                    if(cardsContent[x+1][y+1] != null){
                        if(cardsFace[x+1][y+1] == false){
                            if(cardsContent[x+1][y+1].getFrontCorners()[0] == false){
                                //the card to be placed covers an unavailable corner of another card
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                    }
                }
            }
        }
        if(x+1 <= 80){
            if(cardsContent[x+1][y] != null){
                //the card would cover two corners of the card below it
                return PlacementResult.MULTIPLE_CORNERS_COVERED;
            }
            if(y-1 >= 0){
                if(cardsContent[x+1][y-1] != null){
                    //a placed card is present
                    found = true;
                    if(cardsFace[x+1][y-1] == false){
                        //the placed card is face up
                        if(cardsContent[x+1][y-1].getFrontCorners()[1] == false){
                            //the needed corner is not available
                            return PlacementResult.NOT_LEGIT_CORNER;
                        }
                    }//the placed card is face down, the corner needed is available for sure
                }//card in position x+1 y-1 is not present
                if(x-1 >= 0){
                    if(cardsContent[x-1][y-1] != null){
                        if(cardsFace[x-1][y-1] == false){
                            if(cardsContent[x-1][y-1].getFrontCorners()[2] == false){
                                //the card to be placed covers an unavailable corner of another card
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                    }
                }
            }
            if(y+1 <= 80){
                if(cardsContent[x+1][y+1] != null){
                    //a placed card is present
                    found = true;
                    if(cardsFace[x+1][y+1] == false){
                        //the placed card is face up
                        if(cardsContent[x+1][y-1].getFrontCorners()[0] == false){
                            //the needed corner is not available
                            return PlacementResult.NOT_LEGIT_CORNER;
                        }
                    }//the placed card is face down, the corner needed is available for sure
                }//card in position x+1 y+1 is not present
                if(x-1 >= 0){
                    if(cardsContent[x-1][y+1] != null){
                        if(cardsFace[x-1][y+1] == false){
                            if(cardsContent[x-1][y+1].getFrontCorners()[3] == false){
                                //the card to be placed covers an unavailable corner of another card
                                return PlacementResult.NOT_LEGIT_CORNER;
                            }
                        }
                    }
                }
            }
        }
        if(found == false){
            return PlacementResult.NO_COVERED_CORNER;
        }else{
            return PlacementResult.SUCCESS;
        }
    }

    /**
     * Method that calculates the number of points scored by a card
     * @param gameField
     * @param x
     * @param y
     * @return
     * @throws CardNotPresentException
     */
    @Override
    public int computePoints(GameField gameField, int x, int y) {
        //TODO modificare gestione assert/eccezioni
        if (!gameField.getCardWay(x, y)) {
            return placementScore;
        }
        else{
            return 0;
        }
    }
}
