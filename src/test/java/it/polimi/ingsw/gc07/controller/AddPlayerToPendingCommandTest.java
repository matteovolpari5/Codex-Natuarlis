package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddPlayerToPendingCommandTest {
    GamesManager gamesManager;

    @BeforeEach
    void setUp() {
        gamesManager = GamesManager.getGamesManager();
    }

    @Test
    void addPlayerSuccessful() {
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player2", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success
    }

    @Test
    void addPlayerUnsuccessful() {
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto NOT success
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto NOT success
    }

    @Test
    void addPlayerUnsuccessfulWithJoin() {
        // add Player1 to pending players
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success

        // make Player1 join a new game
        gamesManager.setAndExecuteCommand(new JoinNewGameCommand("Player1", TokenColor.GREEN, 3));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto success

        // try to add Player1 to pending players
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        //TODO assert sulla bandierina / eccezione, in base a cosa useremo
        // mi aspetto NOT success
        // i nomi dei giocatori devono essere diversi anche tra game diversi !!!
    }
}