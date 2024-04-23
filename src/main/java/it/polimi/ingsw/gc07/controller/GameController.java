package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.GameCommand;
import it.polimi.ingsw.gc07.model.*;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.model.chat.Chat;
import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

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

    public Map<String, GameField> getPlayersGameField() {
        return gameModel.getPlayersGameField();
    }

    public List<Player> getPlayers() {
        return gameModel.getPlayers();
    }

    List<Player> getWinners(){
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

    synchronized void setTwentyPointsReached() {
        gameModel.setTwentyPointsReached(true);
    }

    synchronized void setCurrentPlayer(int num) {
        gameModel.setCurrPlayer(num);
    }

    synchronized Chat getChat() {
        return gameModel.getChat();
    }

    public CommandResult getCommandResult() {
        return gameModel.getCommandResult();
    }

    public synchronized void setAndExecuteCommand(GameCommand gameCommand) {
        gameCommand.execute(this);
    }

    // ----------------------
    // command pattern methods
    // ----------------------

    public void addChatPrivateMessage(String content, String sender, String receiver) {
        // no state check, this command be used all the time
        List<String> playersNicknames = getPlayers().stream().map(Player::getNickname).toList();
        // check valid sender
        if(!playersNicknames.contains(sender)) {
            gameModel.setCommandResult(CommandResult.WRONG_SENDER);
            return;
        }
        // check valid receiver
        if(!playersNicknames.contains(receiver)) {
            gameModel.setCommandResult(CommandResult.WRONG_RECEIVER);
            return;
        }
        if(sender.equals(receiver)) {
            gameModel.setCommandResult(CommandResult.WRONG_RECEIVER);
            return;
        }
        // adds message to the chat
        getChat().addPrivateMessage(content, sender, receiver, playersNicknames);
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    public void addChatPublicMessage(String content, String sender) {
        // no state check, this command be used all the time
        List<String> playersNicknames = getPlayers().stream().map(Player::getNickname).toList();
        // check valid sender
        if(!playersNicknames.contains(sender)){
            gameModel.setCommandResult(CommandResult.WRONG_SENDER);
            return;
        }
        // add message to chat
        getChat().addPublicMessage(content, sender, playersNicknames);
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    // TODO synchronized chi lo chiama?
    public void addPlayer(Player newPlayer) {
        if(!getState().equals(GameState.GAME_STARTING)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(getPlayersGameField().containsKey(newPlayer.getNickname())) {
            gameModel.setCommandResult(CommandResult.PLAYER_ALREADY_PRESENT);
            return;
        }
        Timer timeout = new Timer();
        playersTimer.put(newPlayer.getNickname(), timeout);
        startTimeoutReconnection(timeout, newPlayer.getNickname());
        // draw card can't return null, since the game hasn't already started
        newPlayer.addCardHand(getResourceCardsDeck().drawCard());
        newPlayer.addCardHand(getResourceCardsDeck().drawCard());
        newPlayer.addCardHand(getGoldCardsDeck().drawCard());
        newPlayer.setSecretObjective(getObjectiveCardsDeck().drawCard());

        PlaceableCard starterCard = getStarterCardsDeck().drawCard();
        GameField gameField = new GameField(starterCard);
        getPlayers().add(newPlayer);
        getPlayersGameField().put(newPlayer.getNickname(), gameField);
        getScoreTrackBoard().addPlayer(newPlayer.getNickname());
        if (isFull()) {
            setup();
            setState(GameState.PLACING_STARTER_CARDS);
        }
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    public void disconnectPlayer(String nickname) {
        // this command can always be used
        if(!getPlayersGameField().containsKey(nickname)){
            gameModel.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            playersTimer.get(nickname).cancel();
            playersTimer.get(nickname).purge();
            int pos = getPlayerByNickname(nickname);
            if(!getPlayers().get(pos).isConnected())
            {
                gameModel.setCommandResult(CommandResult.PLAYER_ALREADY_DISCONNECTED);
                return;
            }
            getPlayers().get(pos).setIsConnected(false);
            int numPlayersConnected = getNumPlayersConnected();
            if (numPlayersConnected == 1){
                setState(GameState.WAITING_RECONNECTION);
                // TODO start the timer, when it ends, the only player left wins
                startTimeoutGameEnd();
            }
            else if (numPlayersConnected == 0) {
                setState(GameState.NO_PLAYERS_CONNECTED);
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
                    if (getNumPlayersConnected() == 1) {
                        timeout.cancel();
                        timeout.purge();
                        //TODO vedere cosa succede se uno si riconnette
                    }

                    if (getNumPlayersConnected() > 1) {
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

    }
    public void drawDeckCard(String nickname, CardType type) {
        if(!getState().equals(GameState.PLAYING)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(getCurrPlayer()).getNickname().equals(nickname)){
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
        if (type.equals(CardType.RESOURCE_CARD)) {
            card = getResourceCardsDeck().drawCard();
            if(card == null) {
                gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(getCurrPlayer()).addCardHand(card);
        }
        if (type.equals(CardType.GOLD_CARD)) {
            card = getGoldCardsDeck().drawCard();
            if(card == null){
                gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            getPlayers().get(getCurrPlayer()).addCardHand(card);
        }
        changeCurrPlayer();
        gameModel.setCommandResult(CommandResult.SUCCESS);
    }

    public void drawFaceUpCard(String nickname, CardType type, int pos) {
        if(!getState().equals(GameState.PLAYING)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(getCurrPlayer()).getNickname().equals(nickname)){
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
            getPlayers().get(getCurrPlayer()).addCardHand(card);

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
            getPlayers().get(getCurrPlayer()).addCardHand(card);

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
        Player player = null;
        DrawableCard card = null;
        if(!getState().equals(GameState.PLAYING)){
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!getPlayers().get(getCurrPlayer()).getNickname().equals(nickname)) {
            gameModel.setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(getHasCurrPlayerPlaced()) {
            gameModel.setCommandResult(CommandResult.CARD_ALREADY_PLACED);
            return;
        }
        try {
            player = getPlayers().get(getPlayerByNickname(nickname));
        } catch (PlayerNotPresentException e) {
            throw new RuntimeException(e);
        }
        if(pos < 0 || pos >= player.getCurrentHand().size()) {
            gameModel.setCommandResult(CommandResult.CARD_NOT_PRESENT);
            return;
        }
        card = player.getCurrentHand().get(pos);
        CommandResult result = getPlayersGameField().get(nickname).placeCard(card,x,y,way);
        if(result.equals(CommandResult.SUCCESS)) {
            setHasCurrPlayerPlaced();
            getPlayers().get(getCurrPlayer()).removeCardHand(card);
            addPoints(nickname, x, y);    // the card has just been placed

            // check if the player is stalled
            boolean isStalled = true;
            CommandResult resultStall;
            for(int i = 0; i < GameField.getDim() && isStalled; i++) {
                for (int j = 0; j < GameField.getDim() && isStalled; j++) {
                    // check if the firs card (a casual card), is placeable on the back,
                    // i.e. check only the indexes
                    try {
                        resultStall = getPlayers().get(getPlayerByNickname(nickname)).getCurrentHand().getFirst()
                                .isPlaceable(new GameField(getPlayersGameField().get(nickname)), i, j, true);
                        if (resultStall.equals(CommandResult.SUCCESS)) {
                            isStalled = false;
                        }
                    } catch (PlayerNotPresentException e) {
                        // the current player must be present
                        throw new RuntimeException();
                    }
                }
            }
            getPlayers().get(getCurrPlayer()).setIsStalled(isStalled);
        }
        gameModel.setCommandResult(result);
    }

    public void placeStarterCard(String nickname, boolean way) {
        // check right state
        if(!getState().equals(GameState.PLACING_STARTER_CARDS)) {
            gameModel.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        // check player has not already placed the starter card
        if(getPlayersGameField().get(nickname).isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2)) {
            gameModel.setCommandResult(CommandResult.CARD_ALREADY_PRESENT);
            return;
        }
        // no check for current player, starter cards can be placed in any order

        assert(getPlayersGameField().containsKey(nickname)): "The player is not in the game";
        gameModel.setCommandResult(getPlayersGameField().get(nickname).placeCard(
                getPlayersGameField().get(nickname).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, way)
        );

        boolean changeState = true;
        for(String p: getPlayersGameField().keySet()) {
            if(!getPlayersGameField().get(p).isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2)) {
                // someone has to place the starter card
                changeState = false;
            }
        }
        if(changeState) {
            setState(GameState.PLAYING);
        }
    }

    // TODO synchronized chi lo chiama?
    public void reconnectPlayer(String nickname) {
        // this command can always be used
        if(!getPlayersGameField().containsKey(nickname)){
            gameModel.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            int pos = getPlayerByNickname(nickname);
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
                setState(GameState.WAITING_RECONNECTION);
                // TODO start the timer, when it ends, the only player connected wins
            }
            else if (numPlayersConnected > 1) {
                // players can re-start to play
                setState(GameState.PLAYING);
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

    private int getNumPlayersConnected() {
        int numPlayersConnected = 0;
        for (Player p: getPlayers()){
            if (p.isConnected()){
                numPlayersConnected++;
            }
        }
        return numPlayersConnected;
    }

    /**
     * Method telling if a player is in a game.
     * @param nickname nickname of the player
     * @return true if the player is in the game
     */
    synchronized boolean hasPlayer(String nickname) {
        boolean found = false;
        for(Player p: getPlayers()){
            if(p.getNickname().equals(nickname)){
                found = true;
            }
        }
        return found;
    }

    /**
     * Method telling if a player with the given token color is in the game.
     * @param tokenColor token color to search
     * @return true if there is a player with the given token color
     */
    synchronized boolean hasPlayerWithTokenColor(TokenColor tokenColor) {
        boolean found = false;
        for(Player p: getPlayers()){
            if(p.getTokenColor().equals(tokenColor)){
                found = true;
            }
        }
        return found;
    }

    /**
     * Method that returns the position of the player in the List players.
     * @param nickname: nickname of the player whose position is being searched
     * @return position of the player in the List players
     * @throws PlayerNotPresentException: thrown if the nickname is not present in the list players.
     */
    private int getPlayerByNickname(String nickname) throws PlayerNotPresentException {
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
     void changeCurrPlayer () {
        assert(getState().equals(GameState.PLAYING)): "Method changeCurrentPlayer called in a wrong state";
        if(getCurrPlayer() == getPlayers().size()-1)
            setCurrentPlayer(0);
        else
            setCurrentPlayer(getCurrPlayer()+1);
        setHasNotCurrPlayerPlaced();
        if(gameModel.getTwentyPointsReached()) {
            if(getPlayers().get(getCurrPlayer()).isFirst() && gameModel.getAdditionalRound()) {
                setState(GameState.GAME_ENDED);
                getWinners().addAll(computeWinner());
                // the game is ended

                //TODO faccio partire un timer di qualche minuto
                // per permettere ai giocatori di scrivere in chat

                // when the timer is ended
                //GamesManager.getGamesManager().deleteGame(this.id);
                //return;
            }
            else if(getPlayers().get(getCurrPlayer()).isFirst()) {
                gameModel.setAdditionalRound(true);
            }
        }
        if(!getPlayers().get(getCurrPlayer()).isConnected()) {
            changeCurrPlayer();
        }
        if(getPlayers().get(getCurrPlayer()).getIsStalled()) {
            boolean found = false;
            for(Player p: getPlayers()) {
                if(!p.getIsStalled())
                    found = true;
            }
            if(found)
                changeCurrPlayer();
            else {
                setState(GameState.GAME_ENDED);
                getWinners().addAll(computeWinner());
            }
        }
    }

    /**
     * Method that compute the winner/s of the game.
     * @return the list of players who won the game
     */
    private List<Player> computeWinner() {
        assert(getState().equals(GameState.GAME_ENDED)) : "The game state is not correct";
        List<Player> winners = new ArrayList<>();
        int deltaPoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;
        List<Player> playersCopy = new ArrayList<>(getPlayers());
        for (int i=0; i>=0 && i< getPlayers().size(); i++){
            ObjectiveCard objectiveCard;
            objectiveCard = getObjectiveCardsDeck().revealFaceUpCard(0);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives = objectiveCard.numTimesScoringConditionMet(getPlayersGameField().get(getPlayers().get(i).getNickname()));
            //points counter for the 1st common objective
            deltaPoints = objectiveCard.getObjectiveScore(getPlayersGameField().get(getPlayers().get(i).getNickname()));

            objectiveCard = getObjectiveCardsDeck().revealFaceUpCard(1);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives += objectiveCard.numTimesScoringConditionMet(getPlayersGameField().get(getPlayers().get(i).getNickname()));
            //points counter for the 2nd common objective
            deltaPoints += objectiveCard.getObjectiveScore(getPlayersGameField().get(getPlayers().get(i).getNickname()));

            realizedObjectives += getPlayers().get(i).getSecretObjective().numTimesScoringConditionMet(getPlayersGameField().get(getPlayers().get(i).getNickname()));
            //points counter for the secret objective
            deltaPoints += getPlayers().get(i).getSecretObjective().getObjectiveScore(getPlayersGameField().get(getPlayers().get(i).getNickname()));
            getScoreTrackBoard().incrementScore(getPlayers().get(i).getNickname(), deltaPoints);
            if (max <= getScoreTrackBoard().getScore(playersCopy.get(i).getNickname())) {
                max = getScoreTrackBoard().getScore(playersCopy.get(i).getNickname());
                if (realizedObjectives >= maxRealizedObjective) {
                    if (realizedObjectives == maxRealizedObjective) {
                        winners.add(playersCopy.get(i));
                    } else {
                        winners.clear();
                        winners.add(playersCopy.get(i));
                        maxRealizedObjective = realizedObjectives;
                    }
                }
            }
        }
        return winners;
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
        assert(getState().equals(GameState.GAME_STARTING)): "The state is not WAITING_PLAYERS";
        // choose randomly the first player
        Random random= new Random();
        setCurrentPlayer(random.nextInt(gameModel.getPlayersNumber()));
        getPlayers().get(getCurrPlayer()).setFirst();
        setHasNotCurrPlayerPlaced();

        // draw card can't return null, since the game hasn't already started

        //place 2 gold cards
        List<GoldCard> setUpGoldCardsFaceUp = new ArrayList<>();
        setUpGoldCardsFaceUp.add(getGoldCardsDeck().drawCard());
        setUpGoldCardsFaceUp.add(getGoldCardsDeck().drawCard());
        getGoldCardsDeck().setFaceUpCards(setUpGoldCardsFaceUp);

        //place 2 resource card
        List<DrawableCard> setUpResourceCardsFaceUp = new ArrayList<>();
        setUpResourceCardsFaceUp.add(getResourceCardsDeck().drawCard());
        setUpResourceCardsFaceUp.add(getResourceCardsDeck().drawCard());
        getResourceCardsDeck().setFaceUpCards(setUpResourceCardsFaceUp);

        // place common objective cards
        List<ObjectiveCard> setUpObjectiveCardsFaceUp = new ArrayList<>();
        setUpObjectiveCardsFaceUp.add(getObjectiveCardsDeck().drawCard());
        setUpObjectiveCardsFaceUp.add(getObjectiveCardsDeck().drawCard());
        getObjectiveCardsDeck().setFaceUpCards(setUpObjectiveCardsFaceUp);
    }

    /**
     * Method that adds points to a player and checks if a player had reached 20 points.
     * @param nickname nickname of the player
     * @param x where the card is placed in the matrix
     * @param y where the card is placed in the matrix
     */
    private void addPoints(String nickname, int x, int y) {
        assert(getState().equals(GameState.PLAYING)): "Wrong game state";
        assert(getPlayers().get(getCurrPlayer()).getNickname().equals(nickname)): "Not the current player";
        assert (getPlayersGameField().get(nickname).isCardPresent(x, y)) : "No card present in the provided position";
        int deltaPoints;
        deltaPoints = getPlayersGameField().get(nickname).getPlacedCard(x, y).getPlacementScore(getPlayersGameField().get(nickname), x, y);
        if(deltaPoints + getScoreTrackBoard().getScore(nickname) >= 20){
            setTwentyPointsReached();
            if((deltaPoints + getScoreTrackBoard().getScore(nickname)) > 29){
                getScoreTrackBoard().setScore(nickname, 29);
            }
            else{
                getScoreTrackBoard().incrementScore(nickname, deltaPoints);
            }
        }
        else
        {
            getScoreTrackBoard().incrementScore(nickname, deltaPoints);
        }
    }







    // TODO da discutere


    // ----------------------------
    // Metodi che probabilmente non vengono invocati direttamente dal client,
    // ma servono per aggiornare la view
    // ----------------------------

    /**
     * method that reveal the face up card in position pos
     * @param type type of the card
     * @param pos position of the card
     * @return the card that we want to reveal
     * @throws WrongCardTypeException if the card is a starter card
     */
    public Card revealFaceUpCard(CardType type, int pos) throws WrongCardTypeException {       // TODO rimuovere wrongcardtype exception
        if(type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        Card faceUpCard;
        if(type.equals(CardType.GOLD_CARD))
            faceUpCard = this.getGoldCardsDeck().revealFaceUpCard(pos);
        else if(type.equals(CardType.RESOURCE_CARD))
            faceUpCard = this.getResourceCardsDeck().revealFaceUpCard(pos);
        else
            faceUpCard = this.getObjectiveCardsDeck().revealFaceUpCard(pos);
        if(faceUpCard != null) {
            return faceUpCard;
        }
        else {
            return null;
            // TODO
        }
    }

    /**
     * method that reveal the back of the card on top of the deck
     * @param type : type of the card
     * @return the GameResource that represent the back of the card
     * @throws WrongCardTypeException   if we reveal the back of a starter card or the back of an objective card
     */
    public GameResource revealBackDeckCard(CardType type) throws WrongCardTypeException {       // TODO togliere eccezione
        if(type.equals(CardType.STARTER_CARD) || type.equals(CardType.OBJECTIVE_CARD))
        {
            throw new WrongCardTypeException();
        }
        GameResource backResource;
        if(type.equals(CardType.GOLD_CARD)){
            backResource = this.getGoldCardsDeck().revealBackDeckCard();
        }else{
            backResource = this.getResourceCardsDeck().revealBackDeckCard();
        }
        if(backResource != null)
            return backResource;
        else {
            return null;    // TODO cosa ritorno ?
        }
    }

    /**
     * Returns the last message of the chat for a certain player.
     * @return the last message of the chat
     */
    public ChatMessage getLastChatMessage(String receiver)  {
        ChatMessage chatMessage = getChat().getLastMessage(receiver);
        if(chatMessage == null) {
            // TODO no return null!
            return null;
        }
        else
            return chatMessage;
    }

    /**
     * Returns the content of the chat for a certain player.
     * @return the list of the message in the chat
     */
    public List<ChatMessage> getChatContent(String receiver) {
        return getChat().getContent(receiver);
    }
}