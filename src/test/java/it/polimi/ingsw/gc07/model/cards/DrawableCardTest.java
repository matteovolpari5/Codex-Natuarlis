package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
<<<<<<< HEAD
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
=======
>>>>>>> origin/main
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
<<<<<<< HEAD
import java.io.FileNotFoundException;
=======

import java.io.FileNotFoundException;

>>>>>>> origin/main
import static org.junit.jupiter.api.Assertions.*;

class DrawableCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    PlaceableCard myStarterCard;
<<<<<<< HEAD
    DrawableCard myResourceCard;

=======
    PlaceableCard myResourceCard;
>>>>>>> origin/main
    @BeforeEach
    void setUp() {
        try{
            resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
            goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
            starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        } catch (
                FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
<<<<<<< HEAD
    void isResourceCardPlaceable() throws CardNotPresentException, NoCoveredCornerException, NotLegitCornerException,
    MultipleCornersCoveredException, PlacingConditionNotMetException, CardAlreadyPresentException,
    NullPointerException, IndexesOutOfGameFieldException{
        myStarterCard=starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);

=======
    void isPlaceable() {
    }

    @Test
    void getPlacementScore() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, true);
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
        gameField.placeCard(resourceCardsDeck.drawCard(), 42, 42, false);
        assertEquals(1, myResourceCard.getPlacementScore(gameField, 41, 41));
>>>>>>> origin/main
    }
}