package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreTrackBoardView {
    /**
     * Map that link each player with his personal score.
     */
    private final Map<String, Integer> playersScore;
    private final Map<String, TokenColor> playersToken;

    private final List<String> playersNickname;

    /**
     * Constructor method for an empty ScoreTrackBoard.
     */
    public ScoreTrackBoardView() {
        playersNickname = new ArrayList<>();
        playersScore = new HashMap<>();
        playersToken = new HashMap<>();
    }

    /**
     * Method that allows to insert a new Player to the ScoreTrackBoard,
     * initializing it's score to 0.
     * @param nickname player to add
     * @param color token color choose by the player
     */
    public void addPlayer(String nickname, TokenColor color) {
        assert(!playersScore.containsKey(nickname)): "The player is already present";
        playersScore.put(nickname, 0);
        playersToken.put(nickname, color);
        playersNickname.add(nickname);
    }

    /**
     * Setter method, allows to set a certain score for a player.
     * @param nickname player
     * @param newScore new score to set
     */
    public void setNewScore(String nickname, int newScore) {
        assert(playersScore.containsKey(nickname)): "The player is not present";
        playersScore.put(nickname, newScore);
    }
}
