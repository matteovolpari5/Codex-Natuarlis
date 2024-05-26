package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.model_listeners.GameFieldListener;
import it.polimi.ingsw.gc07.model_listeners.GameListener;
import it.polimi.ingsw.gc07.model_listeners.PlayerListener;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.Chat;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.model_view.PlayerView;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.updates.*;

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
    private final Board board;
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
        this.currPlayer = -1;
        this.hasCurrPlayerPlaced = false;
        this.board = new Board();
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

    /**
     * Getter method for attribute id.
     * @return id of the game.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter method for attribute playersNumber.
     * @return the number of players.
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * Setter method for attribute state.
     * @param state state to be set.
     */
    public void setState(GameState state) {
        this.state = state;
        // update listeners
        sendGameModelUpdate();
    }

    /**
     * Getter method for attribute state.
     * @return state of the game.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Getter method for attribute players.
     * @return the list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Getter method for nicknames of players.
     * @return nicknames of players in the game.
     */
    public List<String> getPlayerNicknames() {
        return players.stream().map(Player::getNickname).toList();
    }

    /**
     * Getter method for attribute winners.
     * @return the list of winners.
     */
    public List<String> getWinners() {
        return winners;
    }

    /**
     * Setter method for attribute currPlayer.
     * @param currPlayer the current player that has to be set.
     */
    public void setCurrPlayer(int currPlayer) {
        this.currPlayer = currPlayer;
        // update listeners
        sendGameModelUpdate();
    }

    /**
     * Getter method for attribute currPLayer.
     * @return the current player.
     */
    public int getCurrPlayer() {
        return currPlayer;
    }

    /**
     * Getter method for hasCurrPlayerPlaced attribute.
     * @return if the current player has placed or not.
     */
    public boolean getHasCurrPlayerPlaced() {
        return hasCurrPlayerPlaced;
    }

    /**
     * Setter method for attribute hasCurrPlayerPlaced.
     * @param hasCurrPlayerPlaced the value that has to be set.
     */
    public void setHasCurrPlayerPlaced(boolean hasCurrPlayerPlaced) {
        this.hasCurrPlayerPlaced = hasCurrPlayerPlaced;
    }

    /**
     * Getter method for attribute board.
     * @return the board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Getter method for attribute resourceCardsDeck.
     * @return the resource card deck.
     */
    public DrawableDeck<DrawableCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * Getter method for attribute goldCardsDeck.
     * @return the gold card deck.
     */
    public DrawableDeck<GoldCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }

    /**
     * Getter method for attribute objectiveCardsDeck.
     * @return the objective card deck.
     */
    public PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    /**
     * Getter method for attribute starterCardsDeck.
     * @return the starter card deck.
     */
    public Deck<PlaceableCard> getStarterCardsDeck() {
        return starterCardsDeck;
    }

    /**
     * Getter method for attribute penultimateRound.
     * @return if is the penultimate round or not.
     */
    public boolean getPenultimateRound() {
        return penultimateRound;
    }

    /**
     * Setter method for attribute penultimateRound.
     * @param penultimateRound the value that has to be set.
     */
    public void setPenultimateRound(boolean penultimateRound) {
        this.penultimateRound = penultimateRound;
        // update listeners
        sendGameModelUpdate();
    }

    /**
     * Getter method for attribute additionalRound.
     * @return if is the additional round or not.
     */
    public boolean getAdditionalRound() {
        return additionalRound;
    }

    /**
     * Setter method for additionalRound.
     * @param additionalRound the value that has to be set.
     */
    public void setAdditionalRound(boolean additionalRound) {
        this.additionalRound = additionalRound;
        // update listeners
        sendGameModelUpdate();
    }

    /**
     * Method used to calculate the taken token colors.
     * @return the taken token colors.
     */
    public List<TokenColor> getTakenTokenColors() {
        List<TokenColor> takenColors = new ArrayList<>();
        for(Player p: players) {
            takenColors.add(p.getTokenColor());
        }
        return takenColors;
    }

    /**
     * Setter method for attribute commandResult.
     * @param nickname of the player that execute the command.
     * @param commandResult the result that has to be set.
     */
    public void setCommandResult(String nickname, CommandResult commandResult) {
        this.commandResult = commandResult;

        // update listeners
        CommandResultUpdate update = new CommandResultUpdate(nickname, commandResult);
        for(GameListener l: gameListeners) {
            try {
                l.receiveCommandResultUpdate(update);
            }catch(RemoteException e) {
                // will be detected by PingPongManager
            }
        }
    }

    /**
     * Getter method for the attribute commandResult.
     * @return the command result.
     */
    public CommandResult getCommandResult() {
        return commandResult;
    }

    /**
     * Setter method for the attribute emptyDecks.
     * @param value the boolean value that has to be set.
     */
    public void setEmptyDecks(boolean value) {
        this.emptyDecks = value;
    }

    /**
     * Getter method for the attribute emptyDecks.
     * @return if the decks are empty or not.
     */
    public boolean getEmptyDecks() {
        return this.emptyDecks;
    }

    /**
     * Method used to send a game model update.
     */
    private void sendGameModelUpdate() {
        GameModelUpdate update = new GameModelUpdate(id, state, currPlayer, penultimateRound, additionalRound);
        for(GameListener l: gameListeners) {
            try {
                l.receiveGameModelUpdate(update);
            }catch(RemoteException e) {
                // will be detected by PingPongManager
            }
        }
    }

    /**
     * Method used to send a deck update.
     */
    private void sendDeckUpdate() {
        DeckUpdate update = new DeckUpdate(resourceCardsDeck.revealTopCard(), goldCardsDeck.revealTopCard(),
                resourceCardsDeck.getFaceUpCards(), goldCardsDeck.getFaceUpCards(),
                objectiveCardsDeck.getFaceUpCards());
        for(GameListener l: gameListeners) {
            try {
                l.receiveDeckUpdate(update);
            }catch(RemoteException e) {
                // will be detected by PingPongManager
            }
        }
    }

    /**
     * Method used to send a winners update.
     */
    private void sendWinnersUpdate() {
        for(GameListener l: gameListeners) {
            try {
                l.receiveGameEndedUpdate(new GameEndedUpdate(new ArrayList<>(winners)));
            } catch (RemoteException e) {
                // will be detected by PingPongManager
            }
        }
    }

    /**
     * Method used to set up the resource card deck.
     */
    public void setUpResourceCardsDeck() {
        resourceCardsDeck.setUpDeck();
        // update listeners
        sendDeckUpdate();
    }

    /**
     * Method used to set up the gold card deck.
     */
    public void setUpGoldCardsDeck() {
        goldCardsDeck.setUpDeck();
        // update listeners
        sendDeckUpdate();

    }

    /**
     * Method used to set up the objective card deck.
     */
    public void setUpObjectiveCardsDeck() {
        objectiveCardsDeck.setUpDeck();
        // update listeners
        sendDeckUpdate();
    }

    /**
     * Method used to draw a card from the resource card deck.
     * @return the drawn card.
     */
    public DrawableCard drawResourceCard() {
        DrawableCard card = resourceCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    /**
     * Method used to draw a card from the gold card deck.
     * @return the drawn card.
     */
    public GoldCard drawGoldCard() {
        GoldCard card = goldCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    /**
     * Method used to draw a card from the objective card deck.
     * @return the drawn card.
     */
    public ObjectiveCard drawObjectiveCard() {
        ObjectiveCard card = objectiveCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    /**
     * Method used to draw a card from the starter card deck.
     * @return the drawn card.
     */
    public PlaceableCard drawStarterCard() {
        PlaceableCard card = starterCardsDeck.drawCard();
        // update listeners
        sendDeckUpdate();
        return card;
    }

    /**
     * Method used to draw a card from the face up resource card.
     * @param pos the position from where the card has to be drawn.
     * @return the drawn card.
     */
    public DrawableCard drawFaceUpResourceCard(int pos) {
        DrawableCard card = resourceCardsDeck.drawFaceUpCard(pos);
        // update listeners
        sendDeckUpdate();
        return card;
    }

    /**
     * Method used to draw a card from the face up gold card.
     * @param pos the position from where the card has to be drawn.
     * @return the drawn card.
     */
    public GoldCard drawFaceUpGoldCard(int pos) {
        GoldCard card = goldCardsDeck.drawFaceUpCard(pos);
        // update listeners
        sendDeckUpdate();
        return card;
    }

    /**
     * Method used to add a card to the face up resource card.
     * @param card card that has to be added.
     */
    public void addFaceUpResourceCard(DrawableCard card) {
        resourceCardsDeck.addFaceUpCard(card);
        // update listeners
        sendDeckUpdate();
    }

    /**
     * Method used to add a card to the face up gold card.
     * @param card card that has to be added.
     */
    public void addFaceUpGoldCard(GoldCard card) {
        goldCardsDeck.addFaceUpCard(card);
        // update listeners
        sendDeckUpdate();
    }

    /**
     * Method used to reveal a card from the face up resource card.
     * @param pos position of the card that will be revealed.
     * @return the card in the face up resource card.
     */
    public DrawableCard revealFaceUpResourceCard(int pos) {
        return resourceCardsDeck.revealFaceUpCard(pos);
    }

    /**
     * Method used to reveal a card from the face up gold card.
     * @param pos position of the card that will be revealed.
     * @return the card in the face up gold card.
     */
    public GoldCard revealFaceUpGoldCard(int pos) {
        return goldCardsDeck.revealFaceUpCard(pos);
    }

    /**
     * Method used to set up the player hand.
     * @param newPlayer player whose hand will be set.
     */
    public void setUpPlayerHand(Player newPlayer) {
        // set card hand
        newPlayer.addCardHand(drawResourceCard());
        newPlayer.addCardHand(drawResourceCard());
        newPlayer.addCardHand(drawGoldCard());
        // set secrete objective
        newPlayer.setSecretObjective(drawObjectiveCard());
        // set starter card
        newPlayer.setStarterCard(drawStarterCard());
    }

    /**
     * Method used to add a player to the game.
     * @param newPlayer player that has to be added.
     */
    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
        board.addPlayer(newPlayer.getNickname());

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

        // update sent in addListener,
        // in order to update also the newly created listener
    }

    /**
     * Method used to add listener to the client.
     * @param client client to which the listener has to be added.
     */
    public void addListener(VirtualView client) {
        // called as soon as a player joins a game
        gameListeners.add(client);
        chat.addListener(client);
        board.addListener(client);
        for(Player p: players) {
            p.addListener(client);
            p.getGameField().addListener(client);
        }

        // send first game and deck update to player
        sendGameModelUpdate();
        sendDeckUpdate();

        // update listeners
        List<PlayerView> playerViews = new ArrayList<>();
        for(Player p: players) {
            playerViews.add(new PlayerView(p.getNickname(), p.getTokenColor()));
        }
        PlayerJoinedUpdate playerUpdate = new PlayerJoinedUpdate(playerViews);
        for(GameListener l: gameListeners) {
            try {
                l.receivePlayerJoinedUpdate(playerUpdate);
            }catch(RemoteException e) {
                // will be detected by PingPongManager
            }
        }

        System.out.println("Number of listeners: " + gameListeners.size());
    }

    /**
     * Method used to send a full model view update to a client who has just reconnected.
     * @param nickname nickname of the player to which the update will be sent.
     * @param client client of the player to which the update will be sent.
     */
    public void sendModelViewUpdate(String nickname, VirtualView client) {
        // gameModel update and deckUpdate sent in addListener

        try {
            // when the player view is creates, isConnected = true and isStalled = false
            // if not default, send update
            for (Player p : players) {
                // if not default value, send connection update
                if (!p.isConnected()) {
                    client.receiveConnectionUpdate(new ConnectionUpdate(p.getNickname(), false));
                }

                // if not default value, send stall update
                if (p.getIsStalled()) {
                    client.receiveStallUpdate(new StallUpdate(p.getNickname(), true));
                }

                // send full game field update
                client.receiveFullGameFieldUpdate(new FullGameFieldUpdate(p.getNickname(), p.getStarterCard(), p.getGameField().getCardsContent(), p.getGameField().getCardsFace(), p.getGameField().getCardsOrder()));

                // send current hand update
                client.receiveCardHandUpdate(new CardHandUpdate(p.getNickname(), p.getCurrentHand(), p.getSecretObjective()));

                // send starter card update
                client.receiveStarterCardUpdate(new StarterCardUpdate(p.getNickname(), p.getStarterCard()));

                // send score update
                client.receiveScoreUpdate(new ScoreUpdate(p.getNickname(), board.getScore(p.getNickname())));

            }

            // send full chat update
            client.receiveFullChatUpdate(new FullChatUpdate(new ArrayList<>(chat.getContent(nickname))));
            // send game model update
            sendGameModelUpdate();
            // send deck update
            sendDeckUpdate();

        }catch(RemoteException e) {
            // will be detected by PingPongManager
        }
    }

    /**
     * Method used to remove a listener to a player.
     * @param client client of the player to which the listener will be removed.
     */
    public void removeListener(VirtualView client) {
        gameListeners.remove(client);
        chat.removeListener(client);
        board.removeListener(client);
        for(Player p: players) {
            p.removeListener(client);
            p.getGameField().removeListener(client);
        }

        System.out.println("Number of listeners: " + gameListeners.size());
    }

    /**
     * Method used to add a message to the public chat.
     * @param content content of the message.
     * @param sender nickname of the sender of the message.
     */
    public void addChatPublicMessage(String content, String sender) {
        chat.addPublicMessage(content, sender);
    }

    /**
     * Method used to add a message to the private chat.
     * @param content content of the message.
     * @param sender nickname of the sender of the message.
     * @param receiver nickname of the receiver of the message.
     */
    public void addChatPrivateMessage(String content, String sender, String receiver) {
        chat.addPrivateMessage(content, sender, receiver);
    }

    /**
     * Method used to increment the score of a player.
     * @param nickname nickname of the player to which points will be incremented.
     * @param deltaScore value of the increment.
     */
    private void incrementScore(String nickname, int deltaScore) {
        board.incrementScore(nickname, deltaScore);
    }

    /**
     * Method that return the number of players connected.
     * @return the number of players connected.
     */
    public int getNumPlayersConnected() {
        int numPlayersConnected = 0;
        for (Player p: players){
            if (p.isConnected()){
                numPlayersConnected++;
            }
        }
        return numPlayersConnected;
    }

    /**
     * Method that check if the game has a player or not.
     * @param nickname nickname of the player that has to be checked in the game.
     * @return if the game has the player or not.
     */
    public boolean hasPlayer(String nickname) {
        for(Player p: players){
            if (p.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that check if a player has a specific token color.
     * @param tokenColor token color that will be checked.
     * @return if ha player has the specific token color or not.
     */
    public boolean hasPlayerWithTokenColor(TokenColor tokenColor) {
        for(Player p: players){
            if(p.getTokenColor().equals(tokenColor)){
                return true;
            }
        }
        return false;
    }

    /**
     * Method that compute the winner/s of the game.
     */
    public void computeWinners() {
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
            if (max == board.getScore(players.get(i).getNickname())) {
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
            else if(max < board.getScore(players.get(i).getNickname()))
            {
                max = board.getScore(players.get(i).getNickname());
                computedWinners.clear();
                computedWinners.add(players.get(i).getNickname());
                maxRealizedObjective = realizedObjectives;
            }
        }

        this.winners = computedWinners;
        // update listeners
        sendGameModelUpdate();
        sendWinnersUpdate();
    }

    /**
     * Setter method for attribute winners.
     * @param nickname nickname of the winner that has to be added to the winners.
     */
    public void setWinner(String nickname){
        winners.add(nickname);
        // update listeners
        sendWinnersUpdate();
    }

    /**
     * Method that add points to a player.
     * @param player player to whom the points will be added.
     * @param x row where the card is placed.
     * @param y column where the card is placed.
     */
    public void addPoints(Player player, int x, int y) {
        int deltaPoints;
        deltaPoints = player.getGameField().getPlacedCard(x, y).getPlacementScore(player.getGameField(), x, y);
        if(deltaPoints + board.getScore(player.getNickname()) >= 20) {
            setPenultimateRound(true); // setter updated listeners
            if((deltaPoints + board.getScore(player.getNickname())) > 29) {
                board.setScore(player.getNickname(), 29);
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
