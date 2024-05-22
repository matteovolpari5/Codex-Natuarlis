package it.polimi.ingsw.gc07.view.gui;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.Client;
import it.polimi.ingsw.gc07.view.Ui;
import it.polimi.ingsw.gc07.view.gui.gui_controllers.StageController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public  class Gui extends Application implements Ui {
    private static Gui guiInstance = null;
    private String nickname;
    private Client client;

    @Override
    public void init() {
        synchronized(Gui.class) {
            guiInstance = this;
            Gui.class.notifyAll();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        String lobbyScene = "/it/polimi/ingsw/gc07/fxml/lobby.fxml";
        String sceneTitle = "Lobby";
        StageController.setup(stage, lobbyScene, sceneTitle);
    }

    public synchronized static Gui getGuiInstance() {
        while(guiInstance == null) {
            try {
                Gui.class.wait();
            }catch(InterruptedException e) {
                throw new RuntimeException();
            }
        }
        return guiInstance;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void runJoinGameInterface() {
        // scene already present

        // TODO
        // cugola ha detto che al massimo possiamo fare che start crea senza mostrare,
        // mentre qua lo mostro
        // ma anche così non è terribile
    }

    @Override
    public void runGameInterface() {
        // change scene to PlayerScene
        String playerScene = "/it/polimi/ingsw/gc07/fxml/lobby.fxml";
        String title = "Codex Naturalis - Board";
        try {
            StageController.setScene(playerScene, title);
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveScoreUpdate(Map<String, Integer> playerScores, Map<String, TokenColor> playerTokenColors) {
        // TODO
    }

    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        // TODO
    }

    @Override
    public void receiveDecksUpdate(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {
        // TODO
    }

    @Override
    public void receiveGameFieldUpdate(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {
        // TODO
    }

    @Override
    public void receiveStarterCardUpdate(PlaceableCard starterCard) {
        // TODO
    }

    @Override
    public void receiveCardHandUpdate(List<DrawableCard> hand, ObjectiveCard personalObjective) {
        // TODO
    }

    @Override
    public void receiveGeneralModelUpdate(GameState gameState, String currPlayer) {
        // TODO
    }

    @Override
    public void receivePenultimateRoundUpdate() {
        // TODO
    }

    @Override
    public void receiveAdditionalRoundUpdate() {
        // TODO
    }

    @Override
    public void receiveCommandResultUpdate(CommandResult commandResult) {
        // TODO
    }

    @Override
    public void receiveExistingGamesUpdate(Map<Integer, Integer> existingGames) {
        // TODO
    }

    @Override
    public void receiveWinnersUpdate(List<String> winners) {
        // TODO
    }
}
