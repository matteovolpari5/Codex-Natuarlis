package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.PlayerAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;

import java.util.Map;
import java.util.HashMap;

/**
 * Class representing the ScoreTrackBoard
 */
public class ScoreTrackBoard {
    /**
     * Map that link each player with his personal score.
     */
    private Map<String, Integer> playersScore;

    /**
     * Constructor method for an empty ScoreTrackBoard.
     */
    public ScoreTrackBoard() {
        playersScore = new HashMap<>();
    }

    /**
     * Method that allows to insert a new Player to the ScoreTrackBoard,
     * initializing it's score to 0.
     * @param nickname player to add
     */
    public void addPlayer(String nickname) throws PlayerAlreadyPresentException {
       if(playersScore.containsKey(nickname)){
           throw new PlayerAlreadyPresentException();
       }
        playersScore.put(nickname, 0);
    }

    /**
     * Setter method, allows to set a certain score for a player.
     * @param nickname player
     * @param newScore new score to set
     */
    public void setScore(String nickname, int newScore) throws PlayerNotPresentException {
        if(!playersScore.containsKey(nickname)){
            throw new PlayerNotPresentException();
        }
        this.playersScore.put(nickname, newScore);
    }

    /**
     * Getter method for the score, allows to know the current score of a player.
     * @param nickname player
     * @return current score for the player
     */
    public int getScore(String nickname) throws PlayerNotPresentException {
        if(!playersScore.containsKey(nickname)){
            throw new PlayerNotPresentException();
        }
        return playersScore.get(nickname);
    }
    public void incrementScore(String nickname, int deltaScore) throws PlayerNotPresentException {
        if(!playersScore.containsKey(nickname)){
            throw new PlayerNotPresentException();
        }
        int newScore = this.getScore(nickname) + deltaScore;
        this.playersScore.put(nickname, newScore);
    }
}
