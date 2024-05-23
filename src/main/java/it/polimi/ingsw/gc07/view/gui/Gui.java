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
            }catch(InterruptedException e) {
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
        StageController.setNickname(nickname);
    }

    /**
     * Setter for client.
     * @param client client
     */
    public void setClient(Client client) {
        StageController.setGameView(client.getGameView());  // TODO platform.runlater
    }

    /**
     * Method used to start the interface.
     * It doesn't do anything, because Gui must start at application launch.
     */
    @Override
    public void startInterface() {
        // launch has already started interface
    }

    /**
     * Method used to run the join game interface, i.e. the lobby scene.
     */
    @Override
    public void runJoinGameInterface() {
        StageController.setScene(SceneType.LOBBY_SCENE);
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
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateDecks(topResourceDeck, topGoldDeck, faceUpResourceCard, faceUpGoldCard, commonObjective);
        }
    }

    /**
     * Method used to receive a game field update.
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    @Override
    public void receiveGameFieldUpdate(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

        // TODO devo aggiungere il nickname di chi manda,
        // se è il player, mando a player scene, altrimenti a other_player_Scene


        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().updateGameField(cardsContent, cardsFace, cardsOrder);
        }
    }

    /**
     * Method used to receive a starter card update.
     * @param starterCard starter card update
     */
    @Override
    public void receiveStarterCardUpdate(PlaceableCard starterCard) {
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
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.OTHER_PLAYER_SCENE)) {
            StageController.getController().updateCommandResult(commandResult);
        }
    }

    /**
     * Method used to receive an existing games update.
     * @param existingGames existing games
     */
    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGames) {
        if(StageController.getCurrentSceneType().equals(SceneType.LOBBY_SCENE)) {
            StageController.getController().displayExistingGames(existingGames);
        }
    }

    /**
     * Method used to receive winners update.
     * @param winners winners
     */
    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        StageController.setScene(SceneType.GAME_ENDED_SCENE);
        StageController.getController().displayWinners(winners);
    }
}
