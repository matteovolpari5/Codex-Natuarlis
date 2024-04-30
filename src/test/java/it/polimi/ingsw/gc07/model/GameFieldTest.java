package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;
    DrawableCard myGoldCard;
    Player p;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        p = new Player("p", true, true);
    }

    @Test
    void placeCard() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        assertEquals(p.getGameField().getPlacedCard(40, 40), myStarterCard);
        myResourceCard = resourceCardsDeck.drawCard();
        p.placeCard(myResourceCard, 40, 41, true);
        assertNull(p.getGameField().getPlacedCard(40, 41));
    }

    @Test
    void isCardPresent() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 15){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, false);
            }
            if (c.getId() == 18){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 39){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, false);
            }
        }
        assertTrue(p.getGameField().isCardPresent(41, 41));
    }

    @Test
    void getPlacedCard() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 11){
                p.placeCard(c, 41, 41, false);
            }
            if (c.getId() == 10){
                p.placeCard(c, 39, 41, false);
            }
            if (c.getId() == 20){
                p.placeCard(c, 39, 39, false);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
        }
        assertEquals(myResourceCard, p.getGameField().getPlacedCard(41, 39));
    }

    @Test
    void removePlacedCard() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, false);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, false);
            }
        }
        p.getGameField().removePlacedCard(41, 39);
        assertFalse(p.getGameField().isCardPresent(41, 39));
    }

    @Test
    void getCardWay() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 41, true);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 39, true);
        assertEquals(true, p.getGameField().getCardWay(39,41));
        assertEquals(false, p.getGameField().getCardWay(41,41));
        assertEquals(true, p.getGameField().getCardWay(39,39));
        assertEquals(false, p.getGameField().getCardWay(41,39));
    }

    @Test
    void getCardsOrder() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 41, false);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        int [][] cardOrder = p.getGameField().getCardsOrder();
        assertEquals(1, cardOrder[40][40]);
        assertEquals(2, cardOrder[41][41]);
        assertEquals(3, cardOrder[39][41]);
        assertEquals(4, cardOrder[41][39]);
        assertEquals(5, cardOrder[39][39]);

    }

    @Test
    void getNumPlayedCards() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 41, false);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        assertEquals(5, p.getGameField().getNumPlayedCards());
    }
}