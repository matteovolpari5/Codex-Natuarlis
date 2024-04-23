package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
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

class ReconnectPlayerCommandTest {
    GameController gameController;
    @BeforeEach
    void setUp() {
        // create a gameController
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
        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);

        Player firstPlayer = new Player("Player1", true, false);
        gameController.addPlayer(firstPlayer);
        CommandResult result = gameController.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        Player secondPlayer = new Player("Player2", false, false);
        gameController.addPlayer(secondPlayer);
        result = gameController.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
    }
    @Test
    void reconnectPlayerSuccess()
    {
        gameController.getPlayers().getFirst().setIsConnected(false);
        gameController.reconnectPlayer("Player1");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void reconnectToAWrongGame()
    {
        GameController gameController2;
        // create another gameController
        int id = 1;
        int playersNumber = 2;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        starterCardsDecks.shuffle();
        gameController2 = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        Player firstPlayer = new Player("P1", true, false);
        gameController2.addPlayer(firstPlayer);
        CommandResult result = gameController2.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        Player secondPlayer = new Player("P2", false, false);
        gameController2.addPlayer(secondPlayer);
        result = gameController2.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();

        gameController2.getPlayers().getFirst().setIsConnected(false);
        gameController2.reconnectPlayer("Player1");
        CommandResult result2 = gameController2.getCommandResult();
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, result2);
    }

    @Test
    void alreadyConnected()
    {
        gameController.getPlayers().getFirst().setIsConnected(false);
        gameController.reconnectPlayer("Player1");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);

        gameController.reconnectPlayer("Player1");
        result = gameController.getCommandResult();
        assertEquals(CommandResult.PLAYER_ALREADY_CONNECTED, result);
    }

}