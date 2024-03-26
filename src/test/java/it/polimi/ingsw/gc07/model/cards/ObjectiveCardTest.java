package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.*;
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


    @BeforeEach
    void setUp() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
            PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
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
            gameField = new GameField(myStarterCard);
            gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
    }
    @Test
    public void OneTimeLayoutConditionMet () throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 42, false);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 43, false);
            }
        }
        assertEquals(1, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));
        assertEquals(2, myObjectiveCard.getObjectiveScore(new GameField(gameField)));

    }
    @Test
    public void TwoTimesLayoutConditionMet () throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        for (DrawableCard c : resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 42, false);
            }
            if (c.getId() == 3) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 43, false);
            }
            if (c.getId() == 4) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 5) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 42, 38, false);
            }
            if (c.getId() == 6) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 43, 37, false);
            }
        }
        assertEquals(4, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(2, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));


    }
    @Test
    public void NoLayoutConditionMet () throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        for(DrawableCard c: resourceCardsDeck.getContent()) {
            if (c.getId() == 1) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 2) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 38, 42, false);
            }
            if (c.getId() == 13) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameField.placeCard(myResourceCard, 37, 43, false);
            }
        }
        assertEquals(0, myObjectiveCard.getObjectiveScore(new GameField(gameField)));
        assertEquals(0, myObjectiveCard.numTimesScoringConditionMet(new GameField(gameField)));

    }
}