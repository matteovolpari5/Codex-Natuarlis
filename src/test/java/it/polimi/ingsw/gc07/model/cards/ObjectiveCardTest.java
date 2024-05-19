package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.conditions.Condition;
import it.polimi.ingsw.gc07.model.conditions.ItemsCondition;
import it.polimi.ingsw.gc07.model.conditions.LayoutCondition;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ObjectiveCardTest {
    DrawableDeck<DrawableCard> resourceCardsDeck;
    DrawableDeck<GoldCard> goldCardsDeck;
    ObjectiveCard myObjectiveCard;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;
    PlayingDeck<ObjectiveCard> objectiveCardsDeck;
    Player p;


    @BeforeEach
    void setUp() {
            objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
            resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
            goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
            Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
            //salvo objective card
            for(ObjectiveCard c: objectiveCardsDeck.getContent()){
                if(c.getId() == 87){
                    myObjectiveCard = c;
                }
            }
            assertNotNull(myObjectiveCard);
            //salvo starter card
            for(PlaceableCard c: starterCardsDeck.getContent()){
                if(c.getId() == 81){
                 myStarterCard = c;
                }
            }
            assertNotNull(myStarterCard);

            p = new Player("p", true, true);
            p.setStarterCard(myStarterCard);
            p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
    }
    @Test
    public void OneTimeLayoutConditionMet () {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 43, true);
            }
        }
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
        assertEquals(2, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
    }
    @Test
    public void TwoTimesLayoutConditionMet () {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 43, true);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 41, 39, true);
            }
            if (c.getId() == 5) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 42, 38, true);
            }
            if (c.getId() == 6) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 43, 37, true);
            }
        }
        assertEquals(4, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(2, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void NoLayoutConditionMet () {
        for(DrawableCard c: resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 13) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 43, true);
            }
        }
        assertEquals(0, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(0, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void NotReusableCardLayout () {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 43, true);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 36, 44, true);
            }
            if (c.getId() == 5) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 35, 45, true);
            }
        }
        assertEquals(2, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void OneTimeLayoutConditionL () {
        for(ObjectiveCard c: objectiveCardsDeck.getContent()){
            if(c.getId() == 91){
                myObjectiveCard = c;
            }
        }
        assertNotNull(myObjectiveCard);
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 8) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 24) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 36, 38, true);
            }
        }
        assertEquals(3, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void TwoTimesLayoutConditionL () {
        for(ObjectiveCard c: objectiveCardsDeck.getContent()){
            if(c.getId() == 91){
                myObjectiveCard = c;
            }
        }
        assertNotNull(myObjectiveCard);
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 7) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 41, 41, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 8) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 24) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 39, true);
            }
            if (c.getId() == 5) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 42, 42, true);
            }
            if (c.getId() == 6) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 43, 41, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 36, 38, true);
            }
            if (c.getId() == 14) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 44, 42, true);
            }
        }
        assertEquals(6, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(2, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void NoLayoutConditionL () {
        for(ObjectiveCard c: objectiveCardsDeck.getContent()){
            if(c.getId() == 91){
                myObjectiveCard = c;
            }
        }
        assertNotNull(myObjectiveCard);
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 18) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 24) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 36, 38, true);
            }
        }
        assertEquals(0, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(0, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void NotReusableCardLayoutL () {
        for(ObjectiveCard c: objectiveCardsDeck.getContent()){
            if(c.getId() == 91){
                myObjectiveCard = c;
            }
        }
        assertNotNull(myObjectiveCard);
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 12) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 31) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 37, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 36, 38, true);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 38, 40, true);
            }
        }
        assertEquals(3, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void OneTimeItemCondition () {
        for(ObjectiveCard c: objectiveCardsDeck.getContent()){
            if(c.getId() == 95){
                myObjectiveCard = c;
            }
        }
        assertNotNull(myObjectiveCard);
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 42, 38, false);
            }
        }
        assertEquals(2, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void TwoTimesItemCondition () {
        for(ObjectiveCard c: objectiveCardsDeck.getContent()){
            if(c.getId() == 95){
                myObjectiveCard = c;
            }
        }
        assertNotNull(myObjectiveCard);
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 41, 41, false);
            }
        }
        assertEquals(4, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(2, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void NoItemConditionMet () {
        for(ObjectiveCard c: objectiveCardsDeck.getContent()){
            if(c.getId() == 95){
                myObjectiveCard = c;
            }
        }
        assertNotNull(myObjectiveCard);
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 12) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 13) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                p.placeCard(myResourceCard, 41, 41, false);
            }
        }
        assertEquals(0, myObjectiveCard.getObjectiveScore(new GameField(p.getGameField())));
        assertEquals(0, myObjectiveCard.numTimesScoringConditionMet(new GameField(p.getGameField())));
    }
    @Test
    public void getterScoringConditionAndPoints () {
        for (ObjectiveCard c : objectiveCardsDeck.getContent()) {
            if (c.getId() == 95) {
                myObjectiveCard = c;
                assertNotNull(myObjectiveCard);
                Condition con = myObjectiveCard.getScoringCondition();
                assertInstanceOf(ItemsCondition.class,con);
                assertEquals(2,myObjectiveCard.getPoints());
            }
            if (c.getId() == 94) {
                myObjectiveCard = c;
                assertNotNull(myObjectiveCard);
                Condition con = myObjectiveCard.getScoringCondition();
                assertInstanceOf(LayoutCondition.class,con);
                assertEquals(3,myObjectiveCard.getPoints());
            }
        }
    }
    @Test
    public void numTimesScoringConditionNotMet () {
        for (ObjectiveCard c : objectiveCardsDeck.getContent()) {
            if (c.getId() == 95) {
                myObjectiveCard = c;
                assertNotNull(myObjectiveCard);
                assertEquals(myObjectiveCard.getObjectiveScore(p.getGameField()),0);
                assertEquals(myObjectiveCard.numTimesScoringConditionMet(p.getGameField()),0);
            }
        }
    }
    public void numTimesScoringConditionMetAndScored () {
        for (ObjectiveCard c : objectiveCardsDeck.getContent()) {
            if (c.getId() == 95) {
                myObjectiveCard = c;
                assertNotNull(myObjectiveCard);
            }
        }

        assertEquals(myObjectiveCard.getObjectiveScore(p.getGameField()),0);
        assertEquals(myObjectiveCard.numTimesScoringConditionMet(p.getGameField()),0);
    }
}