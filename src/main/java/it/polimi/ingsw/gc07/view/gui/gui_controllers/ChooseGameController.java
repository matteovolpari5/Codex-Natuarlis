package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ChooseGameController implements GuiController, Initializable {
    // TODO da eliminare

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {

    }

    @Override
    public void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

    }

    /*

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
    */
}
