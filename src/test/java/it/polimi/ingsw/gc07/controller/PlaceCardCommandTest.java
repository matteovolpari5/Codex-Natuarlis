package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.DrawDeckCardCommand;
import it.polimi.ingsw.gc07.game_commands.PlaceCardCommand;
import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceCardCommandTest {
    GameController gameController;
    @BeforeEach
    void setUp() {
        // create a gameController
        int id = 0;
        int playersNumber = 3;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();

        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);

        // add first player
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        gameController.addPlayer(firstPlayer);
        CommandResult result = gameController.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add second player
        Player secondPlayer = new Player("Player2", false, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        gameController.addPlayer(secondPlayer);
        result = gameController.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add third player
        Player thirdPlayer = new Player("Player3", false, false);
        thirdPlayer.setTokenColor(TokenColor.YELLOW);
        gameController.addPlayer(thirdPlayer);
        result = gameController.getCommandResult();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        gameController.setCurrentPlayer(2);
        gameController.getPlayers().get(0).setIsStalled(true);
        gameController.getPlayers().get(1).setIsStalled(true);
        gameController.setState(GameState.PLAYING);
    }

    @Test
    void placeCardSuccess() {
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard( currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand())
        {
            if (c.getId() == 35) {
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
            }
            i++;
        }
    }

    @Test
    void placeCardNoCoveredCorner() {
        DrawableCard myResourceCard;
        int i=0;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 36,36,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.NO_COVERED_CORNER, result);
                assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
            }
            i++;
        }
    }

    @Test
    void placeCardNotLegitCorner() {
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 39,39,false));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 38, 38, false));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.NOT_LEGIT_CORNER, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
    }

    @Test
    void placeCardMultipleCornersCovered() {
        DrawableCard myResourceCard;
        int i=0;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,41,true));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 42, 41, false));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardAlreadyPresent() {
        int i=0;
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
       currPlayer.placeCard(currPlayer.getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,41,true));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        i=0;
        for (DrawableCard c: gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,41,false));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.CARD_ALREADY_PRESENT, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardOutOfBound() {
        int i=0;
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 100,100,false));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.INDEXES_OUT_OF_GAME_FIELD, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardConditionNotMet() {
        int i=0;
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,41,false));
                gameController.setHasNotCurrPlayerPlaced();
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.PLACING_CONDITION_NOT_MET, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardConditionMetAndPointsScored(){
        DrawableCard myResourceCard;
        int i=0;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,41,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.RESOURCE_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.GOLD_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 3);
    }
    @Test
    void placeCardTwentyPointsReached(){
        int i=0;
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
       currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        gameController.getScoreTrackBoard().incrementScore("Player3",19);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,41,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.RESOURCE_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.GOLD_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 22);
    }
    @Test
    void placeCardOverTwentyNinePoints(){
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,41,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.RESOURCE_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
                gameController.setAndExecuteCommand(new DrawDeckCardCommand("Player3", CardType.GOLD_CARD));
            }
            i++;
        }
        i=0;
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                gameController.getScoreTrackBoard().incrementScore("Player3",27);
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 41,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.SUCCESS, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 29);
    }

    @Test
    void PlaceCardNotCurrPlayer() {
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        int i=0;
        for (DrawableCard c : gameController.getPlayers().get(1).getCurrentHand()) {
            if (c.getId() == 79) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(1).getNickname(), i, 41,41,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.WRONG_PLAYER, result);
            }
            i++;
        }
        assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(1).getNickname()), 0);
    }
    @Test
    void placeCardDuringWaiting() {
        gameController.setState(GameState.GAME_STARTING);
        DrawableCard myResourceCard;
        int i=0;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), i, 39,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.WRONG_STATE, result);
                assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
            }
            i++;
        }
    }
    @Test
    void placeCardOutOfHandBounds() {
        DrawableCard myResourceCard;
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        currPlayer.placeCard(currPlayer.getGameField().getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : gameController.getPlayers().get(gameController.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                gameController.setAndExecuteCommand(new PlaceCardCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), 3, 39,39,false));
                CommandResult result = gameController.getCommandResult();
                assertEquals(CommandResult.CARD_NOT_PRESENT, result);
                assertEquals(gameController.getScoreTrackBoard().getScore(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname()), 0);
            }
        }
    }
}