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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

import static java.lang.Integer.parseInt;


public class LobbySceneController implements Initializable, GuiController {
    /**
     * Attribute used to choice if the player select "new game" or "join game".
     * if select-->join game.
     * if not select-->new game.
     */
    @FXML
    public ToggleButton choice;

    /**
     * Attribute that represent the text "Join game".
     */
    @FXML
    public Text textJoinGame;

    /**
     * Attribute that represent the text "New game".
     */
    @FXML
    public Text textNewGame;

    /**
     * Attribute that represent the pane that contain object only for the "Join Game" section.
     */
    @FXML
    public AnchorPane joinPane;

    /**
     * Attribute that represent the list of existing games.
     */
    @FXML
    public ListView<String> gameList;

    /**
     * Attribute that represent the pane that contain object only for the "New Game" section.
     */
    @FXML
    public AnchorPane newPane;

    /**
     * Attribute that represent the box where a player choice the number of players.
     */
    @FXML
    public ChoiceBox<Integer> boxNumPlayers;

    /**
     * Attribute that represent the text "Insert the token color:".
     */
    @FXML
    public Text textTokenColor;

    /**
     * Attribute that represent the button used to confirm the entry info.
     */
    @FXML
    public Button next;

    /**
     * Attribute that represent the box where a player choice the token color.
     */
    @FXML
    public ChoiceBox<TokenColor> boxTokenColor;

    /**
     * Attribute that represent the pane that contain all the objects in the scene.
     */
    @FXML
    public AnchorPane screenPane;

    /**
     * Attribute that represent the text "Insert the number of players:".
     */
    @FXML
    public Text textInsertNumPlayers;

    /**
     * Id of the game selected.
     */
    public int idGame;

    /**
     * Map that contains the id game and the list of token color get.
     */
    public Map<Integer, List<TokenColor>> gettedTokenColor;

    /**
     * Attribute that represent the game image.
     */
    @FXML
    public ImageView gameImage;

