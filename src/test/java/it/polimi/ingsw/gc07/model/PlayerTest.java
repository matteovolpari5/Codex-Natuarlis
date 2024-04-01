package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = null;
    String nickname = null;
    TokenColor tokenColor = null;
    boolean connectionType;
    boolean interfaceType;

    @BeforeEach
    void setUp() {
        // create a test player
        nickname = "TestNickname";
        tokenColor = TokenColor.RED;
        connectionType = true;
        interfaceType = true;
        player = new Player(nickname, tokenColor, connectionType, interfaceType);
    }

    @Test
    public void checkInitAttributes() {
        assertEquals(nickname, player.getNickname());
        assertEquals(tokenColor, player.getTokenColor());
        assertFalse(player.isFirst());
        assertEquals(connectionType, player.getConnectionType());
        assertEquals(interfaceType, player.getInterfaceType());
        assertTrue(player.isConnected());
        assertFalse(player.getIsStalled());
        assertNull(player.getSecretObjective());
        assertNotNull(player.getCurrentHand());
        assertEquals(0, player.getCurrentHand().size());
    }

    @Test
    public void checkAddCardHand() {
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();

        assertEquals(0, player.getCurrentHand().size());
        try {
            player.addCardHand(resourceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, player.getCurrentHand().size());
        try {
            player.addCardHand(resourceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, player.getCurrentHand().size());
        List<DrawableCard> currentHand = player.getCurrentHand();
        DrawableCard card = null;
        try {
            card = resourceCardsDeck.drawCard();
            player.addCardHand(card);
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3, player.getCurrentHand().size());
        List<DrawableCard> newCurrentHand = player.getCurrentHand();
        for(DrawableCard c: currentHand){
            assertTrue(newCurrentHand.contains(c));
        }
        assertTrue(newCurrentHand.contains(card));
    }

    @Test
    public void removeAddCardHand() {
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();

        assertEquals(0, player.getCurrentHand().size());
        try {
            player.addCardHand(resourceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, player.getCurrentHand().size());
        try {
            player.addCardHand(resourceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, player.getCurrentHand().size());
        DrawableCard card = null;
        try {
            card = resourceCardsDeck.drawCard();
            player.addCardHand(card);
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3, player.getCurrentHand().size());
        List<DrawableCard> currentHand = player.getCurrentHand();
        player.removeCardHand(card);
        List<DrawableCard> newCurrentHand = player.getCurrentHand();
        assertEquals(2, player.getCurrentHand().size());
        for(DrawableCard c: currentHand){
            assertTrue(c.equals(card) || newCurrentHand.contains(c));
        }
    }
}