package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model.decks.Deck;

import java.util.ArrayList;
import java.util.List;

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
     * List of players in the game.
     */
    private List<Player> players;
    /**
     * Integer value representing the index of the current player in the List
     */
    private int currPlayer;
    /**
     * Score track board of the game.
     */
    private ScoreTrackBoard scoreTrackBoard;
    /**
     * Deck of resource cards.
     */
    private Deck resourceCardsDeck;
    /**
     * Deck of gold cards.
     */
    private Deck goldCardsDeck;
    /**
     * Deck of objective cards.
     */
    private Deck objectiveCardsDeck;
    /**
     * Deck of starter cards.
     */
    private Deck starterCardsDeck;

    /**
     * Constructor of a Game with only the first player.
     * @param gameId id of the game
     * @param playersNumber number of players
     * @param firstPlayer player who creates the game
     * @param resourceCardsDeck
     * @param goldCardsDeck
     * @param objectiveCardsDeck
     * @param starterCardsDeck
     */
    public Game(int gameId, int playersNumber, Player firstPlayer, Deck resourceCardsDeck,
                Deck goldCardsDeck, Deck objectiveCardsDeck, Deck starterCardsDeck) {
        this.gameId = gameId;
        // TODO: mettiamo un'eccezione per playersNumber?
        this.playersNumber = playersNumber;
        this.players = new ArrayList<>();
        this.players.add(new Player(firstPlayer));
        this.scoreTrackBoard = new ScoreTrackBoard();
        this.resourceCardsDeck = resourceCardsDeck;
        this.goldCardsDeck = goldCardsDeck;
        this.objectiveCardsDeck = objectiveCardsDeck;
        this.starterCardsDeck = starterCardsDeck;
    }

    /**
     * Method to add a new player.
     * @param player player to add to the game
     */
    public void addPlayer(Player player){
        // TODO
        // Inserisce un giocatore
        // Controlla se è ultimo, se sì, scegliere il primo giocatore a caso e
        // modifica il suo attributo isFirst e lo mette come currPlayer
    }

    /**
     * Method telling if there are available places in the game.
     * @return true if no other player can connect to the game
     */
    public boolean isFull(){
        return players.size() == playersNumber;
    }

    public void disconnectPlayer(Player player){
        //TODO: disconnect the player
    }

    public void reconnectPlayer(Player player){
        // TODO: reconnect the player
    }
}
