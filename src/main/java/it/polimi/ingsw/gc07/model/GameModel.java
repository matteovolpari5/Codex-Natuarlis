package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.listeners.GameFieldListener;
import it.polimi.ingsw.gc07.listeners.GameListener;
import it.polimi.ingsw.gc07.listeners.PlayerListener;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.Chat;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.PlayerView;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.PlayerJoinedUpdate;

import java.rmi.RemoteException;
import java.util.*;

public class GameModel {
    /**
     * ID of the game.
     */
    private final int id;
    /**
     * State of the game.
     */
    private GameState state;
    /**
     * Number of players in the game, chose by the first player.
     */
    private final int playersNumber;
    /**
     * List of players.
     */
    private final List<Player> players;
    /**
     * List of winner(s) of the game.
     */
    private final List<String> winners;
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
    private final ResourceCardsDeck resourceCardsDeck;
    /**
     * Deck of gold cards.
     */
    private final GoldCardsDeck goldCardsDeck;
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
    private boolean twentyPointsReached;
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
     * Constructor of a GameModel with only the first player.
     */
    public GameModel(int id, int playersNumber, ResourceCardsDeck resourceCardsDeck,
                GoldCardsDeck goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                Deck<PlaceableCard> starterCardsDeck) {
        this.id = id;
        this.state = GameState.GAME_STARTING;
        assert(playersNumber >= 2 && playersNumber <= 4): "Wrong players number";
        this.playersNumber = playersNumber;
        this.players = new ArrayList<>();
        this.winners = new ArrayList<>();
        this.currPlayer = 0;
        this.hasCurrPlayerPlaced = false;
        this.scoreTrackBoard = new ScoreTrackBoard();
        this.resourceCardsDeck = new ResourceCardsDeck(resourceCardsDeck);
        this.goldCardsDeck = new GoldCardsDeck(goldCardsDeck);
        this.objectiveCardsDeck = new PlayingDeck<>(objectiveCardsDeck);
        this.starterCardsDeck = new Deck<>(starterCardsDeck);
        this.twentyPointsReached = false;
        this.additionalRound = false;
        this.chat = new Chat();
        this.commandResult = null;
        this.gameListeners = new ArrayList<>();
    }

    /**
     * Method to add a game listener.
     * @param gameListener game listener
     */
    public void addListener(GameListener gameListener) {
        gameListeners.add(gameListener);
    }

    public int getId() {
        return id;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public List<String> getPlayerNicknames() {
        return players.stream().map(Player::getNickname).toList();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setCurrPlayer(int currPlayer) {
        this.currPlayer = currPlayer;
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

    public ResourceCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    public GoldCardsDeck getGoldCardsDeck() {
        return goldCardsDeck;
    }

    public PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    public Deck<PlaceableCard> getStarterCardsDeck() {
        return starterCardsDeck;
    }

    public boolean getTwentyPointsReached() {
        return twentyPointsReached;
    }

    public void setTwentyPointsReached(boolean twentyPointsReached) {
        this.twentyPointsReached = twentyPointsReached;
    }

    public boolean getAdditionalRound() {
        return additionalRound;
    }

    public void setAdditionalRound(boolean additionalRound) {
        this.additionalRound = additionalRound;
    }

    public void setCommandResult(CommandResult commandResult) {
        this.commandResult = commandResult;
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
        scoreTrackBoard.addPlayer(newPlayer.getNickname());

        // set card hand
        newPlayer.addCardHand(resourceCardsDeck.drawCard());
        newPlayer.addCardHand(resourceCardsDeck.drawCard());
        newPlayer.addCardHand(goldCardsDeck.drawCard());
        // set secrete objective
        newPlayer.setSecretObjective(objectiveCardsDeck.drawCard());
        // set starter card
        newPlayer.setStarterCard(starterCardsDeck.drawCard());

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
    }

    public void addRMIListener(VirtualView client) {
        // TODO voglio tenere separati listener RMI e socket?
        //  se si devo creare due liste in tutte le classi

        // called as soon as a player joins a game
        gameListeners.add(client);
        chat.addListener(client);
        resourceCardsDeck.addListener(client);
        goldCardsDeck.addListener(client);
        starterCardsDeck.addListener(client);
        objectiveCardsDeck.addListener(client);
        scoreTrackBoard.addListener(client);
        for(Player p: players) {
            p.addListener(client);
            p.getGameField().addListener(client);
        }
    }

    public void addChatPublicMessage(String content, String sender) {
        chat.addPublicMessage(content, sender);
    }

    public void addChatPrivateMessage(String content, String sender, String receiver) {
        chat.addPrivateMessage(content, sender, receiver);
    }

    public void setUpResourceCardsDeck() {
        resourceCardsDeck.setUpDeck();
    }
    public void setUpGoldCardsDeck() {
        goldCardsDeck.setUpDeck();

    }
    public void setUpObjectiveCardsDeck() {
        objectiveCardsDeck.setUpDeck();
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
        for(Player p: getPlayers()){
            if(p.getTokenColor().equals(tokenColor)){
                found = true;
            }
        }
        return found;
    }

    /**
     * Method that compute the winner/s of the game.
     * @return the list of players who won the game
     */
    public List<String> computeWinner() {
        List<String> winners = new ArrayList<>();
        int deltaPoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;

        List<Player> playersCopy = new ArrayList<>(players);
        for (int i = 0; i >= 0 && i < players.size(); i++) {
            GameField gameField = players.get(i).getGameField();

            ObjectiveCard objectiveCard;
            objectiveCard = getObjectiveCardsDeck().revealFaceUpCard(0);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives = objectiveCard.numTimesScoringConditionMet(gameField);
            //points counter for the 1st common objective
            deltaPoints = objectiveCard.getObjectiveScore(gameField);

            objectiveCard = getObjectiveCardsDeck().revealFaceUpCard(1);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives += objectiveCard.numTimesScoringConditionMet(gameField);
            //points counter for the 2nd common objective
            deltaPoints += objectiveCard.getObjectiveScore(gameField);

            realizedObjectives += getPlayers().get(i).getSecretObjective().numTimesScoringConditionMet(gameField);
            //points counter for the secret objective
            deltaPoints += getPlayers().get(i).getSecretObjective().getObjectiveScore(gameField);
            incrementScore(getPlayers().get(i).getNickname(), deltaPoints);
            if (max <= getScore(playersCopy.get(i).getNickname())) {
                max = getScore(playersCopy.get(i).getNickname());
                if (realizedObjectives >= maxRealizedObjective) {
                    if (realizedObjectives == maxRealizedObjective) {
                        winners.add(playersCopy.get(i).getNickname());
                    } else {
                        winners.clear();
                        winners.add(playersCopy.get(i).getNickname());
                        maxRealizedObjective = realizedObjectives;
                    }
                }
            }
        }
        return winners;
    }

    public void addPoints(Player player, int x, int y) {
        int deltaPoints;
        deltaPoints = player.getGameField().getPlacedCard(x, y).getPlacementScore(player.getGameField(), x, y);
        if(deltaPoints + getScore(player.getNickname()) >= 20) {
            twentyPointsReached = true;
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
