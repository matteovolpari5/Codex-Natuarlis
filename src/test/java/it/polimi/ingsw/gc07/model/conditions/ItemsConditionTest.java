package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.controller.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.GameObject;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemsConditionTest {
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    private Deck<PlaceableCard> starterCardsDeck;
    ItemsCondition condition;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = null;
        goldCardsDeck = null;
        starterCardsDeck = null;
        try {
            resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
            goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
            starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(resourceCardsDeck);
        assertNotNull(goldCardsDeck);
        assertNotNull(starterCardsDeck);
        condition = null;
    }

    @Test
    public void constructorAndGetter() {
        List<GameItem> myList = new ArrayList<>();
        myList.add(GameObject.QUILL);
        myList.add(GameObject.INKWELL);
        myList.add(GameResource.ANIMAL);
        myList.add(GameResource.FUNGI);
        ItemsCondition condition = new ItemsCondition(myList);
        List<GameItem> checkList = condition.getNeededItems();
        assertNotNull(checkList);
        assertEquals(myList, checkList);
    }

    // checks the case of only the starter card placed face down
    // the card has only permanent resources
    @Test
    public void onlyStarterCardPermanentResources() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 86){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        List<GameItem> neededItems = new ArrayList<>();
        neededItems.add(GameResource.PLANT);
        neededItems.add(GameResource.ANIMAL);
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    // checks the case of only the starter card placed face up
    // the card has only temporary resources
    @Test
    public void onlyStarterCardTemporaryResources() throws CardNotPresentException, CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        PlaceableCard myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        List<GameItem> neededItems = new ArrayList<>();
        neededItems.add(GameResource.PLANT);
        neededItems.add(GameResource.ANIMAL);
        neededItems.add(GameResource.FUNGI);
        neededItems.add(GameResource.INSECT);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    // checks the case of only the starter card placed face down
    // the card has bought temporary and permanent resources
    @Test
    public void onlyStarterCardResources() throws CardAlreadyPresentException, IndexesOutOfGameFieldException, PlacingConditionNotMetException, MultipleCornersCoveredException, NotLegitCornerException, NoCoveredCornerException {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 81){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        List<GameItem> neededItems = new ArrayList<>();
        neededItems.add(GameResource.PLANT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }
}