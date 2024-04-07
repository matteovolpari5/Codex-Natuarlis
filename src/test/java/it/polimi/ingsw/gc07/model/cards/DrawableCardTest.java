package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
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
        assertEquals(CommandResult.NO_COVERED_CORNER, myResourceCard.isPlaceable(gameField, 42, 42, false));
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
        assertEquals(CommandResult.INDEXES_OUT_OF_GAME_FIELD, myResourceCard.isPlaceable(gameField, 81, 81, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner1() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
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
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(gameField, 40, 41, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner2() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
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
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(gameField, 41, 40, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner3() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
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
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(gameField, 40, 39, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner4() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
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
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(gameField, 39, 40, false));
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
        assertEquals(CommandResult.CARD_ALREADY_PRESENT, myResourceCard.isPlaceable(gameField, 40, 40, false));
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
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 42, false));
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
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 38, false));
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
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 42, false));
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
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 40, 38, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner5() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 41, 41, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner6() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 41, 39, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner7() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 39, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 38, 42, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner9() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 4){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 39, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 38, 40, false));
    }

    //TODO: dal debug risulta che al metodo viene passato x=41, y=41 e way=true, diversamente da quanto passato alla riga 365
    @Test
    void isResourcePlaceableNoLegitCorner10() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
            MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
            NullPointerException, IndexesOutOfGameFieldException {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 41, 41, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        gameField.placeCard(myResourceCard, 42, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(gameField, 41, 39, false));
    }
}