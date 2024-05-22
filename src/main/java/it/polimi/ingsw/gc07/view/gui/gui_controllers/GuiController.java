package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

import java.util.Map;

public interface GuiController {
    void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor);
    void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder);
}
