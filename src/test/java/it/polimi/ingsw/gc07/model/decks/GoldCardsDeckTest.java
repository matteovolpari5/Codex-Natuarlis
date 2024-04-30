package it.polimi.ingsw.gc07.model.decks;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardsDeckTest {
    private DrawableDeck<GoldCard> deck;
    @BeforeEach
    void setUp() {
        deck = DecksBuilder.buildGoldCardsDeck();
    }

    @Test
    public void testDrawFaceUpCards() {
        GoldCard card1 = deck.drawCard();
        GoldCard card2 = deck.drawCard();
        List<GoldCard> list = new ArrayList<>();
        list.add(card1);
        list.add(card2);
        deck.setFaceUpCards(list);
        assertEquals(card1, deck.drawFaceUpCard(0));
        // after removing card1, card2 is in position 0
        assertEquals(card2, deck.drawFaceUpCard(0));
    }
}