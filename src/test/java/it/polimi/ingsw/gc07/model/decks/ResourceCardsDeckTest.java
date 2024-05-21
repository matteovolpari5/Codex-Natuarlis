package it.polimi.ingsw.gc07.model.decks;

import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardsDeckTest {
    DrawableDeck<DrawableCard> deck;
    @BeforeEach
    void setUp() {
        deck = DecksBuilder.buildResourceCardsDeck();
    }

    @Test
    public void testDrawFaceUpCards() {
        DrawableCard card1 = deck.drawCard();
        DrawableCard card2 = deck.drawCard();
        List<DrawableCard> list = new ArrayList<>();
        list.add(card1);
        list.add(card2);
        deck.setFaceUpCards(list);
        assertEquals(card1, deck.drawFaceUpCard(0));
        // after removing card1, card2 is in position 0
        assertEquals(card2, deck.drawFaceUpCard(0));
    }
    @Test
    public void testReveal() {
        DrawableDeck<DrawableCard> deck1 = new DrawableDeck<>(deck);
        DrawableCard card1 = deck.drawFaceUpCard(-1);
        assertNull(card1);
        card1 = deck.drawFaceUpCard(4);
        assertNull(card1);
        card1 = deck.revealTopCard();
        assertNotNull(card1);
        DrawableCard card2 = deck.drawCard();
        while(deck.drawCard()!=null) {  }
        card1 = deck.revealTopCard();
        assertNull(card1);
        List<DrawableCard> list = new ArrayList<>();
        list.add(card2);
        deck.setFaceUpCards(list);
        card1 = deck.drawFaceUpCard(0);
    }
}