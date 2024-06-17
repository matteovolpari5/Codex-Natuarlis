package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.CommandResult;
import it.polimi.ingsw.gc07.model.decks.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawableCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private DrawableDeck<DrawableCard> resourceCardsDeck;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;
    Player p;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        p = new Player("P", true, true);
    }

    @Test
    void isResourcePlaceableNoCoveredCorner() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NO_COVERED_CORNER, myResourceCard.isPlaceable(p.getGameField(), 42, 42, false));
    }

    @Test
    void isResourcePlaceableIndexOut() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.INDEXES_OUT_OF_GAME_FIELD, myResourceCard.isPlaceable(p.getGameField(), 81, 81, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner1() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(p.getGameField(), 40, 41, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner2() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(p.getGameField(), 41, 40, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner3() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(p.getGameField(), 40, 39, false));
    }

    @Test
    void isResourcePlaceableMultipleCorner4() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myResourceCard.isPlaceable(p.getGameField(), 39, 40, false));
    }

    @Test
    void isResourcePlaceableCardAlreadyPresent() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.CARD_ALREADY_PRESENT, myResourceCard.isPlaceable(p.getGameField(), 40, 40, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner1() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 41, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 41, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 40, 42, false));
    }

    @Test
    void getResourcePlacementScore() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 9){
                myResourceCard = c;
            }
        }
        assertNotNull(myResourceCard);
        p.placeCard(myResourceCard, 41, 41, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 41, true);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 39, false);
        assertEquals(1, myResourceCard.getPlacementScore(p.getGameField(), 41, 41));
    }

    @Test
    void isResourcePlaceableNoLegitCorner2() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 39, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 4){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 41, 39, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 40, 38, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner3() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 8){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 41, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 40, 42, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner4() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 39, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 41, 39, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 40, 38, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner5() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 41, 41, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner6() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 41, 39, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner7() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 38, 42, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner9() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 4){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 38, 40, false));
    }

    @Test
    void isResourcePlaceableNoLegitCorner10() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 41, 41, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 42, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 41, 39, false));
    }
    @Test
    void ResourcePlaceableWithPoints() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 8){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 41, false);
        assertEquals(myResourceCard.getPoints(),1);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 39, false);
        assertEquals(myResourceCard.getPoints(),0);
    }
    @Test
    void isPlaceableNoLegitCorner11() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 40, 40, true);
        assertEquals(CommandResult.CARD_ALREADY_PRESENT, myResourceCard.isPlaceable(p.getGameField(), 40, 40, false));
    }
    @Test
    void isPlaceableNoLegitCorner12() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, true);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 41, 39, true);
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 41, 39, false));
    }
    @Test
    void isPlaceableNoLegitCorner13() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, true);
        //assertEquals(CommandResult.SUCCESS,p.placeCard(myStarterCard, 40, 40, false));
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 41, 41, false));
    }
    @Test
    void isPlaceableNoLegitCorner14() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, true);
        //assertEquals(CommandResult.SUCCESS,p.placeCard(myStarterCard, 40, 40, false));
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 3){
                myResourceCard = c;
            }
        }
        p.placeCard(myResourceCard, 39, 41, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 1){
                myResourceCard = c;
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myResourceCard.isPlaceable(p.getGameField(), 41, 39, false));
    }
}