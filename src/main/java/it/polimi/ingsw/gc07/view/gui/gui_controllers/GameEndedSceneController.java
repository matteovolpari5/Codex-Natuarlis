package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.updates.ScoreUpdate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Window;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GameEndedSceneController implements Initializable, GuiController {
    /**
     * Attribute that represents the general text.
     */
    @FXML
    public Text generalText;
    /**
     * Attribute that represents the first winner
     */
    @FXML
    public Text winner1;
    /**
     * Attribute that represents the second winner
     */
    @FXML
    public Text winner2;
    /**
     * Attribute that represents the third winner
     */
    @FXML
    public Text winner3;
    /**
     * Attribute that represents the fourth winner
     */
    @FXML
    public Text winner4;
    /**
     * Attribute that exit button
     */
    @FXML
    public Button exitGame;

    /**
     * Initialize method of this controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // don't need to initialize anything
    }

    /**
     * Method used to display a score update.
     * @param playerScore map containing players' scores
     * @param playerTokenColor map containing players' token colors
     */
    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {
        // not displayed
    }

    /**
     * Method used to display a new chat message.
     * @param chat new chat message
     */
    @Override
    public void addMessage(ChatMessage chat) {
        // not displayed
    }

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
        // not displayed
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
        // not displayed
    }

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    @Override
    public void updateStarterCard(PlaceableCard starterCard) {
        // not displayed
    }

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, List<ObjectiveCard> personalObjective) {
        // not displayed
    }

    /**
     * Method used to display updated game infos.
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {
        // not displayed
    }

    /**
     * Method used to display a penultimate round update.
     */
    @Override
    public void setPenultimateRound() {
        // not displayed
    }

    /**
     * Method used to display an additional round update.
     */
    @Override
    public void setAdditionalRound() {
        // not displayed
    }

    /**
     * Method used to display the last command result.
     * @param commandResult command result
     */
    @Override
    public void updateCommandResult(CommandResult commandResult) {
        // not displayed
    }

    /**
     * Method used to display a user existing and free games.
     * @param existingGamesPlayerNumber players number in existing games
     * @param existingGamesTokenColor take token colors in existing games
     */
    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        // not displayed
    }

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    @Override
    public void displayWinners(List<String> winners) {
        // already on Platform.runLater
        if(winners.size()==1){
            generalText.setText("AND THE WINNER IS...");
        }
        else {
            generalText.setText("AND THE WINNERS ARE...");
        }
        int size = winners.size();
        if(size==1){
            winner1.setText(winners.get(0));
            winner1.setVisible(true);
            winner2.setVisible(false);
            winner3.setVisible(false);
            winner4.setVisible(false);
        }
        else if(size==2){
            winner1.setText(winners.get(0));
            winner1.setVisible(true);
            winner2.setText(winners.get(1));
            winner2.setVisible(true);
            winner3.setVisible(false);
            winner4.setVisible(false);
        }
        else if(size==3){
            winner1.setText(winners.get(0));
            winner1.setVisible(true);
            winner2.setText(winners.get(1));
            winner2.setVisible(true);
            winner3.setText(winners.get(2));
            winner3.setVisible(true);
            winner4.setVisible(false);
        }
        else if(size==4){
            winner1.setText(winners.get(0));
            winner1.setVisible(true);
            winner2.setText(winners.get(1));
            winner2.setVisible(true);
            winner3.setText(winners.get(2));
            winner3.setVisible(true);
            winner4.setText(winners.get(3));
            winner4.setVisible(true);
        }
    }

    /**
     * Method used to set the nickname.
     * @param nickname nickname
     */
    @Override
    public void setNickname(String nickname) {
        // don't need to save it
    }

    /**
     * Method used to set the full chat content.
     * @param chatMessages full chat content
     */
    @Override
    public void setFullChat(List<ChatMessage> chatMessages) {
        // not displayed
    }

    /**
     * Method used to set the game id.
     * @param gameId game id
     */
    @Override
    public void setGameId(int gameId) {
        // not displayed
    }

    /**
     * Method used to display a new connection value.
     * @param nickname nickname
     * @param value    new connection value
     */
    @Override
    public void receiveConnectionUpdate(String nickname, boolean value) {
        // not displayed
    }

    /**
     * Method used to display a new stall value.
     * @param nickname nickname
     * @param value    new stall value
     */
    @Override
    public void receiveStallUpdate(String nickname, boolean value) {
        // not displayed
    }

    /**
     * Method used to display players in the game.
     * @param nicknames        nicknames
     * @param connectionValues connection values
     * @param stallValues      stall values
     */
    @Override
    public void receivePlayersUpdate(Map<String, TokenColor> nicknames, Map<String, Boolean> connectionValues, Map<String, Boolean> stallValues) {
        // not displayed
    }

    /**
     * Method used to redirect the client.
     * @param actionEvent action event
     */
    @FXML
    public void endGame(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            StageController.getClient().setClientAlive(false);
            //TODO: close the window without closing the client //
        });
    }
}
