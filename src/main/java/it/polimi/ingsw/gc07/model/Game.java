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
        addPlayer(nickname, tokenColor, connectionType, interfaceType,starterCardWay);
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
     * @throws PlayerNotPresentExcpetion exception thrown when a player is not present in the game
     */
    public GameField getGameField(String nickname) throws PlayerNotPresentExcpetion {
        if(!this.playersGameField.containsKey(nickname)) {
            throw new PlayerNotPresentExcpetion();
        }
        return new GameField(this.playersGameField.get(nickname));
    }

    /**
     * get the players' score
     * @param nickname : nickname of the player
     * @return the players' score
     * @throws PlayerNotPresentExcpetion exception thrown when a player is not present in the game
     */
    public int getScore(String nickname) throws PlayerNotPresentExcpetion {
        return this.scoreTrackBoard.getScore(nickname);
    }

    /**
     * Method to add a new player.
     * @param nickname player to add to the game
     * @param tokenColor color of player's token
     * @param connectionType type of connection
     * @param interfaceType type of the interface
     * @param starterCardWay way of the starter card
     */
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType, boolean starterCardWay) {
        try{
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
            }catch (PlayerNotPresentExcpetion e)
            {
                e.printStackTrace();
            }
            this.scoreTrackBoard.addPlayer(newPlayer.getNickname());
            if(isFull())
            {
                setup();
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
     */
    private void setup(){
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
    public void disconnectPlayer(String nickname)
    {
        //TODO: disconnect the player
    }
    /**
     * method that reconnect a player from the game
     * @param nickname : nickname of the player
     */
    public void reconnectPlayer(String nickname){
        // TODO: reconnect the player
    }

    /**
     * method that change the current player, if it's the last turn and all the players
     * played the same amount of turn it computes the winner
     */
    public void changeCurrPlayer () {
        if(this.currPlayer==this.players.size()-1)
            this.currPlayer=0;
        else
            this.currPlayer++;
        if(this.lastTurn)
        {
            if(getCurrentPlayer().isFirst()&&this.additionalRound)
            {
                //Player winner = computeWinner();
                //TODO: fare qualcosa con questo winner
            }
            else if(getCurrentPlayer().isFirst())
            {
                this.additionalRound=true;
            }

        }
    }

    /**
     * method that place a card in the gamefield of the current player
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
        {
            throw new WrongPlayerException();
        }
        try {
            getGameField(nickname).placeCard(card,x,y,way);
        }catch (PlayerNotPresentExcpetion e)
        {
            e.printStackTrace();
        }
        List<NonStarterCard> newHand = new ArrayList<>(getCurrentPlayer().getCurrentHand());
        if(!getCurrentPlayer().getCurrentHand().contains(card))
            throw new CardNotPresentException();
        else
            newHand.remove(card);
        getCurrentPlayer().setCurrentHand(newHand);
        addPoints(nickname,x,y);
    }

    /**
     * method that add points to a player
     * @param nickname: nickname of the player
     * @param x: where the card is placed in the matrix
     * @param y: where the card is placed in the matrix
     * @throws WrongPlayerException : if the player is not the current player
     */
    private void addPoints(String nickname, int x, int y) throws WrongPlayerException{
        if(getCurrentPlayer().getNickname().equals(nickname))
        {
            //TODO
            //
        }
        else {
            throw new WrongPlayerException();
        }
        // TODO
        // controllo sia currentPlayer
        // la card è già stata piazzata
        // conoscendo x e y posso verificare quanti punti ha fatto e
        // aggiornare il punteggio su scoreTrackBoard
        // se il punteggio è >= 20 (?) lastTurn = true
        // riceve un player equals (ma non ==) a uno dei suoi
        // prima il suo e poi
    }

    /**
     *
     * @return the player that is the winner of the game.
     */
    private List<Player> computeWinner() throws CardNotPresentException, PlayerNotPresentExcpetion {
        // TODO: fare catch di CardNotPresentException
        List<Player> winners = new ArrayList<>();
        int deltapoints;
        int max = 0;
        int realizedObjectives;
        int maxRealizedObjective = 0;
        for (int i=0; i>=0 && i< players.size(); i++){
            realizedObjectives = objectiveCardsDeck.revealFaceUpCard(0).getScoringCondition().numTimesMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 1st common objective
            deltapoints = objectiveCardsDeck.revealFaceUpCard(0).getScoringCondition().numTimesMet(playersGameField.get(players.get(i).getNickname())) * objectiveCardsDeck.revealFaceUpCard(0).getScore();
            realizedObjectives += objectiveCardsDeck.revealFaceUpCard(1).getScoringCondition().numTimesMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the 2nd common objective
            deltapoints += objectiveCardsDeck.revealFaceUpCard(1).getScoringCondition().numTimesMet(playersGameField.get(players.get(i).getNickname())) * objectiveCardsDeck.revealFaceUpCard(1).getScore();
            realizedObjectives += players.get(i).getSecretObjective().getScoringCondition().numTimesMet(playersGameField.get(players.get(i).getNickname()));
            //points counter for the secret objective
            deltapoints += players.get(i).getSecretObjective().getScoringCondition().numTimesMet(playersGameField.get(players.get(i).getNickname())) * players.get(i).getSecretObjective().getScore();
            scoreTrackBoard.incrementScore(players.get(i).getNickname(), deltapoints);
            List<Player> playersCopy = new ArrayList<>(players);
            if (max <= scoreTrackBoard.getScore(playersCopy.get(i).getNickname())){
                max = scoreTrackBoard.getScore(playersCopy.get(i).getNickname());
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


    // TODO
    // metodi per pescare una carta
    // metodi per rivelare le carte scoperte
    // uno per ogni tipo di carta oppure prendono un tipo di carta?
    // Nel metodo per pescare una carta, alla fine si chiama changeCurrPlayer

    public void drawDeckCard(String nickname, CardType type) throws WrongCardTypeException, CardNotPresentException {
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        if(type.equals(CardType.RESOURCE_CARD)){
            // return resourceCardsDeck.drawCard();
        }
        // return goldCardsDeck.drawCard();
        // TODO
        // modifica currentHand, aggiungendo la carta pescata (setCurrenHand di Player)
        // riceve un player equals (ma non ==) a uno dei suoi
        // prima il suo e poi ci chiama setCurrentHand
    }

    public void drawFaceUpCard(String nickname, CardType type, int pos) throws WrongCardTypeException, CardNotPresentException {
        // TODO
        // se starter o objective, eccezione
        // altrimenti chiamo drawFaceUpCard sul giusto deck
        // TODO
        // modifica currentHand, aggiungendo la carta pescata (setCurrenHand di Player)
        // riceve un player equals (ma non ==) a uno dei suoi
        // prima il suo e poi ci chiama setCurrentHand
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
