package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.controller.GameState;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model_listeners.ChatListener;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gameModel;
    DrawableDeck<DrawableCard> resourceCardsDeck;
    DrawableDeck<GoldCard> goldCardsDeck;
    Deck<PlaceableCard> starterCardsDeck;
    ObjectiveCard myObjectiveCard;
    PlaceableCard myStarterCard;
    DrawableCard myResourceCard;
    PlayingDeck<ObjectiveCard> objectiveCardsDeck;
    Player p;
    @BeforeEach
    void setUp()
    {
        objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        gameModel = new GameModel(0,2,resourceCardsDeck,goldCardsDeck,objectiveCardsDeck,starterCardsDeck);
    }

    @Test
    void getterAndSetter()
    {
        assertEquals(0,gameModel.getId());
        assertEquals(2,gameModel.getPlayersNumber());
        gameModel.setState(GameState.GAME_ENDED);
        assertEquals(gameModel.getState(),GameState.GAME_ENDED);
        gameModel.setState(GameState.GAME_STARTING);
        assertNotNull(gameModel.getPlayers());
        assertNotNull(gameModel.getPlayerNicknames());
        assertEquals(0,gameModel.getWinners().size());
        gameModel.setCurrPlayer(0);
        assertEquals(0,gameModel.getCurrPlayer());
        gameModel.setHasCurrPlayerPlaced(false);
        assertFalse(gameModel.getHasCurrPlayerPlaced());
        assertNotNull(gameModel.getScoreTrackBoard());
        assertNotNull(gameModel.getResourceCardsDeck());
        assertNotNull(gameModel.getGoldCardsDeck());
        assertNotNull(gameModel.getObjectiveCardsDeck());
        assertNotNull(gameModel.getStarterCardsDeck());
        gameModel.setPenultimateRound(true);
        assertTrue(gameModel.getPenultimateRound());
        gameModel.setAdditionalRound(true);
        assertTrue(gameModel.getAdditionalRound());
        gameModel.setCommandResult("Player1", CommandResult.SUCCESS);
        assertEquals(CommandResult.SUCCESS,gameModel.getCommandResult());
        gameModel.setEmptyDecks(true);
        assertTrue(gameModel.getEmptyDecks());
        Player p = new Player("Player1",false,false);
        gameModel.addPlayer(p);
        assertNull(gameModel.revealFaceUpGoldCard(0));
        assertNull(gameModel.revealFaceUpResourceCard(0));
        gameModel.setUpPlayerHand(p);
    }
}
