package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.AddPlayerToPendingCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.VirtualServerGamesManager;
import it.polimi.ingsw.gc07.network.VirtualView;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.junit.jupiter.api.Assertions.*;

class AddPlayerToPendingCommandTest {
    GamesManager gamesManager;

    @BeforeEach
    void setUp() {
        gamesManager = GamesManager.getGamesManager();
        gamesManager.resetGamesManager();
    }

    @Test
    void addPlayerSuccessful() {
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResult());
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player2", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResult());
    }

    @Test
    void addPlayerUnsuccessful() {
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResult());
        assertThrows(AssertionError.class, () -> {
                gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        });
    }

    @Test
    void addPlayerUnsuccessfulWithJoin() throws NotBoundException, RemoteException {
        // add Player1 to pending players
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResult());

        // make Player1 join a new gameController
        String name = "VirtualServerGamesManager";
        RmiServerGamesManager serverGamesManager = RmiServerGamesManager.getRmiServerGamesManager();
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, serverGamesManager);

        Registry registry2 = LocateRegistry.getRegistry("127.0.0.1", 1234);
        VirtualServerGamesManager rmiServerGamesManager = (VirtualServerGamesManager) registry2.lookup("VirtualServerGamesManager");
        RmiClient newRmiClient = new RmiClient("Player1", true, rmiServerGamesManager);
        newRmiClient.connectToGamesManagerServer(true, true);
        gamesManager.setAndExecuteCommand(new JoinNewGameCommand("Player1", TokenColor.GREEN, 3));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResult());

        // try to add Player1 to pending players
        assertThrows(RuntimeException.class, () -> {
            Registry registry3 = LocateRegistry.getRegistry("127.0.0.1", 1234);
            VirtualServerGamesManager rmiServerGamesManager2 = (VirtualServerGamesManager) registry3.lookup("VirtualServerGamesManager");
            RmiClient newRmiClient2 = new RmiClient("Player1", true, rmiServerGamesManager2);
            newRmiClient2.connectToGamesManagerServer(true, true);
            gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", false, true));
        });
    }
}