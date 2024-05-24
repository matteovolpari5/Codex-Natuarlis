package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.GameResource;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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

        nickname1.setText(nickname + " (you");
        // set decks data
        DeckView deckView = gameView.getDeckView();
        // set top decks
        int topDeckId = deckView.getTopResourceDeck().getId();
        Image image = new Image("@../graphic_resources/Card/Back/"+ topDeckId + ".png");
        //topDeckResource.setImage(image)
        topDeckId = deckView.getTopGoldDeck().getId();
        image = new Image("@../graphic_resources/Card/Back/"+ topDeckId + ".png");
        //topDeckGold.setImage(image)


        // = deckView.getTopGoldDeck();
        // = deckView.getFaceUpResourceCard();
        // = deckView.getFaceUpGoldCard();
        // = deckView.getCommonObjective();

        // set current hand data
        int imageId;
        imageId = gameView.getCurrentHand().getFirst().getId();
        image = new Image("@../graphic_resources/Card/Front/"+ imageId + ".png");
        handCard1.setImage(image);
        imageId = gameView.getCurrentHand().get(1).getId();
        image = new Image("@../graphic_resources/Card/Back/"+ imageId + ".png");
        handCard2.setImage(image);
        imageId = gameView.getCurrentHand().get(2).getId();
        image = new Image("@../graphic_resources/Card/Back/"+ imageId + ".png");
        handCard3.setImage(image);
        // = gameView.getSecretObjective();

        // set starter card
        int starterCardId = gameView.getStarterCard().getId();
        image = new Image("@../graphic_resources/Card/Back/"+ starterCardId + ".png");
        myStarterCard.setImage(image);

        // set scores
        // = gameView.getPlayersScores(), gameView.getPlayersTokenColors();

        // set id and current player
        gameId.setText("game Id: " + gameView.getId());
        currentPlayer.setText("Current player: " + gameView.getCurrentPlayerNickname());

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

    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {

    }

    @Override
    public void addMessage(ChatMessage chat) {
        chatItem.add(chat.getSender() + ": " + chat.getContent());
    }

    @Override
    public void updateDecks(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {

    }

    @Override
    public void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

    }

    @Override
    public void updateStarterCard(PlaceableCard starterCard) {
        int id = starterCard.getId();
        Image image = new Image("@../graphic_resources/Card/Front/" + id + ".png");
        myStarterCard.setImage(image);
    }

    @Override
    public void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective) {

    }

    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {
        updatesItem.add("new game state: " + gameState.name());
        currentPlayer.setText("Current player: " + currPlayer);
    }

    @Override
    public void setPenultimateRound() {
        updatesItem.add("is the penultimate turn");
    }

    @Override
    public void setAdditionalRound() {
        updatesItem.add("there will be an additional turn");
    }

    @Override
    public void updateCommandResult(CommandResult commandResult) {
        if(!commandResult.equals(CommandResult.SUCCESS)){
            updatesItem.add(commandResult.getResultMessage());
        }
    }

    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {

    }

    @Override
    public void displayWinners(List<String> winners) {

    }

    public void setNickname(String nickname) {
        // don't use
    }
}
