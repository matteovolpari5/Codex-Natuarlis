package it.polimi.ingsw.gc07.model.decks;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
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
        assertThrows(CardNotPresentException.class,
                () -> deck.revealFaceUpCard(0));
        try {
            ObjectiveCard card1 = deck.drawCard();
            ObjectiveCard card2 = deck.drawCard();
            List<ObjectiveCard> list = new ArrayList<>();
            list.add(card1);
            list.add(card2);
            deck.setFaceUpCards(list);
            assertEquals(card1, deck.revealFaceUpCard(0));
            assertEquals(card2, deck.revealFaceUpCard(1));
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
    }
}