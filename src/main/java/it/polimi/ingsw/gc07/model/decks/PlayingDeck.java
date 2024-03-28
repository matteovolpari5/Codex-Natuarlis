package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
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
     * @param type cards type
     * @param content deck content
     */
    public PlayingDeck(CardType type, Stack<T> content) {
        super(type, content);
        this.faceUpCards = new ArrayList<>();
    }

    /**
     * Copy constructor of PlayingDeck.
     * @param existingDeck existing deck
     */
    public PlayingDeck(PlayingDeck<T> existingDeck){
        super(existingDeck);
        this.faceUpCards = new ArrayList<>(existingDeck.faceUpCards);
    }

    /**
     * Setter method for face up cards.
     * @param faceUpCards list of face up cards
     */
    public void setFaceUpCards(List<T> faceUpCards) {
        this.faceUpCards = new ArrayList<>(faceUpCards);
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
