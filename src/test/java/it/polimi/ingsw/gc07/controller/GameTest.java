package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    @BeforeEach
    void setUp() {
        int id = 0;
        int playersNumber = 2;
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
    }

    @Test
    void computeWinnerOneWinner() {
        // add first player
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        // add second player
        Player secondPlayer = new Player("Player2", false, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        PlaceableCard myStarterCard1 =null;
        for (PlaceableCard p: game.getStarterCardsDeck().getContent()){
            if (p.getId() == 83){
                myStarterCard1 = p;
            }
        }
        PlaceableCard myStarterCard2 = null;
        for (PlaceableCard p: game.getStarterCardsDeck().getContent()){
            if (p.getId() == 85){
                myStarterCard2 = p;
            }
        }
        GameField gameField1 = new GameField(myStarterCard1);
        game.getPlayers().add(firstPlayer);
        game.getPlayersGameField().put("Player1", gameField1);
        game.getScoreTrackBoard().addPlayer("Player1");
        gameField1.placeCard(myStarterCard1, 40, 40, false);
        GameField gameField2 = new GameField(myStarterCard1);
        game.getPlayers().add(secondPlayer);
        game.getPlayersGameField().put("Player2", gameField2);
        game.getScoreTrackBoard().addPlayer("Player2");
        gameField2.placeCard(myStarterCard2, 40, 40, false);

        List<ObjectiveCard> publicObjective = new ArrayList<>();
        for (DrawableCard c: game.getResourceCardsDeck().getContent()){
            if (c.getId() ==1){
                firstPlayer.addCardHand(c);
            }
            if(c.getId()==6){
                secondPlayer.addCardHand(c);
            }
        }
        for (GoldCard g: game.getGoldCardsDeck().getContent()){
            if (g.getId()==79){
                firstPlayer.addCardHand(g);
            }
            if(g.getId()==66){
                secondPlayer.addCardHand(g);
            }
        }
        for(ObjectiveCard o: game.getObjectiveCardsDeck().getContent()){
            if (o.getId()==97){
                firstPlayer.setSecretObjective(o);
            }
            if (o.getId()==95){
                secondPlayer.setSecretObjective(o);
            }
            if (o.getId()==90){
                publicObjective.add(o);
            }
            if (o.getId()==100){
                publicObjective.add(o);
            }
        }
        game.getObjectiveCardsDeck().setFaceUpCards(publicObjective);
        secondPlayer.setFirst();
        game.setCurrentPlayer(1);
        game.setState(GameState.PLAYING);

        //place all cards for player1

        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 3) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 39, 39, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 2) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 41, 39, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 24) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 38, 38, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 23) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 41, 41, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 68) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 41, 41, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 80) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 40, 38, true));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 34) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 37, 37, true));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 17) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 39, 41, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 10) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 38, 36, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 72) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 39, 37, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 28) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 36, 38, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 9) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 41, 37, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 68) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 37, 39, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 33) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 38, 36, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 37) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 36, 36, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 19) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 39, 39, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 53) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 35, 39, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 14) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 42, 42, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 40) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 35, 35, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 58) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 43, 41, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 15) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 40, 42, true));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 48) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 41, 43, true));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 39) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 39, 43, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 45) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 42, 40, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 78) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 38, 42, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 29) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 43, 39, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 35) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 37, 41, false));
                game.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 47) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 43, 43, false));
                game.changeCurrPlayer();
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 5) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 38, 44, false));
                game.changeCurrPlayer();
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 64) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 40, 44, false));
                game.changeCurrPlayer();
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 63) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 41, 43, false));
                game.changeCurrPlayer();
            }
        }
        for (GoldCard c: game.getGoldCardsDeck().getContent()) {
            if (c.getId() == 77) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 41, 45, false));
                game.changeCurrPlayer();
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 12) {
                secondPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player2", c, 41, 39, true));
                game.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: game.getResourceCardsDeck().getContent()) {
            if (c.getId() == 13) {
                firstPlayer.addCardHand(c);
                game.setAndExecuteCommand(new PlaceCardCommand(game, "Player1", c, 42, 36, true));
                game.changeCurrPlayer();
                break;
            }
        }
        assertEquals(1, game.getWinners().size());
        assertEquals("Player1", game.getWinners().getFirst().getNickname());
    }

    @Test
    void computeWinnerDraw() {
        // TODO
    }

    @Test
    void changeCurrPlayer() {
        Player firstPlayer = new Player("Player1", true, false);
        Player secondPlayer = new Player("Player2", false, false);
        game.setAndExecuteCommand(new AddPlayerCommand(game, firstPlayer));
        game.setAndExecuteCommand(new AddPlayerCommand(game, secondPlayer));
        firstPlayer.setFirst();
        game.setCurrentPlayer(0);
        //game.setState(GameState.GAME_STARTING);
        //game.changeCurrPlayer();
        //assertEquals(game.getCommandResultManager().getCommandResult(), CommandResult.WRONG_STATE);
        game.setState(GameState.PLAYING);
        game.changeCurrPlayer();
        assertEquals(1, game.getCurrPlayer());
        game.changeCurrPlayer();
        assertEquals(0, game.getCurrPlayer());
        //Testing disconnection
        game.getPlayers().get(1).setIsConnected(false);
        game.changeCurrPlayer();
        assertEquals(0, game.getCurrPlayer());
        game.getPlayers().get(1).setIsConnected(true);
        //Testing player stalled
        game.getPlayers().get(1).setIsStalled(true);
        game.changeCurrPlayer();
        assertEquals(0, game.getCurrPlayer());
        //Testing the final phase
        game.setTwentyPointsReached();
        game.getPlayers().get(1).setIsStalled(false);
        game.changeCurrPlayer();
        game.changeCurrPlayer();
        // game.changeCurrPlayer();
    }
}