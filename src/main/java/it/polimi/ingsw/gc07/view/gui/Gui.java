package it.polimi.ingsw.gc07.view.gui;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.view.Ui;
import it.polimi.ingsw.gc07.view.gui.gui_controllers.StageController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public  class Gui extends Application implements Ui {
    /**
     * Gui instance, automatically created al application launch.
     */
    private static Gui guiInstance = null;
    /**
     * Nickname of Gui's owner.
     */
    private String nickname;

    /**
     * Init method, called at application launch.
     * Sets the value of gui instance, created ad launch.
     */
    @Override
    public void init() {
        synchronized(Gui.class) {
            guiInstance = this;
            Gui.class.notifyAll();
        }
    }

    /**
     * Start method, called at application launch.
     * Start the Gui interface, displaying the lobby.
     */
    @Override
    public void start(Stage stage) {
        StageController.setup(stage);
    }

    /**
     * Getter method for the Gui instance, automatically created at launch.
     * @return gui instance
     */
    public synchronized static Gui getGuiInstance() {
        while(guiInstance == null) {
            try {
                Gui.class.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        return guiInstance;
    }

    /**
     * Setter for client's nickname.
     * @param nickname nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        StageController.setNickname(nickname);
    }

    /**
     * Setter for client.
     * @param client client
     */
    public void setClient(Client client) {
        StageController.setClient(client);
    }

    /**
     * Method used to run the join game interface, i.e. the lobby scene.
     */
    @Override
    public void runJoinGameInterface() {
        // launch has already started interface
    }

    /**
     * Method used to run the game interface, i.e. player scene.
     */
    @Override
    public void runGameInterface() {
        // change scene to PlayerScene
        StageController.setScene(SceneType.PLAYER_SCENE);
    }

    /**
     * Method used to receive a score update.
     * @param playerScores players' scores
     * @param playerTokenColors players' token colors
     */
    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateScore(playerScores, playerTokenColors);
        }
    }

    /**
     * Method used to receive a chat message.
     * @param chatMessage chat message
     */
    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
        StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().addMessage(chatMessage);
        }
    }

    /**
     * Method used to receive a deck update.
     * @param topResourceDeck top resource deck
     * @param topGoldDeck top gold deck
     * @param faceUpResourceCard face up resource
     * @param faceUpGoldCard face up gold
     * @param commonObjective common objective
     */
    @Override
    public void receiveDecksUpdate(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateDecks(topResourceDeck, topGoldDeck, faceUpResourceCard, faceUpGoldCard, commonObjective);
        }
    }

    /**
     * Method used to receive a game field update.
     * @param nickname nickname
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    @Override
    public void receiveGameFieldUpdate(String nickname, PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(nickname.equals(this.nickname) && StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            // player's game field update, will be sent to PlayerSceneController
            StageController.getController().updateGameField(cardsContent, cardsFace, cardsOrder);
        }else if(!nickname.equals(this.nickname) && StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            // other player's game field update, will be sent to OtherPlayerSceneController
            StageController.getController().updateGameField(cardsContent, cardsFace, cardsOrder);
        }
    }

    /**
     * Method used to receive a starter card update.
     * @param starterCard starter card update
     */
    @Override
    public void receiveStarterCardUpdate(PlaceableCard starterCard) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateStarterCard(starterCard);
        }
    }

    /**
     * Method used to receive a card hand update.
     * @param hand card hand
     * @param personalObjective persona objective
     */
    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateCardHand(hand, personalObjective);
        }
    }

    /**
     * Method used to receive a general model update.
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void receiveGeneralModelUpdate(GameState gameState, String currPlayer) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateGameInfo(gameState, currPlayer);
        }
    }

    /**
     * Method used to receive penultimate round update.
     */
    @Override
    public void receivePenultimateRoundUpdate() {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().setPenultimateRound();
        }
    }

    /**
     * Method used to receive additional round update.
     */
    @Override
    public void receiveAdditionalRoundUpdate() {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().setAdditionalRound();
        }
    }

    /**
     * Method used to receive a command result update.
     * @param commandResult command result
     */
    @Override
    public void receiveCommandResultUpdate(CommandResult commandResult) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateCommandResult(commandResult);
        }
    }

    /**
     * Method used to receive an existing games update.
     * @param existingGamesPlayerNumber existing games player number
     * @param existingGamesTokenColor  existing games token color
     */
    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.LOBBY_SCENE)) {
            StageController.getController().displayExistingGames(existingGamesPlayerNumber, existingGamesTokenColor);
        }
    }

    /**
     * Method used to receive winners update.
     * @param winners winners
     */
    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        StageController.setScene(SceneType.GAME_ENDED_SCENE);
        StageController.getController().displayWinners(winners);
    }

    /**
     * Method used to notify the player he has received a full chat update.
     * @param chatMessages full chat update
     */
    @Override
    public void receiveFullChatUpdate(List<ChatMessage> chatMessages) {
        if(StageController.getController() == null) {
            // starting phase
            return;
        }
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().setFullChat(chatMessages);
        }
    }
}
