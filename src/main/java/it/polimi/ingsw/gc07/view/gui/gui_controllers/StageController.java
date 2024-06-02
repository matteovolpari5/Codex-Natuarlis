package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.game_commands.RefreshGameViewCommand;
import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.view.gui.Gui;
import it.polimi.ingsw.gc07.view.gui.SceneType;
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

    /**
     * Setter method for nickname.
     * @param clientNickname nickname
     */
    public static void setNickname(String clientNickname) {
        nickname = clientNickname;
    }

    /**
     * Setter method for client.
     * @param clientRef client
     */
    public static void setClient(Client clientRef) {
        client = clientRef;
    }

    /**
     * Getter method for nickname.
     * @return nickname
     */
    public static String getNickname() {
        return nickname;
    }

    /**
     * Getter method for client.
     * @return client
     */
    public static Client getClient() {
        return client;
    }

    /**
     * Getter method for client's game view.
     * @return client's game view
     */
    public static GameView getGameView() {
        return client.getGameView();
    }

    /**
     * Getter method for the current scene type.
     * @return type of the current scene
     */
    public static SceneType getCurrentSceneType() {
        return currentSceneType;
    }

    /**
     * Getter method for the current gui controller.
     * @return current gui controller
     */
    public static GuiController getController() {
        return currentGuiController;
    }

    /**
     * Method used to set up the stage once the Gui is started.
     * @param stage primary stage
     */
    public synchronized static void setup(Stage stage) {
        // set stage
        currentStage = stage;
        // create scene loader
        currentSceneType = SceneType.LOBBY_SCENE;
        FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(currentSceneType.getFxmlScene()));
        // set scene
        try {
            currentScene = new Scene(sceneLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set controller
        currentGuiController = sceneLoader.getController();
        // set stage
        currentStage.setTitle(currentSceneType.getTitle());
        currentStage.setScene(currentScene);
        currentStage.setOnCloseRequest(event -> System.exit(0));
        currentStage.show();

        StageController.class.notifyAll();
    }

    /**
     * Method used to set a scene, except for OTHER_PLAYER_SCENE.
     * @param sceneType scene type
     */
    public synchronized static void setScene(SceneType sceneType) {
        while(currentScene == null) {
            try {
                StageController.class.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        assert(sceneType != SceneType.OTHER_PLAYER_SCENE);
        // create scene loader
        currentSceneType = sceneType;
        FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(currentSceneType.getFxmlScene()));
        // set scene
        try {
            currentScene.setRoot(sceneLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set controller
        currentGuiController = sceneLoader.getController();
        // set stage
        currentStage.setTitle(currentSceneType.getTitle());

        if(currentSceneType.equals(SceneType.PLAYER_SCENE)) {
            client.setAndExecuteCommand(new RefreshGameViewCommand(nickname));
        }
    }

    /**
     * Method used to set OTHER_PLAYER_SCENE on a specified player.
     * @param nickname nickname
     */
    public static void setOtherPlayerScene(String nickname) {
        // create scene loader
        currentSceneType = SceneType.OTHER_PLAYER_SCENE;
        FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(currentSceneType.getFxmlScene()));
        // set scene
        try {
            currentScene.setRoot(sceneLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set controller
        currentGuiController = sceneLoader.getController();
        currentGuiController.setNickname(nickname);
        // set stage
        currentStage.setTitle(currentSceneType.getTitle());

        // scene type is OTHER_PLAYER_SCENE
        client.setAndExecuteCommand(new RefreshGameViewCommand(StageController.nickname));
    }
}
