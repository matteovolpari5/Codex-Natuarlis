package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends GuiController implements Initializable {
    /**
     * Handles the event when the "Play" button is clicked.
     * Navigates to the login scene where the player can enter their information.
     */
    @FXML
    protected void onPlayButtonClick() {
        Platform.runLater(() -> StageController.setScene("fxml/signup_scene.fxml", "SignupScene")
        );
    }

    /**
     * Handles the event when the "Exit" button is clicked.
     * Exits the application.
     */
    @FXML
    protected void onExitButtonClick() {
        //end game and close the window
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
