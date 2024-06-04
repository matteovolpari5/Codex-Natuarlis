package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.model.CardType;

import java.util.*;

/**
 * Class representing a collection of cards.
 * Starter cards deck is a Deck object.
 */
public class Deck<T> {
    /**
     * Attribute representing the deck type, which
     * corresponds to the card type of all cards contained in the deck.
     */
    private final CardType type;
    /**
     *  Stack containing cards currently present in the deck.
     *  Cards drawn by the player are removed from the deck.
     */
    private final Stack<T> content;

    /**
     *  Constructor class Deck.
     * @param type type of cards contained
     * @param content content of the deck
     */
    public Deck(CardType type, Stack<T> content) {
        this.type = type;
        this.content = new Stack<>();
        this.content.addAll(content);
    }

    /**
     * Copy constructor of class Deck.
     * @param existingDeck deck to copy
     */
    public Deck(Deck<T> existingDeck) {
        this.type = existingDeck.type;
        this.content = new Stack<>();
        this.content.addAll(existingDeck.content);
    }

    /**
     * Getter method for deck content, used in tests.
     * @return content of the deck
     */
    public Stack<T> getContent() {
        Stack<T> contentCopy = new Stack<>();
        contentCopy.addAll(content);
        return contentCopy;
    }

    /**
     * Method that allows a player to draw the first card from the deck.
     * At the beginning of the game, players can draw also from
     * objective cards and starter cards deck.
     * @return first card of the deck
     */
    public T drawCard() {
        try {
            return this.content.pop();
        }
        catch(EmptyStackException e){
            return null;
        }
    }

    /**
     * Method to shuffle the deck after it has been created from the JSON file.
     */
    public void shuffle() {
        List<T> contentList = new ArrayList<>(content);
        Collections.shuffle(contentList);
        content.clear();
        for(T card: contentList){
            content.push(card);
        }
    }
}
