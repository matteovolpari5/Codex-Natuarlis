package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView {
    /**
     * ID of the game.
     */
    private int id;
    /**
     * State of the game.
     */
    private GameState state;
    /**
     * Number of players in the game, chose by the first player.
     */
    private int playersNumber;
    /**
     * List of winner(s) of the game.
     */
    private List<String> winners;
    /**
     * Integer value representing the position of the current player.
     */
    private int currPlayer;
    /**
     * Boolean attribute, true if a player has reached 20 points.
     */
    private boolean twentyPointsReached;
    /**
     * Boolean attribute, if it is the additional round of the game.
     */
    private boolean additionalRound;
    /**
     * Command result.
     */
    private CommandResult commandResult;

    /**
     * Map of players and their game field views.
     */
    private Map<String, GameFieldView> playerGameFieldViews;
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
     * List of PlayerViews.
     * Same order of the list of players on the server.
     */
    private final List<PlayerView> playerViews;

    // RMIclient / SocketClient contiene il riferimento a game view
    // riceve un update con listener, chiama metodo su gameview per salvare
    // nella copia locale l'aggiornamento

    /**
     * Constructor of the class GameView
     */
    public GameView() {
        this.winners = null;
        this.playerGameFieldViews = new HashMap<>();
        this.scoreTrackBoardView = new ScoreTrackBoardView();
        this.deckView = new DeckView();
        this.chatView = new ChatView();
        this.playerViews = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }

    public void setCurrPlayer(int currPlayer) {
        this.currPlayer = currPlayer;
    }

    public void setTwentyPointsReached(boolean twentyPointsReached) {
        this.twentyPointsReached = twentyPointsReached;
    }

    public void setAdditionalRound(boolean additionalRound) {
        this.additionalRound = additionalRound;
    }

    public void setCommandResult(CommandResult commandResult) {
        this.commandResult = commandResult;
    }

    public void addGameFieldView(GameFieldView gameFieldView) {
        // TODO
        //playerGameFieldViews.put(    ,gameFieldView);
        // come prima, non ho il nickname!!!
    }

    // problema: gameField non ha il nickname, se invia aggiornamento, non so di chi sia!
    /*
    // TODO change
    public void setStarterCard(PlaceableCard starterCard) {
        gameFieldView.setStarterCard(starterCard);
    }

    // TODO change
    public void addCard(int x, int y, PlaceableCard card, boolean way, int orderPosition) {
        gameFieldView.addCard(x, y, card, way, orderPosition);
    }
     */

    public void addPlayer(String nickname, TokenColor color) {
        scoreTrackBoardView.addPlayer(nickname, color);
    }

    public void setNewScore(String nickname, int newScore) {
        scoreTrackBoardView.setNewScore(nickname, newScore);
    }

    public void setCommonObjective(List<ObjectiveCard> commonObjective) {
        deckView.setCommonObjective(commonObjective);
    }

    public void setGoldFaceUpCards(List<GoldCard> faceUpGoldCard) {
        deckView.setFaceUpGoldCard(faceUpGoldCard);
    }

    public void setResourceFaceUpCards(List<DrawableCard> faceUpResourceCard) {
        deckView.setFaceUpResourceCard(faceUpResourceCard);
    }

    public void setTopGoldCard(GoldCard topGoldDeck) {
        deckView.setTopGoldDeck(topGoldDeck);
    }

    public void setTopResourceCard(DrawableCard topResourceDeck) {
        deckView.setTopResourceDeck(topResourceDeck);
    }

    public void addMessage(ChatMessage chatMessage) {
        chatView.addMessage(chatMessage);
    }

    public void addPlayerView(PlayerView playerView) {
        playerViews.add(playerView);
    }

    public void setIsStalled(String nickname, boolean isStalled) {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                p.setIsStalled(isStalled);
            }
        }
    }

    public void setIsConnected(String nickname, boolean isConnected) {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                p.setIsConnected(isConnected);
            }
        }
    }

    public void setCardHand(String nickname, List<DrawableCard> newHand) {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                p.setCardHand(newHand);
            }
        }
    }
}
