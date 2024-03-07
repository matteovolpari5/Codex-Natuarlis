package it.polimi.ingsw.gc07.model;

public class LayoutCondition extends Condition{
    private boolean[][] cardsPosition;
    private GameResource[][] cardsColor;
    // TODO: Resource avrà quel costruttore?
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
    public boolean[][] getCardsPosition() {
        boolean[][] cardsPositionCopy = new boolean[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cardsPositionCopy[i][j] = this.cardsPosition[i][j];
            }
        }
        return cardsPositionCopy;
    }
    // TODO: Resource avrà quel costruttore?
    public GameResource[][] getCardsColor() {
        GameResource[][] cardsColorCopy = new GameResource[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cardsColorCopy[i][j] = cardsColor[i][j];
            }
        }
        return cardsColorCopy;
    }
}
