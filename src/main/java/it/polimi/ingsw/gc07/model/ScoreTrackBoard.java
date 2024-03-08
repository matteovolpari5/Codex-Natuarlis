package it.polimi.ingsw.gc07.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Class representing the ScoreTrackBoard
 */
public class ScoreTrackBoard {
    private Map<Player, Integer> playersScore;

    /**
     * Constructor method for an empty ScoreTrackBoard.
     */
    public ScoreTrackBoard() {
        playersScore = new HashMap<Player, Integer>();
        // TODO: devo popolarla con i Player che non sappiamo ancora dove siano memorizzati
    }

    /**
     * Setter method, allows to set a certain score for a player.
     * @param player player
     * @param newScore new score to set
     */
    public void setScore(Player player, int newScore){
        // check that the Player belongs to the map
        // if it does, change the score
        // TODO: exception try-catch/trhows, sostituire if
        if(playersScore.containsKey(player)){
            playersScore.put(player, newScore);
        }
    }

    /**
     * Getter method for the score, allows to know the current score of a player.
     * @param player player
     * @return current for the player
     */
    public int getScore(Player player){
        // check that the Player belongs to the map
        // return the score
        // TODO: exception try-catch/trhows, sostituire if
        if(playersScore.containsKey(player)){
            return playersScore.get(player);
        }
    }
}
