package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawFaceUpCardCommandTest {
    Game game;
    @BeforeEach
    void setUp() {
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
        Player p1 = new Player("P1", true, true);
        p1.setTokenColor(TokenColor.GREEN);
        game.setAndExecuteCommand(new AddPlayerCommand(game, p1));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        Player p2 = new Player("P2", true, true);
        p2.setTokenColor(TokenColor.YELLOW);
        game.setAndExecuteCommand(new AddPlayerCommand(game, p2));
        result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        game.getPlayers().get(0).setSecretObjective(game.getObjectiveCardsDeck().drawCard());
        game.getPlayers().get(1).setSecretObjective(game.getObjectiveCardsDeck().drawCard());
        game.setCurrentPlayer(0);
    }
    /*
    @Test
    void drawFaceUpCardWrongState() {
        game.setState(GameState.GAME_STARTING);
        game.setAndExecuteCommand(new DrawFaceUpCardCommand(game, "P1", CardType.RESOURCE_CARD, 0));
        assertEquals(game.getCommandResultManager().getCommandResult(), CommandResult.WRONG_STATE);
    }

    @Test
    void drawFaceUpCardWrongPlayer() {
        game.setAndExecuteCommand(new DrawFaceUpCardCommand(game, "P2", CardType.RESOURCE_CARD, 0));
        assertEquals(game.getCommandResultManager().getCommandResult(), CommandResult.WRONG_PLAYER);
    }

    @Test
    void drawFaceUpCardWrongCardType() {
        game.setAndExecuteCommand(new DrawFaceUpCardCommand(game, "P1", CardType.OBJECTIVE_CARD, 0));
        assertEquals(game.getCommandResultManager().getCommandResult(), CommandResult.WRONG_CARD_TYPE);
    }*/

    @Test
    void drawFaceUpCardResourceCardSuccess() {
        System.out.println("inizio:");
        for( DrawableCard c : game.getPlayers().get(0).getCurrentHand()){
            System.out.println(c.getId());

        }
        System.out.println("---");
        DrawableCard card = game.getPlayers().get(0).getCurrentHand().get(0);
        game.getPlayers().get(0).removeCardHand(card);
        System.out.println("rimossa:");
        for( Card c : game.getPlayers().get(0).getCurrentHand()){
            System.out.println(c.getId());

        }
        System.out.println("---");
        int id = game.getResourceCardsDeck().revealFaceUpCard(0).getId();
        game.setAndExecuteCommand(new DrawFaceUpCardCommand(game, "P1", CardType.RESOURCE_CARD, 0));
        System.out.println("dopo pescaggio:");
        for( DrawableCard c : game.getPlayers().get(0).getCurrentHand()){
            System.out.println(c.getId());
        }
        assertEquals(game.getCommandResultManager().getCommandResult(), CommandResult.SUCCESS);
        assertEquals(id, game.getPlayers().get(0).getCurrentHand().get(0).getId());
        //assertNotEquals(id, game.getResourceCardsDeck().revealFaceUpCard(0).getId());
    }
}