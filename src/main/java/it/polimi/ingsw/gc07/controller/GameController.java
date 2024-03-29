package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.*;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.GameModel;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.chat.Message;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.CardType;
import it.polimi.ingsw.gc07.model.enumerations.GameResource;
import it.polimi.ingsw.gc07.model.enumerations.GameState;

import java.util.List;

public class GameController {
    /**
     * Reference to the game model.
     */
    private GameModel gameModel;

    public GameController(int playersNumber, int id) {
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardDeck.shuffle();
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        starterCardsDeck.shuffle();
        gameModel = null;
        try {
            gameModel = new GameModel(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardDeck, starterCardsDeck);
        } catch (WrongNumberOfPlayersException e) {
            // TODO ????
        }
    }

    public int getId() {
        return gameModel.getId();
    }

    public GameState getState() {
        return gameModel.getState();
    }

    public List<Player> getPlayers() {
        return gameModel.getPlayers();
    }

    public Player getCurrentPlayer() {
        return gameModel.getCurrentPlayer();
    }

    public GameField getGameField(String nickname) throws PlayerNotPresentException {
        return gameModel.getGameField(nickname);
    }

    public int getScore(String nickname) {
        return gameModel.getScore(nickname);
    }

    // i getter chiamerebbero i getter sottostanti

    // per tutti i metodi seguenti si può provare a spostare della logica nel controller

    public boolean hasPlayer(String nickname) {
        return gameModel.hasPlayer(nickname);
        // TODO
        // è possibile togliere il metodo da GameModel e spostarlo solo nel GameController
    }

    public void addPlayer(Player player) throws WrongStateException {
        gameModel.addPlayer(player);
        // TODO
        // non si può spostare logica
    }

    public void placeStarterCard(String nickname, boolean way) {
        gameModel.placeStarterCard(nickname, way);
        // TODO
        // non si può spostare logica
    }

    public boolean isFull(){
        return gameModel.isFull();
        // TODO
        // è possibile togliere il metodo da GameModel e spostarlo solo nel GameController,
        // ma servirebbe introdurre un getter playersNumber
        // nell'ottica di rendere gli oggetti funzionali non avrebbe molto senso
    }

    public void disconnectPlayer(String nickname){
        gameModel.disconnectPlayer(nickname);
        // TODO
        // è possibile spostare qua il controllo di player rimasti connessi e cambiamento di stato
    }

    public void reconnectPlayer(String nickname){
        gameModel.reconnectPlayer(nickname);
        // TODO
        // è possibile spostare qua il controllo di player rimasti connessi e cambiamento di stato
    }

    public void placeCard(String nickname, DrawableCard card, int x, int y, boolean way) throws WrongPlayerException, CardAlreadyPresentException, CardNotPresentException, WrongStateException {
        gameModel.placeCard(nickname, card, x, y, way);
        // TODO
        // non si può spostare logica
    }

    public void drawDeckCard(String nickname, CardType type) throws WrongCardTypeException, CardNotPresentException, WrongPlayerException {
        gameModel.drawDeckCard(nickname, type);
        // TODO
        // non si può spostare logica
    }

    public void drawFaceUpCard(String nickname, CardType type, int pos) throws WrongCardTypeException, CardNotPresentException, WrongPlayerException, PlayerNotPresentException {
        gameModel.drawFaceUpCard(nickname, type, pos);
        // TODO
        // non si può spostare logica
    }

    public Card revealFaceUpCard(CardType type, int pos) throws WrongCardTypeException, CardNotPresentException {
        return gameModel.revealFaceUpCard(type, pos);
        // TODO
        // non si può spostare logica
    }

    public GameResource revealBackDeckCard(CardType type) throws WrongCardTypeException, CardNotPresentException {
        return gameModel.revealBackDeckCard(type);
        // TODO
        // non si può spostare logica
    }

    public void addChatPublicMessage(String content, String sender) {
        gameModel.addChatPublicMessage(content, sender);
        // TODO
        // non si può spostare logica
    }

    public void addChatPrivateMessage(String content, String sender, String receiver) throws InvalidReceiverException {
        gameModel.addChatPrivateMessage(content, sender, receiver);
        // TODO
        // non si può spostare logica
    }

    public Message getLastChatMessage(String receiver)  {
        return gameModel.getLastChatMessage(receiver);
        // TODO
        // non si può spostare logica
    }

    public List<Message> getChatContent(String receiver) {
        return gameModel.getChatContent(receiver);
        // TODO
        // non si può spostare logica
    }
}
