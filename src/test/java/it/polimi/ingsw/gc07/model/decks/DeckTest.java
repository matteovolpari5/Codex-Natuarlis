package it.polimi.ingsw.gc07.model.decks;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck<PlaceableCard> deck;

    @BeforeEach
    void setUp() {
        deck = DecksBuilder.buildStarterCardsDeck();
    }

    @Test
    public void testDeckSizeAndContent() {
        assertNotNull(deck.getContent());
        assertEquals(6, deck.getContent().size());
        for(PlaceableCard c: deck.getContent()) {
            assertNotNull(c);
            assertEquals(CardType.STARTER_CARD, c.getType());
        }
    }

    @Test
    public void checkDraw() {
        PlaceableCard topCard = deck.getContent().peek();
        int size = deck.getContent().size();
        try {
            PlaceableCard drawedCard  = deck.drawCard();
            assertEquals(topCard, drawedCard);
            assertEquals(size-1, deck.getContent().size());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkDrawEmptyDeck() {
        try {
            PlaceableCard drawedCard  = deck.drawCard();
            drawedCard = deck.drawCard();
            drawedCard = deck.drawCard();
            drawedCard = deck.drawCard();
            drawedCard = deck.drawCard();
            drawedCard = deck.drawCard();
            assertThrows(CardNotPresentException.class,
                    () -> deck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testShuffle() {
        Deck<PlaceableCard> deckCopy = new Deck<>(deck);
        deck.shuffle();
        assertEquals(deckCopy.getContent().size(), deck.getContent().size());
        for(PlaceableCard card: deckCopy.getContent()) {
            assertTrue(deck.getContent().contains(card));
        }
    }
}