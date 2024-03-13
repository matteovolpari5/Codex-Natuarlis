package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Class representing a collection of cards.
 * Starter cards deck is a Deck object.
 */
public class Deck<T> {
    /**
     * Attribute representing the deck type, which
     * corresponds to the card type of all cards contained in the deck.
     */
    CardType type;

    /**
     *  Stack containing cards currently present in the deck.
     *  Cards drawn by the player are removed from the deck.
     */
    Stack<T> content;

    /**
     * Constructor class Deck.
     */
    public Deck() {
        this.type = null;
        this.content = null;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    /**
     * Return the card type of all cards in the deck.
     * @return card type of the deck
     */
    public CardType getType() {
        return this.type;
    }

    public void setContent(Stack<T> content) {
        this.content = new Stack<>();
        this.content.addAll(content);
    }

    /**
     * Method that allows a player to draw the first card from the deck.
     * At the beginning of the game, players can draw also from
     * objective cards and starter cards deck.
     * @return first card of the deck
     */
    public T drawCard() throws CardNotPresentException {
        try {
            return this.content.pop();
        }
        catch(EmptyStackException e){
            throw new CardNotPresentException();
        }
    }
}
