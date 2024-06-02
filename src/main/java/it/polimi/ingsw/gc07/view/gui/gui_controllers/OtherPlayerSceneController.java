package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.view.gui.SceneType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

public class OtherPlayerSceneController implements GuiController, Initializable {
    String otherPlayerNickname;
    private final int BOARD_SIZE = 80;
    @FXML
    protected ListView<String> myUpdates;
    private final ObservableList<String> updatesItem = FXCollections.observableArrayList();
    @FXML
    protected Label currentPlayer;
    @FXML
    protected Label gameState;
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
    @FXML
    protected ImageView commonObjective1;
    @FXML
    protected ImageView commonObjective2;
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
    protected ImageView tokenColor1;
    @FXML
    protected ImageView tokenColor2;
    @FXML
    protected ImageView tokenColor3;
    @FXML
    protected ImageView tokenColor4;
    @FXML
    protected HBox nickContainer;
    @FXML
    protected GridPane gridPaneBoard;
    @FXML
    protected GridPane scoreGrid;
    private final ImageView[][] imageViews = new ImageView[BOARD_SIZE][BOARD_SIZE];
    private final ImageView [][] scoreImages = new ImageView[21][8];
    /**
     * List of images containing the tokenColor of players.
     */
    private final List<ImageView> tokenColorsList = new ArrayList<>();
    /**
     * List of labels containing the connection/stall status of players.
     */
    private final List<Label> statusLabels = new ArrayList<>();
    /**
     * List of labels containing nicknames.
     */
    private final List<Label> nicknameLabels = new ArrayList<>();

    @FXML
    protected void goToOtherGameField(MouseEvent e){
        Platform.runLater(() -> {
            String otherGameFieldNickname;
            if (e.getSource().equals(nickname1)) {
                otherGameFieldNickname = nickname1.getText();
            } else if (e.getSource().equals(nickname2)) {
                otherGameFieldNickname = nickname2.getText();
            } else if (e.getSource().equals(nickname3)) {
                otherGameFieldNickname = nickname3.getText();
            } else if (e.getSource().equals(nickname4)) {
                otherGameFieldNickname = nickname4.getText();
            } else {
                return;
            }
            StageController.setOtherPlayerScene(otherGameFieldNickname);
        });
    }
    @FXML
    protected void onGoBackButtonClick(){
        Platform.runLater(() -> {
            StageController.setScene(SceneType.PLAYER_SCENE);
        });
    }

    /**
     * Method to round the corners of the cards.
     * @param card image of the card to be rounded
     */
    private void setRoundedCorners(ImageView card) {
        Rectangle imageClip = new Rectangle(card.getFitWidth(), card.getFitHeight());
        imageClip.setArcHeight(20);
        imageClip.setArcWidth(20);
        card.setClip(imageClip);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myUpdates.setItems(updatesItem);
        nicknameLabels.add(nickname1);
        nicknameLabels.add(nickname2);
        nicknameLabels.add(nickname3);
        nicknameLabels.add(nickname4);
        statusLabels.add(nickStatus1);
        statusLabels.add(nickStatus2);
        statusLabels.add(nickStatus3);
        statusLabels.add(nickStatus4);
        tokenColorsList.add(tokenColor1);
        tokenColorsList.add(tokenColor2);
        tokenColorsList.add(tokenColor3);
        tokenColorsList.add(tokenColor4);
        for (int row = 0; row < BOARD_SIZE; row++) {
            gridPaneBoard.getRowConstraints().add(new RowConstraints(69));
            gridPaneBoard.getColumnConstraints().add(new ColumnConstraints(133));
            for (int col = 0; col < BOARD_SIZE; col++) {
                ImageView gridImage = new ImageView();
                gridImage.setFitHeight(114.0);
                gridImage.setFitWidth(171.0);
                setRoundedCorners(gridImage);
                gridPaneBoard.add(gridImage, row, col);
                GridPane.setHalignment(gridImage, Pos.CENTER.getHpos());
                GridPane.setValignment(gridImage, Pos.CENTER.getVpos());
                imageViews[row][col] = gridImage;
            }
        }
        setRoundedCorners(commonObjective1);
        setRoundedCorners(commonObjective2);
        setRoundedCorners(revealedGold1);
        setRoundedCorners(revealedGold2);
        setRoundedCorners(revealedResource1);
        setRoundedCorners(revealedResource2);
        setRoundedCorners(topDeckGold);
        setRoundedCorners(topDeckResource);
        for(int i = 0; i < scoreGrid.getRowCount(); i++){
            for (int j = 0; j < scoreGrid.getColumnCount(); j++){
                ImageView pointsImage = new ImageView();
                pointsImage.setFitWidth(30);
                pointsImage.setFitHeight(30);
                scoreGrid.add(pointsImage, j, i);
                scoreImages[i][j] = pointsImage;
                GridPane.setHalignment(scoreImages[i][j], Pos.CENTER.getHpos());
                GridPane.setValignment(scoreImages[i][j], Pos.CENTER.getVpos());
            }
        }
    }

