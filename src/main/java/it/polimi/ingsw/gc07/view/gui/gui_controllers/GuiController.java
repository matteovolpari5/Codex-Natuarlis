package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;

import java.util.List;
import java.util.Map;

public interface GuiController {
    void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor);
    void addMessage(ChatMessage chat);
    void updateDecks(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective);
    void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder);
    void updateStarterCard(PlaceableCard starterCard);
    void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective);
    void updateGameInfo(GameState gameState, String currPlayer);
    void setPenultimateRound();
    void setAdditionalRound();
    void updateCommandResult(CommandResult commandResult);
    void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor);
    void displayWinners(List<String> winners);
    void setNickname(String nickname);
}
