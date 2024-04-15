package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinExistingGameCommandTest {
    GamesManager gamesManager;
    int gameId;

    @BeforeEach
    void setUp() {
        gamesManager = new GamesManager();
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand(gamesManager, "P1", true, true));
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand(gamesManager, "P2", true, true));
        gamesManager.setAndExecuteCommand(new JoinNewGameCommand(gamesManager, "P1", TokenColor.GREEN, 4));
    }
    @Test
    void JoinExistingGameSuccess() {
        gamesManager.setAndExecuteCommand(new JoinExistingGameCommand(gamesManager, "P2", TokenColor.RED, 0));
        //assert mancante
        assertNull(gamesManager.getPendingPlayer("P2"));
        boolean found;
        found = false;
        for(Game g : gamesManager.getGames()){
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
        gamesManager.setAndExecuteCommand(new JoinExistingGameCommand(gamesManager, "P2", TokenColor.RED, 1));
        //assert mancante
        assertNotNull(gamesManager.getPendingPlayer("P2"));
        boolean found;
        found = false;
        for(Game g : gamesManager.getGames()){
            for(Player p : g.getPlayers()){
                if(p.getNickname().equals("P2")){
                    found = true;
                }
            }
        }
        assertFalse(found);
    }
}