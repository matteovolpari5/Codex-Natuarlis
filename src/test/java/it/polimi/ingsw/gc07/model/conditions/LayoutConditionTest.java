package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.controller.Game;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class LayoutConditionTest {
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    private Deck<PlaceableCard> starterCardsDeck;
    LayoutCondition condition;
    GameField gameField;
    PlaceableCard myStarterCard;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = null;
        goldCardsDeck = null;
        starterCardsDeck = null;
        condition = null;
        gameField = null;
        myStarterCard = null;
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
    }

    @Test
    public void onlyStarterCard() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.INSECT;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.INSECT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = GameResource.INSECT;
        layout[3][0] = null;
        layout[3][1] = null;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);
        assertEquals(0, condition.numTimesMet(gameField));
    }

    @Test
    public void condition3x3() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 31){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 39, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 37){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 38, 38, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 36){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 37, 37, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 26){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 41, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 24){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 41, false);

        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 35){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 42, 42, true);

        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.INSECT;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.INSECT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = GameResource.INSECT;
        layout[3][0] = null;
        layout[3][1] = null;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void condition4x2() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 18){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 39, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 38, 38, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 22){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 37, 37, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 32){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 41, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 5){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 36, 38, false);

        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.FUNGI;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = null;
        layout[1][2] = null;
        layout[2][0] = GameResource.FUNGI;
        layout[2][1] = null;
        layout[2][2] = null;
        layout[3][0] = null;
        layout[3][1] = GameResource.PLANT;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void checkDoNotReuseCards() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        // checks that the same is card is not reused
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 11){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 41, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 12){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 42, 42, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 13){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 43, 43, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 14){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 44, 44, false);

        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.PLANT;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.PLANT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = GameResource.PLANT;
        layout[3][0] = null;
        layout[3][1] = null;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        // if cards are used only one time, I expect numTimesMet to return 1, not 2
        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void multipleTimesMet() {
        // the condition is met multiple times
    }
}