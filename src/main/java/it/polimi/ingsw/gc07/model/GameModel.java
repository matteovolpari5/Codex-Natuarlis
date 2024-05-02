package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.listeners.GameFieldListener;
import it.polimi.ingsw.gc07.listeners.GameListener;
import it.polimi.ingsw.gc07.listeners.PlayerListener;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.Chat;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.PlayerView;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.CommandResultUpdate;
import it.polimi.ingsw.gc07.updates.DeckUpdate;
import it.polimi.ingsw.gc07.updates.GameModelUpdate;
import it.polimi.ingsw.gc07.updates.PlayerJoinedUpdate;

import java.rmi.RemoteException;
import java.util.*;

public class GameModel {
    /**
     * ID of the game.
     */
    private final int id;
    /**
     * Number of players in the game, chose by the first player.
     */
    private final int playersNumber;
    /**
     * State of the game.
     */
    private GameState state;
    /**
     * List of players.
     */
    private final List<Player> players;
    /**
     * List of winner(s) of the game.
     */
    private List<String> winners;
    /**
     * Integer value representing the position of the current player.
     */
    private int currPlayer;
    /**
     * Boolean value representing if the current player has placed a card.
     */
    private boolean hasCurrPlayerPlaced;
    /**
     * Score track board of the game.
     */
    private final ScoreTrackBoard scoreTrackBoard;
    /**
     * Deck of resource cards.
     */
    private final DrawableDeck<DrawableCard> resourceCardsDeck;
    /**
     * Deck of gold cards.
     */
    private final DrawableDeck<GoldCard> goldCardsDeck;
    /**
     * Deck of objective cards.
     */
    private final PlayingDeck<ObjectiveCard> objectiveCardsDeck;
    /**
     * Deck of starter cards.
     */
    private final Deck<PlaceableCard> starterCardsDeck;
    /**
     * Boolean attribute, true if a player has reached 20 points.
     */
    private boolean penultimateRound;
    /**
     * Boolean attribute, if it is the additional round of the game.
     */
    private boolean additionalRound;
    /**
     * Chat of the game.
     */
    private final Chat chat;
    /**
     * Command result.
     */
    private CommandResult commandResult;
    /**
     * List of game listeners.
     */
    private final List<GameListener> gameListeners;
    /**
     * Attribute that tells if decks and their face up cards are empty.
     */
    private boolean emptyDecks;

    /**
     * Constructor of a GameModel with only the first player.
     */
    public GameModel(int id, int playersNumber, DrawableDeck<DrawableCard> resourceCardsDeck,
                     DrawableDeck<GoldCard> goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                Deck<PlaceableCard> starterCardsDeck) {
        this.id = id;
        assert(playersNumber >= 2 && playersNumber <= 4): "Wrong players number";
        this.playersNumber = playersNumber;
        this.state = GameState.GAME_STARTING;
        this.players = new ArrayList<>();
        this.winners = new ArrayList<>();
        this.currPlayer = 0;
        this.hasCurrPlayerPlaced = false;
        this.scoreTrackBoard = new ScoreTrackBoard();
        this.resourceCardsDeck = new DrawableDeck<>(resourceCardsDeck);
        this.goldCardsDeck = new DrawableDeck<>(goldCardsDeck);
        this.objectiveCardsDeck = new PlayingDeck<>(objectiveCardsDeck);
        this.starterCardsDeck = new Deck<>(starterCardsDeck);
        this.penultimateRound = false;
        this.additionalRound = false;
        this.chat = new Chat();
        this.commandResult = null;
        this.gameListeners = new ArrayList<>();
        this.emptyDecks = false;
    }

