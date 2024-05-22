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
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public  class Gui extends Application implements Ui {
    private static Gui guiInstance = null;
    private String nickname;
    private Client client;

    @Override
    public void init() {
        synchronized(Gui.class) {
            guiInstance = this;
            Gui.class.notifyAll();
        }
    }

    @Override
    public void start(Stage stage) {
        StageController.setup(stage);
        StageController.setNickname(nickname);
        StageController.setGameView(client.getGameView());  // TODO platform.runlater
    }

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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void runJoinGameInterface() {
        // scene already present, loaded at launch
    }

    @Override
    public void runGameInterface() {
        // change scene to PlayerScene
        StageController.setScene(SceneType.PLAYER_SCENE);
    }

    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().updateScore(playerScores, playerTokenColors);
        }
    }

    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE) ||
                StageController.getCurrentSceneType().equals(SceneType.CHAT_SCENE)) {
            StageController.getController().addMessage(chatMessage);
        }
    }

    @Override
    public void receiveDecksUpdate(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().updateDecks(topResourceDeck, topGoldDeck, faceUpResourceCard, faceUpGoldCard, commonObjective);
        }
    }

    @Override
    public void receiveGameFieldUpdate(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().updateGameField(cardsContent, cardsFace, cardsOrder);
        }
        // TODO nota su documento riguardo al game field degli altri giocatori
    }

    @Override
    public void receiveStarterCardUpdate(PlaceableCard starterCard) {
        // TODO
    }

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        // TODO
    }

    @Override
    public void receiveGeneralModelUpdate(GameState gameState, String currPlayer) {
        // TODO
    }

    @Override
    public void receivePenultimateRoundUpdate() {
        // TODO
    }

    @Override
    public void receiveAdditionalRoundUpdate() {
        // TODO
    }

    @Override
    public void receiveCommandResultUpdate(CommandResult commandResult) {
        // TODO
    }

    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGames) {
        // TODO
    }

    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        // TODO
    }
}
