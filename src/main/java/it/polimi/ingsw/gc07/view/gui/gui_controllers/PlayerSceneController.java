package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.game_commands.AddChatPrivateMessageCommand;
import it.polimi.ingsw.gc07.game_commands.AddChatPublicMessageCommand;
import it.polimi.ingsw.gc07.game_commands.SetInitialCardsCommand;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.*;
import java.util.ResourceBundle;

public class PlayerSceneController implements GuiController, Initializable {
    @FXML
    protected ListView<String> myChat;
    private ObservableList<String> chatItem= FXCollections.observableArrayList();
    @FXML
    protected ListView<String> myUpdates;
    private final ObservableList<String> updatesItem = FXCollections.observableArrayList();
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
    protected Label nickStatus1;
    @FXML
    protected Label nickStatus2;
    @FXML
    protected Label nickStatus3;
    @FXML
    protected Label nickStatus4;
    /**
     * List of labels containing the connection/stall status of the player.
     */
    private final List<Label> statusLabels = new ArrayList<>();
    /**
     * List of labels containing nicknames.
     */
    private final List<Label> nicknameLabels = new ArrayList<>();
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
    protected ImageView chatNotification;
    @FXML
    protected AnchorPane startingPhaseBox;
    @FXML
    protected Label startingPhaseController;
    @FXML
    protected ImageView option1Starter;
    @FXML
    protected ImageView option2Starter;
    @FXML
    protected ImageView option1Objective;
    @FXML
    protected ImageView option2Objective;
    @FXML
    protected Pane str1Pane;
    @FXML
    protected Pane str2Pane;
    @FXML
    protected Pane obj1Pane;
    @FXML
    protected Pane obj2Pane;
    @FXML
    protected Label option1Label;
    @FXML
    protected Label option2Label;
    @FXML
    protected Label startingPhaseLabel;
    @FXML
    protected Button continueButton;
    @FXML
    protected Button sendCommandButton;
    @FXML
    protected ImageView tokenColor1;
    @FXML
    protected ImageView tokenColor2;
    @FXML
    protected ImageView tokenColor3;
    @FXML
    protected ImageView tokenColor4;
    private final List<ImageView> tokenColorsList = new ArrayList<>();
    @FXML
    protected TextField messageContent;
    @FXML
    protected HBox nickContainer;
    @FXML
    protected ChoiceBox<String> receiverSelector;

