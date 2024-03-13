package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.cards.NonStarterCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
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
    private DrawableDeck resourceCardsDeck;
    /**
     * Deck of gold cards.
     */
    private DrawableDeck goldCardsDeck;
    /**
     * Deck of objective cards.
     */
    private PlayingDeck objectiveCardsDeck;
    /**
     * Deck of starter cards.
     */
    private Deck starterCardsDeck;

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
            playersGameField.get(nickname).placeCard(); // TODO: StarterCard,40,40,starterCardWay
            this.scoreTrackBoard.addPlayer(newPlayer.getNickname());
            if(players.size()==playersNumber)
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

    private void setup() {
        Random random= new Random();
        this.currPlayer=random.nextInt(4);
        players.get(this.currPlayer).setFirst();
        // TODO
        // scopre 2 carte per ogni deck ---> Dipende da come implementiamo Deck

    }

    /**
     * Method telling if there are available places in the game.
     * @return true if no other player can connect to the game
     */
    public boolean isFull(){
        return players.size() == playersNumber;
    }

    public void disconnectPlayer(String nickname){
        //TODO: disconnect the player
    }

    public void reconnectPlayer(String nickname){
        // TODO: reconnect the player
    }

    public void changeCurrPlayer() {
        // TODO
        // Cambia currPlayer al prossimo
        // Se lastTurn = false, rotazione normale
        // Se lastTurn = true, controlla di non andare oltre all'ultimo giro
        // Se lastTurn = true e ha completato l'ultimo giro,
        // chiama computeWinner()
        // Comunica al player che è il suo turno (?)
    }

    public void placeCard(String nickname, PlaceableCard card, int x, int y, boolean way) {
        // TODO
        // posso aggiungerla solo al currentPlayer, controllo e lancio eccezion
        // chiama placeCard sul gamefield del current player
        // rimuove la carta dalla currentHand
        // chiama il metodo addPoints che aggiunge i punti al giocatore
        // riceve un player equals (ma non ==) a uno dei suoi
        // prima il suo e poi ci ...
    }

    private void addPoints(String nickname, int x, int y) {
        // TODO
        // controllo sia currentPlayer
        // la card è già stata piazzata
        // conoscendo x e y posso verificare quanti punti ha fatto e
        // aggiornare il punteggio su scoreTrackBoard
        // se il punteggio è >= 20 (?) lastTurn = true
        // riceve un player equals (ma non ==) a uno dei suoi
        // prima il suo e poi
    }

    private Player computeWinner() {
        // TODO
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
