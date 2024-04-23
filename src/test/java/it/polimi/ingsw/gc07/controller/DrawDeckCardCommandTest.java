package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.DrawDeckCardCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;
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

class DrawDeckCardCommandTest {
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

        // add first player
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        game.addPlayer(firstPlayer);
        CommandResult result = game.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add second player
        Player secondPlayer = new Player("Player2", false, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        game.addPlayer(secondPlayer);
        result = game.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        game.getPlayers().get(0).setSecretObjective(game.getObjectiveCardsDeck().drawCard());
        game.getPlayers().get(1).setSecretObjective(game.getObjectiveCardsDeck().drawCard());
        game.setCurrentPlayer(0);
        game.setState(GameState.PLAYING);

        // remove a card to Player1, to be able to draw
        firstPlayer.removeCardHand(firstPlayer.getCurrentHand().getFirst());
    }

    @Test
    void DrawDeckCard() {
        game.setAndExecuteCommand(new DrawDeckCardCommand("Player2", CardType.RESOURCE_CARD));
        assertEquals(game.getCommandResult(), CommandResult.WRONG_PLAYER);
        game.setHasCurrPlayerPlaced();
        game.setAndExecuteCommand(new DrawDeckCardCommand("Player1", CardType.OBJECTIVE_CARD));
        assertEquals(game.getCommandResult(), CommandResult.WRONG_CARD_TYPE);
        game.setHasCurrPlayerPlaced();
        game.setAndExecuteCommand(new DrawDeckCardCommand("Player1", CardType.RESOURCE_CARD));
        game.setHasCurrPlayerPlaced();
        assertEquals(game.getCommandResult(), CommandResult.SUCCESS);
        int id = game.getPlayers().get(1).getCurrentHand().getFirst().getId();
        boolean found = false;
        for (DrawableCard c: game.getResourceCardsDeck().getContent()){
            if (c.getId() == id){
                found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    void DrawFromEmptyDeck(){
        DrawableCard resourceCard;
        for (int i = 0; i < 34; i++ ){
            resourceCard = game.getResourceCardsDeck().drawCard();
            game.setHasCurrPlayerPlaced();
        }
        game.setAndExecuteCommand(new DrawDeckCardCommand("Player1", CardType.RESOURCE_CARD));
        assertEquals(game.getCommandResult(), CommandResult.CARD_NOT_PRESENT);
    }
}