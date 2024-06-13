package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.GameControllerCommand;
import it.polimi.ingsw.gc07.model.*;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.model.CardType;
import it.polimi.ingsw.gc07.model.TokenColor;
import it.polimi.ingsw.gc07.network.PingPongManager;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Class representing the controller of a single game.
 * Manages 2 to 4 players and all the actions they can perform during a game.
 */
public class GameController {
    /**
     * Reference to game model.
     */
    private final GameModel gameModel;
    /**
     * PingPongManager of the game, accepts players' pings and sends pongs, in order to manage connection problems.
     */
    private final PingPongManager pingPongManager;
    /**
     * Max time before a reconnection, after which the game ends.
     */
    private int maxReconnectionTime;

    /**
     * Constructor of a GameController with only the first player.
     */
    public GameController(int id, int playersNumber, DrawableDeck<DrawableCard> resourceCardsDeck,
                          DrawableDeck<GoldCard> goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                          Deck<PlaceableCard> starterCardsDeck) {
        this.gameModel = new GameModel(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDeck);
        this.pingPongManager = new PingPongManager(this);
        this.maxReconnectionTime = 60;
    }


    // setters and getters

    /**
     * Method used to set max reconnection time.
     * @param maxReconnectionTime max reconnection time in seconds
     */
    // used in tests
    void setMaxReconnectionTime(int maxReconnectionTime) {
        this.maxReconnectionTime = maxReconnectionTime;
    }

    /**
     * Getter for the game id.
     * @return game id
     */
    int getId() {
        // id is final
        return gameModel.getId();
    }

    /**
     * Setter for the state of the game.
     */
    // used in tests
    void setState(GameState state) {
        gameModel.setState(state);
    }

    /**
     * Getter for the state of the game.
     * @return the state of the game
     */
    synchronized GameState getState() {
        return gameModel.getState();
    }

    /**
     * Getter for the list of players in the game.
     * @return players
     */
    public synchronized List<Player> getPlayers() {
        return gameModel.getPlayers();
    }

    /**
     * Method used to get the number of players in the game.
     * @return number of players in the game
     */
    int getPlayersNumber() {
        // final attribute
        return gameModel.getPlayersNumber();
    }

    /**
     * Method used to get the taken token colors.
     * @return taken token colors
     */
    synchronized List<TokenColor> getTakenTokenColors() {
        return gameModel.getTakenTokenColors();
    }

    /**
     * Method used to the get the list of winners' nicknames.
     * @return winners' nicknames
     */
    // used in tests
    List<String> getWinners() {
        return gameModel.getWinners();
    }

    /**
     * Method used to get the current player.
     * @return current player position in the list
     */
    // used in tests
    int getCurrPlayer() {
        return gameModel.getCurrPlayer();
    }

    /**
     * Method used to receive the board reference.
     * @return board reference
     */
    // used in tests
    Board getBoard() {
        return gameModel.getBoard();
    }

    /**
     * Method used to get the resource cards deck.
     * @return resource cards deck
     */
    // used in tests
    DrawableDeck<DrawableCard> getResourceCardsDeck() {
        return gameModel.getResourceCardsDeck();
    }

    /**
     * Method used to get the gold cards deck.
     * @return gold cards deck
     */
    // used in tests
    DrawableDeck<GoldCard> getGoldCardsDeck() {
        return gameModel.getGoldCardsDeck();
    }

