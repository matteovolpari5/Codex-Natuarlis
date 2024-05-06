package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.DrawFaceUpCardControllerCommand;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawFaceUpCardCommandTest {
    GameController gameController;
    @BeforeEach
    void setUp() {
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
        Player p1 = new Player("P1", true, true);
        p1.setTokenColor(TokenColor.GREEN);
        gameController.addPlayer(p1);
        Player p2 = new Player("P2", true, true);
        p2.setTokenColor(TokenColor.YELLOW);
        gameController.addPlayer(p2);
        gameController.getPlayers().get(0).setSecretObjective(gameController.getObjectiveCardsDeck().drawCard());
        gameController.getPlayers().get(1).setSecretObjective(gameController.getObjectiveCardsDeck().drawCard());
        gameController.setCurrentPlayer(0);
    }

    @Test
    void drawFaceUpCardWrongState() {
        gameController.setState(GameState.GAME_STARTING);
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand("P1", CardType.RESOURCE_CARD, 0));
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_STATE);
    }

    @Test
    void drawFaceUpCardWrongPlayer() {
        gameController.setState(GameState.PLAYING);
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand("P2", CardType.RESOURCE_CARD, 0));
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_PLAYER);
    }

    @Test
    void drawFaceUpCardWrongCardType() {
        gameController.setState(GameState.PLAYING);
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand("P1", CardType.OBJECTIVE_CARD, 0));
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_CARD_TYPE);
    }

    @Test
    void drawFaceUpCardResourceCardSuccess() {
        gameController.setState(GameState.PLAYING);
        DrawableCard card = gameController.getPlayers().get(0).getCurrentHand().get(0);
        gameController.getPlayers().get(0).removeCardHand(card);
        gameController.setHasCurrPlayerPlaced();
        int id = gameController.getResourceCardsDeck().revealFaceUpCard(0).getId();
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand("P1", CardType.RESOURCE_CARD, 0));
        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);
        assertEquals(id, gameController.getPlayers().get(0).getCurrentHand().get(2).getId());
        assertNotEquals(id, gameController.getResourceCardsDeck().revealFaceUpCard(0).getId());
    }

    @Test
    void drawFaceUpCardGoldCardSuccess() {
        gameController.setState(GameState.PLAYING);
        DrawableCard card = gameController.getPlayers().get(0).getCurrentHand().get(0);
        gameController.getPlayers().get(0).removeCardHand(card);
        gameController.setHasCurrPlayerPlaced();
        int id = gameController.getGoldCardsDeck().revealFaceUpCard(0).getId();
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand("P1", CardType.GOLD_CARD, 0));
        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);
        assertEquals(id, gameController.getPlayers().get(0).getCurrentHand().get(2).getId());
        assertNotEquals(id, gameController.getGoldCardsDeck().revealFaceUpCard(0).getId());
    }

    @Test
    void drawFaceUpCardResourceCardNotSuccess() {
        gameController.setState(GameState.PLAYING);
        DrawableCard card = gameController.getPlayers().get(0).getCurrentHand().get(0);
        gameController.getPlayers().get(0).removeCardHand(card);
        gameController.setHasCurrPlayerPlaced();
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand("P1", CardType.RESOURCE_CARD, 2));
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);
    }

    @Test
    void drawFaceUpCardGoldCardNotSuccess() {
        gameController.setState(GameState.PLAYING);
        DrawableCard card = gameController.getPlayers().get(0).getCurrentHand().get(0);
        gameController.getPlayers().get(0).removeCardHand(card);
        gameController.setHasCurrPlayerPlaced();
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand("P1", CardType.GOLD_CARD, 2));
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);
    }
}