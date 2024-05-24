package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.game_commands.DisplayGamesCommand;
import it.polimi.ingsw.gc07.game_commands.JoinExistingGameCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;


public class LobbySceneController implements Initializable, GuiController {
    /**
     * se select-->join game
     * se non select-->new game
     */
    @FXML
    public ToggleButton choice;
    @FXML
    public Text textJoinGame;
    @FXML
    public Text textNewGame;
    @FXML
    public AnchorPane joinPane;
    @FXML
    public ListView<String> gameList;
    @FXML
    public AnchorPane newPane;
    @FXML
    public ChoiceBox<Integer> boxNumPlayers;
    @FXML
    public Text textTokenColor;
    @FXML
    public Button next;
    @FXML
    public ChoiceBox<TokenColor> boxTokenColor;
    @FXML
    public AnchorPane screenPane;
    @FXML
    public Text textInsertNumPlayers;

    public int idGame;


    @FXML
    protected void onContinueButtonClick() {
        if(!choice.isSelected()){
            int numPlayers = boxNumPlayers.getValue();
            TokenColor tokenColor = boxTokenColor.getValue();
            if(boxNumPlayers.getValue()>0&&boxNumPlayers.getValue()<5)
            {
                if(boxTokenColor.getValue().equals(TokenColor.GREEN)||boxTokenColor.getValue().equals(TokenColor.BLUE)||boxTokenColor.getValue().equals(TokenColor.RED)||boxTokenColor.getValue().equals(TokenColor.YELLOW))
                {
                    StageController.getClient().setAndExecuteCommand(new JoinNewGameCommand(StageController.getNickname(),tokenColor,numPlayers));
                    screenPane.setVisible(false);
                }
            }
        }
        else if(choice.isSelected()) {
            TokenColor tokenColor = boxTokenColor.getValue();
            if(boxTokenColor.getValue().equals(TokenColor.GREEN)||boxTokenColor.getValue().equals(TokenColor.BLUE)||boxTokenColor.getValue().equals(TokenColor.RED)||boxTokenColor.getValue().equals(TokenColor.YELLOW))
            {
                // TODO: check unique token color
                //StageController.getClient().setAndExecuteCommand(new JoinExistingGameCommand(StageController.getNickname(),tokenColor,idGame));
                screenPane.setVisible(false);
            }
        }
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
    public void updateCommandResult(CommandResult commandResult) {

    }

    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        gameList.getItems().clear();
        ObservableList<String> listViewComponent = FXCollections.observableArrayList();
        listViewComponent.add("GAMEID                                                                              NUMBER OF PLAYERS");
        for(Integer id : existingGamesPlayerNumber.keySet()){
            String newLine = id+"                                                                                              "+existingGamesPlayerNumber.get(id);
            listViewComponent.add(newLine);
        }
        gameList.setItems(listViewComponent);
    }

    @Override
    public void displayWinners(List<String> winners) {

    }

    @FXML
    public void selectJoin(MouseEvent mouseEvent) {
        // TODO CHIEDERE: arriva update anche se un game esce dagli existing games? //
        boxTokenColor.getItems().clear();
        StageController.getClient().setAndExecuteCommand(new DisplayGamesCommand(StageController.getNickname()));
        next.setVisible(true);
        next.setTextFill(Paint.valueOf("#b401a8"));
        boxTokenColor.setVisible(true);
        textTokenColor.setVisible(true);

        textTokenColor.setFill(Paint.valueOf("#b401a8"));
        textNewGame.setOpacity(0.4);
        textJoinGame.setOpacity(1);
        boxNumPlayers.setVisible(false);
        textInsertNumPlayers.setVisible(false);

        joinPane.setVisible(true);
        newPane.setVisible(true);
        choice.setSelected(true);
    }

    @FXML
    public void selectNew(MouseEvent mouseEvent) {
        boxTokenColor.getItems().clear();

        ObservableList<TokenColor> listColor = FXCollections.observableArrayList();
        listColor.add(TokenColor.RED);
        listColor.add(TokenColor.GREEN);
        listColor.add(TokenColor.BLUE);
        listColor.add(TokenColor.YELLOW);
        boxTokenColor.setItems(listColor);

        ObservableList<Integer> listNumPlayer = FXCollections.observableArrayList();
        listNumPlayer.add(2);
        listNumPlayer.add(3);
        listNumPlayer.add(4);
        boxNumPlayers.setItems(listNumPlayer);

        next.setVisible(true);
        next.setTextFill(Paint.valueOf("#0008db"));
        boxTokenColor.setVisible(true);
        textTokenColor.setVisible(true);

        boxNumPlayers.setVisible(true);
        textInsertNumPlayers.setVisible(true);
        textTokenColor.setFill(Paint.valueOf("#0008db"));
        textNewGame.setOpacity(1);
        textJoinGame.setOpacity(0.4);

        joinPane.setVisible(false);
        newPane.setVisible(true);
        choice.setSelected(false);
    }

    @FXML
    public void clickedGame(MouseEvent mouseEvent) {
        //update della lista di token color//
        boxTokenColor.getItems().clear();
        int i=0,numSize=0;
        while(gameList.getSelectionModel().getSelectedItem().charAt(i) != ' ')
        {
            numSize++;
            i++;
        }
        if(gameList.getSelectionModel().getSelectedItem().charAt(0) != 'G')
        {
            //calcolo idGame
            idGame=0;
            for(int j=0;j<numSize;j++)
            {
                if(gameList.getSelectionModel().getSelectedItem().charAt(j) == ' ')
                {
                    idGame += gameList.getSelectionModel().getSelectedItem().charAt(j)*(numSize-j);
                }
            }
            System.out.println(idGame);
            //TODO: add in boxTokenColor i colori giusti in base al game idGame
            // TODO: serve metodo getFreeTokenColor(idGame)
        }
    }
}
