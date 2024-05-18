package it.polimi.ingsw.gc07.view.gui.gui_controllers;
import it.polimi.ingsw.gc07.view.gui.Gui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class StageController {
    private static Stage currentStage = new Stage();
    private static GuiController guiController;
    private static Scene currentScene;

    public static void setup(Stage firstStage, String fxmlScene) throws IOException {
        //get the fxml file
        FXMLLoader sceneLoader = new FXMLLoader(Gui.class.getResource(fxmlScene));
        Scene scene = new Scene(sceneLoader.load());

        guiController = sceneLoader.getController();
        currentStage = firstStage;
        currentScene = scene;
        //firstStage.getIcons().add(new Image(""));
        firstStage.setTitle("Lobby");
        firstStage.setScene(scene);
    }

    public static GuiController getController() {
        return guiController;
    }

    public static void setScene(String fxmlScene, String title){
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource(fxmlScene));
        try{
            Parent root = fxmlLoader.load();
            currentScene.setRoot(root);
            guiController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentStage.setTitle(title);
        currentStage.setOnCloseRequest(event -> System.exit(0));
        currentStage.show();
    }
}
