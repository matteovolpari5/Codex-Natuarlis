package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
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
    protected ImageView myStarterCard;

    @FXML
    AnchorPane chatContainer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatItem = FXCollections.observableArrayList();
        myChat.setItems(chatItem);
        updatesItem = FXCollections.observableArrayList();
        myUpdates.setItems(updatesItem);
        //todo settare la situa iniziale
    }

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
    public void displayExistingGames(Map<Integer, Integer> existingGames) {

    }

    @Override
    public void displayWinners(List<String> winners) {

    }

    @Override
    public void displayFullChat(List<ChatMessage> messages) {
        chatItem.clear();
        for(ChatMessage message : messages){
            chatItem.add(message.getSender() + ": " + message.getContent());
        }
    }

}
