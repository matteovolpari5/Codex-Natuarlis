package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddPlayerToPendingsCommandTest {
    GamesManager gamesManager;

    @BeforeEach
    void setUp() {
        gamesManager = new GamesManager();
    }

    @Test
    void addPlayerSuccessful() {
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "Player2", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success
    }

    @Test
    void addPlayerUnsuccessful() {
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto NOT success
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto NOT success
    }

    @Test
    void addPlayerUnsuccessfulWithJoin() {
        // add Player1 to pending players
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success

        // make Player1 join a new game
        gamesManager.setGameCommand(new JoinNewGameCommand(gamesManager, "Player1", TokenColor.GREEN, 3));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success

        // try to add Player1 to pending players
        gamesManager.setGameCommand(new AddPlayerToPendingsCommand(gamesManager, "Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto NOT success
        // i nomi dei giocatori devono essere diversi anche tra game diversi !!!
    }
}