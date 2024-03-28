package it.polimi.ingsw.gc07.model.decks;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardsDeckTest {
    ResourceCardsDeck deck;
    @BeforeEach
    void setUp() {
        deck = DecksBuilder.buildResourceCardsDeck();
    }

    @Test
    public void testDrawFaceUpCards() {
        try {
            DrawableCard card1 = deck.drawCard();
            DrawableCard card2 = deck.drawCard();
            List<DrawableCard> list = new ArrayList<>();
            list.add(card1);
            list.add(card2);
            deck.setFaceUpCards(list);
            assertEquals(card1, deck.drawFaceUpCard(0));
            // after removing card1, card2 is in position 0
            assertEquals(card2, deck.drawFaceUpCard(0));
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
    }
}