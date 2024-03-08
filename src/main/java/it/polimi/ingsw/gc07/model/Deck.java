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
    private final CardType type;
    /**
     * List containing the cards currently present in the deck.
     * Cards drawn by player are removed from the deck.
     */
    private Stack<Card> content;
    /**
     * Array containing the two face up cards that players can draw.
     */
    private Card[] faceUpCards;
    // TODO: aggiungere array di boolean per evitare di avere null?

    /**
     * Constructor of the deck.
     * @param type type of the deck
     * @param content List containing deck cards
     * @param faceUpCards Array containing the two revealed cards
     */
    public Deck(CardType type, Stack<Card> content, Card[] faceUpCards) {
        this.type = type;
        this.content = new Stack<>();
        this.content.addAll(content);
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
    public CardType getType() {
        return this.type;
    }

    /**
     * Method that allows a player to draw the first card from the deck
     * @return first card of the deck
     */
    public Card drawCard() throws CardNotPresentException {
        try {
            return this.content.pop();
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
    public PlaceableCard drawFaceUpCard(int cardPos){
        return null;
    }

    /**
     * Method that allows a player to see the card in position cardPos
     * without necessarily having to draw it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    // TODO
    public Card revealFaceUpCard(int cardPos){
        return null;
    }

    /**
     * Method that allows the player to see the color (i.e. the permanent resource)
     * present on the back of the first covered card of the deck.
     * @return permanent resource on the back of the first covered card of the deck
     */
    // TODO
    public GameResource revealBackCard(){
        return null;
    }
}
