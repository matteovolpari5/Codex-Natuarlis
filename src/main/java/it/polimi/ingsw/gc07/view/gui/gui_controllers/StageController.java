package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.view.gui.Gui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class StageController {
    private static Stage currentStage;
    private static Scene currentScene;
    private static GuiController guiController;

    public static GuiController getController() {
        return guiController;
    }

    public static void setup(Stage stage, String fxmlScene, String title) throws IOException {
        // set stage
        currentStage = stage;
        // create scene loader
        FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(fxmlScene));
        // set controller
        guiController = sceneLoader.getController();
        // set scene
        currentScene = new Scene(sceneLoader.load());
        currentStage.setTitle(title);
        currentStage.setScene(currentScene);
        currentStage.setOnCloseRequest(event -> System.exit(0));
        currentStage.show();
    }

    public static void setScene(String fxmlScene, String title) throws IOException {
        // create scene loader
        FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(fxmlScene));
        // set controller
        guiController = sceneLoader.getController();
        // set scene
        currentScene.setRoot(sceneLoader.load());
        currentStage.setTitle(title);
        currentStage.setOnCloseRequest(event -> System.exit(0));
        currentStage.show();
    }
}
