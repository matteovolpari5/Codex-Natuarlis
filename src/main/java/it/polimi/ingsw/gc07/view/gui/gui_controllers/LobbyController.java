package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends GuiController implements Initializable {

    @FXML
    protected RadioButton newGame;

    @FXML
    protected RadioButton joinGame;

    @FXML
    protected Button next;

    @FXML
    protected void onContinueButtonClick() {
        if(newGame.isSelected())
            Platform.runLater(() -> StageController.setScene("/it/polimi/ingsw/gc07/fxml/waitingRoom.fxml", "Waiting room"));
        else if(joinGame.isSelected())
            Platform.runLater(() -> StageController.setScene("/it/polimi/ingsw/gc07/fxml/chooseGame.fxml", "Choose game scene"));
    }

    @FXML
    protected void clickedNew()
    {
        joinGame.setSelected(false);
        next.setVisible(newGame.isSelected());
    }

    @FXML
    protected void clickedJoin()
    {
        newGame.setSelected(false);
        next.setVisible(joinGame.isSelected());
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
