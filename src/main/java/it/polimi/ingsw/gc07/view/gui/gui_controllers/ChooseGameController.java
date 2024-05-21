package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class ChooseGameController extends GuiController implements Initializable {

    /**
     * list of string composed with: gameId +++ numOfPlayers
     */
    @FXML
    ListView<String> gameList;

    @FXML
    TextField gameId;

    Map<Integer,Integer> games;

    @FXML
    Text err;


    @FXML
    protected void onConfirmButtonClick(ActionEvent actionEvent) {
        if(games.containsKey(parseInt(gameId.getText())))
        {
            //TODO: join del player al game
            Platform.runLater(() -> {
                try {
                    StageController.setScene("/it/polimi/ingsw/gc07/fxml/waitingRoom.fxml", "Waiting room");
                } catch (IOException e) {
                    // TODO
                    throw new RuntimeException(e);
                }
            });
        }
        else{
            gameId.clear();
            err.setVisible(true);
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                StageController.setScene("/it/polimi/ingsw/gc07/fxml/lobby.fxml", "Waiting room");
            } catch (IOException e) {
                // TODO
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void insertText(MouseEvent actionEvent) {
        err.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        games = new HashMap<>();
        games.put(0,3);
    }
}
