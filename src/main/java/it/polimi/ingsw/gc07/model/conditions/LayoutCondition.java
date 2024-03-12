package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.enumerations.ConditionType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;

/**
 * Condition regarding cards placement on the game field and their color,
 * i.e. the permanent resource on the back of GoldCards and ResourceCards.
 */
public class LayoutCondition extends Condition{
    /**
     * Matrix of dimension 3x3, the biggest dimension for a layout condition
     * that can be found on playing cards.
     * Each cell contains true if a card needs to be found in that position,
     * false otherwise.
     */
    private final boolean[][] cardsPosition;
    /**
     * Matrix of dimension 3x3, the biggest dimension for a layout condition
     * that can be found on playing cards.
     * Each cell contains the GameResource (corresponding to a color) that needs
     * to be found in that cell, null if the cell needs to be empty.
     */
    private final GameResource[][] cardsColor;

    /**
     * Constructor for layout conditions.
     * @param conditionType condition type, must be LAYOUT_CONDITION
     * @param cardsPosition position of the card
     * @param cardsColor resource (= color) of the card
     */
    public LayoutCondition(ConditionType conditionType, boolean[][] cardsPosition, GameResource[][] cardsColor) {
        super(conditionType);
        boolean[][] cardsPositionCopy = new boolean[3][3];
        GameResource[][] cardsColorCopy = new GameResource[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cardsPositionCopy[i][j] = cardsPosition[i][j];
                cardsColorCopy[i][j] = cardsColor[i][j];
            }
        }
        this.cardsPosition = cardsPositionCopy;
        this.cardsColor = cardsColorCopy;
    }

    /**
     * Getter returning a copy of the matrix cardsPosition, showing
     * cards' position in the layout condition.
     * @return copy of the matrix cardsPosition
     */
    public boolean[][] getCardsPosition() {
        boolean[][] cardsPositionCopy = new boolean[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cardsPositionCopy[i][j] = this.cardsPosition[i][j];
            }
        }
        return cardsPositionCopy;
    }

    /**
     * Getter method returning a copy of cardColor, the matrix showing
     * the color / resource that needs to be found in every position.
     * @return copy of matrix cardsColor
     */
    public GameResource[][] getCardsColor() {
        GameResource[][] cardsColorCopy = new GameResource[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cardsColorCopy[i][j] = cardsColor[i][j];
            }
        }
        return cardsColorCopy;
    }

    /**
     * Implementation of numTimesMet.
     * Counts how many times the layout is found in the gameField.
     * @return number of times the layout is found
     */
    public int numTimesMet(GameField gameField) {
        // TODO implementare
        // Attenzione! starter card non può essere usata (?)
        return 0;
    }
}
