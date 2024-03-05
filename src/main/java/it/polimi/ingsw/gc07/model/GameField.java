package it.polimi.ingsw.gc07.model;

public class GameField {
    private boolean[][] gameFieldCardsPosition;
    private PlaceableCard[][] gameFieldCardsContent;
    private boolean[][] gameFieldCardsFace;

    public GameField() {
        this.gameFieldCardsPosition = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.gameFieldCardsPosition[i][j] = false;
            }
        }
        this.gameFieldCardsContent = new PlaceableCard[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.gameFieldCardsContent[i][j] = null;
            }
        }
        this.gameFieldCardsFace = new boolean[80][80];
        for(int i=0; i <80; i++){
            for(int j=0; j<80; j++){
                this.gameFieldCardsFace[i][j] = false;
            }
        }
    }

    public void setCard(PlaceableCard card, int x, int y, boolean way){}
    public boolean isCardPresent(int x, int y);
    public PlaceableCard getPlaceableCard(int x, int y);
    public boolean getCardWay(int x, int y);
}
