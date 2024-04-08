package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.DecksBuilder;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemsConditionTest {
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    private Deck<PlaceableCard> starterCardsDeck;
    ItemsCondition condition;
    List<GameItem> neededItems;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = null;
        goldCardsDeck = null;
        starterCardsDeck = null;
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        assertNotNull(resourceCardsDeck);
        assertNotNull(goldCardsDeck);
        assertNotNull(starterCardsDeck);
        condition = null;
        neededItems = new ArrayList<>();
    }

    // checks the case of only the starter card placed face down
    // the card has only permanent resources
    @Test
    public void onlyStarterCardPermanentResources() {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 86){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        neededItems.add(GameResource.PLANT);
        neededItems.add(GameResource.ANIMAL);
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    // checks the case of only the starter card placed face up
    // the card has only temporary resources
    @Test
    public void onlyStarterCardTemporaryResources() {
        PlaceableCard myStarterCard = starterCardsDeck.drawCard();
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
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
    public void onlyStarterCard() {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 81){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        neededItems.add(GameResource.PLANT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void oneTimeMet() {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 81){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        // 2 INSECT, 1 PLANT

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 33){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 39, false);
        // 4 INSECT, 1 PLANT

        card = null;
        for(PlaceableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 46){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 41, true);
        // 4 INSECT, 1 FUNGI

        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));

        card = null;
        for(PlaceableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 76){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 41, false);
        // 4 INSECT, 1 FUNGI
        assertEquals(1, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void numTimesZero() {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 81){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        // 2 INSECT, 1 PLANT

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 33){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 39, false);
        // 4 INSECT, 1 PLANT

        card = null;
        for(PlaceableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 46){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 41, true);
        // 4 INSECT, 1 FUNGI

        card = null;
        for(PlaceableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 76){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 41, false);
        // 4 INSECT, 1 FUNGI

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.PLANT);
        condition = new ItemsCondition(neededItems);
        assertEquals(0, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.PLANT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(0, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.ANIMAL);
        condition = new ItemsCondition(neededItems);
        assertEquals(0, condition.numTimesMet(gameField));
    }

    @Test
    public void moreTimesMet() {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 81){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        // 2 INSECT, 1 PLANT

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 33){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 39, false);
        // 4 INSECT, 1 PLANT

        card = null;
        for(PlaceableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 46){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 39, 41, true);
        // 4 INSECT, 1 FUNGI

        card = null;
        for(PlaceableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 76){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 41, false);
        // 4 INSECT, 1 FUNGI

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        condition = new ItemsCondition(neededItems);
        assertEquals(2, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.INSECT);
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }

    @Test
    public void gameResourceAndGameObjectList() {
        PlaceableCard myStarterCard = null;
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 82){
                myStarterCard = c;
            }
        }
        assertNotNull(myStarterCard);
        GameField gameField = new GameField(myStarterCard);
        gameField.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 30){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 39, false);

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.FUNGI);
        neededItems.add(GameResource.ANIMAL);
        condition = new ItemsCondition(neededItems);
        assertEquals(2, condition.numTimesMet(gameField));

        card = null;
        for(PlaceableCard c: goldCardsDeck.getContent()){
            if(c.getId() == 63){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 41, 41, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 14){
                card = c;
            }
        }
        assertNotNull(card);
        gameField.placeCard(card, 42, 38, true);

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.FUNGI);
        neededItems.add(GameResource.ANIMAL);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.ANIMAL);
        condition = new ItemsCondition(neededItems);
        assertEquals(2, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.FUNGI);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameObject.QUILL);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));

        neededItems = new ArrayList<>();
        neededItems.add(GameResource.ANIMAL);
        neededItems.add(GameResource.ANIMAL);
        neededItems.add(GameResource.FUNGI);
        neededItems.add(GameObject.QUILL);
        condition = new ItemsCondition(neededItems);
        assertEquals(1, condition.numTimesMet(gameField));
    }
}