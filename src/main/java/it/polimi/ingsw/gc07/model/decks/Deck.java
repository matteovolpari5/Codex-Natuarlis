package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Class representing a collection of cards.
 * Starter cards deck is a Deck object.
 */
public class Deck {
    /**
     * Attribute representing the deck type, which
     * corresponds to the card type of all cards contained in the deck.
     */
    final CardType type;

    /**
     *  Stack containing cards currently present in the deck.
     *  Cards drawn by the player are removed from the deck.
     */
    Stack<Card> content;

    /**
     * Constructor class Deck.
     * @param type type of the deck
     * @param content Stack containing deck cards
     */
    public Deck(CardType type, Stack<Card> content) {
        this.type = type;
        this.content = new Stack<>();
        this.content.addAll(content);
    }

    /**
     * Return the card type of all cards in the deck.
     * @return card type of the deck
     */
    public CardType getType() {
        return this.type;
    }

    /**
     * Method that allows a player to draw the first card from the deck.
     * At the beginning of the game, players can draw also from
     * objective cards and starter cards deck.
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
}
