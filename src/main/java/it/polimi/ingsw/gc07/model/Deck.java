package it.polimi.ingsw.gc07.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Class representing a collection of cards: every game has 4 decks,
 * one for each cardType.
 */
public class Deck {
    /**
     * Attribute representing the deck type, which
     * corresponds to the card type of all cards contained in the deck.
     */
    private final CardType deckType;
    /**
     * List containing the cards currently present in the deck.
     * Cards drawn by player are removed from the deck.
     */
    private List<Card> deckContent;
    /**
     * Array containing the two face up cards that players can draw.
     */
    private Card[] faceUpCards;
    // TODO: aggiungere array di boolean per evitare di avere null?

    /**
     * Constructor of the deck.
     * @param deckType type of the deck
     * @param deckContent List containing deck cards
     * @param faceUpCards Array containing the two revealed cards
     */
    public Deck(CardType deckType, List<Card> deckContent, Card[] faceUpCards) {
        this.deckType = deckType;
        // Per crearere i deck, devono tenere conto di deckType!
        // TODO: modificare il costruttore!
        this.deckContent = new ArrayList<Card>(deckContent);
        this.faceUpCards = new Card[2];
        // this.faceUpCards[0] = new
        // this.faceUpCards[1] = new
    }

    /**
     * Method that allows a player to draw the first card from the deck
     * @return first card of the deck
     */
    public PlaceableCard drawDeckCard(){}

    /**
     * Method that allows a player to draw the card in position cardPos
     * from the two revealed cards.
     * @param cardPos position of the card the player wants to draw
     * @return face up card in position cardPos
     */
    public PlaceableCard drawFaceUpCard(int cardPos){}

    /**
     * Method that allows a player to see the card in position cardPos
     * without necessarily having to draw it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    public Card revealFaceUpCard(int cardPos){}

    /**
     * Method that allows the player to see the color (i.e. the permanent resource)
     * present on the back of the first covered card of the deck.
     * @return permanent resource on the back of the first covered card of the deck
     */
    public GameResource revealBackDeckCard(){}
}
