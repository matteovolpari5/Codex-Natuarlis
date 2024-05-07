package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.Map;

public interface ScoreTrackBoardViewListener {
    void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors);
}
