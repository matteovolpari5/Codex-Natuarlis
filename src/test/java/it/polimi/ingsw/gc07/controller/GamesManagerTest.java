package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.NicknameCheck;
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
        assertEquals(gm.checkNickname("player1"), NicknameCheck.NEW_NICKNAME);
        RmiClient newRmiClient = new RmiClient("player1", false, serverGamesManager);
        newRmiClient.connectToGamesManagerServer(false);
        RmiClient newRmiClient2 = new RmiClient("player2", false, serverGamesManager);
        newRmiClient2.connectToGamesManagerServer(false);
        assertNull(gm.getGameById(0));
        gm.joinNewGame("player1", TokenColor.GREEN, 2);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        assertEquals(gm.checkNickname("player1"), NicknameCheck.EXISTING_NICKNAME);
        gm.joinExistingGame("player2", TokenColor.GREEN, 0);
        assertEquals(CommandResult.TOKEN_COLOR_ALREADY_TAKEN, gm.getCommandResult());

        gm.joinExistingGame("player2", TokenColor.RED, 1);
        assertEquals(CommandResult.GAME_NOT_PRESENT, gm.getCommandResult());

        gm.joinNewGame("player2", TokenColor.GREEN, -1);
        assertEquals(CommandResult.WRONG_PLAYERS_NUMBER, gm.getCommandResult());

        gm.joinNewGame("player2", TokenColor.GREEN, 5);
        assertEquals(CommandResult.WRONG_PLAYERS_NUMBER, gm.getCommandResult());

        gm.joinExistingGame("player2", TokenColor.RED, 0);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        RmiClient newRmiClient4 = new RmiClient("player4", false, serverGamesManager);

        gm.addPlayerToPending("player4",true,false);
        gm.addVirtualView("player4",newRmiClient4);
        gm.displayExistingGames("player4");
        assertNotNull(gm.getFreeGamesDetails());
        gm.joinExistingGame("player4", TokenColor.RED, 0);
        assertEquals(CommandResult.GAME_FULL, gm.getCommandResult());
        gm.joinNewGame("player4", TokenColor.RED,3);
        assertEquals(CommandResult.SUCCESS, gm.getCommandResult());

        assertNotNull(gm.getGameById(0));

        assertEquals(0, gm.getGameIdWithPlayer("player1"));

        assertEquals(-1, gm.getGameIdWithPlayer("player5"));

        GameController gc = GamesManager.getGamesManager().getGameById(0);
        assertEquals(GameState.PLACING_STARTER_CARDS, gc.getState());

        gc.setAndExecuteCommand(new PlaceStarterCardCommand("player1", false));
        gc.disconnectPlayer("player2");
        assertEquals(gm.checkNickname("player2"), NicknameCheck.RECONNECTION);
        gc.reconnectPlayer(newRmiClient2,"player2",true, false);
        gc.disconnectPlayer("WrongPlayer");
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, gc.getCommandResult());

        gc.disconnectPlayer("player1");
        assertEquals(GameState.WAITING_RECONNECTION, gc.getState());

        gc.disconnectPlayer("player1");
        assertEquals(GameState.WAITING_RECONNECTION, gc.getState());

        gm.reconnectPlayer(newRmiClient,"player1",true,false);

        assertEquals(GameState.PLAYING, gc.getState());

        String nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        gc.placeCard(nick,0,41,41,true);
        gc.disconnectPlayer(nick);
        assertEquals(gc.getPlayerByNickname(nick).getCurrentHand().size(), 3);
        gm.reconnectPlayer(newRmiClient,nick,true,false);

        nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        gc.disconnectPlayer(nick);
        gm.reconnectPlayer(newRmiClient,nick,true,false);

        for(int i=0;i<40;i++)
        {
            gm.getGameById(0).getResourceCardsDeck().drawFaceUpCard(0);
        }
        nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        gm.getGameById(0).placeCard(nick,0,41,41,true);

        gc.disconnectPlayer(nick);

        assertEquals(gc.getPlayerByNickname(nick).getCurrentHand().size(), 3);
        gm.reconnectPlayer(newRmiClient,nick,true,false);

        nick = gm.getGameById(0).getPlayers().get(gm.getGameById(0).getCurrPlayer()).getNickname();
        for(int i=0;i<40;i++)
        {
            gm.getGameById(0).getGoldCardsDeck().drawFaceUpCard(0);
        }
        gm.getGameById(0).placeCard(nick,0,41,41,true);
        gc.disconnectPlayer(nick);
        assertEquals(gc.getPlayerByNickname(nick).getCurrentHand().size(), 2);
        gm.reconnectPlayer(newRmiClient,nick,true,false);

        gc.disconnectPlayer("player1");
        gc.disconnectPlayer("player2");
        assertEquals(GameState.NO_PLAYERS_CONNECTED,gc.getState());

        gm.getGameById(0).setState(GameState.GAME_ENDED);
        gm.deleteGame(0);
        assertNull(gm.getGameById(0));
    }
}