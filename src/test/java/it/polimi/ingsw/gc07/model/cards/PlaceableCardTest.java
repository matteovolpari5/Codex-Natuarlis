package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceableCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    PlaceableCard myStarterCard;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
    }

    @Test
    public void onlyStarterCardScore () {
        myStarterCard=starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        assertEquals(0, myStarterCard.getPlacementScore(gameField, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2));
    }

    @Test
    public void starterCardScore() {
        myStarterCard=starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, 40, 40, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 42, 42, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        gameField.placeCard(resourceCardsDeck.drawCard(), 39, 41, false);
        assertEquals(0, myStarterCard.getPlacementScore(gameField, 40, 40));
    }


}