    @FXML
    protected void onChatButtonClick(){
        Platform.runLater(() -> {
            chatNotification.setVisible(false);
            if(!chatContainer.isVisible()) {
                chatContainer.setVisible(true);
                chatButton.setText("close chat");
                nickContainer.setVisible(false);
                ObservableList<String> possiblesReceivers = FXCollections.observableArrayList();
                possiblesReceivers.add("everyone");
                for(Label nickname: nicknameLabels){
                    if(!nickname.getText().equals("Player") && !nickname.getText().equals(StageController.getNickname()) && !receiverSelector.getItems().contains(nickname.getText())) {
                        possiblesReceivers.add(nickname.getText());
                    }
                }
                receiverSelector.setItems(possiblesReceivers);
            }
            else{
                chatContainer.setVisible(false);
                chatButton.setText("show chat");
                nickContainer.setVisible(true);
            }
        });
    }
    @FXML
    protected void onSendMessage(KeyEvent e){
        Platform.runLater(() -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                String content = messageContent.getText();
                if(receiverSelector.getValue() == null ||receiverSelector.getValue().equals("everyone")){
                    StageController.getClient().setAndExecuteCommand(new AddChatPublicMessageCommand(content, StageController.getNickname()));
                }
                else{
                    StageController.getClient().setAndExecuteCommand(new AddChatPrivateMessageCommand(content, StageController.getNickname(), receiverSelector.getValue()));
                }
            }
        });
    }
    @FXML
    protected void onStarterCardClick(){
        Platform.runLater(() -> {
            startingPhaseBox.setVisible(true);
            option1Label.setText("Front");
            option2Label.setText("Back");
            startingPhaseController.setText("");
        });
    }
    @FXML
    protected void onStarter1CardClick(){
        Platform.runLater(() -> {
            str1Pane.setStyle("-fx-border-color: #0000ff; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            str2Pane.setStyle("-fx-border-color: #fff8dc; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            startingPhaseController.setText("1");
            System.out.println("selezionata1");
        });
    }
    @FXML
    protected void onStarter2CardClick(){
        Platform.runLater(() -> {
            str1Pane.setStyle("-fx-border-color: #fff8dc; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            str2Pane.setStyle(" -fx-border-color: #0000ff; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            startingPhaseController.setText("2");
            System.out.println("selezionata2");
        });
    }
    @FXML
    protected void onObjective1CardClick(){
        Platform.runLater(() -> {
            obj1Pane.setStyle("-fx-border-color: #0000ff; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            obj2Pane.setStyle("-fx-border-color: #fff8dc; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            String previousSelection = startingPhaseController.getText();
            if(previousSelection.equals("1")||previousSelection.equals("11")||previousSelection.equals("12")){
                startingPhaseController.setText("11");
            }
            else{
                startingPhaseController.setText("21");
            }
        });
    }
    @FXML
    protected void onObjective2CardClick(){
        Platform.runLater(() -> {
            obj1Pane.setStyle("-fx-border-color: #fff8dc; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            obj2Pane.setStyle("-fx-border-color: #0000ff; -fx-padding: 10; -fx-background-radius: 15; -fx-border-radius: 5; -fx-border-width: 5;");
            String previousSelection = startingPhaseController.getText();
            if(previousSelection.equals("1")||previousSelection.equals("11")||previousSelection.equals("12")){
                startingPhaseController.setText("12");
            }
            else{
                startingPhaseController.setText("22");
            }
        });
    }

    @FXML
    protected void onContinueButtonClick(){
        Platform.runLater(() -> {
            if(startingPhaseController.getText().equals("1")||startingPhaseController.getText().equals("2")) {
                option1Label.setText("Option 1");
                option2Label.setText("Option 2");
                str1Pane.setVisible(false);
                str2Pane.setVisible(false);
                obj1Pane.setVisible(true);
                obj2Pane.setVisible(true);
                continueButton.setVisible(false);
                sendCommandButton.setVisible(true);
            }
        });
    }
    @FXML
    protected void onSendCommandButtonClick(){
        Platform.runLater(() -> {
            startingPhaseLabel.setVisible(false);
            boolean starterCardWay = false;
            boolean objectiveCardSelected = false;
            switch (startingPhaseController.getText()) {
                case "11":
                    starterCardWay = false;
                    objectiveCardSelected = false;
                    break;
                case "12":
                    starterCardWay = false;
                    objectiveCardSelected = true;
                    break;
                case "21":
                    starterCardWay = true;
                    objectiveCardSelected = false;
                    break;
                case "22":
                    starterCardWay = true;
                    objectiveCardSelected = true;
                    break;
            }
            StageController.getClient().setAndExecuteCommand(new SetInitialCardsCommand(StageController.getNickname(), starterCardWay, objectiveCardSelected));
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            chatItem = FXCollections.observableArrayList();
            myChat.setItems(chatItem);

            myUpdates.setItems(updatesItem);
            nicknameLabels.add(nickname1);
            nicknameLabels.add(nickname2);
            nicknameLabels.add(nickname3);
            nicknameLabels.add(nickname4);
            statusLabels.add(nickStatus1);
            statusLabels.add(nickStatus2);
            statusLabels.add(nickStatus3);
            statusLabels.add(nickStatus4);
            startingPhaseLabel.setText("Select the placing way of your starter card");
            tokenColorsList.add(tokenColor1);
            tokenColorsList.add(tokenColor2);
            tokenColorsList.add(tokenColor3);
            tokenColorsList.add(tokenColor4);
        });
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
        chatItem.add(chat.getSender() + ": " + chat.getContent());
        if(!chatContainer.isVisible()) {
            chatNotification.setVisible(true);
        }
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
        // set top decks
        int topDeckId;
        if(topDeckResource!=null) {
            topDeckId = topResourceDeck.getId();
            topDeckResource.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + topDeckId + ".png")).toExternalForm()));
        }
        if(topGoldDeck!=null) {
            topDeckId = topGoldDeck.getId();
            topDeckGold.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + topDeckId + ".png")).toExternalForm()));
        }
        // TODO
        if(faceUpGoldCard!= null) {
            if (!faceUpGoldCard.isEmpty()) {
                revealedGold1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + faceUpGoldCard.getFirst().getId() + ".png")).toExternalForm()));
            }
            if (faceUpGoldCard.size() == 2) {
                revealedGold2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + faceUpGoldCard.get(1).getId() + ".png")).toExternalForm()));
            }
        }
        if(faceUpResourceCard!=null) {
            if (!faceUpResourceCard.isEmpty()) {
                revealedResource1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + faceUpResourceCard.getFirst().getId() + ".png")).toExternalForm()));
            }
            if (faceUpResourceCard.size() == 2) {
                revealedResource2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + faceUpResourceCard.get(1).getId() + ".png")).toExternalForm()));
            }
        }
        if(commonObjective!=null) {
            if (!commonObjective.isEmpty()) {
                commonObjective1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + commonObjective.get(0).getId() + ".png")).toExternalForm()));
                commonObjective2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + commonObjective.get(1).getId() + ".png")).toExternalForm()));
            }
        }
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
        myStarterCard.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + id +".png")).toExternalForm()));
        option1Starter.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + id +".png")).toExternalForm()));
        option2Starter.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + id +".png")).toExternalForm()));
    }

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, List<ObjectiveCard> personalObjective) {
        // set current hand data
        int imageId;
        if(hand.size()==3) {
            imageId = hand.getFirst().getId();
            handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
            imageId = hand.get(1).getId();
            handCard2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
            imageId = hand.get(2).getId();
            handCard3.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
        } else if (hand.size()==2) {
            imageId = hand.getFirst().getId();
            handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
            imageId = hand.get(1).getId();
            handCard2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
        } else if (hand.size()==1) {
            imageId = hand.getFirst().getId();
            handCard1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
        }
        // TODO
        if(personalObjective.size()==2) {
            imageId = personalObjective.getFirst().getId();
            option1Objective.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
            imageId = personalObjective.get(1).getId();
            option2Objective.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
        } else if (personalObjective.size()==1) {
            imageId = personalObjective.getFirst().getId();
            secretObjective.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
        }
    }

    /**
     * Method used to display updated game infos.
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {
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
    }

    /**
     * Method used to display a penultimate round update.
     */
    @Override
    public void setPenultimateRound() {
        updatesItem.add("This is the penultimate round");
    }

    /**
     * Method used to display an additional round update.
     */
    @Override
    public void setAdditionalRound() {
        updatesItem.add("There will be an additional turn");
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
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {}

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    @Override
    public void displayWinners(List<String> winners) {}

    /**
     * Method used to set the nickname.
     * @param nickname nickname
     */
    public void setNickname(String nickname) {}

    /**
     * Method used to set the full chat content.
     * @param chatMessages full chat content
     */
    @Override
    public void setFullChat(List<ChatMessage> chatMessages) {
        // set full chat
        for (ChatMessage c: chatMessages){
            chatItem.add(c.getSender() + ": " + c.getContent());
        }
    }

    /**
     * Method used to set the game id.
     * @param id game id
     */
    @Override
    public void setGameId(int id) {
        gameId.setText("game Id: " + id);
    }

    /**
     * Method used to display a new connection value.
     * @param nickname nickname
     * @param value new connection value
     */
    @Override
    public void receiveConnectionUpdate(String nickname, boolean value) {
        for(int i = 0; i < nicknameLabels.size(); i++){
            if(nicknameLabels.get(i).getText().equals(nickname)){
                if(value){
                    statusLabels.get(i).setText("[disconnected]");
                    statusLabels.get(i).setVisible(true);
                    nicknameLabels.get(i).setOpacity(0.8);
                }
                else{
                    nicknameLabels.get(i).setOpacity(1);
                    statusLabels.get(i).setVisible(false);
                }
            }
        }
    }

    /**
     * Method used to display a new stall value.
     * @param nickname nickname
     * @param value new stall value
     */
    @Override
    public void receiveStallUpdate(String nickname, boolean value) {
        for(int i = 0; i < nicknameLabels.size(); i++){
            if(nicknameLabels.get(i).getText().equals(nickname)){
                if(value){
                    statusLabels.get(i).setText("[stalled]");
                    statusLabels.get(i).setVisible(true);
                    nicknameLabels.get(i).setOpacity(0.8);
                }
                else{
                    nicknameLabels.get(i).setOpacity(1);
                    statusLabels.get(i).setVisible(false);
                }
            }
        }
    }

    /**
     * Method used to display players in the game.
     * @param tokenColors nicknames
     * @param connectionValues connection values
     * @param stallValues stall values
     */
    @Override
    public void receivePlayersUpdate(Map<String, TokenColor> tokenColors, Map<String, Boolean> connectionValues, Map<String, Boolean> stallValues) {
        boolean found = false;
        for(String newNickname: tokenColors.keySet()){
            for(int i = 0; i < nicknameLabels.size(); i++) {
                if (nicknameLabels.get(i).getText().equals(newNickname)) {
                    // the nickname is already present
                    nicknameLabels.get(i).setVisible(true);
                    // set disconnected/stalled visible attribute
                    if (connectionValues.get(newNickname)) {
                        statusLabels.get(i).setText("[disconnected]");
                        statusLabels.get(i).setVisible(true);
                        nicknameLabels.get(i).setOpacity(0.8);
                    } else if (stallValues.get(newNickname)) {
                        statusLabels.get(i).setVisible(true);
                        statusLabels.get(i).setText("[stalled]");
                        nicknameLabels.get(i).setOpacity(0.8);
                    } else {
                        nicknameLabels.get(i).setOpacity(1);
                        statusLabels.get(i).setVisible(false);
                    }
                    found = true;
                    tokenColorsList.get(i).setVisible(true);
                    // set token color image
                    switch (tokenColors.get(newNickname)) {
                        case GREEN:
                            tokenColorsList.get(i).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/green.png")).toExternalForm()));
                            break;
                        case RED:
                            tokenColorsList.get(i).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/red.png")).toExternalForm()));
                            break;
                        case BLUE:
                            tokenColorsList.get(i).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/blue.png")).toExternalForm()));
                            break;
                        case YELLOW:
                            tokenColorsList.get(i).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/yellow.png")).toExternalForm()));
                            break;
                    }
                    break;
                }
            }
            if(!found){
                // insert the nickname in the first free label
                for(int j = 0; j < nicknameLabels.size(); j++){
                    if(nicknameLabels.get(j).getText().equals("Player")){
                        nicknameLabels.get(j).setText(newNickname);
                        nicknameLabels.get(j).setVisible(true);
                        // set token color image
                        switch(tokenColors.get(newNickname)){
                            case GREEN:
                                tokenColorsList.get(j).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/green.png")).toExternalForm()));
                                break;
                            case RED:
                                tokenColorsList.get(j).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/red.png")).toExternalForm()));
                                break;
                            case BLUE:
                                tokenColorsList.get(j).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/blue.png")).toExternalForm()));
                                break;
                            case YELLOW:
                                tokenColorsList.get(j).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/yellow.png")).toExternalForm()));
                                break;
                        }
                        tokenColorsList.get(j).setVisible(true);
                        // set disconnected/stalled visible attribute
                        if(!connectionValues.get(newNickname)){
                            statusLabels.get(j).setText("[disconnected]");
                            statusLabels.get(j).setVisible(true);
                            nicknameLabels.get(j).setOpacity(0.8);
                        }
                        else if(stallValues.get(newNickname)){
                            statusLabels.get(j).setText("[stalled]");
                            statusLabels.get(j).setVisible(true);
                            nicknameLabels.get(j).setOpacity(0.8);
                        }
                        else{
                            nicknameLabels.get(j).setOpacity(1);
                            statusLabels.get(j).setVisible(false);
                        }
                        break;
                    }
                }
            }
        }
    }
}
