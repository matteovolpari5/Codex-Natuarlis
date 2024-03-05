package it.polimi.ingsw.gc07.model;

import java.util.HashMap;

public class ScoreTrackBoard {
    private Map<Player, int> playersScore;

    public ScoreTrackBoard() {
        playersScore = new HashMap<>();
        // TODO
        // devo popolarla con i Player che non sappiamo ancora dove siano memorizzati
    }
    public void setScore(Player player, int newScore){
        // check that the Player belongs to the map
        // if it does, change the score
        // TODO exception try-catch/trhows, sostituire if
        if(playersScore.containsKey(player)){
            playersScore.put(player, newScore);
        }
    }
    public int getScore(Player player){
        // check that the Player belongs to the map
        // return the score
        // TODO exception try-catch/trhows, sostituire if
        if(playersScore.containsKey(player)){
            return playersScore.get(player);
        }
    }

}
