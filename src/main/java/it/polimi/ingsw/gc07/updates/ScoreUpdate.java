package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;

public class ScoreUpdate implements Update {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Player's score.
     */
    private final int newScore;

    /**
     * Constructor of ScoreUpdate.
     * @param nickname nickname
     * @param newScore new score
     */
    public ScoreUpdate(String nickname, int newScore) {
        this.nickname = nickname;
        this.newScore = newScore;
    }

    /**
     * Execute method of the concrete update: sets a new score for the player.
     * @param gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setNewScore(nickname, newScore);
    }
}
