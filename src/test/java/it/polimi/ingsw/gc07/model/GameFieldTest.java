package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
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
    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
    }

    @Test
    void placeCard() {

    }

    @Test
    void isCardPresent() throws CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 15){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 41, false);
            }
            if (c.getId() == 18){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 39){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 39, false);
            }
        }
        assertTrue(gameField.isCardPresent(41, 41));
    }

    @Test
    void getPlacedCard() throws CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 11){
                gameField.placeCard(c, 41, 41, false);
            }
            if (c.getId() == 10){
                gameField.placeCard(c, 39, 41, false);
            }
            if (c.getId() == 20){
                gameField.placeCard(c, 39, 39, false);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 39, true);
            }
        }
        assertEquals(myResourceCard, gameField.getPlacedCard(41, 39));
    }

    @Test
    void removePlacedCard() throws CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 41, false);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 39, false);
            }
        }
        gameField.removePlacedCard(41, 39);
        assertFalse(gameField.isCardPresent(41, 39));
    }

    @Test
    void getCardWay() throws CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 41, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 39, true);
        assertEquals(true, gameField.getCardWay(39,41));
        assertEquals(false, gameField.getCardWay(41,41));
        assertEquals(true, gameField.getCardWay(39,39));
        assertEquals(false, gameField.getCardWay(41,39));
    }

    @Test
    void getCardsOrder() throws CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 41, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        int [][] cardOrder = gameField.getCardsOrder();
        assertEquals(1, cardOrder[40][40]);
        assertEquals(2, cardOrder[41][41]);
        assertEquals(3, cardOrder[39][41]);
        assertEquals(4, cardOrder[41][39]);
        assertEquals(5, cardOrder[39][39]);

    }

    @Test
    void getNumPlayedCards() throws CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 41, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        assertEquals(5, gameField.getNumPlayedCards());
    }
}