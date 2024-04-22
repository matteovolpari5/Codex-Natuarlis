package it.polimi.ingsw.gc07.model_view;

import java.util.HashMap;
import java.util.Map;

public class ScoreTrackBoardView {
    /**
     * Map that link each player with his personal score.
     */
    private final Map<String, Integer> playersScore;

    /**
     * Constructor method for an empty ScoreTrackBoard.
     */
    public ScoreTrackBoardView() {
        playersScore = new HashMap<>();
    }

    /**
     * Method that allows to insert a new Player to the ScoreTrackBoard,
     * initializing it's score to 0.
     * @param nickname player to add
     */
    public void addPlayer(String nickname) {
        assert(!playersScore.containsKey(nickname)): "The player is already present";
        playersScore.put(nickname, 0);
    }

    /**
     * Setter method, allows to set a certain score for a player.
     * @param nickname player
     * @param newScore new score to set
     */
    public void setScore(String nickname, int newScore) {
        assert(playersScore.containsKey(nickname)): "The player is not present";
        playersScore.put(nickname, newScore);
    }

    /**
     * Method that allows to increment a player's score of deltaScore points.
     * @param nickname nickname of the player
     * @param deltaScore points to add
     */
    public void incrementScore(String nickname, int deltaScore) {
        assert(playersScore.containsKey(nickname)): "The player is not present";
        playersScore.put(nickname, playersScore.get(nickname) + deltaScore);
    }
}
