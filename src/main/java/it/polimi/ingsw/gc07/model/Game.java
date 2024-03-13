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

    private boolean lastTurn;

    private Chat chat;

    /**
     * Constructor of a Game with only the first player.
     * @param playersNumber number of players
     * @param resourceCardsDeck deck of resource cards
     * @param goldCardsDeck deck of gold cards
     * @param objectiveCardsDeck deck of objective cards
     * @param starterCardsDeck deck of starter cards
     * @param nickname player to add to the game
     * @param tokenColor color of player's token
     * @param connectionType type of connection
     */
    public Game(int playersNumber, DrawableDeck resourceCardsDeck,
                DrawableDeck goldCardsDeck, PlayingDeck objectiveCardsDeck, Deck starterCardsDeck,
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
        addPlayer(nickname, tokenColor, connectionType, interfaceType,starterCardWay);
    }

    public GameState getState() {
        return this.state;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Player getCurrentPlayer(){
        return new Player(players.get(currPlayer));
    }

    public GameField getGameField(String nickname) throws PlayerNotPresentExcpetion {
        if(!playersGameField.containsKey(nickname)) {
            throw new PlayerNotPresentExcpetion();
        }
        return new GameField(playersGameField.get(nickname));
    }

    public int getScore(String nickname) throws PlayerNotPresentExcpetion {
        return scoreTrackBoard.getScore(nickname);
    }

    // l'esterno può chimare player.getCurrentHand, non serve il metodo

    /**
     * Method to add a new player.
     * @param nickname player to add to the game
     * @param tokenColor color of player's token
     * @param connectionType type of connection
     */
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType, boolean interfaceType, boolean starterCardWay) {
        try{
            List<NonStarterCard> currentHand = new ArrayList<>();
            /* TODO: aggiungere le carte alla currentHand, dipende da come implementiamo i deck
            currentHand.add(//NonStarterCard);
            currentHand.add(//NonStarterCard);
            currentHand.add(//GoldCard);
            */
            //controllare dopo Deck class
            ObjectiveCard secretObjective = (ObjectiveCard) objectiveCardsDeck.drawCard();

            Player newPlayer = new Player(nickname, tokenColor, connectionType, interfaceType, currentHand, secretObjective);
            GameField gameField = new GameField();
            this.players.add(newPlayer);
            this.playersGameField.put(newPlayer.getNickname(),gameField);
            this.playersGameField.get(nickname).placeCard(); // TODO: StarterCard,40,40,starterCardWay
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
    }

    /**
     * method to set up the game: the first player is chosen and the 4 cards are revealed
     */
    private void setup() {
        Random random= new Random();
        this.currPlayer=random.nextInt(4);
        this.players.get(this.currPlayer).setFirst();
        // TODO  scopre 2 carte per ogni deck ---> Dipende da come implementiamo Deck

    }

    /**
     * Method telling if there are available places in the game.
     * @return true if no other player can connect to the game
     */
    public boolean isFull(){
        return players.size() == playersNumber;
    }

    public void disconnectPlayer(String nickname)
    {
        //TODO: disconnect the player
    }

    public void reconnectPlayer(String nickname){
        // TODO: reconnect the player
    }

    /**
     * method that change the current player, if it's the last turn and all the players
     * played the same amount of turn it computes the winner
     */
    public void changeCurrPlayer()
    {
        if(this.currPlayer==this.playersNumber-1)
            this.currPlayer=0;
        else
            this.currPlayer++;
        if(this.lastTurn==true)
        {
            if(this.players.get(this.currPlayer).isFirst())
            {
                Player winner = computeWinner();
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
     */
    public void placeCard(String nickname, PlaceableCard card, int x, int y, boolean way) throws WrongPlayerException, CardAlreadyPresentException {
        if(this.players.get(this.currPlayer).getNickname().equals(nickname))
        {
            playersGameField.get(nickname).placeCard(card,x,y,way);
            List<NonStarterCard> newHand = new ArrayList<>(players.get(this.currPlayer).getCurrentHand());
            newHand.remove(card);
            players.get(this.currPlayer).setCurrentHand(newHand);
            addPoints(nickname,x,y);
        }
        else {
            throw new WrongPlayerException();
        }
        // TODO: vanno lanciate altre eccezioni??
    }

    private void addPoints(String nickname, int x, int y) throws WrongPlayerException{
        if(this.players.get(this.currPlayer).getNickname().equals(nickname))
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

    private Player computeWinner() throws CardNotPresentException {
        // TODO
        for (int i=0; i>=0 && i< players.size(); i++){
            /*objectiveCardsDeck.revealFaceUpCard(0).getScoringCondition()
                    numTimesMet(playersGameField.get(players.get(i).getNickname()));*/

        }
        // Considerando le carte obiettivo finale comuni e le
        // carte obiettivo segrete, calcola il punteggio finale
        // per tutti i giocatori
        // se parità, ... (vedi regole)
        // restituisce il player vincitore,
        // in caso di parità ??
        // restituisce una copia del player!
        return null; // TODO
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

    public Card revealFaceUpCard(CardType type, int pos) throws WrongCardTypeException, CardNotPresentException {
        // TODO
        // se starter, eccezione
        // altrimenti chiamo revealFaceUpCard sul giusto deck
        return null; // TODO
    }

    public GameResource revealBackDeckCard(CardType type) throws WrongCardTypeException, CardNotPresentException {
        // TODO
        // se starter o objective, eccezione
        // altrimenti chiamo revealBackDeckCard sul giusto deck
        return null; // TODO
    }

    // metodi chat
    public void addChatMessage(String newMessage) {
        chat.addMessage(newMessage);
    }

    public String getLastChatMessage() throws EmptyChatException {
        return chat.getLastMessage();
    }

    public List<String> getChatContent() {
        return chat.getContent();
    }

}