    /**
     * Method used to get the objective cards deck.
     * @return objective cards deck
     */
    // used by tests
    PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return gameModel.getObjectiveCardsDeck();
    }

    /**
     * Method used to get the starter cards deck.
     * @return starter cards deck
     */
    // used in tests
    Deck<PlaceableCard> getStarterCardsDeck() {
        return gameModel.getStarterCardsDeck();
    }

    /**
     * Method used to set the current round as the penultimate one.
     */
    // used in tests
    void setPenultimateRound() {
        gameModel.setPenultimateRound(true);
    }

    /**
     * Method used to set the current player, used for the purposes.
     */
    // used in tests
    void setCurrentPlayer(int num) {
        gameModel.setCurrPlayer(num);
    }

    /**
     * Method used to see if a player is connected.
     * @param nickname nickname
     * @return true if the player is connected
     */
    public synchronized boolean isPlayerConnected(String nickname) {
        for(Player p: gameModel.getPlayers()) {
            if(p.getNickname().equals(nickname)) {
                return p.isConnected();
            }
        }
        return false;
    }

    /**
     * Method used to set a value for the command result.
     * @param nickname nickname
     * @param commandResult command result
     */
    public synchronized void setCommandResult(String nickname, CommandResult commandResult){
        gameModel.setCommandResult(nickname, commandResult);
    }

    /**
     * Getter method for the last command result.
     * @return last command result
     */
    public synchronized CommandResult getCommandResult() {
        return gameModel.getCommandResult();
    }


    // player management methods

    /**
     * Method used to add a player to the game.
     * @param newPlayer new player
     * @param client client associated to the player
     */
    public synchronized void addPlayer(Player newPlayer, VirtualView client) {
        assert(gameModel.getState().equals(GameState.GAME_STARTING)): "Wrong state";
        assert(!gameModel.getPlayerNicknames().contains(newPlayer.getNickname())): "Player already present";

        gameModel.addPlayer(newPlayer);
        gameModel.addListener(client);
        gameModel.setUpPlayerHand(newPlayer);
        pingPongManager.addPingSender(newPlayer.getNickname(), client);

        if (isFull()) {
            setup();
            gameModel.setState(GameState.SETTING_INITIAL_CARDS);

            // place starter card randomly to players disconnected during GAME_STARTING
            for(Player p: getPlayers()) {
                if(!p.isConnected()) {
                    setInitialCardsRandomly(p.getNickname());
                }
            }
        }
    }

    /**
     * Method used to disconnect the player with the given nickname.
     * @param nickname nickname of the player to disconnect
     */
    public synchronized void disconnectPlayer(String nickname) {
        // called by setAndExecute and pingPongManager
        // this command can always be used
        assert(!gameModel.getState().equals(GameState.NO_PLAYERS_CONNECTED)): "Impossible state";
        if(!gameModel.getPlayerNicknames().contains(nickname)) {
            gameModel.setCommandResult(nickname, CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        Player player = getPlayerByNickname(nickname);
        assert(player != null);
        if(!player.isConnected()) {
            gameModel.setCommandResult(nickname, CommandResult.PLAYER_ALREADY_DISCONNECTED);
            return;
        }

        VirtualView virtualView = pingPongManager.getVirtualView(nickname);

        gameModel.removeListener(virtualView);

        // set player disconnected
        player.setIsConnected(false);
        pingPongManager.notifyPlayerDisconnected(nickname);
        SafePrinter.println("Disconnected " + nickname);

        // if the player is the current one
        if(gameModel.getState().equals(GameState.PLAYING) && gameModel.getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)) {
            if(!gameModel.getHasCurrPlayerPlaced()) {
                // if he has not placed a card
                changeCurrPlayer();
            }else {
                // if he has placed a card (but not drawn one)
                Random random = new Random();
                if(gameModel.revealFaceUpResourceCard(0) == null) {
                    // only gold cards left
                    drawFaceUpCard(nickname, CardType.GOLD_CARD, 0);
                }else if(gameModel.revealFaceUpGoldCard(0) == null) {
                    // only resource cards left
                    drawFaceUpCard(nickname, CardType.RESOURCE_CARD, 0);
                }else {
                    // both decks are not empty
                    if(random.nextBoolean()) {
                        drawFaceUpCard(nickname, CardType.RESOURCE_CARD, 0);
                    }else {
                        drawFaceUpCard(nickname, CardType.GOLD_CARD, 0);
                    }
                }
            }
        }

        // during SETTING_INITIAL_CARDS, place starter card and choose objective randomly
        if(gameModel.getState().equals(GameState.SETTING_INITIAL_CARDS)) {
            setInitialCardsRandomly(nickname);
        }
        if(gameModel.getState().equals(GameState.PLAYING) || gameModel.getState().equals(GameState.WAITING_RECONNECTION) ) {
            changeGameState();
        }
        // for other game states, I don't have to change state

        gameModel.setCommandResult(nickname, CommandResult.DISCONNECTION_SUCCESSFUL);
    }

    /**
     * Method that reconnects a player from ClientMain.
     * It allows the player to choose new connection and interface settings.
     * @param nickname player's nickname
     */
    public synchronized void reconnectPlayer(VirtualView client, String nickname, boolean connectionType, boolean interfaceType) {
        // this command can always be used
        assert(gameModel.getPlayerNicknames().contains(nickname)): "Player not present";
        Player player;
        player = getPlayerByNickname(nickname);
        assert(player != null);
        assert(!player.isConnected()): "Player already connected";
        player.setConnectionType(connectionType);
        player.setInterfaceType(interfaceType);
        try {
            client.setServerGame(getId());
        } catch (RemoteException e) {
            disconnectPlayer(nickname);
            return;
        }
        gameModel.addListener(client);
        gameModel.sendModelViewUpdate(nickname, client);
        pingPongManager.addPingSender(nickname, client);

        // set player connected
        player.setIsConnected(true);
        if(gameModel.getState().equals(GameState.WAITING_RECONNECTION) || gameModel.getState().equals(GameState.NO_PLAYERS_CONNECTED) ) {
            changeGameState();
        }
    }

    /**
     * Method used to receive a full model view update.
     * @param nickname nickname
     */
    public void sendModelView(String nickname) {
        gameModel.sendModelViewUpdate(nickname, pingPongManager.getVirtualView(nickname));
    }

    /**
     * Method used to compute and change the game state after a disconnection or reconnection.
     */
    private void changeGameState() {
        int numPlayersConnected = gameModel.getNumPlayersConnected();
        if(numPlayersConnected == 0) {
            gameModel.setState(GameState.NO_PLAYERS_CONNECTED);
            startTimeoutGameEnd();
        }else if(numPlayersConnected == 1) {
            gameModel.setState(GameState.WAITING_RECONNECTION);
            startTimeoutGameEnd();
        }else {
            gameModel.setState(GameState.PLAYING);
        }
    }

    /**
     * Method used to start a timer that will make the game end.
     */
    private void startTimeoutGameEnd() {
        new Thread(() -> {
            boolean onePlayer;
            boolean gameEnd = true;
            synchronized (this) {
                onePlayer = gameModel.getState().equals(GameState.WAITING_RECONNECTION);
            }
            for (int i = 0; i < this.maxReconnectionTime && gameEnd; i++) {
                try {
                    Thread.sleep(1000); // wait one second for each iteration
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                synchronized (this) {
                    if (gameModel.getNumPlayersConnected() == 1 && !onePlayer) {
                        // restart the timer if one player reconnects
                        i = 0;
                        gameModel.setState(GameState.WAITING_RECONNECTION);
                        onePlayer = true;
                    }
                    if (gameModel.getNumPlayersConnected() == 0 && onePlayer) {
                        //restart the timer if nobody is connected
                        i = 0;
                        gameModel.setState(GameState.NO_PLAYERS_CONNECTED);
                        onePlayer = false;
                    }
                    if (gameModel.getNumPlayersConnected() > 1) {
                        gameModel.setState(GameState.PLAYING);
                        gameEnd = false;
                    }
                }
            }
            if(gameEnd){
                synchronized(this) {
                    gameModel.setState(GameState.GAME_ENDED);
                    if (onePlayer) {
                        for (Player p : gameModel.getPlayers()) {
                            if (p.isConnected()) {
                                gameModel.setWinner(p.getNickname());
                            }
                        }
                    }
                    endGame();
                }
            }
        }).start();
    }


    // command pattern methods

    /**
     * Method used to set and execute a command.
     * @param gameControllerCommand game controller command
     */
    public synchronized void setAndExecuteCommand(GameControllerCommand gameControllerCommand) {
        gameControllerCommand.execute(this);
    }

    /**
     * Method used to receive a ping.
     * @param nickname nickname of the ping sender
     */
    public void receivePing(String nickname) {
        pingPongManager.receivePing(nickname);
    }

    /**
     * Method used to add a private message to the chat.
     * @param content message content
     * @param sender message sender
     * @param receiver message receiver
     */
    public void addChatPrivateMessage(String content, String sender, String receiver) {
        // no state check, this command be used all the time
        List<String> playerNicknames = gameModel.getPlayerNicknames();
        // check valid sender
        if(!playerNicknames.contains(sender)) {
            gameModel.setCommandResult(sender, CommandResult.WRONG_SENDER);
            return;
        }
        // check valid receiver
        if(!playerNicknames.contains(receiver)) {
            gameModel.setCommandResult(sender, CommandResult.WRONG_RECEIVER);
            return;
        }
        if(sender.equals(receiver)) {
            gameModel.setCommandResult(sender, CommandResult.WRONG_RECEIVER);
            return;
        }
        // add message to the chat
        gameModel.addChatPrivateMessage(content, sender, receiver);
        gameModel.setCommandResult(sender, CommandResult.SUCCESS);
    }

    /**
     * Method used to add a public message to the chat.
     * @param content message content
     * @param sender message sender
     */
    public void addChatPublicMessage(String content, String sender) {
        // no state check, this command be used all the time
        List<String> playerNicknames = gameModel.getPlayerNicknames();
        // check valid sender
        if(!playerNicknames.contains(sender)){
            gameModel.setCommandResult(sender, CommandResult.WRONG_SENDER);
            return;
        }
        // add message to chat
        gameModel.addChatPublicMessage(content, sender);
        gameModel.setCommandResult(sender, CommandResult.SUCCESS);
    }

    /**
     * Method used to draw a deck card from a deck.
     * @param nickname nickname
     * @param type card type
     */
    public void drawDeckCard(String nickname, CardType type) {
        if(!gameModel.getState().equals(GameState.PLAYING)) {
            gameModel.setCommandResult(nickname, CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)){
            gameModel.setCommandResult(nickname, CommandResult.WRONG_PLAYER);
            return;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            gameModel.setCommandResult(nickname, CommandResult.WRONG_CARD_TYPE);
            return;
        }
        if(!gameModel.getHasCurrPlayerPlaced()) {
            gameModel.setCommandResult(nickname, CommandResult.NOT_PLACED_YET);
            return;
        }
        DrawableCard card;
        if(type.equals(CardType.RESOURCE_CARD)) {
            card = gameModel.drawResourceCard();
            if(card == null) {
                gameModel.setCommandResult(nickname, CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);
        }
        if(type.equals(CardType.GOLD_CARD)) {
            card = gameModel.drawGoldCard();
            if(card == null){
                gameModel.setCommandResult(nickname, CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);
        }
        changeCurrPlayer();
        gameModel.setCommandResult(nickname, CommandResult.SUCCESS);
    }

    /**
     * Method used to draw a face up card.
     * @param nickname nickname
     * @param type card type
     * @param pos position in the starter cards list
     */
    public void drawFaceUpCard(String nickname, CardType type, int pos) {
        if(!gameModel.getState().equals(GameState.PLAYING)) {
            gameModel.setCommandResult(nickname, CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)){
            gameModel.setCommandResult(nickname, CommandResult.WRONG_PLAYER);
            return;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            gameModel.setCommandResult(nickname, CommandResult.WRONG_CARD_TYPE);
            return;
        }
        if(!gameModel.getHasCurrPlayerPlaced()) {
            gameModel.setCommandResult(nickname, CommandResult.NOT_PLACED_YET);
            return;
        }

        assert(!gameModel.getEmptyDecks()): "Place card changes current player";

        DrawableCard card;
        if(type.equals(CardType.RESOURCE_CARD)) {
            card = gameModel.drawFaceUpResourceCard(pos);
            if(card == null) {
                gameModel.setCommandResult(nickname, CommandResult.CARD_NOT_PRESENT);
                return;
            } else{
                getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);
                // check if the deck is empty
                // if it is, draw a card from the other deck
                if(gameModel.revealFaceUpResourceCard(1) == null) {
                    GoldCard newFaceUpCard = gameModel.drawGoldCard();
                    if(newFaceUpCard != null) {
                        gameModel.addFaceUpGoldCard(newFaceUpCard);
                    }
                }
            }
        }
        else if(type.equals(CardType.GOLD_CARD)) {
            card = gameModel.drawFaceUpGoldCard(pos);
            if(card == null) {
                gameModel.setCommandResult(nickname, CommandResult.CARD_NOT_PRESENT);
                return;
            }
            else{
                getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);
                // check if the deck is empty
                // if it is, draw a card from the other deck
                if(gameModel.revealFaceUpGoldCard(1) == null) {
                    DrawableCard newFaceUpCard = gameModel.drawResourceCard();
                    if(newFaceUpCard != null) {
                        gameModel.addFaceUpResourceCard(newFaceUpCard);
                    }
                }
            }
        }

        // check if decks are empty
        if(gameModel.revealFaceUpResourceCard(0) == null && gameModel.revealFaceUpGoldCard(0) == null)  {
            // both decks are empty
            gameModel.setEmptyDecks(true);
            gameModel.setPenultimateRound(true);
        }
        gameModel.setCommandResult(nickname, CommandResult.SUCCESS);
        changeCurrPlayer();
    }

    /**
     * Method used to place a card on the game field.
     * @param nickname nickname
     * @param pos position of the card in the player's hand
     * @param x x position of the game field
     * @param y y position of the game field
     * @param way way of the card in the game field
     */
    public void placeCard(String nickname, int pos, int x, int y, boolean way) {
        DrawableCard card;
        if(!gameModel.getState().equals(GameState.PLAYING)){
            gameModel.setCommandResult(nickname, CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)) {
            gameModel.setCommandResult(nickname, CommandResult.WRONG_PLAYER);
            return;
        }
        if(gameModel.getHasCurrPlayerPlaced()) {
            gameModel.setCommandResult(nickname, CommandResult.CARD_ALREADY_PLACED);
            return;
        }
        Player player = getPlayerByNickname(nickname);
        assert(player != null);

        if(pos < 0 || pos >= player.getCurrentHand().size()) {
            gameModel.setCommandResult(nickname, CommandResult.CARD_NOT_PRESENT);
            return;
        }
        card = player.getCurrentHand().get(pos);
        CommandResult result = player.placeCard(card,x,y,way);
        if(result.equals(CommandResult.SUCCESS)) {
            gameModel.setHasCurrPlayerPlaced(true);
            getPlayers().get(gameModel.getCurrPlayer()).removeCardHand(card);
            addPoints(nickname, x, y);    // the card has just been placed

            // check if the player is stalled
            boolean isStalled = true;
            CommandResult resultStall;
            for(int i = 0; i < GameField.getDim() && isStalled; i++) {
                for (int j = 0; j < GameField.getDim() && isStalled; j++) {
                    // check if the firs card (a casual card), is placeable on the back,
                    // i.e. check only the indexes

                    resultStall = player.getCurrentHand().getFirst().isPlaceable(
                            new GameField(player.getGameField()), i, j, true);
                    if (resultStall.equals(CommandResult.SUCCESS)) {
                        isStalled = false;
                    }
                }
            }
            if(isStalled) {
                getPlayers().get(gameModel.getCurrPlayer()).setIsStalled(true);
            }
        }
        if(gameModel.getEmptyDecks()) {
            changeCurrPlayer();
        }
        gameModel.setCommandResult(nickname, result);
    }

    /**
     * Method used to place a player's starter card on the provided way and to choose a secret objective.
     * @param nickname nickname
     * @param starterCardWay way the card must be placed
     * @param chosenObjective chosen objective
     */
    public void setInitialCards(String nickname, boolean starterCardWay, boolean chosenObjective) {
        // check right state
        if(!gameModel.getState().equals(GameState.SETTING_INITIAL_CARDS)) {
            gameModel.setCommandResult(nickname, CommandResult.WRONG_STATE);
            return;
        }
        if(!gameModel.getPlayerNicknames().contains(nickname)) {
            gameModel.setCommandResult(nickname, CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        // check player has not already placed the starter card or chosen an objective card
        Player player = getPlayerByNickname(nickname);
        assert(player != null);
        if( player.getGameField().isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2) ||
                player.getSecretObjectives().size() == 1) {
            gameModel.setCommandResult(nickname, CommandResult.CARD_ALREADY_PRESENT);
            return;
        }
        // no check for current player, initial cards can be set in any order

        // place starter card
        gameModel.setCommandResult(nickname, player.placeCard(
                player.getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, starterCardWay)
        );

        // choose secret objective
        player.chooseSecretObjective(chosenObjective);

        // check and change game state
        boolean changeState = true;
        for(Player p: getPlayers()) {
            if(!p.getGameField().isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2) ||
                    p.getSecretObjectives().size() == 2) {
                // someone has to place the starter card
                changeState = false;
            }
        }
        if(changeState) {
            changeGameState();
        }
    }


    // ----------------------
    // utils
    // ----------------------

    /**
     * Method used to place a starter card randomly when a player disconnects.
     * @param nickname nickname
     */
    void setInitialCardsRandomly(String nickname) {
        // compute starter card way
        Random random = new Random();
        boolean starterCardWay = random.nextBoolean();

        // compute secret objective
        boolean secretObjective = random.nextBoolean();

        // place starter card
        setInitialCards(nickname, starterCardWay, secretObjective);
    }

    /**
     * Method telling if a player is in a game.
     * @param nickname nickname of the player
     * @return true if the player is in the game
     */
    synchronized boolean hasPlayer(String nickname) {
        return gameModel.hasPlayer(nickname);
    }

    /**
     * Method telling if a player with the given token color is in the game.
     * @param tokenColor token color to search
     * @return true if there is a player with the given token color
     */
    synchronized boolean hasPlayerWithTokenColor(TokenColor tokenColor) {
        return gameModel.hasPlayerWithTokenColor(tokenColor);
    }

    Player getPlayerByNickname(String nickname) {
        for(Player p: gameModel.getPlayers()) {
            if(p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Method that change the current player, if it's the last turn and all the players
     * played the same amount of turn it computes the winner;
     * if a player is disconnect from the game he loses the turn,
     * if a player is stalled he will be skipped.
     */
    void changeCurrPlayer() {
        assert(gameModel.getState().equals(GameState.PLAYING)): "Method changeCurrentPlayer called in a wrong state";
        if(gameModel.getCurrPlayer() == getPlayers().size()-1)
            gameModel.setCurrPlayer(0);
        else
            gameModel.setCurrPlayer(gameModel.getCurrPlayer()+1);
        gameModel.setHasCurrPlayerPlaced(false);
        if(gameModel.getPenultimateRound()) {
            if(getPlayers().get(gameModel.getCurrPlayer()).isFirst() && gameModel.getAdditionalRound()) {
                gameModel.setState(GameState.GAME_ENDED);
                gameModel.computeWinners();
                // delete game from GamesManager
                endGame();
                return;
                // the game is ended
            }
            else if(getPlayers().get(gameModel.getCurrPlayer()).isFirst()) {
                gameModel.setAdditionalRound(true);
            }
        }
        if(!getPlayers().get(gameModel.getCurrPlayer()).isConnected()) {
            changeCurrPlayer();
        }
        if(getPlayers().get(gameModel.getCurrPlayer()).getIsStalled()) {
            boolean found = false;
            for(Player p: getPlayers()) {
                if (!p.getIsStalled()) {
                    found = true;
                    break;
                }
            }
            if(found)
                changeCurrPlayer();
            else {
                gameModel.setState(GameState.GAME_ENDED);
                gameModel.computeWinners();
                endGame();
            }
        }
    }

    /**
     * Method that deletes the game from the lobby controller.
     */
    private void endGame(){
        synchronized(this) {
            // delete GameController
            GamesManager.getGamesManager().deleteGame(getId());
            // delete rmi virtual views and rmiServerGame
            RmiServerGamesManager.getRmiServerGamesManager().deleteGame(getId());
        }
    }

    /**
     * Method telling if there are available places in the game.
     * @return true if no other player can connect to the game
     */
    private boolean isFull(){
        return getPlayers().size() == gameModel.getPlayersNumber();
    }

    /**
     * Method to set up the game: the first player is chosen and 4 cards (2 gold and 2 resource) are revealed.
     */
    private void setup() {
        assert(gameModel.getState().equals(GameState.GAME_STARTING)): "The state is not GAME_STARTING";
        // choose randomly the first player
        Random random = new Random();
        gameModel.setCurrPlayer(random.nextInt(gameModel.getPlayersNumber()));
        getPlayers().get(gameModel.getCurrPlayer()).setFirst();
        gameModel.setHasCurrPlayerPlaced(false);

        // draw card can't return null, since the game hasn't already started

        // set up decks
        gameModel.setUpResourceCardsDeck();
        gameModel.setUpGoldCardsDeck();
        gameModel.setUpObjectiveCardsDeck();
    }

    /**
     * Method that adds points to a player and checks if a player had reached 20 points.
     * @param nickname nickname of the player
     * @param x where the card is placed in the matrix
     * @param y where the card is placed in the matrix
     */
    private void addPoints(String nickname, int x, int y) {
        assert(gameModel.getState().equals(GameState.PLAYING)): "Wrong game state";
        assert(getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)): "Not the current player";
        Player player = getPlayerByNickname(nickname);
        assert(player != null);
        assert(player.getGameField().isCardPresent(x, y)) : "No card present in the provided position";

        gameModel.addPoints(player, x, y);
    }
}

