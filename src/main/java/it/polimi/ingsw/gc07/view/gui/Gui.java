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
        StageController.setNickname(nickname);
    }

    public void setClient(Client client) {
        this.client = client;
        StageController.setGameView(client.getGameView());  // TODO platform.runlater
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
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().updateStarterCard(starterCard);
        }
    }

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().updateCardHand(hand, personalObjective);
        }
    }

    @Override
    public void receiveGeneralModelUpdate(GameState gameState, String currPlayer) {
        // TODO
        // dove mostriamo queste informazioni?
        // in alto a tutte le schermate, o solo PLAYER_SCENE?
    }

    @Override
    public void receivePenultimateRoundUpdate() {
        // TODO
        // dove mostriamo queste informazioni?
        // in alto a tutte le schermate, o solo PLAYER_SCENE?
    }

    @Override
    public void receiveAdditionalRoundUpdate() {
        // TODO
        // dove mostriamo queste informazioni?
        // in alto a tutte le schermate, o solo PLAYER_SCENE?
    }

    @Override
    public void receiveCommandResultUpdate(CommandResult commandResult) {
        // TODO se non è nella schermata giusta se lo perde, ok?
        if(StageController.getCurrentSceneType().equals(SceneType.PLAYER_SCENE)) {
            StageController.getController().updateCommandResult(commandResult);
        }
    }

    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGames) {
        if(StageController.getCurrentSceneType().equals(SceneType.LOBBY_SCENE)) {
            StageController.getController().displayExistingGames(existingGames);
        }
    }

    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        // TODO se non è nella schermata giusta, vedrebbe il gioco chiudersi e basta???
        // non va bene!
    }
}
