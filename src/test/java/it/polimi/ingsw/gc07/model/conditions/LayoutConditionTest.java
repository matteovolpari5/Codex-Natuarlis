package it.polimi.ingsw.gc07.model.conditions;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LayoutConditionTest {
    private ResourceCardsDeck resourceCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    private Deck<PlaceableCard> starterCardsDeck;
    LayoutCondition condition;
    PlaceableCard myStarterCard;
    Player p;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = null;
        goldCardsDeck = null;
        starterCardsDeck = null;
        condition = null;
        myStarterCard = null;
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        p = new Player("p", true, true);
    }

    @Test
    public void onlyStarterCard() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.INSECT;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.INSECT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = GameResource.INSECT;
        layout[3][0] = null;
        layout[3][1] = null;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);
        assertEquals(0, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void condition3x3() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 31){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 39, 39, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 37){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 38, 38, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 36){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 37, 37, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 26){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 41, 39, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 24){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 41, 41, false);

        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 35){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 42, 42, true);

        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.INSECT;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.INSECT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = GameResource.INSECT;
        layout[3][0] = null;
        layout[3][1] = null;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void condition4x2() {
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 18){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 39, 39, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 2){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 38, 38, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 22){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 37, 37, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 32){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 41, 41, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 5){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 36, 38, false);

        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.FUNGI;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = null;
        layout[1][2] = null;
        layout[2][0] = GameResource.FUNGI;
        layout[2][1] = null;
        layout[2][2] = null;
        layout[3][0] = null;
        layout[3][1] = GameResource.PLANT;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void checkDoNotReuseCards() {
        // checks that the same is card is not reused
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 11){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 41, 41, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 12){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 42, 42, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 13){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 43, 43, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 14){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 44, 44, false);

        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.PLANT;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.PLANT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = GameResource.PLANT;
        layout[3][0] = null;
        layout[3][1] = null;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        // if cards are used only one time, I expect numTimesMet to return 1, not 2
        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void multipleTimesMet() {
        // the condition is met multiple times
        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 11){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 39, 39, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 12){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 38, 38, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 13){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 37, 37, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 22){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 41, 41, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 33){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 42, 42, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 29){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 43, 43, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 7){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 43, 41, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 35){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 44, 42, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 36){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 44, 44, true);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 20){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 45, 45, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 37){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 46, 44, false);

        /*
        P X X X X X X X X
        X P X X X X X X X
        X X P X X X X X X
        X X X S X X X X X
        X X X X A X X X X
        X X X X X I X X X
        X X X X F X A X X
        X X X X X I X I X
        X X X X X X X X P
        X X X X X X X I X
        */

        GameResource[][] layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.PLANT;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.PLANT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = GameResource.PLANT;
        layout[3][0] = null;
        layout[3][1] = null;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));

        layout = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout[0][0] = GameResource.ANIMAL;
        layout[0][1] = null;
        layout[0][2] = null;
        layout[1][0] = null;
        layout[1][1] = GameResource.INSECT;
        layout[1][2] = null;
        layout[2][0] = null;
        layout[2][1] = null;
        layout[2][2] = null;
        layout[3][0] = null;
        layout[3][1] = GameResource.INSECT;
        layout[3][2] = null;
        condition = new LayoutCondition(layout);

        assertEquals(2, condition.numTimesMet(new GameField(p.getGameField())));
    }

    @Test
    public void patternInterruptedByStarterCard() {
        // the pattern is interrupted by a starter card

        myStarterCard = starterCardsDeck.drawCard();
        assertNotNull(myStarterCard);
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);

        PlaceableCard card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 13){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 39, 39, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 12){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 38, 38, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 15){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 41, 41, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 20){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 39, 41, false);

        card = null;
        for(PlaceableCard c: resourceCardsDeck.getContent()){
            if(c.getId() == 32){
                card = c;
            }
        }
        assertNotNull(card);
        p.placeCard(card, 42, 40, false);

        /*
        P X X X X X X X X
        X P X P X X X X X
        X X S X X X X X X
        X X X P X X X X X
        X X I X X X X X X
        X X X X X X X X X
        X X X X X X X X X
        X X X X X X X X X
        X X X X X X X X X
        X X X X X X X X X
        */

        GameResource[][] layout1 = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout1[0][0] = GameResource.PLANT;
        layout1[0][1] = null;
        layout1[0][2] = null;
        layout1[1][0] = null;
        layout1[1][1] = GameResource.PLANT;
        layout1[1][2] = null;
        layout1[2][0] = null;
        layout1[2][1] = null;
        layout1[2][2] = GameResource.PLANT;
        layout1[3][0] = null;
        layout1[3][1] = null;
        layout1[3][2] = null;
        condition = new LayoutCondition(layout1);
        // the layout should not be found because the starter card interrupts it
        assertEquals(0, condition.numTimesMet(new GameField(p.getGameField())));

        GameResource[][] layout2 = new GameResource[LayoutCondition.getRows()][LayoutCondition.getColumns()];
        layout2[0][0] = null;
        layout2[0][1] = GameResource.PLANT;
        layout2[0][2] = null;
        layout2[1][0] = null;
        layout2[1][1] = null;
        layout2[1][2] = null;
        layout2[2][0] = null;
        layout2[2][1] = GameResource.PLANT;
        layout2[2][2] = null;
        layout2[3][0] = GameResource.INSECT;
        layout2[3][1] = null;
        layout2[3][2] = null;
        condition = new LayoutCondition(layout2);
        // the layout should be found because the starter card is in a useless position
        assertEquals(1, condition.numTimesMet(new GameField(p.getGameField())));
    }
}