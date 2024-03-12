package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.enumerations.CardType;

import java.util.Stack;

/**
 * Class representing decks used during the whole game.
 * Objective cards deck is PlayingDeck object.
 */
public class PlayingDeck extends Deck {
    /**
     * Array containing the two face up cards that players can draw.
     */
    Card[] faceUpCards;

    /**
     * Constructor class PlayingDeck.
     * @param type type of the deck
     * @param content Stack containing deck cards
     * @param faceUpCards Array containing the two revealed cards
     */
    public PlayingDeck(CardType type, Stack<Card> content, Card[] faceUpCards) {
        super(type, content);
        // Cards are immutable, I can insert the card I receive
        this.faceUpCards = new Card[2];
        this.faceUpCards[0] = faceUpCards[0];
        this.faceUpCards[1] = faceUpCards[1];
    }

    /**
     * Method that allows a player to see the card in position cardPos
     * without drawing it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    public Card revealFaceUpCard(int cardPos) throws IndexOutOfBoundsException, CardNotPresentException {
        if(cardPos < 0 || cardPos > 1){
            throw new IndexOutOfBoundsException();
        }
        if(faceUpCards[cardPos] == null){
            throw new CardNotPresentException();
        }
        return faceUpCards[cardPos];
    }
}
