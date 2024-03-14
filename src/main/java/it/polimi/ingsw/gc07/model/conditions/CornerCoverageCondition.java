package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.exceptions.CardAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.ConditionType;

public class CornerCoverageCondition extends Condition {
    /**
     * Constructor of class CornerCoverageCondtion.
     * @param type type of the condition
     */
    public CornerCoverageCondition(ConditionType type) {
        super(type);
    }

    /**
     * Counts the number of corners covered by the card in position
     * (x,y) at the moment of placement.
     * Combinations to check (if not on a border):
     * (x+1, y+1) - corner 3
     * (x+1, y-1) - corner 0
     * (x-1, y+1) - corner 2
     * (x-1, y-1) - corner 1
     * Checks if a card is present in these positions.
     * If it is present, checks, if it has a certain corner.
     * @return number of times the list of items is found
     */
    public int numTimesMet(GameField gameField) throws NullPointerException {
        // check valid game field
        if(gameField == null){
            throw new NullPointerException();
        }
        int dim = gameField.getDim();
        // compute x and y
        int x = 0;
        int y = 0;
        int lastCard = 0;
        int[][] cardsOrder = gameField.getCardsOrder();
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                if(cardsOrder[i][j] > lastCard){
                    lastCard = cardsOrder[i][j];
                    x = i;
                    y = j;
                }
            }
        }
        int numTimes = 0;

        // check position (x+1, y+1)
        if(x < dim-1 && y < dim-1) {
            if(gameField.isCardPresent(x+1, y+1)) {
                if(!gameField.getCardWay(x+1, y+1)){
                    // card placed face up
                    try{
                        if(gameField.getPlacedCard(x+1, y+1).getFrontCorners()[3]){
                            numTimes++;
                        }
                    }
                    catch(CardNotPresentException e){
                        // I have checked before!
                        e.printStackTrace();
                    }
                }
                else{
                    // card placed face down

                }
            }
        }

        // check position (x+1, y-1)
        if(x < dim-1 && y > 0) {
            if(gameField.isCardPresent(x+1, y-1)) {
                try{
                    // TODO: sbagliato
                    // se è sul front, uso getFrontCorners
                    // se è sul back no!
                    if(gameField.getPlacedCard(x+1, y-1).getFrontCorners()[0]){
                        numTimes++;
                    }
                }
                catch(CardNotPresentException e){
                    // I should have checked before!
                    e.printStackTrace();
                }
            }
        }

        // check position (x-1, y+1)
        if(x > 0 && y < dim-1) {
            if(gameField.isCardPresent(x-1, y+1)) {
                try{
                    // TODO: sbagliato
                    // se è sul front, uso getFrontCorners
                    // se è sul back no!
                    if(gameField.getPlacedCard(x-1, y+1).getFrontCorners()[2]){
                        numTimes++;
                    }
                }
                catch(CardNotPresentException e){
                    // I should have checked before!
                    e.printStackTrace();
                }
            }
        }

        // check position (x-1, y-1)
        if(x > 0 && y > 0) {
            if(gameField.isCardPresent(x-1, y-1)) {
                try{
                    // TODO: sbagliato
                    // se è sul front, uso getFrontCorners
                    // se è sul back no!
                    if(gameField.getPlacedCard(x-1, y-1).getFrontCorners()[1]){
                        numTimes++;
                    }
                }
                catch(CardNotPresentException e){
                    // I should have checked before!
                    e.printStackTrace();
                }
            }
        }
        return numTimes;
    }
}
