package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.DrawFaceUpCardControllerCommand;
import it.polimi.ingsw.gc07.game_commands.PlaceCardControllerCommand;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController gameController;
    @BeforeEach
    void setUp() {
        int id = 0;
        int playersNumber = 2;
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
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
        for (PlaceableCard p: gameController.getStarterCardsDeck().getContent()){
            if (p.getId() == 83){
                myStarterCard1 = p;
            }
        }
        PlaceableCard myStarterCard2 = null;
        for (PlaceableCard p: gameController.getStarterCardsDeck().getContent()){
            if (p.getId() == 85){
                myStarterCard2 = p;
            }
        }
        firstPlayer.setStarterCard(myStarterCard1);
        gameController.getPlayers().add(firstPlayer);
        gameController.getScoreTrackBoard().addPlayer("Player1");
        assertNotNull(myStarterCard1);
        secondPlayer.setStarterCard(myStarterCard1);
        gameController.getPlayers().add(secondPlayer);
        gameController.getScoreTrackBoard().addPlayer("Player2");
        assertNotNull(myStarterCard2);

        gameController.setState(GameState.PLACING_STARTER_CARDS);
        gameController.placeStarterCard("Player1", false);
        gameController.placeStarterCard("Player2", false);
        gameController.setState(GameState.PLAYING);

        List<ObjectiveCard> publicObjective = new ArrayList<>();
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()){
            if (c.getId() ==1){
                firstPlayer.addCardHand(c);
            }
            if(c.getId()==6){
                secondPlayer.addCardHand(c);
            }
        }
        for (GoldCard g: gameController.getGoldCardsDeck().getContent()){
            if (g.getId()==79){
                firstPlayer.addCardHand(g);
            }
            if(g.getId()==66){
                secondPlayer.addCardHand(g);
            }
        }
        for(ObjectiveCard o: gameController.getObjectiveCardsDeck().getContent()){
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
        gameController.getObjectiveCardsDeck().setFaceUpCards(publicObjective);
        secondPlayer.setFirst();
        gameController.setCurrentPlayer(1);
        gameController.setState(GameState.PLAYING);

        //place all cards for player1

        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 3) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 39, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 2) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 41, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 24) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 38, 38, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 23) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 41, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 68) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 41, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 80) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 40, 38, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 34) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 37, 37, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 17) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 39, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 10) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 38, 36, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 72) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 39, 37, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 28) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 36, 38, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 9) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 41, 37, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 68) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 37, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 33) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 38, 36, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 37) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 36, 36, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 19) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 39, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 53) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 35, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 14) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 42, 42, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 40) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 35, 35, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 58) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 43, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 15) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 40, 42, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 48) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 41, 43, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 39) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 39, 43, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 45) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 42, 40, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 78) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 38, 42, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 29) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 43, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 35) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 37, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 47) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 43, 43, false));
                gameController.changeCurrPlayer();
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 5) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 38, 44, false));
                gameController.changeCurrPlayer();
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 64) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 40, 44, false));
                gameController.changeCurrPlayer();
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 63) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 41, 43, false));
                gameController.changeCurrPlayer();
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 77) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 41, 45, false));
                gameController.changeCurrPlayer();
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 12) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 2, 41, 39, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 13) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 2, 42, 36, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        assertEquals(1, gameController.getWinners().size());
        assertEquals("Player1", gameController.getWinners().getFirst());
    }

    @Test
    void computeWinnerDraw() {
        // add first player
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        firstPlayer.setFirst();
        // add second player
        Player secondPlayer = new Player("Player2", false, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        PlaceableCard myStarterCard1 =null;
        for (PlaceableCard p: gameController.getStarterCardsDeck().getContent()){
            if (p.getId() == 82){
                myStarterCard1 = p;
            }
        }
        PlaceableCard myStarterCard2 = null;
        for (PlaceableCard p: gameController.getStarterCardsDeck().getContent()){
            if (p.getId() == 81){
                myStarterCard2 = p;
            }
        }

        firstPlayer.setStarterCard(myStarterCard1);
        gameController.getPlayers().add(firstPlayer);
        gameController.getScoreTrackBoard().addPlayer("Player1");
        assertNotNull(myStarterCard1);
        firstPlayer.placeCard(myStarterCard1, 40, 40, false);

        secondPlayer.setStarterCard(myStarterCard1);
        gameController.getPlayers().add(secondPlayer);
        gameController.getScoreTrackBoard().addPlayer("Player2");
        assertNotNull(myStarterCard2);
        secondPlayer.placeCard(myStarterCard2, 40, 40, false);

        gameController.setState(GameState.PLAYING);
        firstPlayer.addCardHand(gameController.getResourceCardsDeck().drawCard());
        secondPlayer.addCardHand(gameController.getResourceCardsDeck().drawCard());
        List<ObjectiveCard> publicObjective = new ArrayList<>();
        for(ObjectiveCard o: gameController.getObjectiveCardsDeck().getContent()){
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
        gameController.getObjectiveCardsDeck().setFaceUpCards(publicObjective);
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 8) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player1", 1, 39, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 9) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardControllerCommand("Player2", 1, 39, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        gameController.setPenultimateRound();
        gameController.changeCurrPlayer();
        gameController.changeCurrPlayer();
        gameController.changeCurrPlayer();
        gameController.changeCurrPlayer();

        for(Player p: gameController.getPlayers()) {
            assertTrue(gameController.getWinners().contains(p.getNickname()));
        }
    }

    @Test
    void changeCurrPlayer() {
        Player firstPlayer = new Player("Player1", true, false);
        Player secondPlayer = new Player("Player2", false, false);
        gameController.addPlayer(firstPlayer);
        gameController.addPlayer(secondPlayer);
        gameController.setState(GameState.PLAYING);
        firstPlayer.setFirst();
        gameController.setCurrentPlayer(0);
        gameController.changeCurrPlayer();
        assertEquals(1, gameController.getCurrPlayer());
        gameController.changeCurrPlayer();
        assertEquals(0, gameController.getCurrPlayer());
        //Testing disconnection
        gameController.getPlayers().get(1).setIsConnected(false);
        gameController.changeCurrPlayer();
        assertEquals(0, gameController.getCurrPlayer());
        gameController.getPlayers().get(1).setIsConnected(true);
        //Testing player stalled
        gameController.getPlayers().get(1).setIsStalled(true);
        gameController.changeCurrPlayer();
        assertEquals(0, gameController.getCurrPlayer());
        //Testing the final phase
        gameController.setPenultimateRound();
        gameController.getPlayers().get(1).setIsStalled(false);
        gameController.changeCurrPlayer();
        gameController.changeCurrPlayer();
        if (gameController.getState().equals(GameState.PLAYING)) {
            gameController.changeCurrPlayer();
        }
    }
    @Test
    void changeCurrPlayerAllStalled(){
        Player firstPlayer = new Player("Player1", false, false);
        Player secondPlayer = new Player("Player2", false, true);
        gameController.addPlayer(firstPlayer);
        gameController.addPlayer(secondPlayer);
        gameController.setState(GameState.PLAYING);
        firstPlayer.setFirst();
        gameController.setCurrentPlayer(0);
        gameController.changeCurrPlayer();
        firstPlayer.setIsStalled(true);
        secondPlayer.setIsStalled(true);
        gameController.changeCurrPlayer();
        assertEquals(GameState.GAME_ENDED, gameController.getState());
    }

    @Test
    void emptyResourceCardsDeckDrawFaceUp() {
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        Player secondPlayer = new Player("Player2", true, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        gameController.addPlayer(firstPlayer);
        gameController.addPlayer(secondPlayer);
        // remove one card, to allow drawing cards
        firstPlayer.removeCardHand(firstPlayer.getCurrentHand().getFirst());
        secondPlayer.removeCardHand(secondPlayer.getCurrentHand().getFirst());
        gameController.setHasCurrPlayerPlaced();
        gameController.setState(GameState.PLAYING);
        assertEquals(34, gameController.getResourceCardsDeck().getContent().size());
        // draw all cards
        for(int i = 0; i < 34; i++) {
            gameController.getResourceCardsDeck().drawCard();
            gameController.setHasCurrPlayerPlaced();
        }
        // check empty deck
        assertEquals(0, gameController.getResourceCardsDeck().getContent().size());
        // check exactly 2 face up cards
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(0));
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(1));
        assertNull(gameController.getResourceCardsDeck().revealFaceUpCard(2));
        // check objective deck size
        assertEquals(36, gameController.getGoldCardsDeck().getContent().size());
        // check exactly 2 gold cards
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(0));
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(1));
        assertNull(gameController.getGoldCardsDeck().revealFaceUpCard(2));
        // draw a face up card
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), CardType.RESOURCE_CARD, 0));
        // check card not replaced
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(0));
        assertNull(gameController.getResourceCardsDeck().revealFaceUpCard(1));
        // check 3 objective cards
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(0));
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(1));
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(2));
        assertNull(gameController.getGoldCardsDeck().revealFaceUpCard(3));
    }

    @Test
    void emptyGoldCardsDeckDrawFaceUp() {
        Player firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        Player secondPlayer = new Player("Player2", true, false);
        secondPlayer.setTokenColor(TokenColor.GREEN);
        gameController.addPlayer(firstPlayer);
        gameController.addPlayer(secondPlayer);
        // remove one card, to allow drawing cards
        firstPlayer.removeCardHand(firstPlayer.getCurrentHand().getFirst());
        secondPlayer.removeCardHand(secondPlayer.getCurrentHand().getFirst());
        gameController.setState(GameState.PLAYING);
        assertEquals(36, gameController.getGoldCardsDeck().getContent().size());
        // draw all cards
        for(int i = 0; i < 36; i++) {
            gameController.getGoldCardsDeck().drawCard();
        }
        // check empty deck
        assertEquals(0, gameController.getGoldCardsDeck().getContent().size());
        // check exactly 2 face up cards
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(0));
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(1));
        assertNull(gameController.getGoldCardsDeck().revealFaceUpCard(2));
        // check resource deck size
        assertEquals(34, gameController.getResourceCardsDeck().getContent().size());
        // check exactly 2 resource cards
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(0));
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(1));
        assertNull(gameController.getResourceCardsDeck().revealFaceUpCard(2));
        // draw a face up card
        gameController.setHasCurrPlayerPlaced();
        gameController.setAndExecuteCommand(new DrawFaceUpCardControllerCommand(gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname(), CardType.GOLD_CARD, 0));
        // check card not replaced
        assertNotNull(gameController.getGoldCardsDeck().revealFaceUpCard(0));
        assertNull(gameController.getGoldCardsDeck().revealFaceUpCard(1));
        assertNull(gameController.getGoldCardsDeck().revealFaceUpCard(2));
        // check 3 objective cards
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(0));
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(1));
        assertNotNull(gameController.getResourceCardsDeck().revealFaceUpCard(2));
        assertNull(gameController.getResourceCardsDeck().revealFaceUpCard(3));
    }

}