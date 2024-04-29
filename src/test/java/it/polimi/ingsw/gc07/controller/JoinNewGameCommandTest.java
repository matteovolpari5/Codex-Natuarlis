package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.AddPlayerToPendingCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinNewGameCommandTest {
    GamesManager gamesManager;
    int playersNumber;

    @BeforeEach
    void setUp() {
        gamesManager = GamesManager.getGamesManager();
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", false, false));
    }

    @Test
    void joinNewGameSuccess(){
        playersNumber = 3;
        gamesManager.setAndExecuteCommand(new JoinNewGameCommand("Player1", TokenColor.RED, playersNumber));
        assertEquals(CommandResult.CREATE_SERVER_GAME, gamesManager.getCommandResult());
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

    }
}