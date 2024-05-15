package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model_view_listeners.GameFieldViewListener;
import it.polimi.ingsw.gc07.model_view_listeners.GameViewListener;
import it.polimi.ingsw.gc07.model_view_listeners.PlayerViewListener;
import it.polimi.ingsw.gc07.view.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private boolean penultimateRound;
    /**
     * Boolean attribute, if it is the additional round of the game.
     */
    private boolean additionalRound;
    /**
     * Command result.
     */
    private CommandResult commandResult;
    /**
     * BoardView reference.
     */
    private final BoardView boardView;
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
    /**
     * List of game view listeners.
     */
    private final List<GameViewListener> gameViewListeners;

    /**
     * Constructor of the class GameView
     */
    public GameView(String ownerNickname) {
        this.ownerNickname = ownerNickname;
        this.winners = null;
        this.boardView = new BoardView();
        this.deckView = new DeckView();
        this.chatView = new ChatView();
        this.playerViews = new ArrayList<>();
        this.gameViewListeners = new ArrayList<>();
    }

    /**
     * Method used to add a view listener to game view components.
     * @param uiListener view listener
     */
    public void addViewListener(Ui uiListener) {
        gameViewListeners.add(uiListener);
        boardView.addListener(uiListener);
        chatView.addListener(uiListener);
        deckView.addListener(uiListener);
    }

    // getters

    public GameState getGameState() {
        return state;
    }

    public String getCurrentPlayerNickname() {
        String currPlayerNickname;
        if(currPlayer >= 0 && currPlayer < playerViews.size()) {
            currPlayerNickname = playerViews.get(currPlayer).getNickname();
        }else {
            currPlayerNickname = null;
        }
        return currPlayerNickname;
    }

    public String getOwnerNickname() {
        return ownerNickname;
    }

    public List<ObjectiveCard> getCommonObjective() {
        return deckView.getCommonObjective();
    }

    public DrawableCard getTopResourceDeck() {
        return deckView.getTopResourceDeck();
    }

    public GoldCard getTopGoldDeck() {
        return deckView.getTopGoldDeck();
    }

    public List<DrawableCard> getFaceUpResourceCard() {
        return deckView.getFaceUpResourceCard();
    }

    public List<GoldCard> getFaceUpGoldCard() {
        return deckView.getFaceUpGoldCard();
    }

    /**
     * Method that allows to set game model info.
     * @param id id
     * @param playersNumber players number
     * @param state state
     * @param winners winners
     * @param currPlayer current player
     * @param penultimateRound twentyPointsReached
     * @param additionalRound additionalRound
     */
    public void setGameModel(int id, int playersNumber, GameState state, List<String> winners, int currPlayer, boolean penultimateRound, boolean additionalRound) {
        this.id = id;
        this.playersNumber = playersNumber;
        this.state = state;
        this.winners = winners;
        this.currPlayer = currPlayer;
        if(!this.penultimateRound && penultimateRound) {
            for(GameViewListener l : gameViewListeners) {
                l.receivePenultimateRoundUpdate();
            }
        }
        this.penultimateRound = penultimateRound;

        if(!this.additionalRound && additionalRound) {
            for(GameViewListener l : gameViewListeners) {
                l.receiveAdditionalRoundUpdate();
            }
        }
        this.additionalRound = additionalRound;

        // send general model update
        for(GameViewListener l: gameViewListeners) {
            l.receiveGeneralModelUpdate(state, getCurrentPlayerNickname());
            // for tui, it may print decks
        }
    }

    /**
     * Method that adds a new chat message to the ChatView.
     * @param chatMessage new chat message
     */
    public void addMessage(ChatMessage chatMessage) {
        if(chatMessage.getIsPublic() || chatMessage.isForReceiver(ownerNickname)) {
            chatView.addMessage(chatMessage);
        }
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
        if(ownerNickname.equals(nickname)) {
            for(PlayerView playerView: playerViews) {
                if(playerView.getNickname().equals(nickname)) {
                    playerView.setStarterCard(starterCard);
                }
            }
        }
        // else, don't save it
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
        boardView.setNewScore(nickname, newScore);
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
    public void setCardHand(String nickname, List<DrawableCard> newHand, ObjectiveCard personalObjective) {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                p.setCardHand(newHand, personalObjective);
            }
        }
    }

    /**
     * Method that allows to add a new player view.
     * @param newPlayerViews new list of player views
     */
    public void setPlayerViews(List<PlayerView> newPlayerViews) {
        for(PlayerView playerView: newPlayerViews) {
            boolean found = false;
            for(PlayerView myPlayerView: this.playerViews) {
                if(playerView.getNickname().equals(myPlayerView.getNickname())) {
                    found = true;
                }
            }
            if(!found) {
                this.playerViews.add(playerView);
                boardView.addPlayerToBoard(playerView.getNickname(), playerView.getTokenColor());
                if(playerView.getNickname().equals(ownerNickname)) {
                    playerView.addListener((PlayerViewListener) gameViewListeners.getFirst());
                    playerView.addGameFieldListener((GameFieldViewListener) gameViewListeners.getFirst());
                }
            }
        }
        boardView.updateListeners();
    }

    /**
     * Method to set a command result.
     * @param commandResult command result
     */
    public void setCommandResult(String nickname, CommandResult commandResult) {
        this.commandResult = commandResult;

        // update view
        if(nickname.equals(ownerNickname) && !commandResult.equals(CommandResult.SUCCESS)) {
            for(GameViewListener l: gameViewListeners) {
                l.receiveCommandResultUpdate(this.commandResult);
            }
        }
    }

    public void displayExistingGames(Map<Integer, Integer> existingGames) {
        for(GameViewListener l: gameViewListeners) {
            l.receiveExistingGamesUpdate(existingGames);
        }
    }

    public boolean checkPlayerPresent(String nickname) {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentPlayer(String nickname) {
        if(currPlayer < 0)
            return false;
        for(int i = 0; i < playerViews.size(); i++) {
            if(playerViews.get(i).getNickname().equals(nickname) && i == currPlayer)
                return true;
        }
        return false;
    }

    public int getNumFaceUpCards(CardType cardType) {
        return deckView.getNumFaceUpCards(cardType);
    }

    public int getCurrHardHandSize() {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(ownerNickname)) {
                return p.getCurrHandSize();
            }
        }
        return -1;
    }

    public int getGameFieldDim() {
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(ownerNickname)) {
                return p.getGameField().getDim();
            }
        }
        return -1;
    }

    public GameFieldView getGameField(String nickname) {
        PlayerView player = null;
        for(PlayerView p: playerViews) {
            if(p.getNickname().equals(nickname)) {
                player = p;
            }
        }
        assert(player != null);
        return player.getGameField();
    }

    public void printChat() {
        chatView.printChat();
    }
}
