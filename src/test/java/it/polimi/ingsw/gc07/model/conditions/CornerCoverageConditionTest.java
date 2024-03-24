package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
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

    @Test
    public void oneCornerCovered() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
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
        gameField.placeCard(myResourceCard, 41, 41, false);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void twoCornersCovered() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        DrawableCard myResourceCard = resourceCardsDeck.drawCard();
        assertNotNull(myResourceCard);
        GoldCard myGoldCard = goldCardsDeck.drawCard();
        assertNotNull(myGoldCard);
        DrawableCard lastCard = resourceCardsDeck.drawCard();
        gameField.placeCard(myResourceCard, 41, 41, false);
        gameField.placeCard(myGoldCard, 39, 41, true);
        gameField.placeCard(lastCard, 40, 42, false);
        assertEquals(2, condition.numTimesMet(gameField));
    }

    @Test
    public void threeCornersCovered() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException, CardNotPresentException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        // TODO continue
    }
}