package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.model.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class representing decks used during the whole game.
 * Objective cards deck is PlayingDeck object.
 * @param <T> type of cards the deck is made of
 */
public class PlayingDeck<T> extends Deck<T> {
    /**
     * List containing face up cards that players can draw.
     */
    private List<T> faceUpCards;

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

    public void setUpDeck() {
        addFaceUpCard(drawCard());
        addFaceUpCard(drawCard());
    }

    /**
     * Setter method for face up cards.
     * @param faceUpCards list of face up cards
     */
    public void setFaceUpCards(List<T> faceUpCards) {
        this.faceUpCards = new ArrayList<>(faceUpCards);
    }

    /**
     * Getter method for face up cards.
     * @return face up cards of the deck
     */
    public List<T> getFaceUpCards() {
        return new ArrayList<>(faceUpCards);
    }

    /**
     * Method to add a face up card.
     * @param faceUpCard face up card to add
     */
    public void addFaceUpCard(T faceUpCard) {
        this.faceUpCards.add(faceUpCard);
    }

    /**
     * Method that removes a face up card.
     * Friendly, because it is used only by subclasses.
     * @param pos position of the face up card
     * @return removed card
     */
    T removeFaceUpCard(int pos) {
        assert(pos >= 0 && pos < faceUpCards.size()): "Index out of bound";
        return faceUpCards.remove(pos);
    }

    /**
     * Method that allows a player to see the card in position cardPos
     * without drawing it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    public T revealFaceUpCard(int cardPos) {
        if(cardPos >= faceUpCards.size()){
            return null;
        }
        return faceUpCards.get(cardPos);
    }
}
