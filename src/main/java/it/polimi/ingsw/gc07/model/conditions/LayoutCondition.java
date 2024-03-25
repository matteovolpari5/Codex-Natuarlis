package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

/**
 * Condition regarding cards placement on the game field and their color,
 * i.e. the permanent resource on the back of GoldCards and ResourceCards.
 */
public class LayoutCondition implements Condition{
    /**
     * Matrix of dimension 4x3, the biggest dimension for a layout condition
     * that can be found on playing cards.
     * Each cell contains the GameResource (corresponding to a color) that needs
     * to be found in that cell, null if the cell can contain anything.
     */
    private final GameResource[][] cardsColor;

    private static final int maxLayoutRows = 4;
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

    public static int getRows(){
        return maxLayoutRows;
    }

    public static int getColumns(){
        return maxLayoutColumns;
    }

    // TODO probabilmente da eliminares
    /**
     * Getter method returning a copy of cardColor, the matrix showing
     * the color / resource that needs to be found in every position.
     * @return copy of matrix cardsColor
     */
    /*
    public GameResource[][] getCardsColor() {
        GameResource[][] cardsColorCopy = new GameResource[maxLayoutRows][maxLayoutColumns];
        for(int i = 0; i < maxLayoutRows; i++){
            for(int j = 0; j < maxLayoutColumns; j++){
                cardsColorCopy[i][j] = cardsColor[i][j];
            }
        }
        return cardsColorCopy;
    }
     */

    /**
     * Counts how many times the layout is found in the gameField.
     * @return number of times the layout is found
     */
    public int numTimesMet(GameField gameField) throws NullPointerException {
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
                            try{
                                // a card must be present
                                // it mustn't be a starter card, it can be a resource or gold card
                                // it must have the correct color (i.e. GameResource)
                                if(!(gameField.isCardPresent(i+h,j+k)
                                        && !gameField.getPlacedCard(i+h,j+k).getType().equals(CardType.STARTER_CARD)
                                        && gameField.getPlacedCard(i+h,j+k).getPermanentResources().getFirst().equals(cardsColor[h][k])
                                )) {
                                    // mismatch
                                    flag = false;
                                }
                            }
                            catch (CardNotPresentException e){
                                // I already checked
                                e.printStackTrace();
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
                                try{
                                    gameField.removePlacedCard(i+h,j+k);
                                }
                                catch(CardNotPresentException e){
                                    // I already checked
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }

        return numTimes;
    }
}
