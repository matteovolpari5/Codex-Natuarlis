package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.DecksBuilder;
import it.polimi.ingsw.gc07.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model.decks.Deck;
import it.polimi.ingsw.gc07.model.decks.GoldCardsDeck;
import it.polimi.ingsw.gc07.model.decks.PlayingDeck;
import it.polimi.ingsw.gc07.model.decks.ResourceCardsDeck;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectPlayerCommandTest {
    Game game;

    @BeforeEach
    void setUp() {
        // create a game
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
        try{
            game = new Game(id, playersNumber, resourceCardsDeck, goldCardsDeck, objectiveCardsDeck, starterCardsDecks);
        }catch(WrongNumberOfPlayersException e){
            throw new RuntimeException();
        }
        Player firstPlayer = new Player("Player1", TokenColor.BLUE, true, false);
        game.setCommand(new AddPlayerCommand(game, firstPlayer));
        AddPlayerResult result = (AddPlayerResult) game.execute();
        if(!result.equals(AddPlayerResult.SUCCESS))
            throw new RuntimeException();
        Player secondPlayer = new Player("Player2", TokenColor.GREEN, false, false);
        game.setCommand(new AddPlayerCommand(game, secondPlayer));
        result = (AddPlayerResult) game.execute();
        if(!result.equals(AddPlayerResult.SUCCESS))
            throw new RuntimeException();
    }

    @Test
    void disconnectPlayerSuccess() {
        game.setCommand(new DisconnectPlayerCommand(game, "Player1"));
        ConnectionResult result = (ConnectionResult) game.execute();
        assertEquals(ConnectionResult.SUCCESS, result);
    }

    @Test
    void playerAlreadyDisconnected() {
        // disconnect player
        game.setCommand(new DisconnectPlayerCommand(game, "Player2"));
        ConnectionResult result = (ConnectionResult) game.execute();
        assertEquals(ConnectionResult.SUCCESS, result);
        // try to disconnect the same player
        game.setCommand(new DisconnectPlayerCommand(game, "Player2"));
        result = (ConnectionResult) game.execute();
        assertEquals(ConnectionResult.PLAYER_ALREADY_DISCONNECTED, result);
    }

    @Test
    void disconnectPlayerNotPresent() {
        // disconnect player not present in the game
        game.setCommand(new DisconnectPlayerCommand(game, "AnOtherPlayer"));
        ConnectionResult result = (ConnectionResult) game.execute();
        assertEquals(ConnectionResult.PLAYER_NOT_PRESENT, result);
    }
}