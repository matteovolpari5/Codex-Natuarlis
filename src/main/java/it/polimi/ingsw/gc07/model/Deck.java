package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exception.*;
import java.util.EmptyStackException;
import java.util.Stack;

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
    private Stack<Card> deckContent;
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
    public Deck(CardType deckType, Stack<Card> deckContent, Card[] faceUpCards) {
        this.deckType = deckType;
        this.deckContent = new Stack<>();
        this.deckContent.addAll(deckContent);
        // Per crearere i deck, devono tenere conto di deckType!
        // TODO: modificare il costruttore!
        this.faceUpCards = new Card[2];
        // this.faceUpCards[0] = new
        // this.faceUpCards[1] = new
    }

    /**
     * Return the card type of all cards in the deck.
     * @return card type of the deck
     */
    public CardType getDeckType() {
        return this.deckType;
    }

    /**
     * Method that allows a player to draw the first card from the deck
     * @return first card of the deck
     */
    public Card drawDeckCard() throws CardNotPresentException {
        try {
            return this.deckContent.pop();
        }
        catch(EmptyStackException e){
            throw new CardNotPresentException();
        }
    }

    /**
     * Method that allows a player to draw the card in position cardPos
     * from the two revealed cards.
     * @param cardPos position of the card the player wants to draw
     * @return face up card in position cardPos
     */
    // TODO
    public PlaceableCard drawFaceUpCard(int cardPos){}

    /**
     * Method that allows a player to see the card in position cardPos
     * without necessarily having to draw it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    // TODO
    public Card revealFaceUpCard(int cardPos){}

    /**
     * Method that allows the player to see the color (i.e. the permanent resource)
     * present on the back of the first covered card of the deck.
     * @return permanent resource on the back of the first covered card of the deck
     */
    // TODO
    public GameResource revealBackDeckCard(){
    }
}
