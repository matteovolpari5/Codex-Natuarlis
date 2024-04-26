package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.ArrayList;
import java.util.List;

public class GameView {
    /**
     * Nickname of the owner of the game view.
     */
    private final String ownerNickname;
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

    //TODO
    // RMIclient / SocketClient contiene il riferimento a game view
    // riceve un update con listener, chiama metodo su gameview per salvare
    // nella copia locale l'aggiornamento

    /**
     * Constructor of the class GameView
     */
    public GameView(String ownerNickname) {
        this.ownerNickname = ownerNickname; // TODO set by the client (who has the nickname)
        this.winners = null;
        this.scoreTrackBoardView = new ScoreTrackBoardView();
        this.deckView = new DeckView();
        this.chatView = new ChatView();
        this.playerViews = new ArrayList<>();
    }

    /**
     * Method that adds a new chat message to the ChatView.
     * @param chatMessage new chat message
     */
    public void addMessage(ChatMessage chatMessage) {
        chatView.addMessage(chatMessage);
    }

    /**
     * Method that allows to set the common objective.
     * @param commonObjective common objective
     */
    public void setCommonObjective(List<ObjectiveCard> commonObjective) {
        deckView.setCommonObjective(commonObjective);
    }

    /**
     * Method that allows to set the card on top of resource cards deck.
     * @param topResourceDeck card on top of resource card deck
     */
    public void setTopResourceCard(DrawableCard topResourceDeck) {
        deckView.setTopResourceDeck(topResourceDeck);
    }

    /**
     * Method that allows to set the card on top of gold cards deck.
     * @param topGoldDeck card on top of gold cards deck
     */
    public void setTopGoldCard(GoldCard topGoldDeck) {
        deckView.setTopGoldDeck(topGoldDeck);
    }

    /**
     * Method that allows to set the revealed resource cards.
     * @param faceUpResourceCard revealed resource cards
     */
    public void setResourceFaceUpCards(List<DrawableCard> faceUpResourceCard) {
        deckView.setFaceUpResourceCard(faceUpResourceCard);
    }

    /**
     * Method that allows to set the revealed gold cards.
     * @param faceUpGoldCard revealed gold cards
     */
    public void setGoldFaceUpCards(List<GoldCard> faceUpGoldCard) {
        deckView.setFaceUpGoldCard(faceUpGoldCard);
    }

    /**
     * Method that allows to set the starter card for the owner of the game view.
     * Starter cards of other players will be visible once placed.
     * @param starterCard starter card
     */
    public void setStarterCard(String nickname, PlaceableCard starterCard) {
        assert(nickname.equals(ownerNickname)): "Shouldn't have received the update.";
        for(PlayerView playerView: playerViews) {
            if(playerView.getNickname().equals(nickname)) {
                playerView.setStarterCard(starterCard);
            }
        }
    }

    /**
     * Method that allows to set a new card in the game field.
     * @param nickname nickname of the player who placed the card
     * @param card card
     * @param x x
     * @param y y
     * @param way way
     * @param orderPosition order position
     */
    public void addCard(String nickname, PlaceableCard card, int x, int y, boolean way, int orderPosition) {
        // add the card to the specified game field view specified
        for(PlayerView playerView: playerViews) {
            if(playerView.getNickname().equals(nickname)) {
                playerView.addCard(card, x, y, way, orderPosition);
            }
        }
    }

    /**
     * Method that allows to set a new score for the player.
     * @param nickname nickname
     * @param newScore score
     */
    public void setNewScore(String nickname, int newScore) {
        scoreTrackBoardView.setNewScore(nickname, newScore);
    }

    /**
     * Method to set a value for isStalled.
     * @param nickname nickname
     * @param isStalled isStalled value
     */
    public void setIsStalled(String nickname, boolean isStalled) {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                p.setIsStalled(isStalled);
            }
        }
    }

    /**
     * Method to set a value for isConnected.
     * @param nickname nickname
     * @param isConnected isConnected value
     */
    public void setIsConnected(String nickname, boolean isConnected) {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                p.setIsConnected(isConnected);
            }
        }
    }

    /**
     * Method to set the card hand of a player.
     * @param nickname nickname
     * @param newHand card hand
     */
    public void setCardHand(String nickname, List<DrawableCard> newHand) {
        assert(nickname.equals(ownerNickname)): "Shouldn't have received the update.";
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                p.setCardHand(newHand);
            }
        }
    }

    /**
     * Method that allows to set game model info.
     * @param id id
     * @param playersNumber players number
     * @param state state
     * @param winners winners
     * @param currPlayer current player
     */
    public void setGameModel(int id, int playersNumber, GameState state, List<String> winners, int currPlayer) {
        this.id = id;
        this.playersNumber = playersNumber;
        this.state = state;
        this.winners = winners;
        this.currPlayer = currPlayer;
    }

    /**
     * Method that allows to add a new player view.
     * @param playerView new player view
     */
    public void addPlayerView(PlayerView playerView) {
        playerViews.add(playerView);
        // nickname set in the constructor
        scoreTrackBoardView.addPlayerToBoard(playerView.getNickname());
    }

    /**
     * Method to set a command result.
     * @param commandResult command result
     */
    public void setCommandResult(CommandResult commandResult) {
        this.commandResult = commandResult;
    }




    // to check or add



    public void setTwentyPointsReached(boolean twentyPointsReached) {
        this.twentyPointsReached = twentyPointsReached;
    }

    public void setAdditionalRound(boolean additionalRound) {
        this.additionalRound = additionalRound;
    }


}
