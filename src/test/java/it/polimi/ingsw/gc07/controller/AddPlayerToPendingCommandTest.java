package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.game_commands.AddPlayerToPendingCommand;
import it.polimi.ingsw.gc07.game_commands.JoinNewGameCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddPlayerToPendingCommandTest {
    GamesManager gamesManager;

    @BeforeEach
    void setUp() {
        gamesManager = GamesManager.getNewGamesManager();
    }

    @Test
    void addPlayerSuccessful() {
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResultManager().getCommandResult());
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player2", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResultManager().getCommandResult());
    }

    @Test
    void addPlayerUnsuccessful() {
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResultManager().getCommandResult());
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.PLAYER_ALREADY_PRESENT, gamesManager.getCommandResultManager().getCommandResult());
    }

    @Test
    void addPlayerUnsuccessfulWithJoin() {
        // add Player1 to pending players
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.SUCCESS, gamesManager.getCommandResultManager().getCommandResult());

        // make Player1 join a new gameController
        gamesManager.setAndExecuteCommand(new JoinNewGameCommand("Player1", TokenColor.GREEN, 3));
        assertEquals(CommandResult.CREATE_SERVER_GAME, gamesManager.getCommandResultManager().getCommandResult());

        // try to add Player1 to pending players
        gamesManager.setAndExecuteCommand(new AddPlayerToPendingCommand("Player1", true, true));
        assertEquals(CommandResult.PLAYER_ALREADY_PRESENT, gamesManager.getCommandResultManager().getCommandResult());
    }
}