package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.model.decks.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.enumerations.GameState;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.*;
import java.util.Random;
public class Game {
    /**
     * State of the game.
     */
    private GameState state;
    /**
     * Number of players in the game, chose by the first player.
     */
    private int playersNumber;
    /**
     * Map of players and their game field.
     */
    private Map<String, GameField> playersGameField;
    /**
     * List of players and an integer for their order.
     */
    private List<Player> players;
    /**
     * Integer value representing the position of the current player.
     */
    private int currPlayer;
    /**
     * Score track board of the game.
     */
    private ScoreTrackBoard scoreTrackBoard;
    /**
     * Deck of resource cards.
     */
    private ResourceCardsDeck resourceCardsDeck;
    /**
     * Deck of gold cards.
     */
    private GoldCardsDeck goldCardsDeck;
    /**
     * Deck of objective cards.
     */
    private PlayingDeck<ObjectiveCard> objectiveCardsDeck;
    /**
     * Deck of starter cards.
     */
    private Deck<StarterCard> starterCardsDeck;
    /**
     * indicate if is the last turn of the game
     */
    private boolean lastTurn;
    /**
     * indicate if is the additional round of the game
     */
    private boolean additionalRound;
    /**
     * chat of the game
     */
    private Chat chat;

    /** Constructor of a Game with only the first player.
     *
     * @param playersNumber number of players
     * @param resourceCardsDeck deck of resource cards
     * @param goldCardsDeck deck of gold cards
     * @param objectiveCardsDeck deck of objective cards
     * @param starterCardsDeck deck of starter cards
     * @param nickname player to add to the game
     * @param tokenColor color of player's token
     * @param connectionType type of connection
     * @param interfaceType type of the interface
     * @param starterCardWay way of the starter card
     * @throws WrongNumberOfPlayersException exception thrown when the number of players is wrong
     */
    public Game(int playersNumber, ResourceCardsDeck resourceCardsDeck,
                GoldCardsDeck goldCardsDeck, PlayingDeck<ObjectiveCard> objectiveCardsDeck, Deck<StarterCard> starterCardsDeck,
                String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType, boolean starterCardWay) throws WrongNumberOfPlayersException
    {
        this.state = GameState.WAITING_PLAYERS;
        if (playersNumber<2 || playersNumber>4)  {
            throw new WrongNumberOfPlayersException();
        }
        else{
            this.playersNumber = playersNumber;
        }
        this.players = new ArrayList<>();
        this.playersGameField = new HashMap<>();
        this.scoreTrackBoard = new ScoreTrackBoard();
        this.resourceCardsDeck = resourceCardsDeck;
        this.goldCardsDeck = goldCardsDeck;
        this.objectiveCardsDeck = objectiveCardsDeck;
        this.starterCardsDeck = starterCardsDeck;
        this.currPlayer = 0;
        this.lastTurn = false;
        this.additionalRound = false;
        try {
            addPlayer(nickname, tokenColor, connectionType, interfaceType,starterCardWay);
        }
        catch (WrongStateException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * get the state of the game
     * @return the state of the game
     */
    public GameState getState() {
        return this.state;
    }

    /**
     * get the list of players
     * @return the players in the game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    /**
     * get the current player
     * @return the current player
     */
    public Player getCurrentPlayer(){
        return new Player(this.players.get(this.currPlayer));
    }

    /**
     * get the game field of the player
     * @param nickname : nickname of the player
     * @return the game field of the player
     * @throws PlayerNotPresentException exception thrown when a player is not present in the game
     */
    public GameField getGameField(String nickname) throws PlayerNotPresentException {
        if(!this.playersGameField.containsKey(nickname)) {
            throw new PlayerNotPresentException();
        }
        return new GameField(this.playersGameField.get(nickname));
    }

    /**
     * get the players' score
     * @param nickname : nickname of the player
     * @return the players' score
     * @throws PlayerNotPresentException exception thrown when a player is not present in the game
     */
    public int getScore(String nickname) throws PlayerNotPresentException {
        return this.scoreTrackBoard.getScore(nickname);
    }

    /**
     * Method to add a new player.
     * @param nickname player to add to the game
     * @param tokenColor color of player's token
     * @param connectionType type of connection
     * @param interfaceType type of the interface
     * @param starterCardWay way of the starter card
     * @throws WrongStateException if the state of the game is wrong
     */
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType, boolean starterCardWay) throws WrongStateException{
        try{
            if(getState().equals(GameState.PLAYING)||getState().equals(GameState.GAME_ENDED))
            {
                throw new WrongStateException();
            }
            List<NonStarterCard> currentHand = new ArrayList<>();
            currentHand.add(this.resourceCardsDeck.drawCard());
            currentHand.add(this.resourceCardsDeck.drawCard());
            currentHand.add(this.goldCardsDeck.drawCard());
            ObjectiveCard secretObjective = this.objectiveCardsDeck.drawCard();
            Player newPlayer = new Player(nickname, tokenColor, connectionType, interfaceType, currentHand, secretObjective);
            GameField gameField = new GameField();
            getPlayers().add(newPlayer);
            this.playersGameField.put(newPlayer.getNickname(),gameField);
            try
            {
                getGameField(nickname).placeCard(this.starterCardsDeck.drawCard(), 40,40,starterCardWay);
            }catch (PlayerNotPresentException e)
            {
                e.printStackTrace();
            }
            this.scoreTrackBoard.addPlayer(newPlayer.getNickname());
            if(isFull())
            {
                setup();
                this.state=GameState.PLAYING;
            }
        }
        catch(CardNotPresentException e){
            e.printStackTrace();
        }
        catch (PlayerAlreadyPresentException e)
        {
            e.printStackTrace();
        }
        catch (CardAlreadyPresentException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * method to set up the game: the first player is chosen and 4 cards(2 gold and 2 resource) are revealed
     * @throws WrongStateException if the state of the game is wrong
     */
    private void setup() throws WrongStateException{
        if(getState().equals(GameState.PLAYING)||getState().equals(GameState.GAME_ENDED))
        {
            throw new WrongStateException();
        }
        // chose randomly the first player
        Random random= new Random();
        this.currPlayer=random.nextInt(4);
        getCurrentPlayer().setFirst();
        try {
            //place 2 gold cards
            List<GoldCard> setUpGoldCardsFaceUp = new ArrayList<>();
            setUpGoldCardsFaceUp.add(this.goldCardsDeck.drawCard());
            setUpGoldCardsFaceUp.add(this.goldCardsDeck.drawCard());
            this.goldCardsDeck.setFaceUpCards(setUpGoldCardsFaceUp);
        }
        catch (CardNotPresentException e)
        {
            e.printStackTrace();
        }
        try {
            //place 2 resource card
            List<NonStarterCard> setUpResourceCardsFaceUp = new ArrayList<>();
            setUpResourceCardsFaceUp.add(this.resourceCardsDeck.drawCard());
            setUpResourceCardsFaceUp.add(this.resourceCardsDeck.drawCard());
            this.resourceCardsDeck.setFaceUpCards(setUpResourceCardsFaceUp);
        }
        catch (CardNotPresentException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Method telling if there are available places in the game.
     * @return true if no other player can connect to the game
     */
    public boolean isFull(){
        return players.size() == playersNumber;
    }

    /**
     * method that disconnect a player from the game
     * @param nickname : nickname of the player
     */
    public void disconnectPlayer(String nickname){
        try{
            int pos = getPlayerByNickname(nickname);
            players.get(pos).setIsConnected(false);
        } catch (PlayerNotPresentException e) {
            //TODO gestione eccezione
        }
    }
    /**
     * method that reconnect a player from the game
     * @param nickname : nickname of the player
     */
    public void reconnectPlayer(String nickname){
        try{
            int pos = getPlayerByNickname(nickname);
            players.get(pos).setIsConnected(true);
        } catch (PlayerNotPresentException e) {
            //TODO gestione eccezione
        }
    }

    /**
     * method that returns the position of the player in the List players.
     * @param nickname: is the nickname of the player whose position is being searched.
     * @return the position of the player searched in the List players.
     * @throws PlayerNotPresentException: if the nickname is not present in the list players.
     */
    public int getPlayerByNickname(String nickname) throws PlayerNotPresentException {
        for (int i = 0; i < this.playersNumber; i++){
            if(this.players.get(i).getNickname().equals(nickname)){
                return i;
            }
        }
        throw new PlayerNotPresentException();
    }

    /**
     * method that change the current player, if it's the last turn and all the players
     * played the same amount of turn it computes the winner
     * @throws WrongStateException if the state of the game is wrong
     */
    public void changeCurrPlayer () throws WrongStateException{
        if(getState().equals(GameState.WAITING_PLAYERS)||getState().equals(GameState.GAME_ENDED))
        {
            throw new WrongStateException();
        }
        if(this.currPlayer==this.players.size()-1)
            this.currPlayer=0;
        else
            this.currPlayer++;
        if(this.lastTurn)
        {
            if(getCurrentPlayer().isFirst()&&this.additionalRound)
            {
                //Player winner = computeWinner();
                this.state=GameState.GAME_ENDED;
                //TODO: fare qualcosa con questo winner
                // i vincitori possono essere più di uno
            }
            else if(getCurrentPlayer().isFirst())
            {
                this.additionalRound=true;
            }
        }
        if(!getCurrentPlayer().isConnected())
        {
            changeCurrPlayer();
        }
    }

    /**
     * method that place a card in the game field of the current player
     * this method also remove the card placed from the hand of the current player and calls the method that compute the points scored by placing the card
     * @param nickname : nickname of the player
     * @param card : card that the player wants to play
     * @param x : row of the matrix (gameField)
     * @param y: column of the matrix (gameField)
     * @param way : face of the card
     * @throws WrongPlayerException : if a player that is not the current player try to place a card
     * @throws CardAlreadyPresentException : if a player play a card that is already present in the gameField
     * @throws CardNotPresentException : if the card that the player wants to play is not in his hands
     */
    public void placeCard(String nickname, PlaceableCard card, int x, int y, boolean way) throws WrongPlayerException, CardAlreadyPresentException, CardNotPresentException {
        if(!getCurrentPlayer().getNickname().equals(nickname))
            throw new WrongPlayerException();
        if(!getCurrentPlayer().getCurrentHand().contains(card))
            throw new CardNotPresentException();
        else
        {
            try {
                getGameField(nickname).placeCard(card,x,y,way);
                List<NonStarterCard> newHand = new ArrayList<>(getCurrentPlayer().getCurrentHand());
                newHand.remove(card);
                getCurrentPlayer().setCurrentHand(newHand);
                addPoints(nickname,x,y);
            }catch (PlayerNotPresentException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * method that add points to a player and check if a player is reaching 20 points.
     * @param nickname: nickname of the player
     * @param x: where the card is placed in the matrix
     * @param y: where the card is placed in the matrix
     * @throws WrongPlayerException : if the player is not the current player.
     * @throws PlayerNotPresentException : if the player is not present in the List players.
     * @throws CardNotPresentException: if there isn't a card in the specified position of the game field.
     */
    private void addPoints(String nickname, int x, int y) throws WrongPlayerException, CardNotPresentException, PlayerNotPresentException {
        int deltaPoints;
        if(!getCurrentPlayer().getNickname().equals(nickname))
        {
            throw new WrongPlayerException();
        }
        if (!this.playersGameField.get(nickname).isCardPresent(x, y)){
            throw new CardNotPresentException();
        }
        if(this.playersGameField.get(nickname).getCardWay(x, y)){
            return;
        }
        if(this.playersGameField.get(nickname).getPlacedCard(x, y).getScoringCondition() == null){
            deltaPoints = this.playersGameField.get(nickname).getPlacedCard(x, y).getScore();
            if(deltaPoints + getScore(nickname) >= 20){
                this.lastTurn = true;
                if((deltaPoints + getScore(nickname)) > 29){
                    this.scoreTrackBoard.setScore(nickname, 29);
                }
                else{
                    this.scoreTrackBoard.incrementScore(nickname, deltaPoints);
                }
            }
            return;
        }
        else{
            deltaPoints = this.playersGameField.get(nickname).getPlacedCard(x, y).getScoringCondition().numTimesMet(this.playersGameField.get(nickname)) * this.playersGameField.get(nickname).getPlacedCard(x, y).getScore();
            if(deltaPoints + getScore(nickname) >= 20){
                lastTurn = true;
                if((deltaPoints + getScore(nickname)) > 29){
                    this.scoreTrackBoard.setScore(nickname, 29);
                }
                else{
                    this.scoreTrackBoard.incrementScore(nickname, deltaPoints);
                }
            }
            return;
        }
    }

    /**
     * method that compute the winner/s of the game.
     * @return the list of players who won the game.
     * @throws CardNotPresentException: if there isn't an objective faceUpCard on the board.
     * @throws PlayerNotPresentException: if the player is not present in the List players.
     */
    private List<Player> computeWinner() throws CardNotPresentException, PlayerNotPresentException {
        // TODO: fare catch di CardNotPresentException
        List<Player> winners = new ArrayList<>();
        int deltapoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;
        for (int i=0; i>=0 && i< this.players.size(); i++){
            realizedObjectives = this.objectiveCardsDeck.revealFaceUpCard(0).getScoringCondition().numTimesMet(this.playersGameField.get(this.players.get(i).getNickname()));
            //points counter for the 1st common objective
            deltapoints = this.objectiveCardsDeck.revealFaceUpCard(0).getScoringCondition().numTimesMet(this.playersGameField.get(this.players.get(i).getNickname())) * this.objectiveCardsDeck.revealFaceUpCard(0).getScore();
            realizedObjectives += this.objectiveCardsDeck.revealFaceUpCard(1).getScoringCondition().numTimesMet(this.playersGameField.get(this.players.get(i).getNickname()));
            //points counter for the 2nd common objective
            deltapoints += this.objectiveCardsDeck.revealFaceUpCard(1).getScoringCondition().numTimesMet(this.playersGameField.get(this.players.get(i).getNickname())) * this.objectiveCardsDeck.revealFaceUpCard(1).getScore();
            realizedObjectives += this.players.get(i).getSecretObjective().getScoringCondition().numTimesMet(this.playersGameField.get(this.players.get(i).getNickname()));
            //points counter for the secret objective
            deltapoints += this.players.get(i).getSecretObjective().getScoringCondition().numTimesMet(this.playersGameField.get(this.players.get(i).getNickname())) * this.players.get(i).getSecretObjective().getScore();
            this.scoreTrackBoard.incrementScore(this.players.get(i).getNickname(), deltapoints);
            List<Player> playersCopy = getPlayers();
            if (max <= getScore(playersCopy.get(i).getNickname())){
                max = getScore(playersCopy.get(i).getNickname());
                if (realizedObjectives >= maxRealizedObjective){
                    if (realizedObjectives == maxRealizedObjective){
                        winners.add(playersCopy.get(i));
                    }
                    else{
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
     * method that allows a player to draw one card from a GoldCardDeck or a ResourceCardDeck.
     * @param nickname: nickname of a player.
     * @param type: type of the card a user wants to draw.
     * @throws WrongCardTypeException: if the Type given is not correct
     * @throws CardNotPresentException: if the List of faceUpCards doesn't have a card in the given position
     * @throws WrongPlayerException: if the player is not the current player
     */
    public void drawDeckCard(String nickname, CardType type) throws WrongCardTypeException, CardNotPresentException, WrongPlayerException {
        if(!getCurrentPlayer().getNickname().equals(nickname)){
            throw new WrongPlayerException();
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        List<NonStarterCard> newHand = new ArrayList<>();
        newHand.addAll(this.players.get(this.currPlayer).getCurrentHand());
        if(type.equals(CardType.RESOURCE_CARD)){
            newHand.add(this.resourceCardsDeck.drawCard());
        }
        if(type.equals(CardType.RESOURCE_CARD)){
            newHand.add(this.goldCardsDeck.drawCard());
        }
        this.players.get(this.currPlayer).setCurrentHand(newHand);
        changeCurrPlayer();
    }

    /**
     * method that allows a player to draw one of two faceUp cards of a given type.
     * @param nickname: nickname of a player.
     * @param type: type of the card a user wants to draw.
     * @param pos: position in the List of faceUpCards
     * @throws WrongCardTypeException: if the Type given is not correct
     * @throws CardNotPresentException: if the List of faceUpCards doesn't have a card in the given position
     * @throws WrongPlayerException: if the player is not the current player
     */
    public void drawFaceUpCard(String nickname, CardType type, int pos) throws WrongCardTypeException, CardNotPresentException, WrongPlayerException {
        if(!getCurrentPlayer().getNickname().equals(nickname)){
            throw new WrongPlayerException();
        }
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        List<NonStarterCard> newHand = new ArrayList<>();
        newHand.addAll(this.players.get(this.currPlayer).getCurrentHand());
        if(type.equals(CardType.RESOURCE_CARD)){
            newHand.add(this.resourceCardsDeck.drawFaceUpCard(pos));
        }
        if(type.equals(CardType.GOLD_CARD)){
            newHand.add(this.goldCardsDeck.drawFaceUpCard(pos));
        }
        this.players.get(this.currPlayer).setCurrentHand(newHand);
        changeCurrPlayer();
    }

    /**
     * method that reveal the face up card in position pos
     * @param type type of the card
     * @param pos position of the card
     * @return the card that we want to reveal
     * @throws WrongCardTypeException if the card is a starter card
     * @throws CardNotPresentException if there aren't face up cards
     */
    public Card revealFaceUpCard(CardType type, int pos) throws WrongCardTypeException, CardNotPresentException {
        if(type.equals(CardType.STARTER_CARD))
        {
            throw new WrongCardTypeException();
        }
        if(type.equals(CardType.GOLD_CARD))
            return this.goldCardsDeck.revealFaceUpCard(pos);
        else if(type.equals(CardType.RESOURCE_CARD))
            return this.resourceCardsDeck.revealFaceUpCard(pos);
        else
            return this.objectiveCardsDeck.revealFaceUpCard(pos);
    }

    /**
     * method that reveal the back of the card on top of the deck
     * @param type : type of the card
     * @return the GameResource that represent the back of the card
     * @throws WrongCardTypeException   if we reveal the back of a starter card or the back of an objective card
     * @throws CardNotPresentException if the deck is empty
     */
    public GameResource revealBackDeckCard(CardType type) throws WrongCardTypeException, CardNotPresentException
    {
        if(type.equals(CardType.STARTER_CARD) || type.equals(CardType.OBJECTIVE_CARD))
        {
          throw new WrongCardTypeException();
        }
        if(type.equals(CardType.GOLD_CARD))
            return this.goldCardsDeck.revealBackDeckCard();
        else
            return this.resourceCardsDeck.revealBackDeckCard();
    }

    // metodi chat
    /**
     * add the message at the chat
     * @param newMessage message added at the chat
     */
    public void addChatMessage(String newMessage) {
        this.chat.addMessage(newMessage);
    }

    /**
     * get the last message of the chat
     * @return the last message of the chat
     * @throws EmptyChatException if the chat is empty
     */
    public String getLastChatMessage() throws EmptyChatException {
        return this.chat.getLastMessage();
    }

    /**
     * get the content of the chat
     * @return the list of the message in the chat
     */
    public List<String> getChatContent() {
        return this.chat.getContent();
    }

}
