package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view_listeners.ScoreTrackBoardViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreTrackBoardView {
    /**
     * Map containing players' scores.
     * Key: nickname
     * Value: score
     */
    private final Map<String, Integer> playerScores;
    /**
     * Map containing players' token colors.
     * Key: nickname
     * Value: token color
     */
    private final Map<String, TokenColor> playerTokenColors;
    /**
     * List of score track board view listeners.
     */
    private final List<ScoreTrackBoardViewListener> scoreTrackBoardViewListeners;

    /**
     * Constructor method for an empty ScoreTrackBoard.
     */
    public ScoreTrackBoardView() {
        this.playerScores = new HashMap<>();
        this.playerTokenColors = new HashMap<>();
        this.scoreTrackBoardViewListeners = new ArrayList<>();
    }

    /**
     * Method used to register a new listener.
     * @param scoreTrackBoardViewListener new listener
     */
    public void addListener(ScoreTrackBoardViewListener scoreTrackBoardViewListener) {
        scoreTrackBoardViewListeners.add(scoreTrackBoardViewListener);
    }

    /**
     * Method that allows to insert a new Player to the ScoreTrackBoard,
     * initializing it's score to 0.
     * @param nickname player to add
     */
    public void addPlayerToBoard(String nickname, TokenColor tokenColor) {
        assert(!playerScores.containsKey(nickname)): "The player is already present";
        assert(!playerTokenColors.containsKey(nickname));
        playerScores.put(nickname, 0);
        playerTokenColors.put(nickname, tokenColor);

        updateListeners();
    }

    /**
     * Setter method, allows to set a certain score for a player.
     * @param nickname player
     * @param newScore new score to set
     */
    public void setNewScore(String nickname, int newScore) {
        assert(playerScores.containsKey(nickname)): "The player is not present";
        assert(playerTokenColors.containsKey(nickname));
        playerScores.put(nickname, newScore);

        updateListeners();
    }

    /**
     * Private method used to send updates to listeners.
     */
    private void updateListeners() {
        for(ScoreTrackBoardViewListener l: scoreTrackBoardViewListeners) {
            l.receiveScoreUpdate(playerScores, playerTokenColors);
        }
    }
}
