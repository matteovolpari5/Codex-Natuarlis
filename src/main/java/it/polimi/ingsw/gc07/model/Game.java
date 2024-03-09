package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.model.decks.Deck;

import java.util.List;

public class Game {
    /**
     * List of players in the game.
     */
    private List<Player> players;
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
        // come costrusisco tutte le condition, le carte, i deck?
    }
}
