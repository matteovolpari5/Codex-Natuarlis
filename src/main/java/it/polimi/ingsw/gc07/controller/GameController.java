package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.GameControllerCommand;
import it.polimi.ingsw.gc07.model.*;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.PingPongManager;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;

import java.rmi.RemoteException;
import java.util.*;

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
     * Constructor of a GameController with only the first player.
     */
    public GameController(int id, int playersNumber, DrawableDeck<DrawableCard> resourceCardsDeck,
                          DrawableDeck<GoldCard> goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                          Deck<PlaceableCard> starterCardsDeck) {
        this.gameModel = new GameModel(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDeck);
        this.pingPongManager = new PingPongManager(this);
    }


    // setters and getters

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
    synchronized GameState getState() { //TODO tutte le invocazioni (3) avvengono in GamesManager in cui si ha il lock su GamesManager
        return gameModel.getState();
    }

    public synchronized List<Player> getPlayers() {
        return gameModel.getPlayers();
    }

    int getPlayersNumber() {
        // final attribute
        return gameModel.getPlayersNumber();
    }

    // used in tests
    List<String> getWinners() {
        return gameModel.getWinners();
    }

    // used in tests
    int getCurrPlayer() {
        return gameModel.getCurrPlayer();
    }

    // used in tests
    Board getBoard() {
        return gameModel.getBoard();
    }

    // used in tests
    DrawableDeck<DrawableCard> getResourceCardsDeck() {
        return gameModel.getResourceCardsDeck();
    }

    // used in tests
    DrawableDeck<GoldCard> getGoldCardsDeck() {
        return gameModel.getGoldCardsDeck();
    }

    // used by tests
    PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return gameModel.getObjectiveCardsDeck();
    }

    // used in tests
    Deck<PlaceableCard> getStarterCardsDeck() {
        return gameModel.getStarterCardsDeck();
    }

    // used in tests
    void setPenultimateRound() {
        gameModel.setPenultimateRound(true);
    }

    // used in tests
    void setCurrentPlayer(int num) {
        gameModel.setCurrPlayer(num);
    }

    public synchronized boolean isPlayerConnected(String nickname) {
        for(Player p: gameModel.getPlayers()) {
            if(p.getNickname().equals(nickname)) {
                return p.isConnected();
            }
        }
        return false;
    }

    public synchronized CommandResult getCommandResult() {
        return gameModel.getCommandResult();
    }


    // player management methods

    public synchronized void addPlayer(Player newPlayer, VirtualView client) {
        assert(gameModel.getState().equals(GameState.GAME_STARTING)): "Wrong state";
        assert(!gameModel.getPlayerNicknames().contains(newPlayer.getNickname())): "Player already present";

        gameModel.addPlayer(newPlayer);
        gameModel.addListener(client);
        gameModel.setUpPlayerHand(newPlayer);
        pingPongManager.addPingSender(newPlayer.getNickname(), client);

        if (isFull()) {
            setup();
            gameModel.setState(GameState.PLACING_STARTER_CARDS);

            // place starter card randomly to players disconnected during GAME_STARTING
            for(Player p: getPlayers()) {
                if(!p.isConnected()) {
                    placeStarterCardRandomly(p.getNickname());
                }
            }
        }
    }

    public synchronized void disconnectPlayer(String nickname) {
        // called by setAndExecute and pingPongManager
        // this command can always be used
        assert(!gameModel.getState().equals(GameState.NO_PLAYERS_CONNECTED)): "Impossible state";
        if(!gameModel.getPlayerNicknames().contains(nickname)) {
            gameModel.setCommandResult(nickname, CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        Player player = getPlayerByNickname(nickname);
        if(player == null) {
            gameModel.setCommandResult(nickname, CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        if(!player.isConnected()) {
            gameModel.setCommandResult(nickname, CommandResult.PLAYER_ALREADY_DISCONNECTED);
            return;
        }

        VirtualView virtualView = pingPongManager.getVirtualView(nickname);

        gameModel.removeListener(virtualView);

        // set player disconnected
        player.setIsConnected(false);
        pingPongManager.notifyPlayerDisconnected(nickname);
        System.out.println("Disconnected " + nickname);

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

        // during PLACING_STARTER_CARDS, place starter card randomly
        if(gameModel.getState().equals(GameState.PLACING_STARTER_CARDS)) {
            placeStarterCardRandomly(nickname);
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

        gameModel.addListener(client);
        try {
            gameModel.sendModelViewUpdate(nickname, client);
        }catch(RemoteException e) {
            e.printStackTrace();
            // TODO
            throw new RuntimeException(e);
        }

        try {
            client.setServerGame(getId());
        } catch (RemoteException e) {
            e.printStackTrace();
            // TODO
            throw new RuntimeException(e);
        }
        pingPongManager.addPingSender(nickname, client);

        // set player connected
        player.setIsConnected(true);
        if(gameModel.getState().equals(GameState.WAITING_RECONNECTION) || gameModel.getState().equals(GameState.NO_PLAYERS_CONNECTED) ) {
            changeGameState();
        }
    }

    private void changeGameState() {
        int numPlayersConnected = gameModel.getNumPlayersConnected();
        if(numPlayersConnected == 0) {
            gameModel.setState(GameState.NO_PLAYERS_CONNECTED);
            System.out.println("timeout 0");
            startTimeoutGameEnd();
        }else if(numPlayersConnected == 1) {
            gameModel.setState(GameState.WAITING_RECONNECTION);
            System.out.println("timeout 1");
            startTimeoutGameEnd();
        }else {
            gameModel.setState(GameState.PLAYING);
        }
    }

    private void startTimeoutGameEnd() {
        new Thread(() -> {
            boolean onePlayer;
            boolean gameEnd = true;
            synchronized (this) {
                onePlayer = gameModel.getState().equals(GameState.WAITING_RECONNECTION);
            }
            for (int i = 0; i < 20 && gameEnd; i++) {
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
                System.out.println("la partita finisce");
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
            else System.out.println("la partita non finisce");
        }).start();
    }


    // command pattern methods

    public synchronized void setAndExecuteCommand(GameControllerCommand gameControllerCommand) {
        gameControllerCommand.execute(this);
    }

    public void receivePing(String nickname) {
        pingPongManager.receivePing(nickname);
    }

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
                // check if the card has been replaced or replace
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
                // check if the card has been replaced or replace
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

    public void placeStarterCard(String nickname, boolean way) {
        // check right state
        if(!gameModel.getState().equals(GameState.PLACING_STARTER_CARDS)) {
            gameModel.setCommandResult(nickname, CommandResult.WRONG_STATE);
            return;
        }
        if(!gameModel.getPlayerNicknames().contains(nickname)) {
            gameModel.setCommandResult(nickname, CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        // check player has not already placed the starter card
        Player player = getPlayerByNickname(nickname);
        assert(player != null);
        if(player.getGameField().isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2)) {
            gameModel.setCommandResult(nickname, CommandResult.CARD_ALREADY_PRESENT);
            return;
        }
        // no check for current player, starter cards can be placed in any order

        gameModel.setCommandResult(nickname, player.placeCard(
                player.getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, way)
        );

        boolean changeState = true;
        for(Player p: getPlayers()) {
            if(!p.getGameField().isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2)) {
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

    void placeStarterCardRandomly(String nickname) {
        // compute way
        Random random = new Random();
        boolean way = random.nextBoolean();
        // place starter card
        placeStarterCard(nickname, way);
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
     * Method that starts a timer when the game is ended, when the timer expires
     * it deletes the game from GamesManager.
     */
    private void endGame(){
        synchronized(this) {
            // delete rmi virtual views and rmiServerGame
            RmiServerGamesManager.getRmiServerGamesManager().deleteGame(getId());
            // delete GameController
            GamesManager.getGamesManager().deleteGame(getId());
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
        assert(gameModel.getState().equals(GameState.GAME_STARTING)): "The state is not WAITING_PLAYERS";
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