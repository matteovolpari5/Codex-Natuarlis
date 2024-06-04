package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.CardType;
import it.polimi.ingsw.gc07.model.GameResource;

/**
 * Condition regarding cards placement on the game field and their color,
 * i.e. the permanent resource on the back of GoldCards and ResourceCards.
 */
public class LayoutCondition implements Condition {
    /**
     * Matrix of dimension 4x3, the biggest dimension for a layout condition
     * that can be found on playing cards.
     * Each cell contains the GameResource (corresponding to a color) that needs
     * to be found in that cell, null if the cell can contain anything.
     */
    private final GameResource[][] cardsColor;
    /**
     * Maximum number of rows of a layout.
     */
    private static final int maxLayoutRows = 4;
    /**
     * Maximum number of columns of a layout.
     */
    private static final int maxLayoutColumns = 3;

    /**
     * Constructor for layout conditions.
     * @param cardsColor resource (= color) of the card
     */
    public LayoutCondition(GameResource[][] cardsColor) {
        GameResource[][] cardsColorCopy = new GameResource[maxLayoutRows][maxLayoutColumns];
        for(int i = 0; i < maxLayoutRows; i++){
            for(int j = 0; j < maxLayoutColumns; j++){
                cardsColorCopy[i][j] = cardsColor[i][j];
            }
        }
        this.cardsColor = cardsColorCopy;
    }

    /**
     * Getter method for the maximum number of rows of a layout.
     * @return maximum number of rows of a layout
     */
    public static int getRows(){
        return maxLayoutRows;
    }

    /**
     * Getter method for the maximum number of columns of a layout.
     * @return maximum number of columns of a layout
     */
    public static int getColumns(){
        return maxLayoutColumns;
    }

    /**
     * Counts how many times the layout is found in the gameField.
     * @return number of times the layout is found
     */
    @Override
    public int numTimesMet(GameField gameField) throws NullPointerException {
        // create a copy, since the method modifies it
        GameField myGamefield = new GameField(gameField);

        // check valid game field
        assert(gameField != null): "No GameField passed as parameter";
        int dim = GameField.getDim();

        // find actual layoutRows number
        int layoutRows = maxLayoutRows - 1;
        for(int j = 0; j < maxLayoutColumns; j++){
            if(cardsColor[maxLayoutRows-1][j] != null){
                layoutRows = maxLayoutRows;
            }
        }
        // find actual layoutColumns number
        int layoutColumns = maxLayoutColumns - 1;
        for(int i = 0; i < maxLayoutRows; i++){
            if(cardsColor[i][maxLayoutColumns - 1] != null){
                layoutColumns = maxLayoutColumns;
            }
        }

        int numTimes = 0;
        for(int i = 0; i < dim-layoutRows+1; i++){
            for(int j = 0; j < dim-layoutColumns+1; j++){
                // for every top left cell of the layout
                boolean flag = true;
                for(int h = 0; h < layoutRows; h++){
                    for(int k = 0; k < layoutColumns; k++){
                        if(cardsColor[h][k] != null){
                            // specific card needed
                            // a card must be present
                            // it mustn't be a starter card, it can be a resource or gold card
                            // it must have the correct color (i.e. GameResource)
                            if(     (!myGamefield.isCardPresent(i+h,j+k)) ||
                                    (myGamefield.isCardPresent(i+h,j+k) && myGamefield.getPlacedCard(i+h,j+k).getType().equals(CardType.STARTER_CARD)) ||
                                    (myGamefield.isCardPresent(i+h,j+k) && !myGamefield.getPlacedCard(i+h,j+k).getPermanentResources().getFirst().equals(cardsColor[h][k]))
                            ) {
                                // mismatch
                                flag = false;
                            }
                        }
                    }
                }
                if(flag){
                    // layout found
                    numTimes++;
                    // remove cards used for the objective (a card can be used once)
                    for(int h = 0; h < layoutRows; h++) {
                        for (int k = 0; k < layoutColumns; k++) {
                            if(cardsColor[h][k] != null){
                                assert(myGamefield.isCardPresent(i+h, j+k)): "The card must be present, the pattern was found";
                                myGamefield.removePlacedCard(i+h,j+k);
                            }
                        }
                    }
                }
            }
        }

        return numTimes;
    }

    /**
     * Getter method used to reduce the matrix 4x3 to a matrix 3x3, used to print the layout
     * @return the layout of the condition
     */
    public GameResource[][] getCardsColor() {
        GameResource[][] newCardsColor = new GameResource[3][3];
        int row = 0;
        for (int j = 0; j < 3; j++) {
            if (cardsColor[0][j] != null) {
                row = 1;
            }
        }
        if (row == 1) {
            for (int j = 0; j < 3; j++) {
                if (cardsColor[1][j] != null) {
                    row = 2;
                }
            }
        }
        if(row == 2){
            for (int j = 0; j < 3; j++) {
                if (cardsColor[2][j] != null) {
                    row = 3;
                }
            }
        }
        int iapp=0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 3; j++){
                if(i!=row){
                    newCardsColor[iapp][j] = cardsColor[i][j];
                }
            }
            if(i!=row){
                iapp++;
            }
        }
        return newCardsColor;
    }

}
