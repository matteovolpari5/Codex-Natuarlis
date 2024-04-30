package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.DisconnectPlayerCommand;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectPlayerCommandTest {
    GameController gameController;

    @BeforeEach
    void setUp() {
        // create a gameController
        int id = 0;
        int playersNumber = 2;
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        starterCardsDecks.shuffle();
        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        Player firstPlayer = new Player("Player1", true, false);
        gameController.addPlayer(firstPlayer);
        Player secondPlayer = new Player("Player2", false, false);
        gameController.addPlayer(secondPlayer);
    }

    @Test
    void disconnectPlayerSuccess() {
        gameController.setAndExecuteCommand(new DisconnectPlayerCommand("Player1"));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void playerAlreadyDisconnected() {
        // disconnect player
        gameController.setAndExecuteCommand(new DisconnectPlayerCommand("Player2"));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
        // try to disconnect the same player
        gameController.setAndExecuteCommand(new DisconnectPlayerCommand("Player2"));
        result = gameController.getCommandResult();
        assertEquals(CommandResult.PLAYER_ALREADY_DISCONNECTED, result);
    }

    @Test
    void disconnectPlayerNotPresent() {
        // disconnect player not present in the gameController
        gameController.setAndExecuteCommand(new DisconnectPlayerCommand("AnOtherPlayer"));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, result);
    }
}