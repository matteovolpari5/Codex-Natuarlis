package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.*;
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
    /**
     * Array containing true if the face up card is present, false otherwise.
     */
    private boolean[] faceUpCardsPresent;

    /**
     * Constructor of the deck.
     * @param type type of the deck
     * @param content List containing deck cards
     * @param faceUpCards Array containing the two revealed cards
     * @param faceUpCardsPresent Array containing true if a face up card is present
     */
    public Deck(CardType type, Stack<Card> content, Card[] faceUpCards, boolean[] faceUpCardsPresent) {
        this.type = type;
        this.content = new Stack<>();
        this.content.addAll(content);
        this.faceUpCardsPresent = new boolean[2];
        this.faceUpCardsPresent[0] = faceUpCardsPresent[0];
        this.faceUpCardsPresent[1] = faceUpCardsPresent[1];
        // Cards are immutable, I can insert the card I receive
        this.faceUpCards = new Card[2];
        this.faceUpCards[0] = faceUpCards[0];
        this.faceUpCards[1] = faceUpCards[1];
    }

    /**
     * Return the card type of all cards in the deck.
     * @return card type of the deck
     */
    public CardType getDeckType() {
        return this.type;
    }

    /**
     * Method that allows a player to draw the first card from the deck
     * @return first card of the deck
     */
    public Card drawDeckCard() throws CardNotPresentException {
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
     * This method also draws a card from the top of the deck and places it face up.
     * @param cardPos position of the card the player wants to draw
     * @return face up card in position cardPos
     */
    public Card drawFaceUpCard(int cardPos) throws IndexOutOfBoundsException, CardNotPresentException {
        if(cardPos < 0 || cardPos > 1){
            throw new IndexOutOfBoundsException();
        }
        if(!faceUpCardsPresent[cardPos]){
            throw new CardNotPresentException();
        }
        // Save the card to return
        Card resultCard = faceUpCards[cardPos];
        // Substitute the face up card
        try{
            faceUpCards[cardPos] = this.drawDeckCard();
            // faceUpCardsPresent[cardPos] remains true
        }
        catch(CardNotPresentException e){
            // Deck is empty, face up card cannot be replaced
            faceUpCardsPresent[cardPos] = false;
            faceUpCards[cardPos] = null;
        }
        // Card is immutable, I can return it
        return resultCard;
    }

    /**
     * Method that allows a player to see the card in position cardPos
     * without drawing it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    public Card revealFaceUpCard(int cardPos)throws IndexOutOfBoundsException, CardNotPresentException {
        if(cardPos < 0 || cardPos > 1){
            throw new IndexOutOfBoundsException();
        }
        if(!faceUpCardsPresent[cardPos]){
            throw new CardNotPresentException();
        }
        return faceUpCards[cardPos];
    }

    /**
     * Method that allows the player to see the color (i.e. the permanent resource)
     * present on the back of the first covered card of the deck.
     * @return permanent resource on the back of the first covered card of the deck
     */
    public GameResource revealBackDeckCard() throws CardNotPresentException {
        if(content.empty()){
            throw new CardNotPresentException();
        }
        // TODO:cambia!
        return GameResource.ANIMAL;
    }
}
