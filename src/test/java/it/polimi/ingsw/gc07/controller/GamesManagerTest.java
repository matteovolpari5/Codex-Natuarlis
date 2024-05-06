package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GamesManagerTest {
    GamesManager gm;

    @BeforeEach
    void setUp() {
        gm = GamesManager.getGamesManager();
        gm.resetGamesManager();
    }

    @Test
    void singletonTest() {
        GamesManager gm1 = GamesManager.getGamesManager();
        GamesManager gm2 = GamesManager.getGamesManager();
        assertSame(gm1, gm2);
    }

    @Test
    void testJoinNewGame() {
        gm.addPlayerToPending("player1", false, true);
        gm.addPlayerToPending("player2", false, true);

        assertNull(gm.getGameById(0));

        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        gm.joinExistingGame("player2", TokenColor.RED, 0);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        assertNotNull(gm.getGameById(0));


        assertEquals(0, gm.getGameIdWithPlayer("player1"));
    }

    @Test
    void checkReconnection() {
        gm.addPlayerToPending("player1", false, true);
        gm.addPlayerToPending("player2", false, true);
        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        gm.joinExistingGame("player2", TokenColor.RED, 0);

        GameController gc = gm.getGameById(0);
        assertEquals(GameState.PLACING_STARTER_CARDS, gc.getState());

        gc.setState(GameState.PLAYING);

        gc.disconnectPlayer("player1");
        assertEquals(GameState.WAITING_RECONNECTION, gc.getState());

        gm.addPlayerToPending("player1", true, true);
        assertEquals(GameState.PLAYING, gc.getState());
    }
}