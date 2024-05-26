package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view.GameView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;

import java.net.URL;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.*;
import java.util.ResourceBundle;

public class PlayerSceneController implements GuiController, Initializable {
    @FXML
    protected ListView<String> myChat;
    private ObservableList<String> chatItem;
    @FXML
    protected ListView<String> myUpdates;
    private ObservableList<String> updatesItem;
    @FXML
    protected Button chatButton;
    @FXML
    protected Label currentPlayer;
    @FXML
    protected Label gameId;
    @FXML
    protected Label nickname1;
    @FXML
    protected Label nickname2;
    @FXML
    protected Label nickname3;
    @FXML
    protected Label nickname4;
    @FXML
    protected ImageView myStarterCard;
    @FXML
    protected AnchorPane chatContainer;
    @FXML
    protected ImageView handCard1;
    @FXML
    protected ImageView handCard2;
    @FXML
    protected ImageView handCard3;
    @FXML
    protected ImageView commonObjective1;
    @FXML
    protected ImageView commonObjective2;
    @FXML
    protected ImageView secretObjective;
    @FXML
    protected ImageView topDeckResource;
    @FXML
    protected ImageView topDeckGold;
    @FXML
    protected ImageView revealedGold1;
    @FXML
    protected ImageView revealedGold2;
    @FXML
    protected ImageView revealedResource1;
    @FXML
    protected ImageView revealedResource2;

    @FXML
    protected void onChatButtonClick(){
        if(!chatContainer.isVisible()) {
            chatContainer.setVisible(true);
            chatButton.setText("close chat");
        }
        else{
            chatContainer.setVisible(false);
            chatButton.setText("show chat");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chatItem = FXCollections.observableArrayList();
        myChat.setItems(chatItem);
        updatesItem = FXCollections.observableArrayList();
        myUpdates.setItems(updatesItem);

        /*
        // TODO spostare sotto

        nickname1.setText(nickname);
        int numPlayersConnected = gameView.getPlayersTokenColors().size();
        if (numPlayersConnected <= 3){
            nickname4.setVisible(false);
        }
        if (numPlayersConnected <= 2){
            nickname3.setVisible(false);
        }
        if (numPlayersConnected <= 1){
            nickname2.setVisible(false);
        }

        // set stalled or disconnected
        for (String s: gameView.getConnectionValues().keySet()) {
            if (gameView.getConnectionValues().get(s)) {
                if (s.equals(nickname4.getText())) {
                    nickname1.setText(s + " [disconnected]");
                    nickname1.setOpacity(70);
                }
                if (s.equals(nickname2.getText())) {
                    nickname2.setText(s + " [disconnected]");
                    nickname2.setOpacity(70);
                }
                if (s.equals(nickname3.getText())) {
                    nickname3.setText(s + " [disconnected]");
                    nickname3.setOpacity(70);
                }
            }
        }
        */
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
    public void addMessage(ChatMessage chat) {
        Platform.runLater(() -> {
            chatItem.add(chat.getSender() + ": " + chat.getContent());
        });
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
        // set decks data
        Platform.runLater(() -> {
            // set top decks
            int topDeckId = topResourceDeck.getId();
            topDeckResource.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + topDeckId +".png")).toExternalForm()));
            topDeckId = topGoldDeck.getId();
            topDeckGold.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + topDeckId +".png")).toExternalForm()));

            // TODO
            // = deckView.getFaceUpResourceCard();
            // = deckView.getFaceUpGoldCard();
            // = deckView.getCommonObjective();
        });
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
        // no need to check nickname, already checked by Gui method

        // TODO
    }

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    @Override
    public void updateStarterCard(PlaceableCard starterCard) {
        int id = starterCard.getId();
        Platform.runLater(() -> {
            myStarterCard.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + id +".png")).toExternalForm()));
        });
    }

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        Platform.runLater(() -> {
            // set current hand data
            int imageId;
            imageId = hand.getFirst().getId();
            handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId +".png")).toExternalForm()));
            imageId = hand.get(1).getId();
            handCard2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId +".png")).toExternalForm()));
            imageId = hand.get(2).getId();
            handCard3.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId +".png")).toExternalForm()));

            // TODO
            // = gameView.getSecretObjective();
        });
    }

    /**
     * Method used to display updated game infos.
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {
        Platform.runLater(() -> {
            // game state
            updatesItem.add("New game state: " + gameState.name());
            GameView gameView = StageController.getGameView();
            // current player
            if (gameView.getCurrentPlayerNickname() != null){
                currentPlayer.setText("Current player: " + gameView.getCurrentPlayerNickname());
                currentPlayer.setVisible(true);
            }
            else{
                currentPlayer.setVisible(false);
            }
        });
    }

    /**
     * Method used to display a penultimate round update.
     */
    @Override
    public void setPenultimateRound() {
        Platform.runLater(() -> {
            updatesItem.add("This is the penultimate round");
        });
    }

    /**
     * Method used to display an additional round update.
     */
    @Override
    public void setAdditionalRound() {
        Platform.runLater(() -> {
            updatesItem.add("There will be an additional turn");
        });
    }

    /**
     * Method used to display the last command result.
     * @param commandResult command result
     */
    @Override
    public void updateCommandResult(CommandResult commandResult) {
        Platform.runLater(() -> {
            if(!commandResult.equals(CommandResult.SUCCESS)){
                updatesItem.add(commandResult.getResultMessage());
            }
        });
    }

    /**
     * Method used to display a user existing and free games.
     * @param existingGamesPlayerNumber players number in existing games
     * @param existingGamesTokenColor take token colors in existing games
     */
    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        // not used in this gui controller
    }

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    @Override
    public void displayWinners(List<String> winners) {
        // not used in this gui controller
    }

    /**
     * Method used to set the nickname.
     * @param nickname nickname
     */
    public void setNickname(String nickname) {
        // not used in this gui controller
    }

    /**
     * Method used to set the full chat content.
     * @param chatMessages full chat content
     */
    @Override
    public void setFullChat(List<ChatMessage> chatMessages) {
        Platform.runLater(() -> {
            // set full chat
            for (ChatMessage c: chatMessages){
                chatItem.add(c.getSender() + ": " + c.getContent());
            }
        });
    }

    /**
     * Method used to set the game id.
     * @param id game id
     */
    @Override
    public void setGameId(int id) {
        Platform.runLater(() -> {
            gameId.setText("game Id: " + id);
        });
    }

    /**
     * Method used to display a new connection value.
     * @param nickname nickname
     * @param value new connection value
     */
    @Override
    public void receiveConnectionUpdate(String nickname, boolean value) {
        // TODO non ho capito come lo avevi fatto in initialize
    }

    /**
     * Method used to display a new stall value.
     * @param nickname nickname
     * @param value new stall value
     */
    @Override
    public void receiveStallUpdate(String nickname, boolean value) {
        // TODO non ho capito come lo avevi fatto in initialize
    }

    /**
     * Method used to display players in the game.
     * @param tokenColors nicknames
     * @param connectionValues connection values
     * @param stallValues stall values
     */
    @Override
    public void receivePlayersUpdate(Map<String, TokenColor> tokenColors, Map<String, Boolean> connectionValues, Map<String, Boolean> stallValues) {
        // TODO non ho capito come lo avevi fatto sopra
    }
}
