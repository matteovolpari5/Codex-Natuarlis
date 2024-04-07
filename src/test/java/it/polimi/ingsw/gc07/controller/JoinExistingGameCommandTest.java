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
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "P1", true, true));
        gamesManager.execute();
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "P2", true, true));
        gamesManager.execute();
        gamesManager.setGameCommand(new JoinNewGameCommand(gamesManager, "P1", TokenColor.GREEN, 4));
        gamesManager.execute();
    }
    @Test
    void JoinExistingGameSuccess() {
        gamesManager.setGameCommand(new JoinExistingGameCommand(gamesManager, "P2", TokenColor.RED, 0));
        gamesManager.execute();
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
        gamesManager.setGameCommand(new JoinExistingGameCommand(gamesManager, "P2", TokenColor.RED, 1));
        gamesManager.execute();
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