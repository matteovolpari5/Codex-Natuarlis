package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model.decks.Deck;
import java.util.List;

public class Game {
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

    // TODO
    public Game(){
        // quando lo costruisco? ad es, quando si connette il primo giocatore
        // come costrusisco tutte le condition, le carte, i deck?
    }
    // addPlayer ? -> dipende da come costruisco il Game

    // solo getter o anche setter -> attento a non fare sfuggire riferimenti
}
