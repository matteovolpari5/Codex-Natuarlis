package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    public void checkInitAttributes() {
        // create a test player
        String nickname = "TestNickname";
        TokenColor tokenColor = TokenColor.RED;
        boolean connectionType = true;
        boolean interfaceType = true;
        Player player = new Player(nickname, tokenColor, connectionType, interfaceType);
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
        ResourceCardsDeck reosurceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        reosurceCardsDeck.shuffle();

        // create a test player
        String nickname = "TestNickname";
        TokenColor tokenColor = TokenColor.RED;
        boolean connectionType = true;
        boolean interfaceType = true;
        Player player = new Player(nickname, tokenColor, connectionType, interfaceType);

        assertEquals(0, player.getCurrentHand().size());
        try {
            player.addCardHand(reosurceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, player.getCurrentHand().size());
        try {
            player.addCardHand(reosurceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, player.getCurrentHand().size());
        List<DrawableCard> currentHand = player.getCurrentHand();
        DrawableCard card = null;
        try {
            card = reosurceCardsDeck.drawCard();
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
        ResourceCardsDeck reosurceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        reosurceCardsDeck.shuffle();

        // create a test player
        String nickname = "TestNickname";
        TokenColor tokenColor = TokenColor.RED;
        boolean connectionType = true;
        boolean interfaceType = true;
        Player player = new Player(nickname, tokenColor, connectionType, interfaceType);

        assertEquals(0, player.getCurrentHand().size());
        try {
            player.addCardHand(reosurceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, player.getCurrentHand().size());
        try {
            player.addCardHand(reosurceCardsDeck.drawCard());
        } catch (CardNotPresentException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, player.getCurrentHand().size());
        DrawableCard card = null;
        try {
            card = reosurceCardsDeck.drawCard();
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