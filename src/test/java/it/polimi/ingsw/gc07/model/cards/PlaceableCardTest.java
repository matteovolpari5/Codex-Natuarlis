package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.GameItem;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceableCardTest {
    private Deck<PlaceableCard> starterCardsDeck;
    private DrawableDeck<DrawableCard> resourceCardsDeck;
    private DrawableDeck<GoldCard> goldCardsDeck;
    PlaceableCard myStarterCard;
    Player p;

    @BeforeEach
    void setUp() {
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        p = new Player("Player", true, true);
    }

    @Test
    public void onlyStarterCardScore () {
        myStarterCard = starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, true);
        assertEquals(0, myStarterCard.getPlacementScore(p.getGameField(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2));
    }

    @Test
    public void starterCardScore() {
        myStarterCard=starterCardsDeck.drawCard();
        p.setStarterCard(myStarterCard);
        p.placeCard(myStarterCard, 40, 40, false);
        p.placeCard(resourceCardsDeck.drawCard(), 41, 41, false);
        p.placeCard(resourceCardsDeck.drawCard(), 42, 42, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 39, false);
        p.placeCard(resourceCardsDeck.drawCard(), 39, 41, false);
        assertEquals(0, myStarterCard.getPlacementScore(p.getGameField(), 40, 40));
    }
    @Test
    public void getters() {
        for(PlaceableCard c: starterCardsDeck.getContent()){
            if(c.getId() == 85){
                myStarterCard = c;
            }
        }
        GameItem[] gi= new GameItem[4];
        gi[0]= GameResource.INSECT;
        gi[1]= GameResource.FUNGI;
        gi[2]= GameResource.ANIMAL;
        gi[3]= GameResource.PLANT;
        Boolean[] gib= new Boolean[4];
        gib[0]= true;
        gib[1]= true;
        gib[2]= true;
        gib[3]= true;
        GameItem[] gib3= new GameItem[4];
        gib3[0]= null;
        gib3[1]= null;
        gib3[2]= null;
        gib3[3]= null;
        Boolean[] gib2= new Boolean[4];
        gib2[0]= true;
        gib2[1]= true;
        gib2[2]= false;
        gib2[3]= false;
        List<GameResource> pr= new ArrayList<>();
        pr.add(GameResource.ANIMAL);
        pr.add(GameResource.INSECT);
        pr.add(GameResource.PLANT);
        for(int i=0; i<4;i++)
        {
            assertEquals(gi[i],myStarterCard.getFrontCornersContent()[i]);
            assertEquals(gib[i],myStarterCard.getFrontCorners()[i]);
            assertEquals(gib2[i],myStarterCard.getBackCorners()[i]);
            assertEquals(gib3[i],myStarterCard.getBackCornersContent()[i]);
        }
        for(int i=0; i<pr.size();i++)
        {
            assertEquals(pr.get(i),myStarterCard.getPermanentResources().get(i));
        }
        assertEquals(CommandResult.SUCCESS, myStarterCard.isPlaceable(p.getGameField(), 40, 40, false));
        p.placeCard(myStarterCard,40, 40, false);
        assertEquals(0, myStarterCard.getPlacementScore(p.getGameField(),40,40));
        assertNull(myStarterCard.getPlacementCondition());
        assertNull(myStarterCard.getScoringCondition());
        assertEquals(0,myStarterCard.getPoints());
    }
}