package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.AddChatPrivateMessageCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
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

class AddChatPrivateMessageCommandTest {
    GameController gameController;

    @BeforeEach
    void setUp() {
        // create a gameController
        int id = 0;
        int playersNumber = 3;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        starterCardsDecks.shuffle();
        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);

        // add first player
        Player firstPlayer = new Player("Player1", true, false);
        gameController.addPlayer(firstPlayer);
        // add second player
        Player secondPlayer = new Player("Player2", false, false);
        gameController.addPlayer(secondPlayer);
        // add third player
        Player thirdPlayer = new Player("Player3", false, false);
        gameController.addPlayer(thirdPlayer);
    }

    @Test
    void addMessageSuccess() {
        gameController.setAndExecuteCommand(new AddChatPrivateMessageCommand("My content...", "Player1", "Player3"));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void addMessageWrongSender() {
        gameController.setAndExecuteCommand(new AddChatPrivateMessageCommand("My content...", "WrongSender", "Player1"));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.WRONG_SENDER, result);
    }

    @Test
    void addMessageWrongReceiver() {
        gameController.setAndExecuteCommand(new AddChatPrivateMessageCommand("My content...", "Player3", "WrongReceiver"));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.WRONG_RECEIVER, result);
    }
}