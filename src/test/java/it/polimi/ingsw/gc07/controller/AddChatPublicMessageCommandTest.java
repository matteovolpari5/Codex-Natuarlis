package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.CommandResult;
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

class AddChatPublicMessageCommandTest {
    private Game game;

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
        game.setAndExecuteCommand(new AddPlayerCommand(firstPlayer));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        Player secondPlayer = new Player("Player2", false, false);
        game.setAndExecuteCommand(new AddPlayerCommand(secondPlayer));
        result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
    }

    @Test
    void addMessageSuccess() {
        game.setAndExecuteCommand(new AddChatPublicMessageCommand("My content...", "Player1"));
        CommandResult result = game.getCommandResultManager().getCommandResult();        assertEquals(CommandResult.SUCCESS, result);
        game.setAndExecuteCommand(new AddChatPublicMessageCommand("My other content....", "Player2"));
        result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void addMessageWrongSender() {
        game.setAndExecuteCommand(new AddChatPublicMessageCommand("My content...", "WrongPlayer"));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.WRONG_SENDER, result);
    }
}