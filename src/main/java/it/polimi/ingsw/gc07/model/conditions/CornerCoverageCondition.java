package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.exceptions.CardAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.ConditionType;

/**
 * Condition of corner coverage.
 */
public class CornerCoverageCondition extends Condition {
    /**
     * Constructor of class CornerCoverageCondtion.
     * @param type type of the condition
     */
    public CornerCoverageCondition(ConditionType type) {
        super(type);
    }

    /**
     * This method must be called after placement of the card!
     * Counts the number of corners covered by the card in position (x,y) at the moment of placement,
     * the last card which has been placed.
     * Combinations to check (if not on a border):
     * (x+1, y+1) - corner 3
     * (x+1, y-1) - corner 0
     * (x-1, y+1) - corner 2
     * (x-1, y-1) - corner 1
     * Checks if a card is present in these positions.
     * If it is present, checks the way it is placed and if it has a certain corner.
     * @param gameField game field on which the condition has to be verified
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
            if (gameField.isCardPresent(x + 1, y + 1)) {
                try {
                    if (!gameField.getCardWay(x + 1, y + 1)) {
                        // card placed face up
                        if (gameField.getPlacedCard(x + 1, y + 1).getFrontCorners()[3]) {
                            numTimes++;
                        }
                    }
                    else {
                        // card placed face down
                        if(gameField.getPlacedCard(x + 1, y + 1).getBackCorners() == null){
                            // NonStarterCard
                            numTimes++;
                        }
                        else {
                            // StarterCard
                            if (gameField.getPlacedCard(x + 1, y + 1).getBackCorners()[3]) {
                                numTimes++;
                            }
                        }
                    }
                } catch (CardNotPresentException e) {
                    // I have checked before!
                    e.printStackTrace();
                }
            }
        }

        // check position (x+1, y-1)
        if(x < dim-1 && y > 0) {
            if (gameField.isCardPresent(x+1, y-1)) {
                try {
                    if (!gameField.getCardWay(x + 1, y - 1)) {
                        // card placed face up
                        if(gameField.getPlacedCard(x+1, y-1).getFrontCorners()[0]){
                            numTimes++;
                        }
                    }
                    else {
                        // card placed face down
                        if(gameField.getPlacedCard(x + 1, y - 1).getBackCorners() == null){
                            // NonStarterCard
                            numTimes++;
                        }
                        else {
                            // StarterCard
                            if (gameField.getPlacedCard(x + 1, y - 1).getBackCorners()[0]) {
                                numTimes++;
                            }
                        }
                    }
                } catch (CardNotPresentException e) {
                    // I have checked before!
                    e.printStackTrace();
                }
            }
        }

        // check position (x-1, y+1)
        if(x > 0 && y < dim-1) {
            if (gameField.isCardPresent(x - 1, y + 1)) {
                try {
                    if (!gameField.getCardWay(x - 1, y + 1)) {
                        // card placed face up
                        if (gameField.getPlacedCard(x - 1, y + 1).getFrontCorners()[2]) {
                            numTimes++;
                        }
                    }
                    else {
                        // card placed face down
                        if(gameField.getPlacedCard(x - 1, y + 1).getBackCorners() == null){
                            // NonStarterCard
                            numTimes++;
                        }
                        else {
                            // StarterCard
                            if (gameField.getPlacedCard(x - 1, y + 1).getBackCorners()[2]) {
                                numTimes++;
                            }
                        }
                    }
                } catch (CardNotPresentException e) {
                    // I have checked before!
                    e.printStackTrace();
                }
            }
        }

        // check position (x-1, y-1)
        if(x > 0 && y > 0) {
            if (gameField.isCardPresent(x - 1, y - 1)) {
                try {
                    if (!gameField.getCardWay(x - 1, y - 1)) {
                        // card placed face up
                        if (gameField.getPlacedCard(x - 1, y - 1).getFrontCorners()[1]) {
                            numTimes++;
                        }
                    }
                    else {
                        // card placed face down
                        if(gameField.getPlacedCard(x - 1, y - 1).getBackCorners() == null){
                            // NonStarterCard
                            numTimes++;
                        }
                        else {
                            // StarterCard
                            if (gameField.getPlacedCard(x - 1, y - 1).getBackCorners()[1]) {
                                numTimes++;
                            }
                        }
                    }
                } catch (CardNotPresentException e) {
                    // I have checked before!
                    e.printStackTrace();
                }
            }
        }
        return numTimes;
    }
}
