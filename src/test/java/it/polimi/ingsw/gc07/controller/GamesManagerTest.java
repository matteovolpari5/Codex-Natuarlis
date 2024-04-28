package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesManagerTest {

    @Test
    void singletonTest() {
        GamesManager gm1 = GamesManager.getGamesManager();
        GamesManager gm2 = GamesManager.getGamesManager();
        assertSame(gm1, gm2);
    }

    @Test
    void testJoinNewGame() {
        GamesManager gm = GamesManager.getGamesManager();
        gm.addPlayerToPending("player1", true, true);
        gm.addPlayerToPending("player2", true, true);

        assertNull(gm.getGameById(0));

        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        assertEquals(CommandResult.CREATE_SERVER_GAME, gm.getCommandResultManager().getCommandResult());

        gm.joinExistingGame("player2", TokenColor.RED, 0);
        assertEquals(CommandResult.SET_SERVER_GAME, gm.getCommandResultManager().getCommandResult());

        assertNotNull(gm.getGameById(0));


        assertEquals(0, gm.getGameIdWithPlayer("player1"));
    }

    @Test
    void checkReconnection() {
        GamesManager gm = GamesManager.getGamesManager();
        gm.addPlayerToPending("player1", true, true);
        gm.addPlayerToPending("player2", true, true);
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