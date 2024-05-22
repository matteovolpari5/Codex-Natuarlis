package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.model_view.DeckView;
import it.polimi.ingsw.gc07.model_view.GameFieldView;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.gui.SceneType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class StageController {
    private static Stage currentStage;
    private static Scene currentScene;
    private static SceneType currentSceneType;
    private static GuiController currentGuiController;
    private static GameView gameView;
    private static String nickname;

    public static void setNickname(String clientNickname) {
        nickname = clientNickname;
    }

    public static void setGameView(GameView clientGameView) {
        gameView = clientGameView;
    }

    public static SceneType getCurrentSceneType() {
        return currentSceneType;
    }

    public static GuiController getController() {
        return currentGuiController;
    }

    public static void setup(Stage stage) {
        Platform.runLater(() -> {
            // set stage
            currentStage = stage;
            // create scene loader
            currentSceneType = SceneType.LOBBY_SCENE;
            FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(currentSceneType.getFxmlScene()));
            // set controller
            currentGuiController = sceneLoader.getController();
            // set scene
            try {
                currentScene = new Scene(sceneLoader.load());
            } catch (IOException e) {
                // TODO gestire
                throw new RuntimeException(e);
            }
            currentStage.setTitle(currentSceneType.getTitle());
            currentStage.setScene(currentScene);
            currentStage.setOnCloseRequest(event -> System.exit(0));    // TODO platform.exit?
            currentStage.show();
        });
    }

    public static void setScene(SceneType sceneType) {
        Platform.runLater(() -> {
            // create scene loader
            currentSceneType = sceneType;
            FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(currentSceneType.getFxmlScene()));
            // set controller
            currentGuiController = sceneLoader.getController();
            // set scene
            try {
                currentScene.setRoot(sceneLoader.load());
            } catch (IOException e) {
                // TODO gestire
                throw new RuntimeException(e);
            }
            currentStage.setTitle(currentSceneType.getTitle());

            // TODO inutile, già fatto sopra
            //currentStage.setOnCloseRequest(event -> System.exit(0));    // TODO platform.exit?
            //currentStage.show();  // TODO probabilmente non serve
        });

        // TODO Platform.runLater ???
        switch(currentSceneType) {
            case SceneType.LOBBY_SCENE:
                // don't display anything
                break;
            case SceneType.PLAYER_SCENE:
                // set game field data
                GameFieldView gameFieldView =  gameView.getGameField(nickname);
                currentGuiController.updateGameField(
                        gameFieldView.getCardsContent(),
                        gameFieldView.getCardsFace(),
                        gameFieldView.getCardsOrder());
                // set decks data
                DeckView deckView = gameView.getDeckView();
                currentGuiController.updateDecks(
                        deckView.getTopResourceDeck(),
                        deckView.getTopGoldDeck(),
                        deckView.getFaceUpResourceCard(),
                        deckView.getFaceUpGoldCard(),
                        deckView.getCommonObjective()
                );
                // set current hand data
                currentGuiController.updateCardHand(gameView.getCurrentHand(), gameView.getSecretObjective());
                // set scores
                currentGuiController.updateScore( gameView.getPlayersScores(), gameView.getPlayersTokenColors());

                // TODO informazioni partita, quali mettiamo ???

                break;
            case SceneType.CHAT_SCENE:
                // send full chat
                currentGuiController.displayFullChat(gameView.getOwnerMessages());
                break;
            case SceneType.OTHER_PLAYER_SCENE:

                // TODO devo sapere il player di cui voglio mostrare il campo
                // serve un metodo setScene diverso, che prende come parametro
                // il nome del player di cui voglio mostrare il gioco
                // a quel punto lo avrei

                break;
        }
    }
}
