package it.polimi.ingsw.gc07.model;

/**
 * Class representing the game field of a single player,
 * it contains all the cards the player has placed,
 * including both their position and the way cards are placed.
 */
public class GameField {
    /**
     * Matrix of dimension 80x80, the biggest possible dimension
     * for a player's game field, containing boolean values.
     * The cell contains true if the game field contains a card in
     * that position, false otherwise.
     */
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

    public void placeCard(PlaceableCard card, int x, int y, boolean way) {
        // Nota: se Card e tutte le sue sottoclassi sono immutabili,
        // posso non creare una copia di card
        // Altrimenti inserire una copia
    }
    public boolean isCardPresent(int x, int y){}
    public PlaceableCard getPlacedCard(int x, int y){
        // Nota: se Card e tutte le sue sottoclassi sono immutabili,
        // posso restituire direttamente la carta
        // Altrimenti restituire una copia
    }
    public boolean getCardWay(int x, int y){}
}