    /**
     * Method used to display a score update.
     * @param playerScore map containing players' scores
     * @param playerTokenColor map containing players' token colors
     */
    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {
        for (int i = 0; i < scoreGrid.getRowCount(); i++){
            for(int j = 0; j < scoreGrid.getColumnCount(); j++){
                scoreImages[i][j].setVisible(false);
            }
        }
        int x,y;
        for (String nickname: playerScore.keySet()){
            x = BoardGridLayout.valueOf(playerTokenColor.get(nickname)+ "_"+ playerScore.get(nickname)).getX();
            y = BoardGridLayout.valueOf(playerTokenColor.get(nickname)+"_" +playerScore.get(nickname)).getY();

            scoreImages[x][y].setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/" + playerTokenColor.get(nickname).toString().toLowerCase() + ".png")).toExternalForm()));
            scoreImages[x][y].setVisible(true);
        }
    }

    /**
     * Method used to display a new chat message.
     * @param chat new chat message
     */
    @Override
    public void addMessage(ChatMessage chat) {}

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
        int topDeckId;
        if(topDeckResource!=null) {
            topDeckId = topResourceDeck.getId();
            topDeckResource.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + topDeckId + ".png")).toExternalForm()));
        }
        if(topGoldDeck!=null) {
            topDeckId = topGoldDeck.getId();
            topDeckGold.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + topDeckId + ".png")).toExternalForm()));
        }
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
        if(otherPlayerNickname != null && otherPlayerNickname.equals(nickname)) {
            List<Integer> xPosition = new ArrayList<>();
            List<Integer> yPosition = new ArrayList<>();
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if(cardsOrder[i][j]!=0) {
                        while (xPosition.size() < cardsOrder[i][j]) {
                            xPosition.add(-1);
                            yPosition.add(-1);
                        }
                        xPosition.set(cardsOrder[i][j]-1, i);
                        yPosition.set(cardsOrder[i][j]-1, j);
                    }
                }
            }
            int imageId;
            for (int i = 0; i < xPosition.size() ; i++) {
                imageId = cardsContent[xPosition.get(i)][yPosition.get(i)].getId();
                ImageView gridImage = imageViews[yPosition.get(i)][xPosition.get(i)];
                gridImage.setOpacity(1);
                gridImage.toFront();
                // starter cards have flipped front and back
                if(xPosition.get(i) == 40 && yPosition.get(i) == 40){
                    if(!cardsFace[xPosition.get(i)][yPosition.get(i)]){
                        gridImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + imageId +".png")).toExternalForm()));
                    }
                    else{
                        gridImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
                    }
                }
                else {
                    if (!cardsFace[xPosition.get(i)][yPosition.get(i)]) {
                        gridImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Front/" + imageId + ".png")).toExternalForm()));
                    } else {
                        gridImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/Card/Back/" + imageId + ".png")).toExternalForm()));
                    }
                }
            }
        }
    }

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    @Override
    public void updateStarterCard(PlaceableCard starterCard) {}

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, List<ObjectiveCard> personalObjective) {}

    /**
     * Method used to display updated game infos.
     *
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {
        // game state
        this.gameState.setText("Game state: "+ gameState);
        // current player
        if (currPlayer != null){
            currentPlayer.setText("Current player: " + currPlayer);
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

    @Override
    public void setNickname(String nickname) {
        otherPlayerNickname = nickname;
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
        for(int i = 0; i < nicknameLabels.size(); i++){
            if(nicknameLabels.get(i).getText().equals(nickname)){
                if(!value){
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
     * @param value    new stall value
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
     * @param tokenColors token colors
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
                    if (!connectionValues.get(newNickname)) {
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
                    tokenColorsList.get(i).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/" + tokenColors.get(newNickname).toString().toLowerCase() + ".png")).toExternalForm()));
                }
            }
            if(!found){
                // insert the nickname in the first free label
                for(int j = 0; j < nicknameLabels.size(); j++){
                    if(nicknameLabels.get(j).getText().equals("Player")){
                        nicknameLabels.get(j).setText(newNickname);
                        nicknameLabels.get(j).setVisible(true);
                        // set token color image
                        tokenColorsList.get(j).setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/gc07/graphic_resources/" + tokenColors.get(newNickname).toString().toLowerCase() + ".png")).toExternalForm()));
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
