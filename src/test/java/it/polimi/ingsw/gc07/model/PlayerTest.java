package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model_listeners.PlayerListener;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
        player = new Player(nickname, connectionType, interfaceType);
        player.setTokenColor(tokenColor);
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
        assertEquals(0, player.getSecretObjectives().size());
        assertNotNull(player.getCurrentHand());
        assertEquals(0, player.getCurrentHand().size());
    }

    @Test
    public void checkAddCardHand() {
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();

        assertEquals(0, player.getCurrentHand().size());
        player.addCardHand(resourceCardsDeck.drawCard());
        assertEquals(1, player.getCurrentHand().size());
        player.addCardHand(resourceCardsDeck.drawCard());
        assertEquals(2, player.getCurrentHand().size());
        List<DrawableCard> currentHand = player.getCurrentHand();
        DrawableCard card = null;
        card = resourceCardsDeck.drawCard();
        player.addCardHand(card);
        assertEquals(3, player.getCurrentHand().size());
        List<DrawableCard> newCurrentHand = player.getCurrentHand();
        for(DrawableCard c: currentHand){
            assertTrue(newCurrentHand.contains(c));
        }
        assertTrue(newCurrentHand.contains(card));
    }

    @Test
    public void removeAddCardHand() {
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();

        assertEquals(0, player.getCurrentHand().size());
        player.addCardHand(resourceCardsDeck.drawCard());
        assertEquals(1, player.getCurrentHand().size());
        player.addCardHand(resourceCardsDeck.drawCard());
        assertEquals(2, player.getCurrentHand().size());
        DrawableCard card = null;
        card = resourceCardsDeck.drawCard();
        player.addCardHand(card);
        assertEquals(3, player.getCurrentHand().size());
        List<DrawableCard> currentHand = player.getCurrentHand();
        player.removeCardHand(card);
        List<DrawableCard> newCurrentHand = player.getCurrentHand();
        assertEquals(2, player.getCurrentHand().size());
        for(DrawableCard c: currentHand){
            assertTrue(c.equals(card) || newCurrentHand.contains(c));
        }
    }
    @Test
    public void testListener() {
        PlayerListener listener1;
        try {
            listener1 = new RmiClient("P1", false,null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        player.addListener(listener1);
        assertNotNull(player.getListeners());
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        player.addCardHand(resourceCardsDeck.drawCard());
        player.removeListener(listener1);
        assertEquals(0,player.getListeners().size());
    }
    @Test
    public void testGetter()
    {
        Deck<PlaceableCard> starterDeck = DecksBuilder.buildStarterCardsDeck();
        starterDeck.shuffle();
        assertEquals("TestNickname",player.getNickname());
        assertNotNull(player.getGameField());
        player.setStarterCard(starterDeck.drawCard());
        assertNotNull(player.getStarterCard());
        assertEquals(tokenColor,player.getTokenColor());
        player.setFirst();
        assertTrue(player.isFirst());
        player.setConnectionType(false);
        player.setInterfaceType(false);
        assertFalse(player.getConnectionType());
        assertFalse(player.getInterfaceType());
        player.setIsStalled(true);
        assertTrue(player.getIsStalled());
        player.setIsConnected(true);
        assertTrue(player.isConnected());
        PlayingDeck<ObjectiveCard> objectiveDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveDeck.shuffle();
        ObjectiveCard objective = objectiveDeck.drawCard();
        List<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(objective);
        player.setSecretObjectives(objectives);
        assertEquals(player.getSecretObjectives().getFirst(),objective);
        player.placeCard(player.getStarterCard(),40,40,true);
        assertNotNull(player.getGameField().getPlacedCard(40,40));
    }
}