    /**
     * Action executed when the continue button is clicked.
     * It's checked which choice is made, then there is a little client check on the info submitted and then the command is set and executed.
     */
    @FXML
    protected void onContinueButtonClick() {
        if(!choice.isSelected()){
            if(boxNumPlayers.getValue() != null && boxTokenColor.getValue() != null)
            {
                int numPlayers = boxNumPlayers.getValue();
                TokenColor tokenColor = boxTokenColor.getValue();
                if(boxNumPlayers.getValue() > 0 && boxNumPlayers.getValue() < 5)
                {
                    if(boxTokenColor.getValue().equals(TokenColor.GREEN)||boxTokenColor.getValue().equals(TokenColor.BLUE)||boxTokenColor.getValue().equals(TokenColor.RED)||boxTokenColor.getValue().equals(TokenColor.YELLOW))
                    {
                        StageController.getClient().setAndExecuteCommand(new JoinNewGameCommand(StageController.getNickname(),tokenColor,numPlayers));
                        screenPane.setVisible(false);
                    }
                }
            }
        }
        else if(choice.isSelected()) {
            if(boxTokenColor.getValue() != null && !gameList.getSelectionModel().getSelectedItems().isEmpty())
            {
                TokenColor tokenColor = boxTokenColor.getValue();
                if(boxTokenColor.getValue().equals(TokenColor.GREEN)||boxTokenColor.getValue().equals(TokenColor.BLUE)||boxTokenColor.getValue().equals(TokenColor.RED)||boxTokenColor.getValue().equals(TokenColor.YELLOW))
                {
                    StageController.getClient().setAndExecuteCommand(new JoinExistingGameCommand(StageController.getNickname(),tokenColor,idGame));
                    screenPane.setVisible(false);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<TokenColor> listColor = FXCollections.observableArrayList();
        listColor.add(TokenColor.RED);
        listColor.add(TokenColor.GREEN);
        listColor.add(TokenColor.BLUE);
        listColor.add(TokenColor.YELLOW);
        boxTokenColor.getItems().clear();
        boxTokenColor.setItems(listColor);

        ObservableList<Integer> listNumPlayer = FXCollections.observableArrayList();
        listNumPlayer.add(2);
        listNumPlayer.add(3);
        listNumPlayer.add(4);
        boxNumPlayers.setItems(listNumPlayer);
    }

    /**
     * Method used to display a user existing and free games.
     * @param existingGamesPlayerNumber players number in existing games
     * @param existingGamesTokenColor taken token colors in existing games
     */
    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        Platform.runLater(() -> {
            gameList.getItems().clear();
            ObservableList<String> listViewComponent = FXCollections.observableArrayList();
            listViewComponent.add("GAMEID                                                                              NUMBER OF PLAYERS");
            for(Integer id : existingGamesPlayerNumber.keySet()){
                String newLine = id+"                                                                                              "+existingGamesPlayerNumber.get(id);
                listViewComponent.add(newLine);
            }
            gameList.setItems(listViewComponent);
            gettedTokenColor=existingGamesTokenColor;
        });
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
     * @param nickname nickname
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    @Override
    public void updateGameField(String nickname, PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

    }

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    @Override
    public void updateStarterCard(PlaceableCard starterCard) {

    }

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, List<ObjectiveCard> personalObjective) {

    }

    /**
     * Method used to display updated game infos.
     *
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {

    }

    /**
     * Method used to display a penultimate round update.
     */
    @Override
    public void setPenultimateRound() {

    }

    /**
     * Method used to display an additional round update.
     */
    @Override
    public void setAdditionalRound() {

    }

    /**
     * Method used to display the last command result.
     * @param commandResult command result
     */
    @Override
    public void updateCommandResult(CommandResult commandResult) {

    }

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    @Override
    public void displayWinners(List<String> winners) {

    }

    /**
     * Method executed after that the text "Join game" is clicked.
     * The "newPane" is set invisible, the text "New game" is blurred, the "joinPane" is set visible.
     */
    @FXML
    public void selectJoin(MouseEvent mouseEvent) {
        boxTokenColor.getItems().clear();
        StageController.getClient().setAndExecuteCommand(new DisplayGamesCommand(StageController.getNickname()));
        next.setVisible(true);
        boxTokenColor.setVisible(true);
        textTokenColor.setVisible(true);

        textNewGame.setOpacity(0.4);
        textJoinGame.setOpacity(1);
        boxNumPlayers.setVisible(false);
        textInsertNumPlayers.setVisible(false);

        textTokenColor.setFill(Paint.valueOf("#288a2f"));
        next.setTextFill(Paint.valueOf("#288a2f"));
        joinPane.setVisible(true);
        newPane.setVisible(true);
        choice.setSelected(true);
    }

    /**
     * Method executed after that the text "New game" is clicked.
     * The "joinPane" is set invisible, the text "Join game" is blurred, the "newPane" is set visible.
     */
    @FXML
    public void selectNew(MouseEvent mouseEvent) {
        ObservableList<TokenColor> listColor = FXCollections.observableArrayList();
        listColor.add(TokenColor.RED);
        listColor.add(TokenColor.GREEN);
        listColor.add(TokenColor.BLUE);
        listColor.add(TokenColor.YELLOW);
        boxTokenColor.getItems().clear();
        boxTokenColor.setItems(listColor);

        ObservableList<Integer> listNumPlayer = FXCollections.observableArrayList();
        listNumPlayer.add(2);
        listNumPlayer.add(3);
        listNumPlayer.add(4);
        boxNumPlayers.setItems(listNumPlayer);

        next.setVisible(true);
        boxTokenColor.setVisible(true);
        textTokenColor.setVisible(true);

        boxNumPlayers.setVisible(true);
        textInsertNumPlayers.setVisible(true);
        textNewGame.setOpacity(1);
        textJoinGame.setOpacity(0.4);
        textTokenColor.setFill(Paint.valueOf("#d3251b"));
        next.setTextFill(Paint.valueOf("#d3251b"));

        joinPane.setVisible(false);
        newPane.setVisible(true);
        choice.setSelected(false);
    }

    /**
     * Method executed after the player click (choice) a row in the list of existing game.
     */
    @FXML
    public void clickedGame(MouseEvent mouseEvent) {
        //update della lista di token color
        boxTokenColor.getItems().clear();
        int i=0,numSize=0;
        if(gameList.getSelectionModel().getSelectedItem()!=null){
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
                    if(gameList.getSelectionModel().getSelectedItem().charAt(j) != ' ')
                    {
                        idGame += (int) (parseInt(String.valueOf(gameList.getSelectionModel().getSelectedItem().charAt(j)))*Math.pow(10,j));
                    }
                }
                ObservableList<TokenColor> listColor = FXCollections.observableArrayList();
                listColor.add(TokenColor.RED);
                listColor.add(TokenColor.GREEN);
                listColor.add(TokenColor.BLUE);
                listColor.add(TokenColor.YELLOW);
                // show the correct token colors //
                for(TokenColor c : listColor)
                {
                    if(!gettedTokenColor.get(idGame).contains(c))
                    {
                        boxTokenColor.getItems().add(c);
                    }
                }
            }
        }
    }

    /**
     * Method used to set the nickname.
     * @param nickname nickname
     */
    @Override
    public void setNickname(String nickname) {
        // don't use
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
    public void setGameId(int gameId) {}

    /**
     * Method used to display a new connection value.
     * @param nickname nickname
     * @param value    new connection value
     */
    @Override
    public void receiveConnectionUpdate(String nickname, boolean value) {
        // don't use
    }

    /**
     * Method used to display a new stall value.
     * @param nickname nickname
     * @param value    new stall value
     */
    @Override
    public void receiveStallUpdate(String nickname, boolean value) {
        // don't use
    }

    /**
     * Method used to display players in the game.
     * @param nicknames nicknames
     * @param connectionValues connection values
     * @param stallValues stall values
     */
    @Override
    public void receivePlayersUpdate(Map<String, TokenColor> nicknames, Map<String, Boolean> connectionValues, Map<String, Boolean> stallValues) {
        // don't use
    }
}

