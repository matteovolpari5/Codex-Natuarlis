package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LobbySceneController implements GuiController, Initializable {
    @FXML
    protected RadioButton newGame;
    @FXML
    protected RadioButton joinGame;
    @FXML
    protected Button next;

    @FXML
    protected void onContinueButtonClick() {
        if(newGame.isSelected()) {
            //StageController.setScene("/it/polimi/ingsw/gc07/fxml/waitingRoom.fxml", "Waiting room");
        }else if(joinGame.isSelected()) {
            //StageController.setScene(SceneType.CHOOSE_);
        }
    }

    @FXML
    protected void clickedNew() {
        joinGame.setSelected(false);
        next.setVisible(newGame.isSelected());
    }

    @FXML
    protected void clickedJoin() {
        newGame.setSelected(false);
        next.setVisible(joinGame.isSelected());
    }
    /**
     * Handles the event when the "Exit" button is clicked.
     * Exits the application.
     */
    @FXML
    protected void onExitButtonClick() {
        //end game and close the window
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {

    }

    @Override
    public void addMessage(ChatMessage chat) {

    }

    @Override
    public void updateDecks(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {

    }

    @Override
    public void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

    }

    @Override
    public void updateStarterCard(PlaceableCard starterCard) {

    }

    @Override
    public void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective) {

    }

    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {

    }

    @Override
    public void setPenultimateRound() {

    }

    @Override
    public void setAdditionalRound() {

    }

    @Override
    public void updateCommandResult() {

    }

    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGames) {

    }

    @Override
    public void displayWinners(List<String> winners) {

    }
}
