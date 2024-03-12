package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.cards.NonStarterCard;

import java.util.List;
import java.util.Stack;

/**
 * Class representing decks used during the game from which a player can draw.
 * Resource cards deck and gold cards deck are a DrawableDeck object.
 */
public class DrawableDeck<T> extends PlayingDeck<T> {
    /**
     * Constructor of DrawableDeck
     * @param type type of deck, i.e. type of cards contained
     * @param content Stack containing deck cards
     * @param faceUpCards Array containing the two revealed cards
     */
    public DrawableDeck(CardType type, Stack<T> content, List<T> faceUpCards) {
        super(type, content, faceUpCards);
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
            e.printStackTrace();
        }
        // Card is immutable, I can return it
        return resultCard;
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
        return ((NonStarterCard)(content.peek())).getPermanentResource();
    }
}
