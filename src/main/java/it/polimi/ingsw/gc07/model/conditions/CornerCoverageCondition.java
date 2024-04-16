package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.model.GameField;

/**
 * Condition of corner coverage.
 */
public class CornerCoverageCondition implements Condition {
    /**
     * This method must be called after placement of the card!
     * Counts the number of corners covered by the last card which has been placed.
     * @param gameField game field on which the condition has to be verified
     * @return number of times the list of items is found
     */
    @Override
    public int numTimesMet(GameField gameField) {
        // check valid game field
        assert(gameField != null) : "No GameField passed as parameter";
        int dim = GameField.getDim();
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
                numTimes++;
            }
        }

        // check position (x+1, y-1)
        if(x < dim-1 && y > 0) {
            if (gameField.isCardPresent(x+1, y-1)) {
                numTimes++;
            }
        }

        // check position (x-1, y+1)
        if(x > 0 && y < dim-1) {
            if (gameField.isCardPresent(x - 1, y + 1)) {
                numTimes++;
            }
        }

        // check position (x-1, y-1)
        if(x > 0 && y > 0) {
            if (gameField.isCardPresent(x - 1, y - 1)) {
                numTimes++;
            }
        }

        return numTimes;
    }
}
