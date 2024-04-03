package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
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
        //resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        //goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        //objectiveCardsDeck.shuffle();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        //starterCardsDecks.shuffle();
        try{
            game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        }catch(WrongNumberOfPlayersException e){
            throw new RuntimeException();
        }
        // add first player
        Player firstPlayer = new Player("Player1", TokenColor.BLUE, true, false);
        game.setCommand(new AddPlayerCommand(game, firstPlayer));
        CommandResult result = game.execute();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add second player
        Player secondPlayer = new Player("Player2", TokenColor.GREEN, false, false);
        game.setCommand(new AddPlayerCommand(game, secondPlayer));
        result = game.execute();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        // add third player
        Player thirdPlayer = new Player("Player3", TokenColor.YELLOW, false, false);
        game.setCommand(new AddPlayerCommand(game, thirdPlayer));
        result = game.execute();
        if(!result.equals(CommandResult.SUCCESS))
            throw new RuntimeException();
        game.setCurrentPlayer(2);
    }

    @Test
    void placeCardSuccess() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 39,39,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.SUCCESS, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
        }
    }
    @Test
    void placeCardNoCoveredCorner() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 36,36,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.NO_COVERED_CORNER, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
        }
    }
    @Test
    void placeCardNotLegitCorner() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 39,39,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.SUCCESS, result);
            }
        }
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 38, 38, false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.NOT_LEGIT_CORNER, result);
            }
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardMultipleCornersCovered() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 41,41,true));
                CommandResult result = game.execute();
                assertEquals(CommandResult.SUCCESS, result);
            }
        }
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 42, 41, false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.MULTIPLE_CORNERS_COVERED, result);
            }
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardAlreadyPresent() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 41,41,true));
                CommandResult result = game.execute();
                assertEquals(CommandResult.SUCCESS, result);
            }
        }
        for (DrawableCard c: game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 41,41,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.CARD_ALREADY_PRESENT, result);
            }
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardOutOfBound() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 100,100,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.INDEXES_OUT_OF_GAME_FIELD, result);
            }
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardConditionNotMet() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 41,41,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.PLACING_CONDITION_NOT_MET, result);
            }
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
    }
    @Test
    void placeCardConditionMetAndPointsScored(){
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 41,41,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.SUCCESS, result);
            }
            if (c.getId() == 36) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 39,39,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.SUCCESS, result);
            }
            if (c.getId() == 78) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 41,39,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.SUCCESS, result);
            }
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 3);
    }
    @Test
    void PlaceCardNotCurrPlayer()
    {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(1).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(1).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(1).getCurrentHand()) {
            if (c.getId() == 79) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(1).getNickname(), myResourceCard, 41,41,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.WRONG_PLAYER, result);
            }
        }
        assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(1).getNickname()), 0);
    }
    @Test
    void placeCardNotInHand() {
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(1).getCurrentHand()) {
            if (c.getId() == 37) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 39,39,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.CARD_NOT_PRESENT, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
        }
    }
    @Test
    void placeCardDuringWaiting() {
        game.setState(GameState.WAITING_PLAYERS);
        DrawableCard myResourceCard;
        game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).placeCard(game.getPlayersGameField().get(game.getPlayers().get(game.getCurrPlayer()).getNickname()).getStarterCard(), (GameField.getDim()-1)/2, (GameField.getDim()-1)/2, false);
        for (DrawableCard c : game.getPlayers().get(game.getCurrPlayer()).getCurrentHand()) {
            if (c.getId() == 35) {
                myResourceCard = c;
                assertNotNull(myResourceCard);
                game.setCommand(new PlaceCardCommand(game, game.getPlayers().get(game.getCurrPlayer()).getNickname(), myResourceCard, 39,39,false));
                CommandResult result = game.execute();
                assertEquals(CommandResult.WRONG_STATE, result);
                assertEquals(game.getScoreTrackBoard().getScore(game.getPlayers().get(game.getCurrPlayer()).getNickname()), 0);
            }
        }
    }
}