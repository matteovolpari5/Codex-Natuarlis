package it.polimi.ingsw.gc07.controller;

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
        gamesManager = new GamesManager();
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "Player1", false, false));
        gamesManager.execute();
    }

    @Test
    void joinNewGameSuccess(){
        playersNumber = 3;
        gamesManager.setGameCommand(new JoinNewGameCommand(gamesManager, "Player1", TokenColor.RED, playersNumber));
        gamesManager.execute();
        //Controllo bandierina success
        assertNull(gamesManager.getPendingPlayer("Player1"));
        boolean found = false;
        for (Game g: gamesManager.getGames()){
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