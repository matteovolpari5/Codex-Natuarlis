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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class LobbyController implements Initializable, GuiController {
    @FXML
    protected TextField numPlayers;
    @FXML
    protected RadioButton newGame;
    @FXML
    protected RadioButton joinGame;
    @FXML
    protected AnchorPane joinPane;
    @FXML
    protected Button next;
    @FXML
    protected Button back;
    /**
     * list of string composed with: gameId +++ numOfPlayers
     */
    @FXML
    protected ListView<String> gameList;

    @FXML
    protected TextField gameId;
    @FXML
    protected Text errGameId;

    protected Map<Integer,Integer> games;

    @FXML
    protected TextField tokenColor;
    @FXML
    protected Text errTokenColor;
    @FXML
    protected Text errNumPlayer;

    @FXML
    protected Text textTokenColor;

    @FXML
    protected AnchorPane newPane;

    @FXML
    protected void onContinueButtonClick() {
        if(newGame.isSelected()){
            try {
                if(parseInt(numPlayers.getText())>0&&parseInt(numPlayers.getText())<5)
                {
                    if(tokenColor.getText().equalsIgnoreCase("RED")||tokenColor.getText().equalsIgnoreCase("BLUE")||tokenColor.getText().equalsIgnoreCase("GREEN")||tokenColor.getText().equalsIgnoreCase("YELLOW"))
                    {
                        Platform.runLater(() -> {
                            StageController.setScene(SceneType.WAITING_ROOM_SCENE);
                        });
                    }
                    else {
                        numPlayers.clear();
                        tokenColor.clear();
                        errTokenColor.setVisible(true);
                    }
                }
                else {
                    numPlayers.clear();
                    tokenColor.clear();
                    errNumPlayer.setVisible(true);
                }
            }
            catch (NumberFormatException e) {
                numPlayers.clear();
                tokenColor.clear();
                errNumPlayer.setVisible(true);
            }
        }
        else if(joinGame.isSelected()) {
            try {
                if(games.containsKey(parseInt(gameId.getText())))
                {
                    if(tokenColor.getText().equalsIgnoreCase("RED")||tokenColor.getText().equalsIgnoreCase("BLUE")||tokenColor.getText().equalsIgnoreCase("GREEN")||tokenColor.getText().equalsIgnoreCase("YELLOW"))
                    {
                        //TODO: join del player al game
                        Platform.runLater(() -> {
                            StageController.setScene(SceneType.WAITING_ROOM_SCENE);
                        });
                    }
                    else{
                        gameId.clear();
                        tokenColor.clear();
                        errTokenColor.setVisible(true);
                    }
                }
                else{
                    gameId.clear();
                    tokenColor.clear();
                    errGameId.setVisible(true);
                }
            }catch (NumberFormatException e) {
                gameId.clear();
                tokenColor.clear();
                errGameId.setVisible(true);
            }
        }
    }

    @FXML
    protected void clickedNew() {
        tokenColor.clear();
        gameId.clear();
        numPlayers.clear();

        textTokenColor.setVisible(true);
        tokenColor.setVisible(true);

        newPane.setVisible(true);

        newGame.setVisible(false);
        joinGame.setVisible(false);
        next.setVisible(true);
        back.setVisible(true);
    }

    @FXML
    protected void clickedJoin() {
        tokenColor.clear();
        gameId.clear();
        numPlayers.clear();

        textTokenColor.setVisible(true);
        tokenColor.setVisible(true);

        joinGame.setVisible(false);
        newGame.setVisible(false);

        joinPane.setVisible(true);
        gameId.clear();

        next.setVisible(true);
        back.setVisible(true);
    }

    @FXML
    protected void cleanErrGameId(KeyEvent actionEvent) {
        errGameId.setVisible(false);

    }

    @FXML
    protected void cleanErrTokenColor(KeyEvent actionEvent) {
        errTokenColor.setVisible(false);
    }

    @FXML
    protected void cleanErrNumPlayer(KeyEvent actionEvent) {
        errNumPlayer.setVisible(false);
    }



    @FXML
    protected void onBackButtonClick() {
        errTokenColor.setVisible(false);
        errNumPlayer.setVisible(false);
        errGameId.setVisible(false);

        textTokenColor.setVisible(false);
        tokenColor.setVisible(false);

        joinGame.setVisible(true);
        newGame.setVisible(true);
        joinGame.setSelected(false);
        newGame.setSelected(false);

        joinPane.setVisible(false);
        errGameId.setVisible(false);
        errTokenColor.setVisible(false);

        newPane.setVisible(false);

        next.setVisible(false);
        back.setVisible(false);
    }


        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        games = new HashMap<Integer,Integer>();
        games.put(0,3);
    }

    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {

    }

    @Override
    public void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

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
