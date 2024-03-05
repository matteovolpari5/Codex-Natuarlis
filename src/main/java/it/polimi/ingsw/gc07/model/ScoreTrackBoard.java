package it.polimi.ingsw.gc07.model;

public class ScoreTrackBoard {
    private int scorePlayer1;
    private int scorePlayer2;
    private int scorePlayer3;
    private int scorePlayer4;

    public ScoreTrackBoard() {
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
        this.scorePlayer3 = 0;
        this.scorePlayer4 = 0;
    }
    public void updateScore(Player player, PlaceableCard pCard){}
    public int computeFinalScore(Player player){}
    public int getCurrentScore(Player player){}
}
