package it.polimi.ingsw.gc07.model.cards;

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

class PlaceableCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;

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
    public void starterCardScore () throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard=starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        assertEquals(0, myStarterCard.getPlacementScore(gameField, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2));
    }

    @Test
    public void resourceCardScore() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        myStarterCard=starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        for(DrawableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 9){
                myResourceCard = c;
            }
        }
        assertNotNull(myResourceCard);
        gameField.placeCard(myResourceCard, 41, 41, false);
        assertEquals(1, myResourceCard.getPlacementScore(gameField, 41, 41));

    }


}