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

class ReconnectPlayerCommandTest {
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
        game.setCommand(new AddPlayerCommand(game, firstPlayer));
        game.execute();
        CommandResult result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        Player secondPlayer = new Player("Player2", false, false);
        game.setCommand(new AddPlayerCommand(game, secondPlayer));
        game.execute();
        result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
    }
    @Test
    void reconnectPlayerSuccess()
    {
        game.getPlayers().getFirst().setIsConnected(false);
        game.setCommand(new ReconnectPlayerCommand(game, "Player1"));
        game.execute();
        CommandResult result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void reconnectToAWrongGame()
    {
        Game game2;
        // create another game
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
        game2 = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        Player firstPlayer = new Player("P1", true, false);
        game2.setCommand(new AddPlayerCommand(game2, firstPlayer));
        game2.execute();
        CommandResult result = game2.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        Player secondPlayer = new Player("P2", false, false);
        game2.setCommand(new AddPlayerCommand(game2, secondPlayer));
        game2.execute();
        result = game2.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();

        game2.getPlayers().getFirst().setIsConnected(false);
        game2.setCommand(new ReconnectPlayerCommand(game2, "Player1"));
        game2.execute();
        CommandResult result2 = game2.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, result2);
    }

    @Test
    void alreadyConnected()
    {
        game.getPlayers().getFirst().setIsConnected(false);
        game.setCommand(new ReconnectPlayerCommand(game, "Player1"));
        game.execute();
        CommandResult result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);

        game.setCommand(new ReconnectPlayerCommand(game, "Player1"));
        game.execute();
        result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.PLAYER_ALREADY_CONNECTED, result);
    }

}