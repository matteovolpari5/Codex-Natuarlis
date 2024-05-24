package it.polimi.ingsw.gc07.view.gui.gui_controllers;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view.DeckView;
import it.polimi.ingsw.gc07.model_view.GameFieldView;
import it.polimi.ingsw.gc07.model_view.GameView;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PlayerSceneController implements GuiController, Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameView gameView = StageController.getGameView();
        String nickname = StageController.getNickname();

        // set game field data
        GameFieldView gameFieldView =  gameView.getGameField(nickname);
        // = gameFieldView.getCardsContent();
        // = gameFieldView.getCardsFace();
        // = gameFieldView.getCardsOrder());

        // set decks data
        DeckView deckView = gameView.getDeckView();
        // = deckView.getTopResourceDeck();
        // = deckView.getTopGoldDeck();
        // = deckView.getFaceUpResourceCard();
        // = deckView.getFaceUpGoldCard();
        // = deckView.getCommonObjective();

        // set current hand data
        // = gameView.getCurrentHand();
        // = gameView.getSecretObjective();

        // set starter card
        // = gameView.getStarterCard();

        // set scores
        // = gameView.getPlayersScores(), gameView.getPlayersTokenColors();

        // set id and current player
        // = gameView.getId();
        // = gameView.getCurrentPlayerNickname();

        // set full chat
        // = gameView.getOwnerMessages();

        // set stalled or disconnected
        // = gameView.getConnectionValues();
        // =  gameView.getStallValues();
    }

    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {

    }

    @Override
    public void addMessage(ChatMessage chat) {

    }

    @Override
    public void updateDecks(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {

    }

    @Override
    public void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

    }

    @Override
    public void updateStarterCard(PlaceableCard starterCard) {

    }

    @Override
    public void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective) {

    }

    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {

    }

    @Override
    public void setPenultimateRound() {

    }

    @Override
    public void setAdditionalRound() {

    }

    @Override
    public void updateCommandResult(CommandResult commandResult) {

    }

    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {

    }

    @Override
    public void displayWinners(List<String> winners) {

    }

    @Override
    public void setNickname(String nickname) {
        // don't use
    }
}
