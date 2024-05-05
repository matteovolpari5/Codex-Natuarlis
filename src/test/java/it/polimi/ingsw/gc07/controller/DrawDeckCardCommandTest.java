package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.DrawDeckCardCommand;
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

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class DrawDeckCardCommandTest {
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

        // add first player
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        gameController.addPlayer(null, firstPlayer);
        // add second player
        Player secondPlayer = new Player("Player2", false, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        gameController.addPlayer(null, secondPlayer);
        gameController.getPlayers().get(0).setSecretObjective(gameController.getObjectiveCardsDeck().drawCard());
        gameController.getPlayers().get(1).setSecretObjective(gameController.getObjectiveCardsDeck().drawCard());
        gameController.setCurrentPlayer(0);
        gameController.setState(GameState.PLAYING);

        // remove a card to Player1, to be able to draw
        firstPlayer.removeCardHand(firstPlayer.getCurrentHand().getFirst());
    }

    @Test
    void DrawDeckCard() {
        gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player2", CardType.RESOURCE_CARD));
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_PLAYER);
        gameController.setHasCurrPlayerPlaced();
        gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player1", CardType.OBJECTIVE_CARD));
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_CARD_TYPE);
        gameController.setHasCurrPlayerPlaced();
        gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player1", CardType.RESOURCE_CARD));
        gameController.setHasCurrPlayerPlaced();
        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);
        int id = gameController.getPlayers().get(1).getCurrentHand().getFirst().getId();
        boolean found = false;
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()){
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
            resourceCard = gameController.getResourceCardsDeck().drawCard();
            gameController.setHasCurrPlayerPlaced();
        }
        gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player1", CardType.RESOURCE_CARD));
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);
    }
}