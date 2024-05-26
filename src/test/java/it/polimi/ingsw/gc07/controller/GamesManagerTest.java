package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.NicknameCheck;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GamesManagerTest {
    GamesManager gm;
    RmiServerGamesManager serverGamesManager;

    @BeforeEach
    void setUp() {
        gm = GamesManager.getGamesManager();
        gm.resetGamesManager();
        serverGamesManager = RmiServerGamesManager.getRmiServerGamesManager();
        serverGamesManager.reset();
    }

    @Test
    void singletonTest() {
        GamesManager gm1 = GamesManager.getGamesManager();
        GamesManager gm2 = GamesManager.getGamesManager();
        assertSame(gm1, gm2);
    }

    @Test
    void testJoinGameSuccess() throws RemoteException {
        // add player 1
        gm.addPlayerToPending("player1", true, false);
        RmiClient player1VirtualView = new RmiClient("player1", false, serverGamesManager);
        gm.addVirtualView("player1", player1VirtualView);

        // add player 2
        gm.addPlayerToPending("player2", true, false);
        RmiClient player2VirtualView = new RmiClient("player2", false, serverGamesManager);
        gm.addVirtualView("player2", player2VirtualView);

        // join player 1
        assertNull(gm.getGameById(0));
        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());
        assertEquals(gm.checkNickname("player1"), NicknameCheck.EXISTING_NICKNAME);

        // join player 2
        assertTrue(gm.getFreeGamesPlayerNumber().containsKey(0));
        assertEquals(2, gm.getFreeGamesPlayerNumber().get(0));
        gm.joinExistingGame("player2", TokenColor.RED, 0);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());
    }

    @Test
    void testJoinGameNotSuccess() throws RemoteException {
        // add player 1
        gm.addPlayerToPending("player1", true, false);
        RmiClient player1VirtualView = new RmiClient("player1", false, serverGamesManager);
        gm.addVirtualView("player1", player1VirtualView);

        // add player 2
        gm.addPlayerToPending("player2", true, false);
        RmiClient player2VirtualView = new RmiClient("player2", false, serverGamesManager);
        gm.addVirtualView("player2", player2VirtualView);

        // join player 1
        assertNull(gm.getGameById(0));
        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());
        assertEquals(gm.checkNickname("player1"), NicknameCheck.EXISTING_NICKNAME);

        // join not success player 2
        gm.joinExistingGame("player2", TokenColor.GREEN, 0);
        assertEquals(CommandResult.TOKEN_COLOR_ALREADY_TAKEN, gm.getCommandResult());
        gm.joinExistingGame("player2", TokenColor.RED, 1);
        assertEquals(CommandResult.GAME_NOT_PRESENT, gm.getCommandResult());
        gm.joinNewGame("player2", TokenColor.GREEN, -1);
        assertEquals(CommandResult.WRONG_PLAYERS_NUMBER, gm.getCommandResult());
        gm.joinNewGame("player2", TokenColor.GREEN, 5);
        assertEquals(CommandResult.WRONG_PLAYERS_NUMBER, gm.getCommandResult());
    }

    @Test
    void testJoinGameFull() throws RemoteException {
        // add first player
        gm.addPlayerToPending("player1", true, false);
        RmiClient player1VirtualView = new RmiClient("player1", false, serverGamesManager);
        gm.addVirtualView("player1", player1VirtualView);

        // add second player
        gm.addPlayerToPending("player2", true, false);
        RmiClient player2VirtualView = new RmiClient("player2", false, serverGamesManager);
        gm.addVirtualView("player2", player2VirtualView);

        // join game
        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        gm.joinExistingGame("player2", TokenColor.RED, 0);

        // add other players
        gm.addPlayerToPending("player4", true, false);
        RmiClient newRmiClient4 = new RmiClient("player4", false, serverGamesManager);
        gm.addVirtualView("player4", newRmiClient4);

        // join full game
        gm.displayExistingGames("player4");
        gm.joinExistingGame("player4", TokenColor.RED, 0);
        assertEquals(CommandResult.GAME_FULL, gm.getCommandResult());

        // join new game
        gm.joinNewGame("player4", TokenColor.RED,3);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        assertNotNull(gm.getGameById(0));
        assertEquals(0, gm.getGameIdWithPlayer("player1"));
        assertEquals(-1, gm.getGameIdWithPlayer("player5"));
    }

    @Test
    void testFullGameFlow() throws RemoteException, InterruptedException {
        // add first player
        gm.addPlayerToPending("player1", true, false);
        RmiClient player1VirtualView = new RmiClient("player1", false, serverGamesManager);
        gm.addVirtualView("player1", player1VirtualView);

        // add second player
        gm.addPlayerToPending("player2", true, false);
        RmiClient player2VirtualView = new RmiClient("player2", false, serverGamesManager);
        gm.addVirtualView("player2", player2VirtualView);

        // join game
        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        gm.joinExistingGame("player2", TokenColor.RED, 0);

        GameController gc = GamesManager.getGamesManager().getGameById(0);
        assertEquals(GameState.PLACING_STARTER_CARDS, gc.getState());

        // place starter card
        gc.placeStarterCard("player1", false);

        // disconnect and reconnect
        gc.disconnectPlayer("player2");
        assertEquals(gm.checkNickname("player2"), NicknameCheck.RECONNECTION);
        gc.reconnectPlayer(player2VirtualView,"player2",true, false);

        gc.disconnectPlayer("player1");
        assertEquals(GameState.WAITING_RECONNECTION, gc.getState());
        gc.disconnectPlayer("player1");
        assertEquals(GameState.WAITING_RECONNECTION, gc.getState());

        gm.reconnectPlayer(player1VirtualView,"player1",true,false);

        // reconnect wrong player
        gc.disconnectPlayer("WrongPlayer");
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, gc.getCommandResult());

        assertEquals(GameState.PLAYING, gc.getState());

        // place card
        String nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        gm.getGameById(0).placeCard(nick,0,41,41,true);
        gc.disconnectPlayer(nick);
        assertEquals(gc.getPlayerByNickname(nick).getCurrentHand().size(), 3);
        gm.reconnectPlayer(player1VirtualView,nick,true,false);

        // disconnect
        nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        gc.disconnectPlayer(nick);
        gm.reconnectPlayer(player1VirtualView,nick,true,false);

        for(int i=0;i<40;i++) {
            gm.getGameById(0).getResourceCardsDeck().drawFaceUpCard(0);
        }
        nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        gm.getGameById(0).placeCard(nick,0,41,41,true);
        gc.disconnectPlayer(nick);
        assertEquals(gc.getPlayerByNickname(nick).getCurrentHand().size(), 3);
        gm.reconnectPlayer(player1VirtualView,nick,true,false);


        for(int i=0;i<40;i++)
        {
            gm.getGameById(0).getGoldCardsDeck().drawFaceUpCard(0);
        }
        nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        gm.getGameById(0).placeCard(nick,0,41,41,true);
        gc.disconnectPlayer(nick);
        gm.reconnectPlayer(player1VirtualView,nick,true,false);

        gc.disconnectPlayer("player1");
        gc.disconnectPlayer("player2");
        assertEquals(GameState.NO_PLAYERS_CONNECTED,gc.getState());

        gc.reconnectPlayer(player1VirtualView,"player1",true,false);
        Thread.sleep(30000);

        assertNull(gm.getGameById(0));
    }
}