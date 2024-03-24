package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class CornerCoverageConditionTest {
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    private Deck<PlaceableCard> starterCardsDeck;
    CornerCoverageCondition condition;
    GameField gameField;
    PlaceableCard myStarterCard;

    @BeforeEach
    void setUp() {
        try {
            resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
            goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
            starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        condition = new CornerCoverageCondition();
        myStarterCard = null;
    }

    @Test
    public void onlyStarterCard() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        assertEquals(0, condition.numTimesMet(gameField));
    }

    // resource and gold cards always have all four corners on the back

    @Test
    public void oneCornerCoveredBack() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 84){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        DrawableCard myResourceCard = resourceCardsDeck.drawCard();
        assertNotNull(myResourceCard);
        gameField.placeCard(myResourceCard, 41, 41, true);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void twoCornersCoveredBack() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        DrawableCard myResourceCard = resourceCardsDeck.drawCard();
        assertNotNull(myResourceCard);
        GoldCard myGoldCard = goldCardsDeck.drawCard();
        assertNotNull(myGoldCard);
        DrawableCard lastCard = resourceCardsDeck.drawCard();
        gameField.placeCard(myResourceCard, 41, 41, true);
        gameField.placeCard(myGoldCard, 39, 41, true);
        gameField.placeCard(lastCard, 40, 42, true);
        assertEquals(2, condition.numTimesMet(gameField));
    }

    @Test
    public void threeCornersCoveredBack() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 39, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 39, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 42, 38, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 37, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 40, 38, true);
        assertEquals(3, condition.numTimesMet(gameField));
    }

    @Test
    public void fourCornersCoveredBack() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 39, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 39, true);
        gameField.placeCard(goldCardsDeck.drawCard(), 42, 38, true);
        gameField.placeCard(goldCardsDeck.drawCard(), 41, 37, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 38, 38, true);
        gameField.placeCard(goldCardsDeck.drawCard(), 39, 37, true);
        gameField.placeCard(goldCardsDeck.drawCard(), 40, 38, true);
        assertEquals(4, condition.numTimesMet(gameField));
    }

    // resource and gold cards don't always have all four corner on the front

    @Test
    public void oneCornerCoveredFront() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        // TODO
    }

    @Test
    public void twoCornersCoveredFront() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        // TODO
    }

    @Test
    public void threeCornersCoveredFront() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        // TODO
    }

    @Test
    public void fourCornersCoveredFront() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        // TODO
    }
}