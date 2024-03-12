package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.exceptions.EmptyChatException;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentExcpetion;
import it.polimi.ingsw.gc07.exceptions.WrongCardTypeException;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.cards.NonStarterCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

import java.util.*;

public class Game {
    /**
     * Id of the game.
     */
    //TODO Serve?
    private int gameId;
    /**
     * Number of players in the game, chose by the first player.
     */
    private int playersNumber;
    /**
     * Map of players and their game field.
     */
    private Map<Player, GameField> playersGameField;
    /**
     * Map of players and an integer for their order.
     */
    private Map<Integer, Player> playersPosition;
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
     * @param gameId id of the game
     * @param playersNumber number of players
     * @param resourceCardsDeck deck of resource cards
     * @param goldCardsDeck deck of gold cards
     * @param objectiveCardsDeck deck of objective cards
     * @param starterCardsDeck deck of starter cards
     * @param nickname player to add to the game
     * @param tokenColor color of player's token
     * @param connectionType type of connection
     */
    public Game(int gameId, int playersNumber, DrawableDeck resourceCardsDeck,
                DrawableDeck goldCardsDeck, PlayingDeck objectiveCardsDeck, Deck starterCardsDeck,
                String nickname, TokenColor tokenColor, boolean connectionType) {
        this.gameId = gameId;
        // TODO: mettiamo un'eccezione per playersNumber?
        this.playersNumber = playersNumber;
        this.playersPosition = new HashMap<>();
        this.playersGameField = new HashMap<>();
        this.scoreTrackBoard = new ScoreTrackBoard();
        this.resourceCardsDeck = resourceCardsDeck;
        this.goldCardsDeck = goldCardsDeck;
        this.objectiveCardsDeck = objectiveCardsDeck;
        this.starterCardsDeck = starterCardsDeck;
        this.currPlayer = 0;
        this.lastTurn = false;
        addPlayer(nickname, tokenColor, connectionType);
    }

    public Set<Player> getPlayers() {
        Set<Player> playersSet = new HashSet<>();
        for(Player p: playersGameField.keySet()){
            playersSet.add(new Player(p));
        }
        return playersSet;
    }

    public Player getCurrentPlayer(){
        return new Player(playersPosition.get(currPlayer));
    }

    public GameField getGameField(Player player) throws PlayerNotPresentExcpetion {
        if(!playersGameField.containsKey(player)) {
            throw new PlayerNotPresentExcpetion();
        }
        return new GameField(playersGameField.get(player));
    }

    public int getScore(Player player) throws PlayerNotPresentExcpetion {
        return scoreTrackBoard.getScore(player);
    }

    // l'esterno può chimare player.getCurrentHand, non serve il metodo

    /**
     * Method to add a new player.
     * @param nickname player to add to the game
     * @param tokenColor color of player's token
     * @param connectionType type of connection
     */
    public void addPlayer(String nickname, TokenColor tokenColor, boolean connectionType) {
        try{
            List<NonStarterCard> currentHand = new ArrayList<>();
            // TODO: aggiungere le carte alla currentHand
            ObjectiveCard secretObjective = (ObjectiveCard) objectiveCardsDeck.drawCard();
            Player newPlayer = new Player(nickname, tokenColor, currentHand, secretObjective, connectionType);
            // TODO
            // Creare il GameField vuoto
            // currPlayer è inizializzato a 0 dal costruttore
            // Aggiungere posizione (currPlayer) e Player alla mappa
            // Incrementare currPlayer (per pos del prossimo Player)
            // Aggiunge Player e GameField alla mappa
            // Aggiungere Player allo ScoreTrackBoard
            // Controlla se è ultimo, se sì, chiama un metodo setup()
        }
        catch(CardNotPresentException e){
            e.printStackTrace();
        }
    }

    private void setup() {
        // TODO
        // sceglie il primo giocatore a caso e modifica il suo
        // attributo isFirst e lo mette come currPlayer
        // scopre 2 carte per ogni deck
        // distribuisce casualmente le starter card e le piazza
        // sul game field (chiedendo al player il verso)
    }

    /**
     * Method telling if there are available places in the game.
     * @return true if no other player can connect to the game
     */
    public boolean isFull(){
        return playersPosition.size() == playersNumber;
    }

    public void disconnectPlayer(Player player){
        //TODO: disconnect the player
    }

    public void reconnectPlayer(Player player){
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

    public void placeCard(PlaceableCard card, int x, int y, boolean way) {
        // TODO
        // posso aggiungerla solo al currentPlayer
        // qualcuno deve assicurarsi che solo il current player giochi
        // chiama placeCard sul gamefield del current player
        // rimuove la carta dalla currentHand
        // chiama il metodo addPoints che aggiunge i punti al giocatore
    }

    private void addPoints(int x, int y) {
        // TODO
        // il player è ancora il current player, devo passarglielo?
        // la card è già stata piazzata
        // conoscendo x e y posso verificare quanti punti ha fatto e
        // aggiornare il punteggio su scoreTrackBoard
        // se il punteggio è >= 20 (?) lastTurn = true
    }

    private Player computeWinner() {
        // TODO
        // Considerando le carte obiettivo finale comuni e le
        // carte obiettivo segrete, calcola il punteggio finale
        // per tutti i giocatori
        // se parità, ... (vedi regole)
        // restituisce il player vincitore,
        // in caso di parità ??
    }

    // TODO
    // metodi per pescare una carta
    // metodi per rivelare le carte scoperte
    // uno per ogni tipo di carta oppure prendono un tipo di carta?
    // Nel metodo per pescare una carta, alla fine si chiama changeCurrPlayer

    public void drawDeckCard(CardType type) throws WrongCardTypeException, CardNotPresentException {
        if(type.equals(CardType.OBJECTIVE_CARD) || type.equals(CardType.STARTER_CARD)) {
            throw new WrongCardTypeException();
        }
        if(type.equals(CardType.RESOURCE_CARD)){
            // return resourceCardsDeck.drawCard();
        }
        // return goldCardsDeck.drawCard();
        // TODO
        // modifica currentHand, aggiungendo la carta pescata (setCurrenHand di Player)
    }

    public void drawFaceUpCard(CardType type, int pos) throws WrongCardTypeException, CardNotPresentException {
        // TODO
        // se starter o objective, eccezione
        // altrimenti chiamo drawFaceUpCard sul giusto deck
        // TODO
        // modifica currentHand, aggiungendo la carta pescata (setCurrenHand di Player)
    }

    public Card revealFaceUpCard(CardType type, int pos) throws WrongCardTypeException, CardNotPresentException {
        // TODO
        // se starter, eccezione
        // altrimenti chiamo revealFaceUpCard sul giusto deck
    }

    public GameResource revealBackDeckCard(CardType type) throws WrongCardTypeException, CardNotPresentException {
        // TODO
        // se starter o objective, eccezione
        // altrimenti chiamo revealBackDeckCard sul giusto deck
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
