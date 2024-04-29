package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.model.*;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.VirtualView;

import java.util.*;

public class GameController {
    /**
     * Reference to game model.
     */
    private final GameModel gameModel;

    /**
     * Timeout for the reconnection.
     */
    private final Timer timeout;
    /**
     * Timeout for keeping track of players' connection.
     */
    private final Map<String, Timer> playersTimer;

    /**
     * Constructor of a GameController with only the first player.
     */
    public GameController(int id, int playersNumber, ResourceCardsDeck resourceCardsDeck,
                          GoldCardsDeck goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                          Deck<PlaceableCard> starterCardsDeck) {
        this.gameModel = new GameModel(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDeck);
        this.timeout = new Timer();
        this.playersTimer = new HashMap<>();
    }

    // ------------------------------
    // setters and getters
    // ------------------------------

    /**
     * Getter for the game id.
     * @return game id
     */
    int getId() {
        return gameModel.getId();
    }

    /**
     * Setter for the state of the game.
     */
    // used in tests
    synchronized void setState(GameState state) {
        gameModel.setState(state);
    }

    /**
     * Getter for the state of the game.
     * @return the state of the game
     */
    synchronized GameState getState() {
        return gameModel.getState();
    }

    public List<Player> getPlayers() {
        return gameModel.getPlayers();
    }

    public List<String> getPlayerNicknames() {
        List<String> playerNicknames = new ArrayList<>();
        for(Player p: gameModel.getPlayers()) {
            playerNicknames.add(p.getNickname());
        }
        return playerNicknames;
    }

    List<String> getWinners(){
        return gameModel.getWinners();
    }

    public synchronized int getCurrPlayer() {
        return gameModel.getCurrPlayer();
    }

    synchronized void setHasCurrPlayerPlaced() {
        gameModel.setHasCurrPlayerPlaced(true);
    }

    synchronized void setHasNotCurrPlayerPlaced() {
        gameModel.setHasCurrPlayerPlaced(false);
    }

    synchronized boolean getHasCurrPlayerPlaced() {
        return gameModel.getHasCurrPlayerPlaced();
    }

    // used in tests
    ScoreTrackBoard getScoreTrackBoard() {
        return gameModel.getScoreTrackBoard();
    }

    ResourceCardsDeck getResourceCardsDeck() {
        return gameModel.getResourceCardsDeck();
    }

    GoldCardsDeck getGoldCardsDeck() {
        return gameModel.getGoldCardsDeck();
    }

    PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return gameModel.getObjectiveCardsDeck();
    }

    Deck<PlaceableCard> getStarterCardsDeck() {
        return gameModel.getStarterCardsDeck();
    }

    // used in tests
    synchronized void setTwentyPointsReached() {
        gameModel.setTwentyPointsReached(true);
    }

    synchronized void setCurrentPlayer(int num) {
        gameModel.setCurrPlayer(num);
    }

    public CommandResult getCommandResult() {
        return gameModel.getCommandResult();
    }

    public synchronized void setAndExecuteCommand(GameCommand gameCommand) {
        gameCommand.execute(this);
    }

    public void addRMIListener(VirtualView client) {
        gameModel.addRMIListener(client);
    }

    // ----------------------
    // command pattern methods
    // ----------------------

    public void addChatPrivateMessage(String content, String sender, String receiver) {
        // no state check, this command be used all the time
        List<String> playerNicknames = gameModel.getPlayerNicknames();
        // check valid sender
        if(!playerNicknames.contains(sender)) {
            gameModel.setCommandResult(CommandResult.WRONG_SENDER);
            return;
        }
        // check valid receiver
        if(!playerNicknames.contains(receiver)) {
            gameModel.setCommandResult(CommandResult.WRONG_RECEIVER);
            return;
        }
        if(sender.equals(receiver)) {
            gameModel.setCommandResult(CommandResult.WRONG_RECEIVER);
            return;
        }
        // add message to the chat
        gameModel.addChatPrivateMessage(content, sender, receiver);
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    public void addChatPublicMessage(String content, String sender) {
        // no state check, this command be used all the time
        List<String> playerNicknames = gameModel.getPlayerNicknames();
        // check valid sender
        if(!playerNicknames.contains(sender)){
            gameModel.setCommandResult(CommandResult.WRONG_SENDER);
            return;
        }
        // add message to chat
        gameModel.addChatPublicMessage(content, sender);
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    // TODO synchronized chi lo chiama?
    public void addPlayer(Player newPlayer) {
        if(!gameModel.getState().equals(GameState.GAME_STARTING)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(getPlayerNicknames().contains(newPlayer.getNickname())) {
            gameModel.setCommandResult(CommandResult.PLAYER_ALREADY_PRESENT);
            return;
        }

        Timer timeout = new Timer();
        playersTimer.put(newPlayer.getNickname(), timeout);
        startTimeoutReconnection(timeout, newPlayer.getNickname());

        gameModel.addPlayer(newPlayer);

        if (isFull()) {
            setup();
            gameModel.setState(GameState.PLACING_STARTER_CARDS);
        }
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    public void disconnectPlayer(String nickname) {
        // this command can always be used
        if(!getPlayerNicknames().contains(nickname)) {
            gameModel.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            playersTimer.get(nickname).cancel();
            playersTimer.get(nickname).purge();
            int pos = getPlayerPosByNickname(nickname);
            if(!getPlayers().get(pos).isConnected()) {
                gameModel.setCommandResult(CommandResult.PLAYER_ALREADY_DISCONNECTED);
                return;
            }
            getPlayers().get(pos).setIsConnected(false);
            int numPlayersConnected = gameModel.getNumPlayersConnected();
            if (numPlayersConnected == 1){
                gameModel.setState(GameState.WAITING_RECONNECTION);
                // TODO start the timer, when it ends, the only player left wins
                startTimeoutGameEnd();
            }
            else if (numPlayersConnected == 0) {
                gameModel.setState(GameState.NO_PLAYERS_CONNECTED);
                // TODO start the timer, when it ends, the game ends without winner
                startTimeoutGameEnd();
            }
        }
        catch(PlayerNotPresentException e){
            gameModel.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    private void startTimeoutGameEnd(){
        new Thread(() ->{
            timeout.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (this){
                        timeout.cancel();
                        timeout.purge();
                        //TODO settare il player rimasto come vincitore
                        System.out.println("il player rimanente è il vincitore");
                    }

                }
            }, 30*1000); //timeout of 1 minuto
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 60; i++) {
                try {
                    Thread.sleep(1000); // wait one second for each iteration
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                synchronized (this){
                    if (gameModel.getNumPlayersConnected() == 1) {
                        timeout.cancel();
                        timeout.purge();
                        //TODO vedere cosa succede se uno si riconnette
                    }

                    if (gameModel.getNumPlayersConnected() > 1) {
                        timeout.cancel(); // it stops the timeout
                        timeout.purge();
                        System.out.println("si continua...");
                        break;
                    }
                }
            }
        }).start();
    }

    private void startTimeoutReconnection(Timer timeout, String nickname){
        new Thread(() -> {
            timeout.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (this){
                        disconnectPlayer(nickname);
                    }
                }
            }, 60*1000); //timeout of 1 minuto
        }).start();
    }

    public void notifyClientConnected(String nickname){
        // TODO
    }

    public void drawDeckCard(String nickname, CardType type) {
        if(!gameModel.getState().equals(GameState.PLAYING)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)){
            gameModel.setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            gameModel.setCommandResult(CommandResult.WRONG_CARD_TYPE);
            return;
        }
        if(!getHasCurrPlayerPlaced()) {
            gameModel.setCommandResult(CommandResult.NOT_PLACED_YET);
            return;
        }

        DrawableCard card;
        if(type.equals(CardType.RESOURCE_CARD)) {
            card = getResourceCardsDeck().drawCard();
            if(card == null) {
                gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);
        }
        if(type.equals(CardType.GOLD_CARD)) {
            card = getGoldCardsDeck().drawCard();
            if(card == null){
                gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);
        }
        changeCurrPlayer();
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    public void drawFaceUpCard(String nickname, CardType type, int pos) {
        if(!gameModel.getState().equals(GameState.PLAYING)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)){
            gameModel.setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            gameModel.setCommandResult(CommandResult.WRONG_CARD_TYPE);
            return;
        }
        if(!getHasCurrPlayerPlaced()) {
            gameModel.setCommandResult(CommandResult.NOT_PLACED_YET);
            return;
        }

        DrawableCard card;
        if(type.equals(CardType.RESOURCE_CARD)) {
            card = getResourceCardsDeck().drawFaceUpCard(pos);
            if(card == null) {
                gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);

            // check if the card has been replaced or replace
            if(getResourceCardsDeck().revealFaceUpCard(1) == null) {
                GoldCard newFaceUpCard = getGoldCardsDeck().drawCard();
                if(newFaceUpCard != null) {
                    getGoldCardsDeck().addFaceUpCard(newFaceUpCard);
                }
            }
        }
        if(type.equals(CardType.GOLD_CARD)) {
            card = getGoldCardsDeck().drawFaceUpCard(pos);
            if(card == null) {
                gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(gameModel.getCurrPlayer()).addCardHand(card);

            // check if the card has been replaced or replace
            if(getGoldCardsDeck().revealFaceUpCard(1) == null) {
                DrawableCard newFaceUpCard = getResourceCardsDeck().drawCard();
                if(newFaceUpCard != null) {
                    getResourceCardsDeck().addFaceUpCard(newFaceUpCard);
                }
            }
        }
        changeCurrPlayer();
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    public void placeCard(String nickname, int pos, int x, int y, boolean way) {
        Player player;
        DrawableCard card;
        if(!gameModel.getState().equals(GameState.PLAYING)){
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(gameModel.getCurrPlayer()).getNickname().equals(nickname)) {
            gameModel.setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(getHasCurrPlayerPlaced()) {
            gameModel.setCommandResult(CommandResult.CARD_ALREADY_PLACED);
            return;
        }
        try {
            player = getPlayers().get(getPlayerPosByNickname(nickname));
        } catch (PlayerNotPresentException e) {
            throw new RuntimeException(e);
        }
        if(pos < 0 || pos >= player.getCurrentHand().size()) {
            gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
            return;
        }
        card = player.getCurrentHand().get(pos);
        CommandResult result = player.placeCard(card,x,y,way);
        if(result.equals(CommandResult.SUCCESS)) {
            setHasCurrPlayerPlaced();
            getPlayers().get(gameModel.getCurrPlayer()).removeCardHand(card);
            addPoints(nickname, x, y);    // the card has just been placed

            // check if the player is stalled
            boolean isStalled = true;
            CommandResult resultStall;
            for(int i = 0; i < GameField.getDim() && isStalled; i++) {
                for (int j = 0; j < GameField.getDim() && isStalled; j++) {
                    // check if the firs card (a casual card), is placeable on the back,
                    // i.e. check only the indexes
                    try {
                        resultStall = getPlayers().get(getPlayerPosByNickname(nickname)).getCurrentHand().getFirst()
                                .isPlaceable(new GameField(player.getGameField()), i, j, true);
                        if (resultStall.equals(CommandResult.SUCCESS)) {
                            isStalled = false;
                        }
                    } catch (PlayerNotPresentException e) {
                        // the current player must be present
                        throw new RuntimeException();
                    }
                }
            }
            if(isStalled) {
                getPlayers().get(gameModel.getCurrPlayer()).setIsStalled(isStalled);
            }
        }
        gameModel.setCommandResult(result);
    }

    public void placeStarterCard(String nickname, boolean way) {
        // check right state
        if(!gameModel.getState().equals(GameState.PLACING_STARTER_CARDS)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayerNicknames().contains(nickname)) {
            gameModel.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        // check player has not already placed the starter card
        Player player;
        try {
            player = getPlayers().get(getPlayerPosByNickname(nickname));
        } catch (PlayerNotPresentException e) {
            // already checked
            throw new RuntimeException(e);
        }
        if(player.getGameField().isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2)) {
            gameModel.setCommandResult(CommandResult.CARD_ALREADY_PRESENT);
            return;
        }
        // no check for current player, starter cards can be placed in any order

        gameModel.setCommandResult(player.placeCard(
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
            gameModel.setState(GameState.PLAYING);
        }
    }

    // TODO synchronized chi lo chiama?
    public void reconnectPlayer(String nickname) {
        // this command can always be used
        if(!getPlayerNicknames().contains(nickname)) {
            gameModel.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            int pos = getPlayerPosByNickname(nickname);
            if(getPlayers().get(pos).isConnected()) {
                gameModel.setCommandResult(CommandResult.PLAYER_ALREADY_CONNECTED);
                return;
            }
            getPlayers().get(pos).setIsConnected(true);
            int numPlayersConnected = 0;
            for (Player p : getPlayers()){
                if (p.isConnected()){
                    numPlayersConnected++;
                }
            }
            if (numPlayersConnected == 1) {
                gameModel.setState(GameState.WAITING_RECONNECTION);
                // TODO start the timer, when it ends, the only player connected wins
            }
            else if (numPlayersConnected > 1) {
                // players can re-start to play
                gameModel.setState(GameState.PLAYING);
            }
        } catch (PlayerNotPresentException e) {
            gameModel.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
         gameModel.setCommandResult(CommandResult.SUCCESS);
    }


    // ----------------------
    // utils
    // ----------------------

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

    /**
     * Method that returns the position of the player in the List players.
     * @param nickname: nickname of the player whose position is being searched
     * @return position of the player in the List players
     * @throws PlayerNotPresentException: thrown if the nickname is not present in the list players.
     */
    private int getPlayerPosByNickname(String nickname) throws PlayerNotPresentException {
        for (int i = 0; i < gameModel.getPlayersNumber(); i++){
            if(getPlayers().get(i).getNickname().equals(nickname)){
                return i;
            }
        }
        throw new PlayerNotPresentException();
    }

    /**
     * Method that change the current player, if it's the last turn and all the players
     * played the same amount of turn it computes the winner;
     * if a player is disconnect from the game he loose the turn,
     * if a player is stalled he will be skipped.
     */
    // TODO spostare nel model? l'ho lasciato qua per timer
     void changeCurrPlayer () {
        assert(gameModel.getState().equals(GameState.PLAYING)): "Method changeCurrentPlayer called in a wrong state";
        if(gameModel.getCurrPlayer() == getPlayers().size()-1)
            gameModel.setCurrPlayer(0);
        else
            gameModel.setCurrPlayer(gameModel.getCurrPlayer()+1);
        setHasNotCurrPlayerPlaced();
        if(gameModel.getTwentyPointsReached()) {
            if(getPlayers().get(gameModel.getCurrPlayer()).isFirst() && gameModel.getAdditionalRound()) {
                gameModel.setState(GameState.GAME_ENDED);
                getWinners().addAll(computeWinner());
                // the game is ended

                //TODO faccio partire un timer di qualche minuto
                // per permettere ai giocatori di scrivere in chat

                // when the timer is ended
                // GamesManager.getGamesManager().deleteGame(this.id);
                // return;
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
                if(!p.getIsStalled())
                    found = true;
            }
            if(found)
                changeCurrPlayer();
            else {
                gameModel.setState(GameState.GAME_ENDED);
                getWinners().addAll(computeWinner());
            }
        }
    }

    /**
     * Method that compute the winner/s of the game.
     * @return the list of players who won the game
     */
    private List<String> computeWinner() {
        assert(gameModel.getState().equals(GameState.GAME_ENDED)) : "The game state is not correct";
        return gameModel.computeWinner();
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
        setHasNotCurrPlayerPlaced();

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
        Player player;
        try {
            player = getPlayers().get(getPlayerPosByNickname(nickname));
        } catch (PlayerNotPresentException e) {
            // the curr player must be in the game
            throw new RuntimeException(e);
        }
        assert(player.getGameField().isCardPresent(x, y)) : "No card present in the provided position";

        gameModel.addPoints(player, x, y);
    }
}