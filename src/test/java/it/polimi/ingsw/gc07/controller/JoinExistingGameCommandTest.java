package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.AddPlayerToPendingCommand;
import it.polimi.ingsw.gc07.game_commands.JoinExistingGameCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinExistingGameCommandTest {
    GamesManager gamesManager;

    @BeforeEach
    void setUp() {
        gamesManager = GamesManager.getGamesManager();
        gamesManager.resetGamesManager();
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("P1", false, true));
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("P2", false, true));
        gamesManager.setAndExecuteCommand(new JoinNewGameCommand("P1", TokenColor.GREEN, 4));
    }
    @Test
    void JoinExistingGameSuccess() {
        gamesManager.setAndExecuteCommand(new JoinExistingGameCommand("P2", TokenColor.RED, 0));
        assertEquals(CommandResult.SET_SERVER_GAME, gamesManager.getCommandResult());
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