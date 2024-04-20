package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.listeners.GameListener;
import it.polimi.ingsw.gc07.listeners.ListenersHandler;
import it.polimi.ingsw.gc07.model.CommandResultManager;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.ScoreTrackBoard;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.model.chat.Chat;
import it.polimi.ingsw.gc07.model.chat.Message;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.*;

public class Game {
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
     * Map of players and their game field.
     */
    private final Map<String, GameField> playersGameField;
    /**
     * List of players.
     */
    private final List<Player> players;
    /**
     * List of winner/s of the game.
     */
    private final List<Player> winners;
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
     * Command result manager of the game.
     */
    private final CommandResultManager commandResultManager;

    private final ListenersHandler listenersHandler;

    /** Constructor of a Game with only the first player.
     *
     * @param playersNumber number of players
     * @param resourceCardsDeck deck of resource cards
     * @param goldCardsDeck deck of gold cards
     * @param objectiveCardsDeck deck of objective cards
     * @param starterCardsDeck deck of starter cards
     */
    public Game(int id, int playersNumber, ResourceCardsDeck resourceCardsDeck,
                GoldCardsDeck goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck,
                Deck<PlaceableCard> starterCardsDeck) {
        this.id = id;
        this.state = GameState.GAME_STARTING;
        assert(playersNumber >= 2 && playersNumber <= 4): "Wrong players number";
        this.playersNumber = playersNumber;
        this.players = new ArrayList<>();
        this.winners = new ArrayList<>();
        this.playersGameField = new HashMap<>();
        this.scoreTrackBoard = new ScoreTrackBoard();
        this.resourceCardsDeck = new ResourceCardsDeck(resourceCardsDeck);
        this.goldCardsDeck = new GoldCardsDeck(goldCardsDeck);
        this.objectiveCardsDeck = new PlayingDeck<>(objectiveCardsDeck);
        this.starterCardsDeck = new Deck<>(starterCardsDeck);
        this.currPlayer = 0;
        this.hasCurrPlayerPlaced = false;
        this.twentyPointsReached = false;
        this.additionalRound = false;
        this.chat = new Chat();
        this.commandResultManager = new CommandResultManager();
        this.listenersHandler = new ListenersHandler();
    }

    public void addListener(GameListener client) {
        listenersHandler.addListener(client);
    }

    // ------------------------------
    // setters and getters
    // ------------------------------

    /**
     * Getter for the game id.
     * @return game id
     */
    int getId() {
        return id;
    }

    /**
     * Setter for the state of the game.
     */
    synchronized void setState(GameState state) {
        this.state = state;
    }

    /**
     * Getter for the state of the game.
     * @return the state of the game
     */
    synchronized GameState getState() {
        return state;
    }

    Map<String, GameField> getPlayersGameField() {
        return playersGameField;
    }

    List<Player> getPlayers() {
        return players;
    }

    List<Player> getWinners(){
        return winners;
    }

    synchronized int getCurrPlayer() {
        return currPlayer;
    }

    synchronized void setHasCurrPlayerPlaced() {
        this.hasCurrPlayerPlaced = true;
    }

    synchronized void setHasNotCurrPlayerPlaced() {
        this.hasCurrPlayerPlaced = false;
    }

    ScoreTrackBoard getScoreTrackBoard() {
        return scoreTrackBoard;
    }

    ResourceCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    GoldCardsDeck getGoldCardsDeck() {
        return goldCardsDeck;
    }

    PlayingDeck<ObjectiveCard> getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    Deck<PlaceableCard> getStarterCardsDeck() {
        return starterCardsDeck;
    }

    synchronized void setTwentyPointsReached() {
        this.twentyPointsReached = true;
    }

    synchronized void setCurrentPlayer(int num) {
        this.currPlayer = num;
    }

    public CommandResultManager getCommandResultManager() {
        return commandResultManager;
    }

    public synchronized void setAndExecuteCommand(GameCommand gameCommand) {
        gameCommand.execute(this);
    }

    // ----------------------
    // command pattern methods
    // ----------------------

