package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class OtherPlayerSceneController implements GuiController, Initializable {

    String otherPlayerNickname;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO come per player scene, richiedere l'aggiornamento da fuori
        // no runlater
    }

    /**
     * Method used to display a score update.
     * @param playerScore map containing players' scores
     * @param playerTokenColor map containing players' token colors
     */
    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {
        // TODO
    }

    /**
     * Method used to display a new chat message.
     * @param chat new chat message
     */
    @Override
    public void addMessage(ChatMessage chat) {}

    /**
     * Method used to display a deck update, containing cards a player can draw or see.
     * @param topResourceDeck top resource deck
     * @param topGoldDeck top gold deck
     * @param faceUpResourceCard face up cards resource
     * @param faceUpGoldCard face up cards gold
     * @param commonObjective common objective
     */
    @Override
    public void updateDecks(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        //TODO
    }

    /**
     * Method used to display a new game field update.
     * @param nickname nickname
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    @Override
    public void updateGameField(String nickname, PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        if(otherPlayerNickname != null && otherPlayerNickname.equals(nickname)) {
            // TODO update game field
        }
    }

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    @Override
    public void updateStarterCard(PlaceableCard starterCard) {}

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, List<ObjectiveCard> personalObjective) {}

    /**
     * Method used to display updated game infos.
     *
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {
        // TODO
    }

    /**
     * Method used to display a penultimate round update.
     */
    @Override
    public void setPenultimateRound() {
        // TODO
    }

    /**
     * Method used to display an additional round update.
     */
    @Override
    public void setAdditionalRound() {
        // TODO
    }

    /**
     * Method used to display the last command result.
     * @param commandResult command result
     */
    @Override
    public void updateCommandResult(CommandResult commandResult) {
        // TODO
    }

    /**
     * Method used to display a user existing and free games.
     * @param existingGamesPlayerNumber players number in existing games
     * @param existingGamesTokenColor take token colors in existing games
     */
    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {}

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    @Override
    public void displayWinners(List<String> winners) {}

    @Override
    public void setNickname(String nickname) {
        otherPlayerNickname = nickname;
    }

    /**
     * Method used to set the full chat content.
     * @param chatMessages full chat content
     */
    @Override
    public void setFullChat(List<ChatMessage> chatMessages) {}

    /**
     * Method used to set the game id.
     * @param gameId game id
     */
    @Override
    public void setGameId(int gameId) {
        // TODO
    }

    /**
     * Method used to display a new connection value.
     * @param nickname nickname
     * @param value    new connection value
     */
    @Override
    public void receiveConnectionUpdate(String nickname, boolean value) {
        // TODO
    }

    /**
     * Method used to display a new stall value.
     * @param nickname nickname
     * @param value    new stall value
     */
    @Override
    public void receiveStallUpdate(String nickname, boolean value) {
        // TODO
    }

    /**
     * Method used to display players in the game.
     * @param nicknames nicknames
     * @param connectionValues connection values
     * @param stallValues stall values
     */
    @Override
    public void receivePlayersUpdate(Map<String, TokenColor> nicknames, Map<String, Boolean> connectionValues, Map<String, Boolean> stallValues) {
        // TODO
    }
}
