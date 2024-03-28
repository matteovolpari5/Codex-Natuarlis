package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.enumerations.CardType;

import java.util.Stack;

/**
 * Class representing decks used during the game from which a player can draw.
 * Resource cards deck and gold cards deck are a DrawableDeck object.
 */
public abstract class DrawableDeck<T> extends PlayingDeck<T> {
    /**
     * Constructor of DrawableDeck
     * @param type cards type
     * @param content deck content
     */
    public DrawableDeck(CardType type, Stack<T> content) {
        super(type, content);
    }

    /**
     * Copy constructor of PlayingDeck.
     * @param existingDeck existing deck
     */
    public DrawableDeck(DrawableDeck<T> existingDeck){
        super(existingDeck);
    }

    /**
     * Method that allows a player to draw the card in position cardPos
     * from the two revealed cards.
     * This method also draws a card from the top of the deck and places it face up.
     * @param cardPos position of the card the player wants to draw
     * @return face up card in position cardPos
     */
    public T drawFaceUpCard(int cardPos) throws IndexOutOfBoundsException, CardNotPresentException {
        if(cardPos < 0 || cardPos > 1){
            throw new IndexOutOfBoundsException();
        }
        if(faceUpCards.get(cardPos) == null){
            throw new CardNotPresentException();
        }
        // Save the card to return
        T resultCard = faceUpCards.remove(cardPos);
        // Substitute the face up card
        try{
            faceUpCards.add(this.drawCard());
        }
        catch(CardNotPresentException e){
            // Deck is empty, face up card cannot be replaced
            // TODO Margara ha detto che dovrei sostituire con una carta dell'altro mazzo
        }
        // Card is immutable, I can return it
        return resultCard;
    }

    /**
     * Returns the first covered card of the deck, without removing it.
     * @return first covered card of the deck
     */
    T revealDeckCard() throws CardNotPresentException {
        if(content.empty()){
            throw new CardNotPresentException();
        }
        return content.peek();
    }
}