    void addChatPrivateMessage(String content, String sender, String receiver) {
        // no state check, this command be used all the time
        List<String> playersNicknames = players.stream().map(Player::getNickname).toList();
        // check valid sender
        if(!playersNicknames.contains(sender)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_SENDER);
            return;
        }
        // check valid receiver
        if(!playersNicknames.contains(receiver)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_RECEIVER);
            return;
        }
        if(sender.equals(receiver)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_RECEIVER);
            return;
        }
        // adds message to the chat
        chat.addPrivateMessage(content, sender, receiver, playersNicknames);
        commandResultManager.setCommandResult(CommandResult.SUCCESS);
    }

    void addChatPublicMessage(String content, String sender) {
        // no state check, this command be used all the time
        List<String> playersNicknames = players.stream().map(Player::getNickname).toList();
        // check valid sender
        if(!playersNicknames.contains(sender)){
            commandResultManager.setCommandResult(CommandResult.WRONG_SENDER);
            return;
        }
        // add message to chat
        chat.addPublicMessage(content, sender, playersNicknames);
        commandResultManager.setCommandResult(CommandResult.SUCCESS);
    }

    void addPlayer(Player newPlayer) {
        if(!state.equals(GameState.GAME_STARTING)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }

        // draw card can't return null, since the game hasn't already started
        newPlayer.addCardHand(resourceCardsDeck.drawCard());
        newPlayer.addCardHand(resourceCardsDeck.drawCard());
        newPlayer.addCardHand(goldCardsDeck.drawCard());
        newPlayer.setSecretObjective(objectiveCardsDeck.drawCard());

        PlaceableCard starterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(starterCard);
        players.add(newPlayer);
        playersGameField.put(newPlayer.getNickname(), gameField);
        scoreTrackBoard.addPlayer(newPlayer.getNickname());
        if (isFull()) {
            setup();
            state = GameState.PLACING_STARTER_CARDS;
        }
        commandResultManager.setCommandResult(CommandResult.SUCCESS);
    }

    void disconnectPlayer(String nickname) {
        // this command can always be used
        if(!playersGameField.containsKey(nickname)){
            commandResultManager.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            int pos = getPlayerByNickname(nickname);
            if(!players.get(pos).isConnected())
            {
                commandResultManager.setCommandResult(CommandResult.PLAYER_ALREADY_DISCONNECTED);
                return;
            }
            players.get(pos).setIsConnected(false);
            int numPlayersConnected = getNumPlayersConnected();
            if (numPlayersConnected == 1){
                state = GameState.WAITING_RECONNECTION;
                /*
                // TODO start the timer, when it ends, the only player left wins
                reconnectionOccurred = false;
                Timer timeout = new Timer();
                timeout.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!reconnectionOccurred) {
                            timeout.cancel();
                            timeout.purge();
                            //TODO settare il player rimasto come vincitore
                        }
                    }
                }, 60*1000); //timeout of 1 minute
                new Thread(() -> {
                    for (int i = 0; i < 60; i++) {
                        try {
                            Thread.sleep(1000); // wait one second for each iteration
                        } catch (InterruptedException e) {
                            throw new RuntimeException();
                        }
                        if (game.getNumPlayersConnected() > 1) {
                            reconnectionOccurred = true;
                            timeout.cancel(); // it stops the timeout
                            timeout.purge();
                            break;
                        }
                    }
                }).start();
             */
            } else if (numPlayersConnected == 0) {
                state = GameState.NO_PLAYERS_CONNECTED;
                // TODO start the timer, when it ends, the game ends without winner
            }
        }
        catch(PlayerNotPresentException e){
            commandResultManager.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        commandResultManager.setCommandResult(CommandResult.SUCCESS);
    }

    void drawDeckCard(String nickname, CardType type) {
        if(!state.equals(GameState.PLAYING)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!players.get(currPlayer).getNickname().equals(nickname)){
            commandResultManager.setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_CARD_TYPE);
            return;
        }
        if(!hasCurrPlayerPlaced) {
            commandResultManager.setCommandResult(CommandResult.NOT_PLACED_YET);
            return;
        }

        DrawableCard card;
        if (type.equals(CardType.RESOURCE_CARD)) {
            card = resourceCardsDeck.drawCard();
            if(card == null) {
                commandResultManager.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            players.get(currPlayer).addCardHand(card);
        }
        if (type.equals(CardType.GOLD_CARD)) {
            card = goldCardsDeck.drawCard();
            if(card == null){
                commandResultManager.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            players.get(currPlayer).addCardHand(card);
        }
        changeCurrPlayer();
        commandResultManager.setCommandResult(CommandResult.SUCCESS);
    }

    void drawFaceUpCard(String nickname, CardType type, int pos) {
        if(!state.equals(GameState.PLAYING)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!players.get(currPlayer).getNickname().equals(nickname)){
            commandResultManager.setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_CARD_TYPE);
            return;
        }
        if(!hasCurrPlayerPlaced) {
            commandResultManager.setCommandResult(CommandResult.NOT_PLACED_YET);
            return;
        }

        DrawableCard card;
        if(type.equals(CardType.RESOURCE_CARD)) {
            card = resourceCardsDeck.drawFaceUpCard(pos);
            if(card == null) {
                commandResultManager.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            players.get(currPlayer).addCardHand(card);

            // check if the card has been replaced or replace
            if(resourceCardsDeck.revealFaceUpCard(1) == null) {
                GoldCard newFaceUpCard = goldCardsDeck.drawCard();
                if(newFaceUpCard != null) {
                    goldCardsDeck.addFaceUpCard(newFaceUpCard);
                }
            }
        }
        if(type.equals(CardType.GOLD_CARD)) {
            card = goldCardsDeck.drawFaceUpCard(pos);
            if(card == null) {
                commandResultManager.setCommandResult(CommandResult.CARD_NOT_PRESENT);
                return;
            }
            players.get(currPlayer).addCardHand(card);

            // check if the card has been replaced or replace
            if(goldCardsDeck.revealFaceUpCard(1) == null) {
                DrawableCard newFaceUpCard = resourceCardsDeck.drawCard();
                if(newFaceUpCard != null) {
                    resourceCardsDeck.addFaceUpCard(newFaceUpCard);
                }
            }
        }
        changeCurrPlayer();
        commandResultManager.setCommandResult(CommandResult.SUCCESS);
    }

    void placeCard(String nickname, int pos, int x, int y, boolean way) {
        Player player = null;
        DrawableCard card = null;
        if(!state.equals(GameState.PLAYING)){
            commandResultManager.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        if(!players.get(currPlayer).getNickname().equals(nickname)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_PLAYER);
            return;
        }
        if(hasCurrPlayerPlaced) {
            commandResultManager.setCommandResult(CommandResult.CARD_ALREADY_PLACED);
            return;
        }
        try {
            player = players.get(getPlayerByNickname(nickname));
        } catch (PlayerNotPresentException e) {
            throw new RuntimeException(e);
        }
        if(pos < 0 || pos >= player.getCurrentHand().size()) {
            commandResultManager.setCommandResult(CommandResult.CARD_NOT_PRESENT);
            return;
        }
        card = player.getCurrentHand().get(pos);
        CommandResult result = playersGameField.get(nickname).placeCard(card,x,y,way);
        if(result.equals(CommandResult.SUCCESS)) {
            hasCurrPlayerPlaced = true;
            players.get(currPlayer).removeCardHand(card);
            addPoints(nickname, x, y);    // the card has just been placed

            // check if the player is stalled
            boolean isStalled = true;
            CommandResult resultStall;
            for(int i = 0; i < GameField.getDim() && isStalled; i++) {
                for (int j = 0; j < GameField.getDim() && isStalled; j++) {
                    // check if the firs card (a casual card), is placeable on the back,
                    // i.e. check only the indexes
                    try {
                        resultStall = players.get(getPlayerByNickname(nickname)).getCurrentHand().getFirst()
                                .isPlaceable(new GameField(playersGameField.get(nickname)), i, j, true);
                        if (resultStall.equals(CommandResult.SUCCESS)) {
                            isStalled = false;
                        }
                    } catch (PlayerNotPresentException e) {
                        // the current player must be present
                        throw new RuntimeException();
                    }
                }
            }
            players.get(currPlayer).setIsStalled(isStalled);
        }
        commandResultManager.setCommandResult(result);
    }

    void placeStarterCard(String nickname, boolean way) {
        // check right state
        if(!state.equals(GameState.PLACING_STARTER_CARDS)) {
            commandResultManager.setCommandResult(CommandResult.WRONG_STATE);
            return;
        }
        // check player has not already placed the starter card
        if(playersGameField.get(nickname).isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2)) {
            commandResultManager.setCommandResult(CommandResult.CARD_ALREADY_PRESENT);
            return;
        }
        // no check for current player, starter cards can be placed in any order

        assert(playersGameField.containsKey(nickname)): "The player is not in the game";
        commandResultManager.setCommandResult(playersGameField.get(nickname).placeCard(
                playersGameField.get(nickname).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, way)
        );

        boolean changeState = true;
        for(String p: playersGameField.keySet()) {
            if(!playersGameField.get(p).isCardPresent((GameField.getDim()-1)/2, (GameField.getDim()-1)/2)) {
                // someone has to place the starter card
                changeState = false;
            }
        }
        if(changeState) {
            state = GameState.PLAYING;
        }
    }

    void reconnectPlayer(String nickname) {
        // this command can always be used
        if(!playersGameField.containsKey(nickname)){
            commandResultManager.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            int pos = getPlayerByNickname(nickname);
            if(players.get(pos).isConnected()) {
                commandResultManager.setCommandResult(CommandResult.PLAYER_ALREADY_CONNECTED);
                return;
            }
            players.get(pos).setIsConnected(true);
            int numPlayersConnected = 0;
            for (Player p : players){
                if (p.isConnected()){
                    numPlayersConnected++;
                }
            }
            if (numPlayersConnected == 1) {
                state = GameState.WAITING_RECONNECTION;
                // TODO start the timer, when it ends, the only player connected wins
            }
            else if (numPlayersConnected > 1) {
                // players can re-start to play
                state = GameState.PLAYING;
            }
        } catch (PlayerNotPresentException e) {
            commandResultManager.setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        commandResultManager.setCommandResult(CommandResult.SUCCESS);
    }


    // ----------------------
    // utils
    // ----------------------

    private int getNumPlayersConnected() {
        int numPlayersConnected = 0;
        for (Player p: players){
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
        for(Player p: players){
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
        for(Player p: players){
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
        for (int i = 0; i < playersNumber; i++){
            if(players.get(i).getNickname().equals(nickname)){
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
        assert(state.equals(GameState.PLAYING)): "Method changeCurrentPlayer called in a wrong state";
        if(currPlayer == players.size()-1)
            currPlayer = 0;
        else
            currPlayer++;
        hasCurrPlayerPlaced = false;
        if(twentyPointsReached) {
            if(players.get(currPlayer).isFirst() && additionalRound) {
                state = GameState.GAME_ENDED;
                winners.addAll(computeWinner());
                // the game is ended

                //TODO faccio partire un timer di qualche minuto
                // per permettere ai giocatori di scrivere in chat

                // when the timer is ended
                //GamesManager.getGamesManager().deleteGame(this.id);
                //return;
            }
            else if(players.get(currPlayer).isFirst()) {
                additionalRound=true;
            }
        }
        if(!players.get(currPlayer).isConnected()) {
            changeCurrPlayer();
        }
        if(players.get(currPlayer).getIsStalled()) {
            boolean found = false;
            for(Player p: players) {
                if(!p.getIsStalled())
                    found = true;
            }
            if(found)
                changeCurrPlayer();
            else {
                this.state = GameState.GAME_ENDED;
                winners.addAll(computeWinner());
            }
        }
    }

    /**
     * Method that compute the winner/s of the game.
     * @return the list of players who won the game
     */
    private List<Player> computeWinner() {
        assert(state.equals(GameState.GAME_ENDED)) : "The game state is not correct";
        List<Player> winners = new ArrayList<>();
        int deltaPoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;
        List<Player> playersCopy = new ArrayList<>(players);
        for (int i=0; i>=0 && i< players.size(); i++){
            ObjectiveCard objectiveCard;
            objectiveCard = objectiveCardsDeck.revealFaceUpCard(0);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives = objectiveCard.numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 1st common objective
            deltaPoints = objectiveCard.getObjectiveScore(playersGameField.get(players.get(i).getNickname()));

            objectiveCard = objectiveCardsDeck.revealFaceUpCard(1);
            assert(objectiveCard != null): "The common objective must be present";
            realizedObjectives += objectiveCard.numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 2nd common objective
            deltaPoints += objectiveCard.getObjectiveScore(playersGameField.get(players.get(i).getNickname()));

            realizedObjectives += players.get(i).getSecretObjective().numTimesScoringConditionMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the secret objective
            deltaPoints += players.get(i).getSecretObjective().getObjectiveScore(playersGameField.get(players.get(i).getNickname()));
            scoreTrackBoard.incrementScore(players.get(i).getNickname(), deltaPoints);
            if (max <= scoreTrackBoard.getScore(playersCopy.get(i).getNickname())) {
                max = scoreTrackBoard.getScore(playersCopy.get(i).getNickname());
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
        return players.size() == playersNumber;
    }

    /**
     * Method to set up the game: the first player is chosen and 4 cards (2 gold and 2 resource) are revealed.
     */
    private void setup() {
        assert(state.equals(GameState.GAME_STARTING)): "The state is not WAITING_PLAYERS";
        // choose randomly the first player
        Random random= new Random();
        currPlayer = random.nextInt(playersNumber);
        players.get(currPlayer).setFirst();
        hasCurrPlayerPlaced = false;

        // draw card can't return null, since the game hasn't already started

        //place 2 gold cards
        List<GoldCard> setUpGoldCardsFaceUp = new ArrayList<>();
        setUpGoldCardsFaceUp.add(goldCardsDeck.drawCard());
        setUpGoldCardsFaceUp.add(goldCardsDeck.drawCard());
        goldCardsDeck.setFaceUpCards(setUpGoldCardsFaceUp);

        //place 2 resource card
        List<DrawableCard> setUpResourceCardsFaceUp = new ArrayList<>();
        setUpResourceCardsFaceUp.add(resourceCardsDeck.drawCard());
        setUpResourceCardsFaceUp.add(resourceCardsDeck.drawCard());
        resourceCardsDeck.setFaceUpCards(setUpResourceCardsFaceUp);

        // place common objective cards
        List<ObjectiveCard> setUpObjectiveCardsFaceUp = new ArrayList<>();
        setUpObjectiveCardsFaceUp.add(objectiveCardsDeck.drawCard());
        setUpObjectiveCardsFaceUp.add(objectiveCardsDeck.drawCard());
        objectiveCardsDeck.setFaceUpCards(setUpObjectiveCardsFaceUp);
    }

    /**
     * Method that adds points to a player and checks if a player had reached 20 points.
     * @param nickname nickname of the player
     * @param x where the card is placed in the matrix
     * @param y where the card is placed in the matrix
     */
    private void addPoints(String nickname, int x, int y) {
        assert(state.equals(GameState.PLAYING)): "Wrong game state";
        assert(players.get(currPlayer).getNickname().equals(nickname)): "Not the current player";
        assert (playersGameField.get(nickname).isCardPresent(x, y)) : "No card present in the provided position";
        int deltaPoints;
        deltaPoints = playersGameField.get(nickname).getPlacedCard(x, y).getPlacementScore(playersGameField.get(nickname), x, y);
        if(deltaPoints + scoreTrackBoard.getScore(nickname) >= 20){
            twentyPointsReached = true;
            if((deltaPoints + scoreTrackBoard.getScore(nickname)) > 29){
                scoreTrackBoard.setScore(nickname, 29);
            }
            else{
                scoreTrackBoard.incrementScore(nickname, deltaPoints);
            }
        }
        else
        {
            scoreTrackBoard.incrementScore(nickname, deltaPoints);
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
            faceUpCard = this.goldCardsDeck.revealFaceUpCard(pos);
        else if(type.equals(CardType.RESOURCE_CARD))
            faceUpCard = this.resourceCardsDeck.revealFaceUpCard(pos);
        else
            faceUpCard = this.objectiveCardsDeck.revealFaceUpCard(pos);
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
            backResource = this.goldCardsDeck.revealBackDeckCard();
        }else{
            backResource = this.resourceCardsDeck.revealBackDeckCard();
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
    public Message getLastChatMessage(String receiver)  {
        Message message = chat.getLastMessage(receiver);
        if(message == null) {
            // TODO no return null!
            return null;
        }
        else
            return message;
    }

    /**
     * Returns the content of the chat for a certain player.
     * @return the list of the message in the chat
     */
    public List<Message> getChatContent(String receiver) {
        return chat.getContent(receiver);
    }
}