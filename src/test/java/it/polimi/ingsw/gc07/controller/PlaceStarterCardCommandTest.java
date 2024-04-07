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
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceStarterCardCommandTest {

    Game game;

    @BeforeEach
    void setUp(){
        int id = 0;
        int playersNumber = 2;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);

        Player p1 = new Player("P1", true, true);
        p1.setTokenColor(TokenColor.GREEN);
        game.setCommand(new AddPlayerCommand(game, p1));
        game.execute();
        CommandResult result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add second player
        Player p2 = new Player("P2", true, true);
        p2.setTokenColor(TokenColor.BLUE);
        game.setCommand(new AddPlayerCommand(game, p2));
        game.execute();
        result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        game.setCurrentPlayer(0);
        game.getPlayers().get(1).setIsStalled(true);
    }

    @Test
    void PlaceStarterCardSuccess() {
        game.setCommand(new PlaceStarterCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), true));
        game.execute();
        CommandResult result = game.getCommandResultManager().getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }
}