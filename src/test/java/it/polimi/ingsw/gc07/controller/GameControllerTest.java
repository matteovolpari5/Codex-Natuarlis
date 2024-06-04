package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.utils.DecksBuilder;
import it.polimi.ingsw.gc07.game_commands.PlaceCardCommand;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.DrawableDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.CardType;
import it.polimi.ingsw.gc07.model.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController gameController;
    Player secondPlayer;
    Player firstPlayer;
    @BeforeEach
    void setUp() {
        int id = 0;
        int playersNumber = 2;
        DrawableDeck<DrawableCard> resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        DrawableDeck<GoldCard> goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        PlayingDeck<ObjectiveCard> objectiveCardsDeck = DecksBuilder.buildObjectiveCardsDeck();
        Deck<PlaceableCard> starterCardsDecks = DecksBuilder.buildStarterCardsDeck();
        gameController = new GameController(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        // add first player
        firstPlayer = new Player("Player1", true, false);
        firstPlayer.setTokenColor(TokenColor.BLUE);
        // add second player
        secondPlayer = new Player("Player2", false, false);
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
        gameController.getBoard().addPlayer("Player1");
        assertNotNull(myStarterCard1);
        secondPlayer.setStarterCard(myStarterCard1);
        gameController.getPlayers().add(secondPlayer);
        gameController.getBoard().addPlayer("Player2");
        assertNotNull(myStarterCard2);
        gameController.setState(GameState.SETTING_INITIAL_CARDS);

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
        List<ObjectiveCard> secretObjectives1 = new ArrayList<>();
        List<ObjectiveCard> secretObjectives2= new ArrayList<>();

        for(ObjectiveCard o: gameController.getObjectiveCardsDeck().getContent()){
            if (o.getId()==97){
                secretObjectives1.add(o);
            }
            if (o.getId()==95){
                secretObjectives2.add(o);
            }
            if (o.getId()==90){
                publicObjective.add(o);
            }
            if (o.getId()==100){
                publicObjective.add(o);
            }

        }
        for(ObjectiveCard o: gameController.getObjectiveCardsDeck().getContent()) {
            if (o.getId() == 98) {
                secretObjectives1.add(o);
            }
            if (o.getId() == 99) {
                secretObjectives2.add(o);
            }
        }
        firstPlayer.setSecretObjectives(secretObjectives1);
        secondPlayer.setSecretObjectives(secretObjectives2);
        gameController.setInitialCards("Player1", false, false);
        gameController.setInitialCards("Player2", false, false);
        gameController.getObjectiveCardsDeck().setFaceUpCards(publicObjective);
        gameController.getResourceCardsDeck().setUpDeck();
        gameController.getGoldCardsDeck().setUpDeck();
        secondPlayer.setFirst();
        gameController.setCurrentPlayer(1);
        gameController.setState(GameState.PLAYING);
    }

    @Test
    void checkGetPlayerByNickname()
    {
        assertNotNull(gameController.getPlayerByNickname("Player1"));
        assertNull(gameController.getPlayerByNickname("Player3"));
    }
    @Test
    void correctNumberOfPlayers()
    {
        assertEquals(gameController.getPlayersNumber(),2);
    }

    @Test
    void addMessageSuccess()  {
        gameController.addChatPrivateMessage("My content...", "Player1", "Player2");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void addMessageWrongSender() {
        gameController.addChatPrivateMessage("My content...", "WrongSender", "Player1");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.WRONG_SENDER, result);
    }

    @Test
    void addMessageWrongReceiver()  {
        gameController.addChatPrivateMessage("My content...", "Player1", "WrongReceiver");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.WRONG_RECEIVER, result);
    }
    @Test
    void addMessageWrongReceiver2()  {
        gameController.addChatPrivateMessage("My content...", "Player1", "Player1");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.WRONG_RECEIVER, result);
    }

    @Test
    void addMessagePublicSuccess() {
        gameController.addChatPublicMessage("My content...", "Player1");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
        gameController.addChatPublicMessage("My other content....", "Player2");
        result = gameController.getCommandResult();
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void addMessagePublicWrongSender() {
        gameController.addChatPublicMessage("My content...", "WrongPlayer");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.WRONG_SENDER, result);
    }

    @Test
    void disconnectPlayerNotPresent() {
        // disconnect player not present in the gameController
        gameController.disconnectPlayer("Player3");
        CommandResult result = gameController.getCommandResult();
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, result);
        assertFalse(gameController.isPlayerConnected("Player3"));
    }


    @Test
    void WrongState()
    {
        gameController.setState(GameState.GAME_ENDED);
        gameController.drawDeckCard("Player1",CardType.RESOURCE_CARD);
        assertEquals(gameController.getCommandResult(),CommandResult.WRONG_STATE);
    }
    @Test
    void drawDeckWrongPlayer() {
        gameController.setCurrentPlayer(0);
        gameController.drawDeckCard("Player2",CardType.RESOURCE_CARD);
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_PLAYER);
    }
    @Test
    void DrawDeckCard() {
        gameController.setCurrentPlayer(0);
        gameController.drawDeckCard("Player2", CardType.RESOURCE_CARD);
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_PLAYER);

        gameController.setCurrentPlayer(0);
        gameController.drawDeckCard("Player1", CardType.RESOURCE_CARD);
        assertEquals(gameController.getCommandResult(), CommandResult.NOT_PLACED_YET);

        gameController.setCurrentPlayer(0);
        gameController.drawDeckCard("Player1", CardType.OBJECTIVE_CARD);
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_CARD_TYPE);

        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1",0,41,41,true);

        gameController.drawDeckCard("Player1", CardType.RESOURCE_CARD);

        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);

        int id = gameController.getPlayerByNickname("Player1").getCurrentHand().getLast().getId();
        boolean found = false;
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()){
            if (c.getId() == id) {
                found = true;
                break;
            }
        }
        assertFalse(found);

        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1",0,42,42,true);

        gameController.drawDeckCard("Player1", CardType.GOLD_CARD);

        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);

        int id2 = gameController.getPlayerByNickname("Player1").getCurrentHand().getLast().getId();
        boolean found2 = false;
        for (DrawableCard c: gameController.getGoldCardsDeck().getContent()){
            if (c.getId() == id2) {
                found2 = true;
                break;
            }
        }
        assertFalse(found2);
    }

    @Test
    void DrawFromEmptyDeck(){
        for (int i = 0; i < 40; i++ ){
            gameController.getResourceCardsDeck().drawCard();
        }
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1",0,41,41,true);
        gameController.drawDeckCard("Player1", CardType.RESOURCE_CARD);
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);
    }
    @Test
    void DrawFromEmptyDeck2(){
        for (int i = 0; i < 40; i++ ){
            gameController.getGoldCardsDeck().drawCard();
        }
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1",0,41,41,true);
        gameController.drawDeckCard("Player1", CardType.GOLD_CARD);
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);
    }

    @Test
    void drawFaceUpCardWrongState() {
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1",0,41,41,true);
        gameController.setState(GameState.GAME_ENDED);
        gameController.drawFaceUpCard("Player1", CardType.RESOURCE_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_STATE);
    }
    @Test
    void drawFaceUpCardWrongState2() {
        gameController.setCurrentPlayer(0);
        gameController.drawFaceUpCard("Player1", CardType.RESOURCE_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.NOT_PLACED_YET);
    }

    @Test
    void drawFaceUpCardWrongPlayer() {
        gameController.setInitialCards("Player1", false, false);
        gameController.setInitialCards("Player2", false, false);
        gameController.setCurrentPlayer(0);
        gameController.drawFaceUpCard("Player2", CardType.RESOURCE_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_PLAYER);

    }

    @Test
    void drawFaceUpCardWrongType() {
        gameController.setCurrentPlayer(0);
        gameController.drawFaceUpCard("Player1", CardType.OBJECTIVE_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.WRONG_CARD_TYPE);
    }

    @Test
    void drawFaceUpCardResourceCardSuccess() {
        gameController.setCurrentPlayer(0);

        gameController.placeCard("Player1", 0, 39, 39, true);

        int id = gameController.getResourceCardsDeck().revealTopCard().getId();
        gameController.drawFaceUpCard("Player1", CardType.RESOURCE_CARD, 0);
        int id2 = gameController.getResourceCardsDeck().revealTopCard().getId();
        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);
        assertNotEquals(id, id2);

    }

    @Test
    void drawFaceUpCardGoldCardSuccess() {
        gameController.setInitialCards("Player1", false, false);
        gameController.setInitialCards("Player2", false, false);
        gameController.setCurrentPlayer(0);

        gameController.placeCard("Player1", 0, 39, 39, true);

        int id = gameController.getGoldCardsDeck().revealTopCard().getId();
        gameController.drawFaceUpCard("Player1", CardType.GOLD_CARD, 0);
        int id2 = gameController.getGoldCardsDeck().revealTopCard().getId();
        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);
        assertNotEquals(id, id2);

    }

    @Test
    void drawFaceUpCardResourceCardNotSuccess() {
        gameController.setInitialCards("Player1", false, false);
        gameController.setInitialCards("Player2", false, false);
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1", 0, 39, 39, true);
        for (int i = 0; i < 40; i++) {
            gameController.getResourceCardsDeck().drawFaceUpCard(0);
        }
        gameController.drawFaceUpCard("Player1", CardType.RESOURCE_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);

    }

    @Test
    void drawFaceUpCardGoldCardNotSuccess() {
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1", 0, 39, 39, true);
        for (int i = 0; i < 40; i++) {
            gameController.getGoldCardsDeck().drawFaceUpCard(0);
        }
        gameController.drawFaceUpCard("Player1", CardType.GOLD_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);

    }

    @Test
    void drawLastFaceUpCard()
    {
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1", 0, 39, 39, true);
        for (int i = 0; i < 39; i++) {
            gameController.getResourceCardsDeck().drawFaceUpCard(0);
        }
        gameController.drawFaceUpCard("Player1", CardType.RESOURCE_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);
        assertNotNull(gameController.getGoldCardsDeck().getFaceUpCards().get(2));
    }

    @Test
    void drawLastFaceUpCard2()
    {
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1", 0, 39, 39, true);
        for (int i = 0; i < 39; i++) {
            gameController.getGoldCardsDeck().drawFaceUpCard(0);
        }
        gameController.drawFaceUpCard("Player1", CardType.GOLD_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.SUCCESS);
        assertNotNull(gameController.getResourceCardsDeck().getFaceUpCards().get(2));
    }
    @Test
    void drawFromEmptyDecks()
    {
        gameController.setCurrentPlayer(0);
        gameController.placeCard("Player1", 0, 39, 39, true);
        for (int i = 0; i < 40; i++) {
            gameController.getGoldCardsDeck().drawFaceUpCard(0);
        }
        for (int i = 0; i < 40; i++) {
            gameController.getResourceCardsDeck().drawFaceUpCard(0);
        }
        gameController.drawFaceUpCard("Player1", CardType.GOLD_CARD, 0);
        assertEquals(gameController.getCommandResult(), CommandResult.CARD_NOT_PRESENT);
    }

    @Test
    void placeCardSuccess() {
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        gameController.placeCard(currPlayer.getNickname(),0,41,41,true);
        assertEquals(CommandResult.SUCCESS, gameController.getCommandResult());
    }
    
    @Test
    void cardPlaceCardWrongState(){
        gameController.setState(GameState.GAME_STARTING);
        gameController.placeCard("Player1",0,41,41,true);
        assertEquals(CommandResult.WRONG_STATE, gameController.getCommandResult());
    }
    @Test
    void cardPlaceCardWrongPlayer(){
        String nick = gameController.getPlayers().get(gameController.getCurrPlayer()).getNickname();
        gameController.changeCurrPlayer();
        gameController.placeCard(nick,0,41,41,true);
        assertEquals(CommandResult.WRONG_PLAYER, gameController.getCommandResult());
    }
    @Test
    void cardAlreadyPlaced(){
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        gameController.placeCard(currPlayer.getNickname(),0,41,41,true);
        assertEquals(CommandResult.SUCCESS, gameController.getCommandResult());
        gameController.placeCard(currPlayer.getNickname(),0,42,42,true);
        assertEquals(CommandResult.CARD_ALREADY_PLACED, gameController.getCommandResult());
    }
    @Test
    void outOfHandBound(){
        Player currPlayer = gameController.getPlayers().get(gameController.getCurrPlayer());
        gameController.placeCard(currPlayer.getNickname(),4,41,41,true);
        assertEquals(CommandResult.CARD_NOT_PRESENT, gameController.getCommandResult());
    }
    @Test
    void PlaceStarterCardSuccess() {
        Player newPlayer = new Player("Player3", true, false);
        newPlayer.setTokenColor(TokenColor.BLUE);
        gameController.getPlayers().add(newPlayer);
        PlaceableCard myStarterCard3 = null;
        for (PlaceableCard p: gameController.getStarterCardsDeck().getContent()){
            if (p.getId() == 84){
                myStarterCard3 = p;
            }
        }
        List<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(gameController.getObjectiveCardsDeck().drawCard());
        objectives.add(gameController.getObjectiveCardsDeck().drawCard());
        newPlayer.setSecretObjectives(objectives);
        newPlayer.setStarterCard(myStarterCard3);
        gameController.setState(GameState.SETTING_INITIAL_CARDS);
        gameController.setInitialCards("Player3",false, false);
        assertEquals(CommandResult.SUCCESS, gameController.getCommandResult());
    }

    @Test
    void placeCardWrongState(){
        gameController.setState(GameState.GAME_STARTING);
        gameController.setInitialCards("Player1",false, false);
        assertEquals(CommandResult.WRONG_STATE, gameController.getCommandResult());
    }
    @Test
    void placeCardWrongPlayer(){
        gameController.setState(GameState.SETTING_INITIAL_CARDS);
        gameController.setInitialCards("Player3",false, false);
        assertEquals(CommandResult.PLAYER_NOT_PRESENT, gameController.getCommandResult());
    }
    @Test
    void alreadyPlaced(){
        gameController.setState(GameState.SETTING_INITIAL_CARDS);
        gameController.setInitialCards("Player1",false, false);
        assertEquals(CommandResult.CARD_ALREADY_PRESENT, gameController.getCommandResult());
    }
    @Test
    void placeRandomly()
    {
        Player newPlayer = new Player("Player3", true, false);
        newPlayer.setTokenColor(TokenColor.BLUE);
        gameController.getPlayers().add(newPlayer);
        PlaceableCard myStarterCard3 = null;
        for (PlaceableCard p: gameController.getStarterCardsDeck().getContent()){
            if (p.getId() == 84){
                myStarterCard3 = p;
            }
        }
        List<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(gameController.getObjectiveCardsDeck().drawCard());
        objectives.add(gameController.getObjectiveCardsDeck().drawCard());
        newPlayer.setSecretObjectives(objectives);
        newPlayer.setStarterCard(myStarterCard3);

        gameController.setState(GameState.SETTING_INITIAL_CARDS);
        gameController.setInitialCardsRandomly("Player3");
        assertEquals(CommandResult.SUCCESS, gameController.getCommandResult());
    }

    @Test
    void computeWinnerOneWinner() {
        //place all cards for player1
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 3) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 39, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 2) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 41, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 24) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 38, 38, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 23) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 41, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 68) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 41, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 80) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 40, 38, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 34) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 37, 37, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 17) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 39, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 10) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 38, 36, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 72) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 39, 37, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 28) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 36, 38, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 9) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 41, 37, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 68) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 37, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 33) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 38, 36, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 37) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 36, 36, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 19) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 39, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 53) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 35, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 14) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 42, 42, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 40) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 35, 35, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 58) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 43, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 15) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 40, 42, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 48) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 41, 43, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 39) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 39, 43, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 45) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 42, 40, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 78) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 38, 42, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 29) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 43, 39, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 35) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 37, 41, false));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 47) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 43, 43, false));
                gameController.changeCurrPlayer();
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 5) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 38, 44, false));
                gameController.changeCurrPlayer();
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 64) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 40, 44, false));
                gameController.changeCurrPlayer();
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 63) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 41, 43, false));
                gameController.changeCurrPlayer();
            }
        }
        for (GoldCard c: gameController.getGoldCardsDeck().getContent()) {
            if (c.getId() == 77) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 41, 45, false));
                gameController.changeCurrPlayer();
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 12) {
                secondPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player2", 2, 41, 39, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 13) {
                firstPlayer.addCardHand(c);
                gameController.setAndExecuteCommand(new PlaceCardCommand("Player1", 2, 42, 36, true));
                gameController.changeCurrPlayer();
                break;
            }
        }
        gameController.setPenultimateRound();
        gameController.changeCurrPlayer();
        gameController.changeCurrPlayer();
        gameController.changeCurrPlayer();
        assertEquals(1, gameController.getWinners().size());
        assertEquals("Player2", gameController.getWinners().getFirst());
    }

    @Test
    void computeWinnerDraw() {
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 8) {
                firstPlayer.addCardHand(c);
                gameController.placeCard("Player1", 1, 39, 39, false);
                gameController.changeCurrPlayer();
                break;
            }
        }
        for (DrawableCard c: gameController.getResourceCardsDeck().getContent()) {
            if (c.getId() == 9) {
                secondPlayer.addCardHand(c);
                gameController.placeCard("Player2", 1, 39, 39, false);
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
        firstPlayer.setFirst();
        gameController.setCurrentPlayer(0);
        gameController.changeCurrPlayer();
        firstPlayer.setIsStalled(true);
        secondPlayer.setIsStalled(true);
        gameController.changeCurrPlayer();
        assertEquals(GameState.GAME_ENDED, gameController.getState());
    }

    @Test
    void setCommandResult() {
        gameController.setCommandResult("Player", CommandResult.CARD_ALREADY_PLACED);
        assertEquals(CommandResult.CARD_ALREADY_PLACED, gameController.getCommandResult());
    }
}

