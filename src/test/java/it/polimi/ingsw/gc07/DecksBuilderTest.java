package it.polimi.ingsw.gc07;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.enumerations.GameObject;
import it.polimi.ingsw.gc07.enumerations.GameResource;
import it.polimi.ingsw.gc07.utils.DecksBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecksBuilderTest {
    @Test
    void checkStarterCardsDeckDimension() {
        int deckDim = 6;
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        assertEquals(deckDim, starterCardsDeck.getContent().size());
    }

    @Test
    void checkObjectiveCardsDeckDimension() {
        int deckDim = 16;
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        assertEquals(deckDim, objectiveCardDeck.getContent().size());
    }

    @Test
    void checkResourceCardsDeckDimension() {
        int deckDim = 40;
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        assertEquals(deckDim, resourceCardsDeck.getContent().size());
    }

    @Test
    void checkGoldCardsDeckDimension() {
        int deckDim = 40;
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        assertEquals(deckDim, goldCardsDeck.getContent().size());
    }

    @Test
    void checkStarterCardContent() {
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        PlaceableCard card = null;
        for(PlaceableCard c:  starterCardsDeck.getContent()) {
            if(c.getId() == 81) {
                card = c;
            }
        }
        assertNotNull(card);

        assertTrue(card.getBackCorners()[0]);
        assertTrue(card.getBackCorners()[1]);
        assertTrue(card.getBackCorners()[2]);
        assertTrue(card.getBackCorners()[3]);

        assertNull(card.getBackCornersContent()[0]);
        assertEquals(GameResource.PLANT, card.getBackCornersContent()[1]);
        assertNull(card.getBackCornersContent()[2]);
        assertEquals(GameResource.INSECT, card.getBackCornersContent()[3]);

        assertTrue(card.getFrontCorners()[0]);
        assertTrue(card.getFrontCorners()[1]);
        assertTrue(card.getFrontCorners()[2]);
        assertTrue(card.getFrontCorners()[3]);

        assertEquals(GameResource.FUNGI, card.getFrontCornersContent()[0]);
        assertEquals(GameResource.PLANT, card.getFrontCornersContent()[1]);
        assertEquals(GameResource.ANIMAL, card.getFrontCornersContent()[2]);
        assertEquals(GameResource.INSECT, card.getFrontCornersContent()[3]);
    }

    @Test
    void checkObjectiveCardContent() {
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        ObjectiveCard card = null;
        for(ObjectiveCard c:  objectiveCardDeck.getContent()) {
            if(c.getId() == 87) {
                card = c;
            }
        }
        assertNotNull(card);
    }

    @Test
    void checkResourceCardContent() {
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        DrawableCard card = null;
        for(DrawableCard c:  resourceCardsDeck.getContent()) {
            if(c.getId() == 1) {
                card = c;
            }
        }
        assertNotNull(card);

        assertTrue(card.getBackCorners()[0]);
        assertTrue(card.getBackCorners()[1]);
        assertTrue(card.getBackCorners()[2]);
        assertTrue(card.getBackCorners()[3]);

        assertNull(card.getBackCornersContent()[0]);
        assertNull(card.getBackCornersContent()[1]);
        assertNull(card.getBackCornersContent()[2]);
        assertNull(card.getBackCornersContent()[3]);

        assertTrue(card.getFrontCorners()[0]);
        assertTrue(card.getFrontCorners()[1]);
        assertFalse(card.getFrontCorners()[2]);
        assertTrue(card.getFrontCorners()[3]);

        assertEquals(GameResource.FUNGI, card.getFrontCornersContent()[0]);
        assertNull(card.getFrontCornersContent()[1]);
        assertNull(card.getFrontCornersContent()[2]);
        assertEquals(GameResource.FUNGI, card.getFrontCornersContent()[3]);
    }

    @Test
    void checkGoldCardContent() {
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        GoldCard card = null;
        for(GoldCard c: goldCardsDeck.getContent()) {
            if(c.getId() == 41) {
                card = c;
            }
        }
        assertNotNull(card);

        assertTrue(card.getBackCorners()[0]);
        assertTrue(card.getBackCorners()[1]);
        assertTrue(card.getBackCorners()[2]);
        assertTrue(card.getBackCorners()[3]);

        assertNull(card.getBackCornersContent()[0]);
        assertNull(card.getBackCornersContent()[1]);
        assertNull(card.getBackCornersContent()[2]);
        assertNull(card.getBackCornersContent()[3]);

        assertFalse(card.getFrontCorners()[0]);
        assertTrue(card.getFrontCorners()[1]);
        assertTrue(card.getFrontCorners()[2]);
        assertTrue(card.getFrontCorners()[3]);

        assertNull(card.getFrontCornersContent()[0]);
        assertNull(card.getFrontCornersContent()[1]);
        assertEquals(GameObject.QUILL, card.getFrontCornersContent()[2]);
        assertNull(card.getFrontCornersContent()[3]);
    }
}