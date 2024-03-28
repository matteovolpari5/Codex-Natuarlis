package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.exceptions.WrongStateException;
import it.polimi.ingsw.gc07.model.GameField;
import it.polimi.ingsw.gc07.model.GameModel;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.GameState;

import java.util.List;

public class GameController {
    /**
     * Reference to the game model.
     */
    private final GameModel gameModel;

    public GameController(int playersNumber, int id) {
        ResourceCardsDeck resourceCardsDeck = DecksBuilder.buildResourceCardsDeck();
        resourceCardsDeck.shuffle();
        GoldCardsDeck goldCardsDeck = DecksBuilder.buildGoldCardsDeck();
        goldCardsDeck.shuffle();
        PlayingDeck<ObjectiveCard> objectiveCardDeck = DecksBuilder.buildObjectiveCardsDeck();
        objectiveCardDeck.shuffle();
        Deck<PlaceableCard> starterCardsDeck = DecksBuilder.buildStarterCardsDeck();
        starterCardsDeck.shuffle();
        gameModel = new GameModel(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardDeck, starterCardsDeck);
    }

    public int getId() {
        return gameModel.getId();
    }

    public GameState getState() {
        return gameModel.getState();
    }

    public List<Player> getPlayers(){
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

    public void placeStarterCard(String nickname, boolean way) {
        gameModel.placeStarterCard(nickname, way);
    }

    public void addPlayer(Player player) throws WrongStateException {
        gameModel.addPlayer(player);
    }

    public boolean hasPlayer(String nickname) {
        return gameModel.hasPlayer(nickname);
    }

}
