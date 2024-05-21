package it.polimi.ingsw.gc07.model.decks;

import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayingDeckTest {
    PlayingDeck<ObjectiveCard> deck;

    @BeforeEach
    void setUp() {
        deck = DecksBuilder.buildObjectiveCardsDeck();
    }

    @Test
    public void faceUpCards(){
        assertNull(deck.revealFaceUpCard(0));
        ObjectiveCard card1 = deck.drawCard();
        ObjectiveCard card2 = deck.drawCard();
        List<ObjectiveCard> list = new ArrayList<>();
        list.add(card1);
        list.add(card2);
        deck.setFaceUpCards(list);
        assertEquals(card1, deck.revealFaceUpCard(0));
        assertEquals(card2, deck.revealFaceUpCard(1));
    }
    @Test
    public void testSetUpDeck(){
        PlayingDeck<ObjectiveCard> deck1 = new PlayingDeck<>(deck);
        deck1.setUpDeck();
        assertNotNull(deck1.getFaceUpCards());
        ObjectiveCard card1 = deck1.drawCard();
        ObjectiveCard card2 = deck1.drawCard();
        deck1.removeFaceUpCard(0);
        assertNotNull(deck1.removeFaceUpCard(0));
        deck1.setUpDeck();
        deck1.setFaceUpCards(deck1.getFaceUpCards());
        assertNotNull(deck1.getFaceUpCards());
        assertNotNull(deck1.revealFaceUpCard(0));
    }
}