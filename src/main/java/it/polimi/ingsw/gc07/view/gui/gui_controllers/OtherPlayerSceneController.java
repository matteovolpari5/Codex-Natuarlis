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

public class OtherPlayerSceneController implements GuiController, Initializable {

    String otherPlayerNickname;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameView gameView = StageController.getGameView();


        // TODO devo sapere il player di cui voglio mostrare il campo
        // serve un metodo setScene diverso, che prende come parametro
        // il nome del player di cui voglio mostrare il gioco
        // a quel punto lo avrei

        // TODO gamefield

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

    /**
     * Method used to display a score update.
     * @param playerScore map containing players' scores
     * @param playerTokenColor map containing players' token colors
     */
    @Override
    public void updateScore(Map<String, Integer> playerScore, Map<String, TokenColor> playerTokenColor) {

    }

    /**
     * Method used to display a new chat message.
     * @param chat new chat message
     */
    @Override
    public void addMessage(ChatMessage chat) {

    }

    /**
     * Method used to display a deck update, containing cards a player can draw or see.
     * @param topResourceDeck top resource deck
     * @param topGoldDeck top gold deck
     * @param faceUpResourceCard face up cards resource
     * @param faceUpGoldCard face up cards gold
     * @param commonObjective common objective
     */
    @Override
    public void updateDecks(DrawableCard topResourceDeck, GoldCard topGoldDeck, List<DrawableCard> faceUpResourceCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective) {

    }

    /**
     * Method used to display a new game field update.
     * @param cardsContent cards content
     * @param cardsFace cards face
     * @param cardsOrder cards order
     */
    @Override
    public void updateGameField(PlaceableCard[][] cardsContent, Boolean[][] cardsFace, int[][] cardsOrder) {

    }

    /**
     * Method used to display the starter card.
     * @param starterCard starter card
     */
    @Override
    public void updateStarterCard(PlaceableCard starterCard) {

    }

    /**
     * Method used to display the new card hand.
     * @param hand new card hand
     * @param personalObjective personal objective
     */
    @Override
    public void updateCardHand(List<DrawableCard> hand, ObjectiveCard personalObjective) {

    }

    /**
     * Method used to display updated game infos.
     *
     * @param gameState game state
     * @param currPlayer current player
     */
    @Override
    public void updateGameInfo(GameState gameState, String currPlayer) {

    }

    /**
     * Method used to display a penultimate round update.
     */
    @Override
    public void setPenultimateRound() {

    }

    /**
     * Method used to display an additional round update.
     */
    @Override
    public void setAdditionalRound() {

    }

    /**
     * Method used to display the last command result.
     * @param commandResult command result
     */
    @Override
    public void updateCommandResult(CommandResult commandResult) {

    }

    /**
     * Method used to display a user existing and free games.
     * @param existingGamesPlayerNumber players number in existing games
     * @param existingGamesTokenColor take token colors in existing games
     */
    @Override
    public void displayExistingGames(Map<Integer, Integer> existingGamesPlayerNumber, Map<Integer, List<TokenColor>> existingGamesTokenColor) {

    }

    /**
     * Method used to display an update, containing winners.
     * @param winners winners
     */
    @Override
    public void displayWinners(List<String> winners) {

    }

    @Override
    public void setNickname(String nickname) {
        otherPlayerNickname = nickname;

        GameView gameView = StageController.getGameView();
        // set game field data
        GameFieldView gameFieldView =  gameView.getGameField(nickname);
        // = gameFieldView.getCardsContent();
        // = gameFieldView.getCardsFace();
        // = gameFieldView.getCardsOrder());
    }
}
