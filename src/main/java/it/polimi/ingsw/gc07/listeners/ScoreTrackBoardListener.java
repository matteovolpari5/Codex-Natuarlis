package it.polimi.ingsw.gc07.listeners;

public interface ScoreTrackBoardListener {
    /**
     * Method used to notify players that the score of a players is changed.
     * @param nickname player's nickname
     * @param newScore new score
     */
    void showUpdatedScore(String nickname, int newScore);
}
