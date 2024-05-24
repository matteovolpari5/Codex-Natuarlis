package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.Client;
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
    private static Client client;
    private static String nickname;

    public static Client getClient() {
        return client;
    }

    public static void setNickname(String clientNickname) {
        nickname = clientNickname;
    }

    public static void setClient(Client clientRef) {
        client = clientRef;
    }

    public static String getNickname() {
        return nickname;
    }

    public static GameView getGameView() {
        return client.getGameView();
    }

    public static SceneType getCurrentSceneType() {
        return currentSceneType;
    }

    public static GuiController getController() {
        return currentGuiController;
    }

    public static void setup(Stage stage) {
        // set stage
        currentStage = stage;
        // create scene loader
        currentSceneType = SceneType.LOBBY_SCENE;
        FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(currentSceneType.getFxmlScene()));
        // set scene
        try {
            currentScene = new Scene(sceneLoader.load());
        } catch (IOException e) {
            // TODO gestire
            throw new RuntimeException(e);
        }
        // set controller
        currentGuiController = sceneLoader.getController();
        // set stage
        currentStage.setTitle(currentSceneType.getTitle());
        currentStage.setScene(currentScene);
        currentStage.setOnCloseRequest(event -> System.exit(0));    // TODO platform.exit?
        currentStage.show();
    }

    /**
     * Method used to set a scene, except for OTHER_PLAYER_SCENE.
     * @param sceneType scene type
     */
    public static void setScene(SceneType sceneType) {
        assert(sceneType != SceneType.OTHER_PLAYER_SCENE);
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
            currentStage.setOnCloseRequest(event -> System.exit(0));    // TODO platform.exit?
            currentStage.show();  // TODO probabilmente non serve
        });
    }

    /**
     * Method used to set OTHER_PLAYER_SCENE on a specified player.
     * @param nickname nickname
     */
    public static void setOtherPlayerScene(String nickname) {
        Platform.runLater(() -> {
            // create scene loader
            currentSceneType = SceneType.OTHER_PLAYER_SCENE;
            FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(currentSceneType.getFxmlScene()));
            // set controller
            currentGuiController = sceneLoader.getController();
            currentGuiController.setNickname(nickname);
            // set scene
            try {
                currentScene.setRoot(sceneLoader.load());
            } catch (IOException e) {
                // TODO gestire
                throw new RuntimeException(e);
            }
            currentStage.setTitle(currentSceneType.getTitle());

            // TODO inutile, già fatto sopra
            currentStage.setOnCloseRequest(event -> System.exit(0));    // TODO platform.exit?
            currentStage.show();  // TODO probabilmente non serve
        });
    }
}
