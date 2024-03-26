package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;
    DrawableCard myGoldCard;
    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
    }

    @Test
    void isPlaceable() {
    }

    @Test
    void getGoldPlacementScore() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 49){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 1){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 41, true);
            }
            if (c.getId() == 2){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 39, true);
            }
            if (c.getId() == 4){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 39, true);
            }
        }
        gameField.placeCard(myGoldCard, 38, 38, false);
        assertEquals(3, myGoldCard.getPlacementScore(gameField, 38, 38));
    }

    @Test
    void getGoldCornerPlacementScore() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 45){
                myGoldCard = c;
            }
        }
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if (c.getId() == 1){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 39, false);
            }
            if (c.getId() == 2){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 39, true);
            }
            if (c.getId() == 3){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 39, 41, true);
            }
            if (c.getId() == 11){
                myResourceCard = c;
                gameField.placeCard(myResourceCard, 41, 41, true);
            }
        }
        gameField.placeCard(myGoldCard, 38, 40, false);
        assertEquals(4, myGoldCard.getPlacementScore(gameField, 38, 40));
    }
}