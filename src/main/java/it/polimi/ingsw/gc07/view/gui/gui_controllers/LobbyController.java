package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends GuiController implements Initializable {

    @FXML
    protected void onJoinNewGameButtonClick() {
        Platform.runLater(() -> StageController.setScene("/it/polimi/ingsw/gc07/fxml/waitingRoomStage.fxml", "Waiting room"));
    }


    @FXML
    protected void onJoinExistingGameButtonClick() {
        Platform.runLater(() -> StageController.setScene("/it/polimi/ingsw/gc07/fxml/waitingRoomStage.fxml", "Waiting room"));
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
