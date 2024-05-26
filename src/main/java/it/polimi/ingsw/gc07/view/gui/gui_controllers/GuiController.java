package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view.ChatView;

import java.util.List;
import java.util.Map;

public interface GuiController {
    /**
     * Method used to display a score update.
     * @param playerScore map containing players' scores
     * @param playerTokenColor map containing players' token colors
     */
    void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor);

    /**
     * Method used to display a new chat message.
     * @param chat new chat message
     */
    void addMessage(ChatMessage chat);

    /**
     * Method used to display a deck update, containing cards a player can draw or see.
     * @param topResourceDeck top resource deck
     * @param topGoldDeck top gold deck
     * @param faceUpResourceCard face up cards resource
     * @param faceUpGoldCard face up cards gold
     * @param commonObjective common objective
     */
    void updateDecks(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective);

    /**
     * Method used to display a new game field update.
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder);

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    void updateStarterCard(PlaceableCard starterCard);

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective);

    /**
     * Method used to display updated game infos.
     * @param gameState game state
     * @param currPlayer current player
     */
    void updateGameInfo(GameState gameState, String currPlayer);

    /**
     * Method used to display a penultimate round update.
     */
    void setPenultimateRound();

    /**
     * Method used to display an additional round update.
     */
    void setAdditionalRound();

    /**
     * Method used to display the last command result.
     * @param commandResult command result
     */
    void updateCommandResult(CommandResult commandResult);

    /**
     * Method used to display a user existing and free games.
     * @param existingGamesPlayerNumber players number in existing games
     * @param existingGamesTokenColor take token colors in existing games
     */
    void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor);

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    void displayWinners(List<String> winners);

    /**
     * Method used to set the nickname.
     * @param nickname nickname
     */
    void setNickname(String nickname);

    /**
     * Method used to set the full chat content.
     * @param chatMessages full chat content
     */
    void setFullChat(List<ChatMessage> chatMessages);

    /**
     * Method used to set the game id.
     * @param gameId game id
     */
    void setGameId(int gameId);
}
