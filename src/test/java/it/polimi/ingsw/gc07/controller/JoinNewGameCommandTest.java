package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.AddPlayerToPendingCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class JoinNewGameCommandTest {
    GamesManager gamesManager;
    int playersNumber;

    @BeforeEach
    void setUp() {
        gamesManager = GamesManager.getGamesManager();
        gamesManager.resetGamesManager();
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", false, false));
    }

    @Test
    void joinNewGameSuccess() throws RemoteException {
        playersNumber = 3;
        gamesManager.setAndExecuteCommand(new JoinNewGameCommand("Player1", TokenColor.RED, playersNumber));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResult());
        assertNull(gamesManager.getPendingPlayer("Player1"));
        boolean found = false;
        for (GameController g: gamesManager.getGames()){
            for(Player p: g.getPlayers()){
                if (p.getNickname().equals("Player1")){
                    found = true;
                }
            }
        }
        assertTrue(found);
    }

    @Test
    void joinNewGameTwice(){
        //TODO da fare
    }
}