package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ObjectiveCardTest {
    ResourceCardsDeck resourceCardsDeck;
    ObjectiveCard myObjectiveCard;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;
    GameField gameField;
    PlayingDeck<ObjectiveCard> objectiveCardsDeck;


    @BeforeEach
    void setUp() {
            objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
            resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
            //goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
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
            gameField = new GameField();
            gameField.setStarterCard(myStarterCard);
            gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
    }
    @Test
    public void OneTimeLayoutConditionMet () {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 43, true);
            }
        }
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
        assertEquals(2, myObjectiveCard.getObjectiveScore(new GameField(gameField)));

    }
    @Test
    public void TwoTimesLayoutConditionMet () {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 43, true);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 39, true);
            }
            if (c.getId() == 5) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 42, 38, true);
            }
            if (c.getId() == 6) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 43, 37, true);
            }
        }
        assertEquals(4, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(2, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
    }
    @Test
    public void NoLayoutConditionMet () {
        for(DrawableCard c: resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 13) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 43, true);
            }
        }
        assertEquals(0, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(0, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
    }
    @Test
    public void NotReusableCardLayout () {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 42, true);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 43, true);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 36, 44, true);
            }
            if (c.getId() == 5) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 35, 45, true);
            }
        }
        assertEquals(2, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
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
                gameField.placeCard(myResourceCard, 39, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 8) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 24) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 36, 38, true);
            }
        }
        assertEquals(3, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
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
                gameField.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 7) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 41, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 8) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 24) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 39, true);
            }
            if (c.getId() == 5) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 42, 42, true);
            }
            if (c.getId() == 6) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 43, 41, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 36, 38, true);
            }
            if (c.getId() == 14) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 44, 42, true);
            }
        }
        assertEquals(6, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(2, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
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
                gameField.placeCard(myResourceCard, 39, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 18) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 24) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 36, 38, true);
            }
        }
        assertEquals(0, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(0, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
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
                gameField.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 12) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 38, true);
            }
            if (c.getId() == 31) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 39, true);
            }
        }
        for (DrawableCard c : resourceCardsDeck.getContent())
        {
            if (c.getId() == 9) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 36, 38, true);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 40, true);
            }
        }
        assertEquals(3, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
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
                gameField.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 42, 38, false);
            }
        }
        assertEquals(2, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
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
                gameField.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 41, false);
            }
        }
        assertEquals(4, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(2, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
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
                gameField.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 12) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 13) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 11) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 41, false);
            }
        }
        assertEquals(0, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(0, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
    }
}