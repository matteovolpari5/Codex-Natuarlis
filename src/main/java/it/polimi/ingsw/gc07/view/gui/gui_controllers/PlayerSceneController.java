package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view.DeckView;
import it.polimi.ingsw.gc07.model_view.GameFieldView;
import it.polimi.ingsw.gc07.model_view.GameView;
import javafx.fxml.Initializable;

import java.net.URL;
import javafx.collections.FXCollections;
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
        GameView gameView = StageController.getGameView();
        String nickname = StageController.getNickname();
        chatItem = FXCollections.observableArrayList();
        myChat.setItems(chatItem);
        updatesItem = FXCollections.observableArrayList();
        myUpdates.setItems(updatesItem);
        // set game field data
        GameFieldView gameFieldView =  gameView.getGameField(nickname);
        // = gameFieldView.getCardsContent();
        // = gameFieldView.getCardsFace();
        // = gameFieldView.getCardsOrder());

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
        // set decks data
        DeckView deckView = gameView.getDeckView();
        // set top decks
        int topDeckId = deckView.getTopResourceDeck().getId();
        topDeckResource.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + topDeckId +".png")).toExternalForm()));
        topDeckId = deckView.getTopGoldDeck().getId();
        topDeckGold.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + topDeckId +".png")).toExternalForm()));

        // = deckView.getFaceUpResourceCard();
        // = deckView.getFaceUpGoldCard();
        // = deckView.getCommonObjective();

        // set current hand data
        int imageId;
        imageId = gameView.getCurrentHand().getFirst().getId();
        handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId +".png")).toExternalForm()));
        imageId = gameView.getCurrentHand().get(1).getId();
        handCard2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId +".png")).toExternalForm()));
        imageId = gameView.getCurrentHand().get(2).getId();
        handCard3.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId +".png")).toExternalForm()));
        // = gameView.getSecretObjective();

        // set starter card
        int starterCardId = gameView.getStarterCard().getId();
        myStarterCard.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + starterCardId +".png")).toExternalForm()));

        // set scores
        // = gameView.getPlayersScores(), gameView.getPlayersTokenColors();

        // set id and current player
        gameId.setText("game Id: " + gameView.getId());
        if (gameView.getCurrentPlayerNickname() != null){
            currentPlayer.setText("Current player: " + gameView.getCurrentPlayerNickname());
            currentPlayer.setVisible(true);
        }
        else{
            currentPlayer.setVisible(false);
        }


        // set full chat
        for (ChatMessage c: gameView.getOwnerMessages()){
            chatItem.add(c.getSender() + ": " + c.getContent());
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
        // =  gameView.getStallValues();
    }

    /**
     * Method used to display a score update.
     * @param playerScore map containing players' scores
     * @param playerTokenColor map containing players' token colors
     */
    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {

    }

    /**
     * Method used to display a new chat message.
     * @param chat new chat message
     */
    @Override
    public void addMessage(ChatMessage chat) {
        chatItem.add(chat.getSender() + ": " + chat.getContent());
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

    }

    /**
     * Method used to display a new game field update.
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    @Override
    public void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

    }

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    @Override
    public void updateStarterCard(PlaceableCard starterCard) {
        int id = starterCard.getId();
        myStarterCard.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + id +".png")).toExternalForm()));
    }

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective) {

    }

    /**
     * Method used to display updated game infos.
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {
        updatesItem.add("new game state: " + gameState.name());
        GameView gameView = StageController.getGameView();
        if (gameView.getCurrentPlayerNickname() != null){
            currentPlayer.setText("Current player: " + gameView.getCurrentPlayerNickname());
            currentPlayer.setVisible(true);
        }
        else{
            currentPlayer.setVisible(false);
        }
    }

    /**
     * Method used to display a penultimate round update.
     */
    @Override
    public void setPenultimateRound() {
        updatesItem.add("is the penultimate turn");
    }

    /**
     * Method used to display an additional round update.
     */
    @Override
    public void setAdditionalRound() {
        updatesItem.add("there will be an additional turn");
    }

    /**
     * Method used to display the last command result.
     * @param commandResult command result
     */
    @Override
    public void updateCommandResult(CommandResult commandResult) {
        if(!commandResult.equals(CommandResult.SUCCESS)){
            updatesItem.add(commandResult.getResultMessage());
        }
    }

    /**
     * Method used to display a user existing and free games.
     * @param existingGamesPlayerNumber players number in existing games
     * @param existingGamesTokenColor take token colors in existing games
     */
    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {

    }

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    @Override
    public void displayWinners(List<String> winners) {

    }

    /**
     * Method used to set the nickname.
     * @param nickname nickname
     */
    public void setNickname(String nickname) {
        // don't need to save it
    }
}
