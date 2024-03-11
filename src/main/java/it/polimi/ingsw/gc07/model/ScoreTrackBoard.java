package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.NonValidScoreExcpetion;
import it.polimi.ingsw.gc07.exceptions.PlayerAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentExcpetion;

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
        // Per ora in Game
    }

    /**
     * Method that allows to insert a new Player to the ScoreTrackBoard,
     * initializing it's score to 0.
     * @param player player to add
     */
    public void addPlayer(Player player) throws PlayerAlreadyPresentException {
        if (playersScore.containsKey(player)) {
            throw new PlayerAlreadyPresentException();
        }
        playersScore.put(player, 0);
    }

    /**
     * Setter method, allows to set a certain score for a player.
     * @param player player
     * @param newScore new score to set
     */
    public void setScore(Player player, int newScore) throws PlayerNotPresentExcpetion, NonValidScoreExcpetion {
        if(newScore < 0 || newScore > 29){
            throw new NonValidScoreExcpetion();
        }
        if(!playersScore.containsKey(player)){
            throw new PlayerNotPresentExcpetion();
        }
        this.playersScore.put(player, newScore);
    }

    /**
     * Getter method for the score, allows to know the current score of a player.
     * @param player player
     * @return current for the player
     */
    public int getScore(Player player) throws PlayerNotPresentExcpetion{
        if(!playersScore.containsKey(player)){
            throw new PlayerNotPresentExcpetion();
        }
        return playersScore.get(player);
    }
}
