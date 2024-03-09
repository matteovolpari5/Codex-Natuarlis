package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.CardType;

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
     * Array containing true if the face up card is present, false otherwise.
     */
    boolean[] faceUpCardsPresent;

    /**
     * Constructor class PlayingDeck.
     * @param type type of the deck
     * @param content Stack containing deck cards
     * @param faceUpCards Array containing the two revealed cards
     * @param faceUpCardsPresent Array containing true if a face up card is present
     */
    public PlayingDeck(CardType type, Stack<Card> content,
                       Card[] faceUpCards, boolean[] faceUpCardsPresent) {
        super(type, content);
        this.faceUpCardsPresent = new boolean[2];
        this.faceUpCardsPresent[0] = faceUpCardsPresent[0];
        this.faceUpCardsPresent[1] = faceUpCardsPresent[1];
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
    public Card revealFaceUpCard(int cardPos)throws IndexOutOfBoundsException, CardNotPresentException {
        if(cardPos < 0 || cardPos > 1){
            throw new IndexOutOfBoundsException();
        }
        if(!faceUpCardsPresent[cardPos]){
            throw new CardNotPresentException();
        }
        return faceUpCards[cardPos];
    }
}
