package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.model.CommandResult;
import it.polimi.ingsw.gc07.model.GameField;
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
import it.polimi.ingsw.gc07.controller.enumerations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceCardCommandTest {
    Game game;
    @BeforeEach
    void setUp() {
        // create a game
        int id = 0;
        int playersNumber = 3;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();

        game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);

        // add first player
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        game.setAndExecuteCommand(new AddPlayerCommand(firstPlayer));
        CommandResult result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add second player
        Player secondPlayer = new Player("Player2", false, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        game.setAndExecuteCommand(new AddPlayerCommand(secondPlayer));
        result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add third player
        Player thirdPlayer = new Player("Player3", false, false);
        thirdPlayer.setTokenColor(TokenColor.YELLOW);
        game.setAndExecuteCommand(new AddPlayerCommand(thirdPlayer));
        result = game.getCommandResultManager().getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        game.setCurrentPlayer(2);
        game.getPlayers().get(0).setIsStalled(true);
        game.getPlayers().get(1).setIsStalled(true);
        game.setState(GameState.PLAYING);
    }

    @Test
    void placeCardSuccess() {
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand())
        {
            if (c.getId() == 35) {
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
            i++;
        }
    }
    @Test
    void placeCardNoCoveredCorner() {
        DrawableCard myResourceCard;
        int i=0;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 36,36,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.NO_COVERED_CORNER, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
            i++;
        }
    }
    @Test
    void placeCardNotLegitCorner() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 39,39,false));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 38, 38, false));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.NOT_LEGIT_CORNER, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardMultipleCornersCovered() {
        DrawableCard myResourceCard;
        int i=0;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,41,true));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 42, 41, false));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardAlreadyPresent() {
        int i=0;
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,41,true));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        i=0;
        for (DrawableCard c: game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,41,false));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.CARD_ALREADY_PRESENT, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardOutOfBound() {
        int i=0;
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 100,100,false));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.INDEXES_OUT_OF_GAME_FIELD, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardConditionNotMet() {
        int i=0;
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,41,false));
                game.setHasNotCurrPlayerPlaced();
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.PLACING_CONDITION_NOT_MET, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardConditionMetAndPointsScored(){
        DrawableCard myResourceCard;
        int i=0;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,41,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                game.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.RESOURCE_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                game.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.GOLD_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 3);
    }
    @Test
    void placeCardTwentyPointsReached(){
        int i=0;
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        game.getScoreTrackBoard().incrementScore("Player3",19);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,41,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                game.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.RESOURCE_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                game.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.GOLD_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 22);
    }
    @Test
    void placeCardOverTwentyNinePoints(){
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,41,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                game.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.RESOURCE_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                game.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.GOLD_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                game.getScoreTrackBoard().incrementScore("Player3",27);
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 41,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 29);
    }
    @Test
    void PlaceCardNotCurrPlayer()
    {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(1).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(1).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : game.getPlayers().get(1).getCurrentHand()) {
            if (c.getId() == 79) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(1).getNickname(), i, 41,41,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.WRONG_PLAYER, result);
            }
            i++;
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(1).getNickname()), 0);
    }
    @Test
    void placeCardDuringWaiting() {
        game.setState(GameState.GAME_STARTING);
        DrawableCard myResourceCard;
        int i=0;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.WRONG_STATE, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
            i++;
        }
    }
    @Test
    void placeCardOutOfHandBounds() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setAndExecuteCommand(new PlaceCardCommand(game.getPlayers().get(game.getCurrPlayer()).getNickname(), 3, 39,39,false));
                CommandResult result = game.getCommandResultManager().getCommandResult();
                assertEquals(CommandResult.CARD_NOT_PRESENT, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
        }
    }
}