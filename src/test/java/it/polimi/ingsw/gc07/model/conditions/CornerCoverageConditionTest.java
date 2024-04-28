package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerCoverageConditionTest {
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    private Deck<PlaceableCard> starterCardsDeck;
    CornerCoverageCondition condition;
    PlaceableCard myStarterCard;
    Player p;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = null;
        goldCardsDeck = null;
        starterCardsDeck = null;
        condition = new CornerCoverageCondition();;
        myStarterCard = null;
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        p = new Player("p", true, true);
    }

    @Test
    public void onlyStarterCard() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        assertEquals(0, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void oneCornerCovered() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 84){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        DrawableCard myResourceCard = resourceCardsDeck.drawCard();
        assertNotNull(myResourceCard);
        p.placeCard(myResourceCard, 41, 41, true);
        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void twoCornersCovered() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        DrawableCard myResourceCard = resourceCardsDeck.drawCard();
        assertNotNull(myResourceCard);
        GoldCard myGoldCard = goldCardsDeck.drawCard();
        assertNotNull(myGoldCard);
        DrawableCard lastCard = resourceCardsDeck.drawCard();
        p.placeCard(myResourceCard, 41, 41, true);
        p.placeCard(myGoldCard, 41, 39, true);
        p.placeCard(lastCard, 42, 40, true);
        assertEquals(2, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void threeCornersCovered() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 39, true);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 41, true);
        p.placeCard(resourceCardsDeck.drawCard(), 38, 42, true);
        p.placeCard(resourceCardsDeck.drawCard(), 37, 41, true);
        p.placeCard(resourceCardsDeck.drawCard(), 38, 40, true);
        assertEquals(3, condition.numTimesMet(new GameField(p.getGameField())));
    }

    // testing all four corners, I test all possible positions
    @Test
    public void fourCornersCovered() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 39, true);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 41, true);
        p.placeCard(goldCardsDeck.drawCard(), 38, 42, true);
        p.placeCard(goldCardsDeck.drawCard(), 37, 41, true);
        p.placeCard(resourceCardsDeck.drawCard(), 38, 38, true);
        p.placeCard(goldCardsDeck.drawCard(), 37, 39, true);
        p.placeCard(goldCardsDeck.drawCard(), 38, 40, true);
        assertEquals(4, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void topBorder() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for(int i = 39; i >= 0; i--){
            int j;
            if(i % 2 == 0)
                j = 40;
            else
                j = 39;
            p.placeCard(resourceCardsDeck.drawCard(), i, j, true);
        }
        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void bottomBorder() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for(int i = 41; i < GameField.getDim(); i++){
            int j;
            if(i % 2 == 0)
                j = 40;
            else
                j = 41;
            p.placeCard(resourceCardsDeck.drawCard(), i, j, true);
        }
        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void rightBorder() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for(int j = 41; j < GameField.getDim(); j++){
            int i;
            if(j % 2 == 0)
                i = 40;
            else
                i = 39;
            p.placeCard(goldCardsDeck.drawCard(), i, j, true);
        }
        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void leftBorder() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for(int j = 39; j >= 0; j--){
            int i;
            if(j % 2 == 0)
                i = 40;
            else
                i = 41;
            p.placeCard(goldCardsDeck.drawCard(), i, j, true);
        }
        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }
}
