package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitingRoomController extends GuiController implements Initializable {

    //TODO: una volta raggiunto il numero di player giusti far partire un timeout(?) e inizia la partita
    @FXML
    Text numPlayers;

    @FXML
    ListView<String> playersList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
