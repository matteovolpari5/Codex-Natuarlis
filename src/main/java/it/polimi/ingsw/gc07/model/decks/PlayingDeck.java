package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.enumerations.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class representing decks used during the whole game.
 * Objective cards deck is PlayingDeck object.
 */
public class PlayingDeck<T> extends Deck<T> {
    /**
     * Array containing the two face up cards that players can draw.
     */
    List<T> faceUpCards;

    /**
     * Constructor class PlayingDeck.
     * @param type type of the deck
     * @param content Stack containing deck cards
     * @param faceUpCards Array containing the two revealed cards
     */
    public PlayingDeck(CardType type, Stack<T> content, List<T> faceUpCards) {
        super(type, content);
        // Cards are immutable, I can insert the card I receive
        this.faceUpCards = new ArrayList<>();
        this.faceUpCards.add(faceUpCards.get(0));
        this.faceUpCards.add(faceUpCards.get(1));
    }

    /**
     * Method that allows a player to see the card in position cardPos
     * without drawing it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    public T revealFaceUpCard(int cardPos) throws IndexOutOfBoundsException, CardNotPresentException {
        if(cardPos >= faceUpCards.size()){
            throw new IndexOutOfBoundsException();
        }
        if(faceUpCards.get(cardPos) == null){
            throw new CardNotPresentException();
        }
        return faceUpCards.get(cardPos);
    }
}
