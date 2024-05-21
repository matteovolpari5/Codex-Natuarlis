package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseGameController extends GuiController implements Initializable {

    @FXML
    protected void onConfirmButtonClick(ActionEvent actionEvent) {
        //TODO: se la join al game va a buon fine
        Platform.runLater(() -> StageController.setScene("/it/polimi/ingsw/gc07/fxml/waitingRoom.fxml", "Waiting room"));
    }

    @FXML
    protected void onBackButtonClick(ActionEvent actionEvent) {
        Platform.runLater(() -> StageController.setScene("/it/polimi/ingsw/gc07/fxml/lobby.fxml", "Waiting room"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
