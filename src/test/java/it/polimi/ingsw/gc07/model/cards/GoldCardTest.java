package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.conditions.ItemsCondition;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.decks.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private DrawableDeck<DrawableCard> resourceCardsDeck;
    private DrawableDeck<GoldCard> goldCardsDeck;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;
    DrawableCard myGoldCard;
    Player p;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        p = new Player("p", true, true);
    }

    @Test
    void isGoldPlaceableNoLegitCorner() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, false);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, false);
            }
        }
        assertEquals(CommandResult.NOT_LEGIT_CORNER, myGoldCard.isPlaceable(p.getGameField(), 42, 40, false));
    }

    @Test
    void isGoldPlaceableNoCoveredCorner() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 58){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
        }
        assertEquals(CommandResult.NO_COVERED_CORNER, myGoldCard.isPlaceable(p.getGameField(), 45, 45, true));
    }

    @Test
    void isGoldPlaceableMultipleCorner() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 58){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
        }
        assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, myGoldCard.isPlaceable(p.getGameField(), 42, 41, true));
    }

    @Test
    void isGoldPlaceableIndexOut() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 58){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
        }
        assertEquals(CommandResult.INDEXES_OUT_OF_GAME_FIELD, myGoldCard.isPlaceable(p.getGameField(), 89, 10, true));
    }

    @Test
    void isGoldPlaceableCardAlreadyPresent() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 58){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
        }
        assertEquals(CommandResult.CARD_ALREADY_PRESENT, myGoldCard.isPlaceable(p.getGameField(), 41, 41, true));
    }
    @Test
    void isGoldPlaceableCondition() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 58){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
        }
        assertEquals(CommandResult.PLACING_CONDITION_NOT_MET, myGoldCard.isPlaceable(p.getGameField(), 42, 40, false));

    }

    @Test
    void getGoldPlacementScore() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, true);
            }
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
        }
        p.placeCard(myGoldCard, 38, 38, false);
        assertEquals(3, myGoldCard.getPlacementScore(p.getGameField(), 38, 38));
    }

    @Test
    void getGoldCornerPlacementScore() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 45){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 1){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 2){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 11){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, true);
            }
        }
        p.placeCard(myGoldCard, 38, 40, false);
        assertEquals(4, myGoldCard.getPlacementScore(p.getGameField(), 38, 40));
    }
    @Test
    void getGoldItemsPlacementScore() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 42){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 37){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 6){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 39, false);
            }
            if (c.getId() == 16){
                myResourceCard = c;
                p.placeCard(myResourceCard, 39, 41, false);
            }
            if (c.getId() == 25){
                myResourceCard = c;
                p.placeCard(myResourceCard, 41, 41, false);
            }
        }
        assertEquals(CommandResult.SUCCESS, myGoldCard.isPlaceable(p.getGameField(), 42, 42, false));
        p.placeCard(myGoldCard, 42, 42, false);
        assertEquals(5, myGoldCard.getPlacementScore(p.getGameField(), 42, 42));
    }
    @Test
    public void getters() {
        for(GoldCard c: goldCardsDeck.getContent()){
            if(c.getId() == 51){
                myGoldCard = c;
            }
        }
        assertInstanceOf(ItemsCondition.class,myGoldCard.getPlacementCondition());
        assertInstanceOf(ItemsCondition.class,myGoldCard.getScoringCondition());
    }
    @Test
    void GoldPlaceBack() {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        assertEquals(CommandResult.SUCCESS, myGoldCard.isPlaceable(p.getGameField(), 41, 41, true));
    }
}