package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.model.cards.*;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.enumerations.*;

import java.util.Stack;

/**
 * Class representing a deck of gold cards.
 */
public class GoldCardsDeck extends DrawableDeck<GoldCard> {
    /**
     * Constructor of GoldCardsDeck.
     * @param type cards type
     * @param content deck content
     */
    public GoldCardsDeck(CardType type, Stack<GoldCard> content) {
        super(type, content);
    }

    /**
     * Copy constructor of GoldCardsDeck.
     * @param existingDeck existing deck
     */
    public GoldCardsDeck(GoldCardsDeck existingDeck) {
        super(existingDeck);
    }

    /**
     * Method to reveal the game resource on the back of the card on top
     * of the deck.
     * @return game resource on the back of the card on top of the deck
     */
    public GameResource revealBackDeckCard() {
        try {
            return revealDeckCard().getPermanentResources().getFirst();
        }catch(CardNotPresentException e) {
            return null;
        }
    }
}