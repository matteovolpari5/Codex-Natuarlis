package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.List;

public class GameView {
    /**
     * GameFieldView reference.
     */
    private final GameFieldView gameFieldView;
    /**
     * ScoreTrackBoardView reference.
     */
    private final ScoreTrackBoardView scoreTrackBoardView;
    /**
     * DeckView reference.
     */
    private final DeckView deckView;
    /**
     * ChatView reference.
     */
    private final ChatView chatView;
    /**
     * PlayerView.
     */
    private PlayerView playerView;
    /**
     * Other players' PlayerView.
     */
    private final List<PlayerView> otherPlayerViews;

    // RMIclient / SocketClient contiene il riferimento a game view
    // riceve un update con listener, chiama metodo su gameview per salvare
    // nella copia locale l'aggiornamento

    /**
     * Constructor of the class GameView
     */
    public GameView() {
        chatView = new ChatView();
        deckView = new DeckView();
        gameFieldView = new GameFieldView();
        playerView = null;
        otherPlayerViews = new ArrayList<>();
        scoreTrackBoardView = new ScoreTrackBoardView();
    }

    public void setStarterCard(PlaceableCard starterCard) {
        gameFieldView.setStarterCard(starterCard);
    }

    public void addCard(int x, int y, PlaceableCard card, boolean way, int orderPosition) {
        gameFieldView.addCard(x, y, card, way, orderPosition);
    }

    public void addPlayer(String nickname, TokenColor color) {
        scoreTrackBoardView.addPlayer(nickname, color);
    }

    public void setNewScore(String nickname, int newScore) {
        scoreTrackBoardView.setNewScore(nickname, newScore);
    }

    public void setCommonObjective(List<ObjectiveCard> commonObjective) {
        deckView.setCommonObjective(commonObjective);
    }

    public void setFaceUpGoldCard(List<GoldCard> faceUpGoldCard) {
        deckView.setFaceUpGoldCard(faceUpGoldCard);
    }

    public void setFaceUpResourceCard(List<DrawableCard> faceUpResourceCard) {
        deckView.setFaceUpResourceCard(faceUpResourceCard);
    }

    public void setTopGoldDeck(GoldCard topGoldDeck) {
        deckView.setTopGoldDeck(topGoldDeck);
    }

    public void setTopResourceDeck(DrawableCard topResourceDeck) {
        deckView.setTopResourceDeck(topResourceDeck);
    }

    public void addMessage(ChatMessage chatMessage) {
        chatView.addMessage(chatMessage);
    }

    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }

    public void addOtherPlayerView(PlayerView playerView) {
        otherPlayerViews.add(playerView);
    }

    public void setIsStalled(boolean isStalled) {
        playerView.setIsStalled(isStalled);
    }

    public void setIsConnected(boolean isConnected) {
        playerView.setIsConnected(isConnected);
    }

    public void setCardHand(List<DrawableCard> currentHand) {
        playerView.setCardHand(currentHand);
    }
}
