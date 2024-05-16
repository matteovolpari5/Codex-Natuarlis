package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.PlaceStarterCardCommand;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceStarterCardCommandTest {

    GameController gameController;

    @BeforeEach
    void setUp(){
        int id = 0;
        int playersNumber = 2;
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);

        Player p1 = new Player("P1", true, true);
        p1.setTokenColor(TokenColor.GREEN);
        gameController.addPlayer(p1, null);
        // add second player
        Player p2 = new Player("P2", true, true);
        p2.setTokenColor(TokenColor.BLUE);
        gameController.addPlayer(p2, null);
        gameController.setCurrentPlayer(0);
        gameController.getPlayers().get(1).setIsStalled(true);
    }

    @Test
    void PlaceStarterCardSuccess() {
        gameController.setAndExecuteCommand(new PlaceStarterCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), true));
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }
}