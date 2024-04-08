package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectPlayerCommandTest {
    Game game;

    @BeforeEach
    void setUp() {
        // create a game
        int id = 0;
        int playersNumber = 2;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        starterCardsDecks.shuffle();
        game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        Player firstPlayer = new Player("Player1", true, false);
        game.setAndExecuteCommand(new AddPlayerCommand(game, firstPlayer));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        Player secondPlayer = new Player("Player2", false, false);
        game.setAndExecuteCommand(new AddPlayerCommand(game, secondPlayer));
        result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
    }

    @Test
    void disconnectPlayerSuccess() {
        game.setAndExecuteCommand(new DisconnectPlayerCommand(game, "Player1"));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void playerAlreadyDisconnected() {
        // disconnect player
        game.setAndExecuteCommand(new DisconnectPlayerCommand(game, "Player2"));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
        // try to disconnect the same player
        game.setAndExecuteCommand(new DisconnectPlayerCommand(game, "Player2"));
        result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.PLAYER_ALREADY_DISCONNECTED, result);
    }

    @Test
    void disconnectPlayerNotPresent() {
        // disconnect player not present in the game
        game.setAndExecuteCommand(new DisconnectPlayerCommand(game, "AnOtherPlayer"));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, result);
    }
}