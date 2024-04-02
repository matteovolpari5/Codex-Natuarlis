package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.*;

import java.util.Stack;

/**
 * Class representing a deck of resource cards.
 */
public class ResourceCardsDeck extends DrawableDeck<DrawableCard> {
    /**
     * Constructor of ResourceCardsDeck.
     * @param type cards type
     * @param content deck content
     */
    public ResourceCardsDeck(CardType type, Stack<DrawableCard> content) {
        super(type, content);
    }

    /**
     * Copy constructor of ResourceCardsDeck.
     * @param existingDeck existing deck
     */
    public ResourceCardsDeck(ResourceCardsDeck existingDeck){
        super(existingDeck);
    }

    /**
     * Method to reveal the game resource on the back of the card on top
     * of the deck.
     * @return game resource on the back of the card on top of the deck
     * @throws CardNotPresentException thrown if the deck is empty
     */
    public GameResource revealBackDeckCard() throws CardNotPresentException {
        return revealDeckCard().getPermanentResources().getFirst();
    }
}
