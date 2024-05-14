package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.AddPlayerToPendingCommand;
import it.polimi.ingsw.gc07.game_commands.JoinExistingGameCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.junit.jupiter.api.Assertions.*;

class JoinExistingGameCommandTest {
    GamesManager gamesManager;

    @BeforeEach
    void setUp() throws RemoteException, NotBoundException {
        String name = "VirtualServerGamesManager";
        RmiServerGamesManager serverGamesManager = RmiServerGamesManager.getRmiServerGamesManager();
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, serverGamesManager);

        Registry registry2 = LocateRegistry.getRegistry("127.0.0.1", 1234);
        VirtualServerGamesManager rmiServerGamesManager = (VirtualServerGamesManager) registry2.lookup("VirtualServerGamesManager");
        RmiClient newRmiClient = new RmiClient("P1", true, rmiServerGamesManager);
        newRmiClient.connectToGamesManagerServer(true, true);
        GamesManager.getGamesManager().setAndExecuteCommand(new AddPlayerToPendingCommand("P1", true, true));
        try {
            rmiServerGamesManager.connect("P1", new RmiClient("P1", true, rmiServerGamesManager));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        VirtualServerGamesManager rmiServerGamesManager2 = (VirtualServerGamesManager) registry2.lookup("VirtualServerGamesManager");
        RmiClient newRmiClient2 = new RmiClient("P2", true, rmiServerGamesManager2);
        newRmiClient2.connectToGamesManagerServer(true, true);
        GamesManager.getGamesManager().setAndExecuteCommand(new AddPlayerToPendingCommand("P2", true, true));
        try {
            rmiServerGamesManager.connect("P2",new RmiClient("P2", true, rmiServerGamesManager));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        GamesManager.getGamesManager().setAndExecuteCommand(new JoinNewGameCommand("P1", TokenColor.GREEN, 4));
        gamesManager = GamesManager.getGamesManager();
    }

    @Test
    void JoinExistingGameSuccess() {
        gamesManager.setAndExecuteCommand(new JoinExistingGameCommand("P2", TokenColor.RED, 0));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResult());
        assertNull(gamesManager.getPendingPlayer("P2"));
        boolean found;
        found = false;
        for(GameController g : gamesManager.getGames()){
            for(Player p : g.getPlayers()){
                if(p.getNickname().equals("P2")){
                    found = true;
                }
            }
        }
        assertTrue(found);
    }

    @Test
    void JoinExistingGameFail() {
        gamesManager.setAndExecuteCommand(new JoinExistingGameCommand("P2", TokenColor.RED, 1));
        assertEquals(CommandResult.GAME_NOT_PRESENT, gamesManager.getCommandResult());
        assertNotNull(gamesManager.getPendingPlayer("P2"));
        boolean found;
        found = false;
        for(GameController g : gamesManager.getGames()){
            for(Player p : g.getPlayers()){
                if(p.getNickname().equals("P2")){
                    found = true;
                }
            }
        }
        assertFalse(found);
    }
}