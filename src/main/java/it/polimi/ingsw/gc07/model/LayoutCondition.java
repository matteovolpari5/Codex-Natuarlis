package it.polimi.ingsw.gc07.model;

public class LayoutCondition extends Condition{
    private boolean[][] cardsPosition;
    private Resource[][] cardsColor;
    // TODO: Resource avrà quel costruttore?
    public LayoutCondition(ConditionType conditionType, boolean[][] cardsPosition, Resource[][] cardsColor) {
        super(conditionType);
        boolean[][] cardsPositionCopy = new boolean[3][3];
        Resource[][] cardsColorCopy = new Resource[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cardsPositionCopy[i][j] = cardsPosition[i][j];
                cardsColorCopy[i][j] = new Resource(CardsColor[i][j]);
            }
        }
        this.cardPosition = cardPositionCopy;
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
    public Resource[][] getCardsColor() {
        Resource[][] cardsColorCopy = new Resource[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cardsColorCopy[i][j] = new Resource(CardsColor[i][j]);
            }
        }
        return cardsColorCopy;
    }
}