    public int getId() {
        return id;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setState(GameState state) {
        this.state = state;
        // update listeners
        sendGameModelUpdate();
    }

    public GameState getState() {
        return state;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<String> getPlayerNicknames() {
        return players.stream().map(Player::getNickname).toList();
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setCurrPlayer(int currPlayer) {
        this.currPlayer = currPlayer;
        // update listeners
        sendGameModelUpdate();
    }

    public int getCurrPlayer() {
        return currPlayer;
    }

    public boolean getHasCurrPlayerPlaced() {
        return hasCurrPlayerPlaced;
    }

    public void setHasCurrPlayerPlaced(boolean hasCurrPlayerPlaced) {
        this.hasCurrPlayerPlaced = hasCurrPlayerPlaced;
    }

    public ScoreTrackBoard getScoreTrackBoard() {
        return scoreTrackBoard;
    }

    public DrawableDeck<DrawableCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    public DrawableDeck<GoldCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }

    public PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    public Deck<PlaceableCard> getStarterCardsDeck() {
        return starterCardsDeck;
    }

    public boolean getPenultimateRound() {
        return penultimateRound;
    }

    public void setPenultimateRound(boolean penultimateRound) {
        this.penultimateRound = penultimateRound;
        // update listeners
        sendGameModelUpdate();
    }

    public boolean getAdditionalRound() {
        return additionalRound;
    }

    public void setAdditionalRound(boolean additionalRound) {
        this.additionalRound = additionalRound;
        // update listeners
        sendGameModelUpdate();
    }

    public void setCommandResult(String nickname, CommandResult commandResult) {
        this.commandResult = commandResult;

        // update listeners
        CommandResultUpdate update = new CommandResultUpdate(nickname, commandResult);
        for(GameListener l: gameListeners) {
            try {
                l.receiveCommandResultUpdate(update);
            }catch(RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    public void setEmptyDecks(boolean value) {
        this.emptyDecks = value;
    }

    public boolean getEmptyDecks() {
        return this.emptyDecks;
    }

    private void sendGameModelUpdate() {
        GameModelUpdate update = new GameModelUpdate(id, playersNumber, state, new ArrayList<>(winners), currPlayer, penultimateRound, additionalRound);
        for(GameListener l: gameListeners) {
            try {
                l.receiveGameModelUpdate(update);
            }catch(RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    private void sendDeckUpdate() {
        DeckUpdate update = new DeckUpdate(resourceCardsDeck.revealTopCard(), goldCardsDeck.revealTopCard(),
                resourceCardsDeck.getFaceUpCards(), goldCardsDeck.getFaceUpCards(),
                objectiveCardsDeck.getFaceUpCards());
        for(GameListener l: gameListeners) {
            try {
                l.receiveDeckUpdate(update);
            }catch(RemoteException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public void setUpResourceCardsDeck() {
        resourceCardsDeck.setUpDeck();
        // update listeners
        sendDeckUpdate();
    }

    public void setUpGoldCardsDeck() {
        goldCardsDeck.setUpDeck();
        // update listeners
        sendDeckUpdate();

    }

    public void setUpObjectiveCardsDeck() {
        objectiveCardsDeck.setUpDeck();
        // update listeners
        sendDeckUpdate();
    }

    public DrawableCard drawResourceCard() {
        DrawableCard card = resourceCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    public GoldCard drawGoldCard() {
        GoldCard card = goldCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    public ObjectiveCard drawObjectiveCard() {
        ObjectiveCard card = objectiveCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    public PlaceableCard drawStarterCard() {
        PlaceableCard card = starterCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    public DrawableCard drawFaceUpResourceCard(int pos) {
        DrawableCard card = resourceCardsDeck.drawFaceUpCard(pos);
        // update listeners
        sendDeckUpdate();
        return card;
    }

    public GoldCard drawFaceUpGoldCard(int pos) {
        GoldCard card = goldCardsDeck.drawFaceUpCard(pos);
        // update listeners
        sendDeckUpdate();
        return card;
    }

    public void addFaceUpResourceCard(DrawableCard card) {
        resourceCardsDeck.addFaceUpCard(card);
        // update listeners
        sendDeckUpdate();
    }

    public void addFaceUpGoldCard(GoldCard card) {
        goldCardsDeck.addFaceUpCard(card);
        // update listeners
        sendDeckUpdate();
    }

    public DrawableCard revealFaceUpResourceCard(int pos) {
        return resourceCardsDeck.revealFaceUpCard(pos);
    }

    public GoldCard revealFaceUpGoldCard(int pos) {
        return goldCardsDeck.revealFaceUpCard(pos);
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
        scoreTrackBoard.addPlayer(newPlayer.getNickname());

        // set card hand
        newPlayer.addCardHand(drawResourceCard());
        newPlayer.addCardHand(drawResourceCard());
        newPlayer.addCardHand(drawGoldCard());
        // set secrete objective
        newPlayer.setSecretObjective(drawObjectiveCard());
        // set starter card
        newPlayer.setStarterCard(drawStarterCard());

        // if not the first player
        if(players.size() > 1) {
            // add previously added listeners to new player
            for(PlayerListener listener: players.getFirst().getListeners()) {
                newPlayer.addListener(listener);
            }
            for(GameFieldListener listener: players.getFirst().getGameField().getListeners()) {
                newPlayer.getGameField().addListener(listener);
            }
        }

        // update sent in addRMIListener,
        // in order to update also the newly created listener
    }

    public void addListener(VirtualView client) {
        // called as soon as a player joins a game
        gameListeners.add(client);
        chat.addListener(client);
        scoreTrackBoard.addListener(client);
        for(Player p: players) {
            p.addListener(client);
            p.getGameField().addListener(client);
        }

        // update listeners
        List<PlayerView> playerViews = new ArrayList<>();
        for(Player p: players) {
            playerViews.add(new PlayerView(p.getNickname(), p.getTokenColor(), p.getSecretObjective()));
        }
        PlayerJoinedUpdate playerUpdate = new PlayerJoinedUpdate(playerViews);
        for(GameListener l: gameListeners) {
            try {
                l.receivePlayerJoinedUpdate(playerUpdate);
            }catch(RemoteException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        // send first game and deck update to player
        sendGameModelUpdate();
        sendDeckUpdate();
    }

    public void addChatPublicMessage(String content, String sender) {
        chat.addPublicMessage(content, sender);
    }

    public void addChatPrivateMessage(String content, String sender, String receiver) {
        chat.addPrivateMessage(content, sender, receiver);
    }

    public int getScore(String nickname) {
        return scoreTrackBoard.getScore(nickname);
    }

    public void setScore(String nicknmae, int newScore) {
        scoreTrackBoard.setScore(nicknmae, newScore);
    }

    public void incrementScore(String nickname, int deltaScore) {
        scoreTrackBoard.incrementScore(nickname, deltaScore);
    }

    public int getNumPlayersConnected() {
        int numPlayersConnected = 0;
        for (Player p: players){
            if (p.isConnected()){
                numPlayersConnected++;
            }
        }
        return numPlayersConnected;
    }

    public boolean hasPlayer(String nickname) {
        boolean found = false;
        for(Player p: players){
            if(p.getNickname().equals(nickname)){
                found = true;
            }
        }
        return found;
    }

    public boolean hasPlayerWithTokenColor(TokenColor tokenColor) {
        boolean found = false;
        for(Player p: players){
            if(p.getTokenColor().equals(tokenColor)){
                found = true;
            }
        }
        return found;
    }

    /**
     * Method that compute the winner/s of the game.
     */
    public void computeWinner() {
        assert(state.equals(GameState.GAME_ENDED)) : "The game state is not correct";
        List<String> computedWinners = new ArrayList<>();
        int deltaPoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;

        for (int i = 0; i >= 0 && i < players.size(); i++) {
            GameField gameField = players.get(i).getGameField();

            ObjectiveCard objectiveCard;
            objectiveCard = objectiveCardsDeck.revealFaceUpCard(0);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives = objectiveCard.numTimesScoringConditionMet(gameField);
            //points counter for the 1st common objective
            deltaPoints = objectiveCard.getObjectiveScore(gameField);

            objectiveCard = objectiveCardsDeck.revealFaceUpCard(1);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives += objectiveCard.numTimesScoringConditionMet(gameField);
            //points counter for the 2nd common objective
            deltaPoints += objectiveCard.getObjectiveScore(gameField);

            realizedObjectives += players.get(i).getSecretObjective().numTimesScoringConditionMet(gameField);
            //points counter for the secret objective
            deltaPoints += players.get(i).getSecretObjective().getObjectiveScore(gameField);
            incrementScore(players.get(i).getNickname(), deltaPoints);
            if (max <= getScore(players.get(i).getNickname())) {
                max = getScore(players.get(i).getNickname());
                if (realizedObjectives >= maxRealizedObjective) {
                    if (realizedObjectives == maxRealizedObjective) {
                        computedWinners.add(players.get(i).getNickname());
                    } else {
                        computedWinners.clear();
                        computedWinners.add(players.get(i).getNickname());
                        maxRealizedObjective = realizedObjectives;
                    }
                }
            }
        }

        this.winners = computedWinners;
        // update listeners
        sendGameModelUpdate();
    }

    public void setWinner(String nickname){
        winners.add(nickname);
        // update listeners
        sendGameModelUpdate();
    }
    public void addPoints(Player player, int x, int y) {
        int deltaPoints;
        deltaPoints = player.getGameField().getPlacedCard(x, y).getPlacementScore(player.getGameField(), x, y);
        if(deltaPoints + getScore(player.getNickname()) >= 20) {
            setPenultimateRound(true); // setter updated listeners
            if((deltaPoints + getScore(player.getNickname())) > 29) {
                setScore(player.getNickname(), 29);
            }
            else {
                incrementScore(player.getNickname(), deltaPoints);
            }
        }
        else {
            incrementScore(player.getNickname(), deltaPoints);
        }
    }
}
