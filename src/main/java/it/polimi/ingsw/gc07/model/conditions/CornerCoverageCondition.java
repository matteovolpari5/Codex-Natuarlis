package it.polimi.ingsw.gc07.model.conditions;

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
     * @param x position of the card (x)
     * @param y position of the card (y)
     */
    public int numTimesMet(GameField gameField, int x, int y) throws NullPointerException, IndexOutOfBoundsException {
        // check valid game field
        if(gameField == null){
            throw new NullPointerException();
        }
        int dim = gameField.getDim();
        // check valid indexes
        if(x < 0 || x >= dim || y < 0 || y >= dim) {
            throw new IndexOutOfBoundsException();
        }
        int numTimes = 0;

        // check position (x+1, y+1)
        if(x < dim-1 && y < dim-1) {
            if(gameField.isCardPresent(x+1, y+1)) {
                try{
                    if(gameField.getPlacedCard(x+1, y+1).getFrontCorners()[3]){
                        numTimes++;
                    }
                }
                catch(CardNotPresentException e){
                    // I should have checked before!
                    e.printStackTrace();
                }
            }
        }

        // check position (x+1, y-1)
        if(x < dim-1 && y > 0) {
            if(gameField.isCardPresent(x+1, y-1)) {
                try{
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
