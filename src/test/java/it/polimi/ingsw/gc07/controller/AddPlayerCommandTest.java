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

class AddPlayerCommandTest {
    private Game game;
    private Player newPlayer;

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

        // create a new player
        String nickname = "New player";
        TokenColor tokenColor = TokenColor.RED;
        boolean connectionType = true;
        boolean interfaceType = true;
        newPlayer = new Player(nickname, tokenColor, connectionType, interfaceType);
    }

    @Test
    void addPlayerSuccess() {
        game.setCommand(new AddPlayerCommand(game, newPlayer));
        AddPlayerResult result = (AddPlayerResult) game.execute();      // TODO casting !!!
        assertEquals(AddPlayerResult.SUCCESS, result);
    }

    @Test
    void addPlayerWrongState() {
        Player firstPlayer = new Player("Player1", TokenColor.BLUE, true, false);
        game.setCommand(new AddPlayerCommand(game, firstPlayer));
        AddPlayerResult result = (AddPlayerResult) game.execute();
        assertEquals(AddPlayerResult.SUCCESS, result);
        Player secondPlayer = new Player("Player 2", TokenColor.GREEN, false, false);
        game.setCommand(new AddPlayerCommand(game, secondPlayer));
        result = (AddPlayerResult) game.execute();
        assertEquals(AddPlayerResult.SUCCESS, result);
        game.setCommand(new AddPlayerCommand(game, newPlayer));
        result = (AddPlayerResult) game.execute();
        assertEquals(AddPlayerResult.WRONG_STATE, result);
    }
}