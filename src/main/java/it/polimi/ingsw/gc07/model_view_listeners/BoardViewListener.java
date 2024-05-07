package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.enumerations.TokenColor;

import java.util.Map;

public interface BoardViewListener {
    void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors);
}
