package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.game_commands.AddChatPrivateMessageControllerCommand;
import it.polimi.ingsw.gc07.game_commands.JoinExistingGameCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
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

class AddChatPrivateMessageCommandTest {
    RmiClient newRmiClient;
    RmiClient newRmiClient2;
    RmiServerGamesManager serverGamesManager;
    VirtualServerGamesManager rmiServerGamesManager;

    @BeforeEach
    void setUp() throws RemoteException, NotBoundException {
        String name = "VirtualServerGamesManager";
        serverGamesManager = RmiServerGamesManager.getRmiServerGamesManager();
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, serverGamesManager);

        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
        rmiServerGamesManager = (VirtualServerGamesManager) registry.lookup("VirtualServerGamesManager");
        newRmiClient = new RmiClient("Player1", true, rmiServerGamesManager);
        // add virtual view to rmiServerGamesManager
        newRmiClient.connectToGamesManagerServer(true, false);
        newRmiClient.runCliJoinGame();
        newRmiClient.setAndExecuteCommand(new JoinNewGameCommand("Player1", TokenColor.GREEN, 2));

        newRmiClient2 = new RmiClient("Player2", true, rmiServerGamesManager);
        // add virtual view to rmiServerGamesManager
        newRmiClient2.connectToGamesManagerServer(true, false);
        newRmiClient2.runCliJoinGame();
        newRmiClient2.setAndExecuteCommand(new JoinExistingGameCommand("Player2", TokenColor.RED, 0));
    }

    @Test
    void addMessageSuccess() throws RemoteException {
        newRmiClient.setAndExecuteCommand(new AddChatPrivateMessageControllerCommand("My content...", "Player1", "Player2"));
        CommandResult result = GamesManager.getGamesManager().getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);

        //TODO: buttare giù server
    }

    @Test
    void addMessageWrongSender() {
        newRmiClient.setAndExecuteCommand(new AddChatPrivateMessageControllerCommand("My content...", "WrongSender", "Player1"));
        CommandResult result = GamesManager.getGamesManager().getCommandResult();
        assertEquals(CommandResult.WRONG_SENDER, result);

        //TODO: buttare giù server
    }

    @Test
    void addMessageWrongReceiver() throws RemoteException, NotBoundException {
        newRmiClient.setAndExecuteCommand(new AddChatPrivateMessageControllerCommand("My content...", "Player3", "WrongReceiver"));
        CommandResult result = GamesManager.getGamesManager().getCommandResult();
        assertEquals(CommandResult.WRONG_RECEIVER, result);

        //TODO: buttare giù server
    }
}