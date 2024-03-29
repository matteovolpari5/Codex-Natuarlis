package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DrawableCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private ResourceCardsDeck resourceCardsDeck;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
    }

    @Test
    void isResourcePlaceableNoCoveredCorner() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
    MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
    NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.NO_COVERED_CORNER, myResourceCard.isPlaceable(gameField, 42, 42, false));
    }

    @Test
    void isResourcePlaceableIndexOut() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.INDEXES_OUT_OF_GAME_FIELD, myResourceCard.isPlaceable(gameField, 81, 81, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(gameField, 40, 41, false));
    }

    @Test
    void isResourcePlaceableCardAlreadyPresent() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.CARD_ALREADY_PRESENT, myResourceCard.isPlaceable(gameField, 40, 40, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner1() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 39, 41, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 41, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 42, false));
    }

    @Test
    void getResourcePlacementScore() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 9){
                myResourceCard = c;
            }
        }
        assertNotNull(myResourceCard);
        gameField.placeCard(myResourceCard, 41, 41, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 41, true);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        assertEquals(1, myResourceCard.getPlacementScore(gameField, 41, 41));
    }

    @Test
    void isResourcePlaceableNoLegitCorner2() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 39, 39, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 4){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 41, 39, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 38, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner3() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 8){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 39, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 41, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 42, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner4() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 39, 39, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 41, 39, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        assertEquals(PlacementResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 38, false));
    }
}