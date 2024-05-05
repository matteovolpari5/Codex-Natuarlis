package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.AddChatPublicMessageCommand;
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

class AddChatPublicMessageCommandTest {
    private GameController gameController;

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
        gameController.addPlayer(null, firstPlayer);
        Player secondPlayer = new Player("Player2", false, false);
        gameController.addPlayer(null, secondPlayer);
    }

    @Test
    void addMessageSuccess() {
        gameController.setAndExecuteCommand(new AddChatPublicMessageCommand("My content...", "Player1"));
        CommandResult result = gameController.getCommandResult();        assertEquals(CommandResult.SUCCESS, result);
        gameController.setAndExecuteCommand(new AddChatPublicMessageCommand("My other content....", "Player2"));
        result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void addMessageWrongSender() {
        gameController.setAndExecuteCommand(new AddChatPublicMessageCommand("My content...", "WrongPlayer"));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.WRONG_SENDER, result);
    }
}