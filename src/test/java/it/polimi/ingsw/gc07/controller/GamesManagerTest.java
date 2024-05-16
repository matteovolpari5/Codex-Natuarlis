package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.game_commands.PlaceStarterCardCommand;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
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
    void testJoinNewGameAndDisconnectionAndReconnection() throws RemoteException {
        RmiServerGamesManager serverGamesManager = RmiServerGamesManager.getRmiServerGamesManager();

        RmiClient newRmiClient = new RmiClient("player1", false, serverGamesManager);
        newRmiClient.connectToGamesManagerServer(true, false);
        gm.addPlayerToPending("player1", true, false);
        RmiClient newRmiClient2 = new RmiClient("player2", false, serverGamesManager);
        newRmiClient2.connectToGamesManagerServer(true, false);
        gm.addPlayerToPending("player2", true, false);
        assertNull(gm.getGameById(0));

        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        gm.joinExistingGame("player2", TokenColor.RED, 0);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        assertNotNull(gm.getGameById(0));

        assertEquals(0, gm.getGameIdWithPlayer("player1"));

        GameController gc = GamesManager.getGamesManager().getGameById(0);
        assertEquals(GameState.PLACING_STARTER_CARDS, gc.getState());

        gc.setAndExecuteCommand(new PlaceStarterCardCommand("player1", false));
        gc.setAndExecuteCommand(new PlaceStarterCardCommand("player2", false));

        gc.disconnectPlayer("player1");
        assertEquals(GameState.WAITING_RECONNECTION, gc.getState());

        RmiClient newRmiClient3 = new RmiClient("player1", false, serverGamesManager);
        newRmiClient3.connectToGamesManagerServer(true, false);
        gc.reconnectPlayer(newRmiClient3,"player1",true,false);
        //gm.addPlayerToPending("player1", true, true);
        assertEquals(GameState.PLAYING, gc.getState());
    }
    /*
    @Test
    void reconnectPlayerSuccess() {
        gameController.disconnectPlayer("Player2");
        assertFalse(gameController.getPlayerByNickname("Player2").isConnected());
        gameController.reconnectPlayer(newRmiClient2,"Player2",true,false);
        assertTrue(gameController.getPlayerByNickname("Player2").isConnected());
    }
    @Test
    void JoinExistingGameSuccess() throws RemoteException {
        newRmiClient2 = new RmiClient("Player2", true, rmiServerGamesManager);
        GamesManager.getGamesManager().addPlayerToPending("Player2", true, false);
        GamesManager.getGamesManager().addVirtualView("Player2", newRmiClient2);
        GamesManager.getGamesManager().joinExistingGame("Player2", TokenColor.RED, 0);
        assertEquals(GamesManager.getGamesManager().getCommandResult(), CommandResult.SUCCESS);
        assertNotNull(GamesManager.getGamesManager().getGameById(0).getPlayerByNickname("Player2"));

    }

    @Test
    void JoinExistingGameFail() throws RemoteException {
        newRmiClient2 = new RmiClient("Player2", true, rmiServerGamesManager);
        GamesManager.getGamesManager().addPlayerToPending("Player2", true, false);
        GamesManager.getGamesManager().addVirtualView("Player2", newRmiClient2);
        GamesManager.getGamesManager().joinExistingGame("Player2", TokenColor.RED, 1);
        assertEquals(GamesManager.getGamesManager().getCommandResult(), CommandResult.GAME_NOT_PRESENT);
        assertNotNull(GamesManager.getGamesManager().getGameById(0).getPlayerByNickname("Player2"));

    }
    @Test
    void JoinExistingGameFail2() throws RemoteException {
        newRmiClient2 = new RmiClient("Player2", true, rmiServerGamesManager);
        GamesManager.getGamesManager().addPlayerToPending("Player2", true, false);
        GamesManager.getGamesManager().addVirtualView("Player2", newRmiClient2);
        GamesManager.getGamesManager().joinExistingGame("Player2", TokenColor.RED, 0);
        assertEquals(GamesManager.getGamesManager().getCommandResult(), CommandResult.SUCCESS);
        assertNotNull(GamesManager.getGamesManager().getGameById(0).getPlayerByNickname("Player2"));

        RmiClient newRmiClient3 = new RmiClient("Player3", true, rmiServerGamesManager);
        GamesManager.getGamesManager().addPlayerToPending("Player3", true, false);
        GamesManager.getGamesManager().addVirtualView("Player3", newRmiClient2);
        GamesManager.getGamesManager().joinExistingGame("Player3", TokenColor.BLUE, 0);
        assertEquals(GamesManager.getGamesManager().getCommandResult(), CommandResult.GAME_FULL);
        assertNull(GamesManager.getGamesManager().getGameById(0).getPlayerByNickname("Player3"));

    }
    @Test
    void joinNewGameSuccess() throws RemoteException {
        newRmiClient = new RmiClient("Player1", true, rmiServerGamesManager);
        // add virtual view to rmiServerGamesManager
        GamesManager.getGamesManager().addPlayerToPending("Player1", true, false);
        GamesManager.getGamesManager().addVirtualView("Player1", newRmiClient);

        //newRmiClient.connectToGamesManagerServer(true, false);
        newRmiClient.runCliJoinGame();
        GamesManager.getGamesManager().joinNewGame("Player1", TokenColor.GREEN, 2);

        assertEquals(GamesManager.getGamesManager().getCommandResult(),CommandResult.SUCCESS);
        assertNotNull(GamesManager.getGamesManager().getGameById(0).getPlayerByNickname("Player1"));
    }

    @Test
    void joinNewGameUnSuccess() throws RemoteException {
        newRmiClient = new RmiClient("Player1", true, rmiServerGamesManager);
        // add virtual view to rmiServerGamesManager
        GamesManager.getGamesManager().addPlayerToPending("Player1", true, false);
        GamesManager.getGamesManager().addVirtualView("Player1", newRmiClient);

        GamesManager.getGamesManager().joinNewGame("Player1", TokenColor.GREEN, -1);

        assertEquals(GamesManager.getGamesManager().getCommandResult(),CommandResult.WRONG_PLAYERS_NUMBER);
    }
    @Test
    void joinNewGameUnSuccess2() throws RemoteException {
        newRmiClient = new RmiClient("Player1", true, rmiServerGamesManager);
        // add virtual view to rmiServerGamesManager
        GamesManager.getGamesManager().addPlayerToPending("Player1", true, false);
        GamesManager.getGamesManager().addVirtualView("Player1", newRmiClient);

        GamesManager.getGamesManager().joinNewGame("Player1", TokenColor.GREEN, 5);

        assertEquals(GamesManager.getGamesManager().getCommandResult(),CommandResult.WRONG_PLAYERS_NUMBER);
    }
    @Test
    void addPlayerToPendingSuccessful() {
        gm.addPlayerToPending("Player1", true, true);
        assertEquals(gm.getPendingPlayer("Player1").getNickname(),"Player1");
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());
    }

    @Test
    void disconnectPlayerSuccess() {
        gameController.disconnectPlayer("Player1");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.DISCONNECTION_SUCCESSFUL, result);
    }

    @Test
    void playerAlreadyDisconnected() throws InterruptedException {
        gameController.disconnectPlayer("Player1");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.DISCONNECTION_SUCCESSFUL, result);
        // try to disconnect the same player
        gameController.disconnectPlayer("Player1");
        result = gameController.getCommandResult();
        assertEquals(CommandResult.PLAYER_ALREADY_DISCONNECTED, result);
    }
*/
}